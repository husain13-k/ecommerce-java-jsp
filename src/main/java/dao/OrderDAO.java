package dao;

import model.Order;
import model.OrderItem;
import util.DB;

import java.sql.*;
import java.util.*;

public class OrderDAO {

    // Places an order using ONE transaction:
    // either everything succeeds (order + items + stock reduced) or nothing is saved.
    public static void place(int userId, Map<Integer, Integer> cart, String address) throws Exception {
        try (Connection c = DB.get()) {
            c.setAutoCommit(false);
            try {
                double total = 0;
                Map<Integer, Double> prices = new HashMap<>();

                // 1. check stock and lock the rows so two buyers can't buy the last item
                for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "SELECT name, price, stock FROM products WHERE id=? FOR UPDATE")) {
                        ps.setInt(1, e.getKey());
                        ResultSet r = ps.executeQuery();
                        if (!r.next()) throw new Exception("A product in your cart no longer exists");
                        if (r.getInt("stock") < e.getValue())
                            throw new Exception("Not enough stock for " + r.getString("name"));
                        double price = r.getDouble("price");
                        prices.put(e.getKey(), price);
                        total += price * e.getValue();
                    }
                }

                // 2. create the order
                int orderId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO orders(user_id,total,address) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    ps.setDouble(2, total);
                    ps.setString(3, address);
                    ps.executeUpdate();
                    ResultSet k = ps.getGeneratedKeys();
                    k.next();
                    orderId = k.getInt(1);
                }

                // 3. add order items (price saved at purchase time) and reduce stock
                for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO order_items(order_id,product_id,quantity,price) VALUES(?,?,?,?)")) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, e.getKey());
                        ps.setInt(3, e.getValue());
                        ps.setDouble(4, prices.get(e.getKey()));
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = c.prepareStatement(
                            "UPDATE products SET stock = stock - ? WHERE id=?")) {
                        ps.setInt(1, e.getValue());
                        ps.setInt(2, e.getKey());
                        ps.executeUpdate();
                    }
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            }
        }
    }

    private static List<Order> query(String sql, Integer userId) {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (userId != null) ps.setInt(1, userId);
            ResultSet r = ps.executeQuery();
            List<Order> list = new ArrayList<>();
            while (r.next()) {
                list.add(new Order(r.getInt("id"), r.getDouble("total"), r.getString("status"),
                    r.getString("address"), r.getTimestamp("order_date"), r.getString("customer")));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Order> forUser(int userId) {
        return query("SELECT o.*, u.name AS customer FROM orders o JOIN users u ON o.user_id=u.id " +
                     "WHERE o.user_id=? ORDER BY o.id DESC", userId);
    }

    public static List<Order> all() {
        return query("SELECT o.*, u.name AS customer FROM orders o JOIN users u ON o.user_id=u.id " +
                     "ORDER BY o.id DESC", null);
    }

    public static List<OrderItem> items(int orderId) {
        String sql = "SELECT p.name, oi.quantity, oi.price FROM order_items oi " +
                     "JOIN products p ON oi.product_id=p.id WHERE oi.order_id=?";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet r = ps.executeQuery();
            List<OrderItem> list = new ArrayList<>();
            while (r.next()) list.add(new OrderItem(r.getString("name"), r.getInt("quantity"), r.getDouble("price")));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStatus(int orderId, String status) throws SQLException {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement("UPDATE orders SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
}