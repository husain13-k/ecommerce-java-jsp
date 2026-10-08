package dao;

import model.User;
import util.DB;
import util.Util;

import java.sql.*;

public class UserDAO {

    // returns null on success, or an error message
    public static String register(String name, String email, String password) {
        String sql = "INSERT INTO users(name,email,password) VALUES(?,?,?)";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, Util.hash(password));
            ps.executeUpdate();
            return null;
        } catch (SQLIntegrityConstraintViolationException e) {
            return "This email is already registered";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // returns the User if email/password are correct, otherwise null
    public static User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email=?";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet r = ps.executeQuery();
            if (r.next() && Util.verify(password, r.getString("password"))) {
                return new User(r.getInt("id"), r.getString("name"),
                                r.getString("email"), r.getString("role"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}