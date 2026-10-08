package dao;

import model.Category;
import model.Product;
import util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final String BASE =
        "SELECT p.*, c.name AS category FROM products p JOIN categories c ON p.category_id=c.id ";

    private static Product map(ResultSet r) throws SQLException {
        return new Product(r.getInt("id"), r.getString("name"), r.getString("description"),
            r.getDouble("price"), r.getInt("stock"), r.getString("image"),
            r.getInt("category_id"), r.getString("category"));
    }

    // ---------- read methods (used by JSP pages) ----------
    public static List<Product> search(String q, int categoryId) {
        String sql = BASE + "WHERE p.name LIKE ? " + (categoryId > 0 ? "AND p.category_id=? " : "")
                          + "ORDER BY p.id DESC";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + (q == null ? "" : q) + "%");
            if (categoryId > 0) ps.setInt(2, categoryId);
            ResultSet r = ps.executeQuery();
            List<Product> list = new ArrayList<>();
            while (r.next()) list.add(map(r));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Product find(int id) {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(BASE + "WHERE p.id=?")) {
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();
            return r.next() ? map(r) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Category> categories() {
        try (Connection c = DB.get(); Statement st = c.createStatement()) {
            ResultSet r = st.executeQuery("SELECT * FROM categories ORDER BY name");
            List<Category> list = new ArrayList<>();
            while (r.next()) list.add(new Category(r.getInt("id"), r.getString("name")));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- write methods (used by admin servlet) ----------
    public static void addCategory(String name) throws SQLException {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement("INSERT INTO categories(name) VALUES(?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    public static void add(Product p) throws SQLException {
        String sql = "INSERT INTO products(name,description,price,stock,image,category_id) VALUES(?,?,?,?,?,?)";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.name());
            ps.setString(2, p.description());
            ps.setDouble(3, p.price());
            ps.setInt(4, p.stock());
            ps.setString(5, p.image());
            ps.setInt(6, p.categoryId());
            ps.executeUpdate();
        }
    }

    public static void update(Product p) throws SQLException {
        String sql = "UPDATE products SET name=?,description=?,price=?,stock=?,image=?,category_id=? WHERE id=?";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.name());
            ps.setString(2, p.description());
            ps.setDouble(3, p.price());
            ps.setInt(4, p.stock());
            ps.setString(5, p.image());
            ps.setInt(6, p.categoryId());
            ps.setInt(7, p.id());
            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws SQLException {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}