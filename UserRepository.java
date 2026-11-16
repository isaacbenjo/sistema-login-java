import java.sql.*;
import java.util.Optional;

public class UserRepository {

    public void save(String username, String email, String passwordHash, String salt) {
        String sql = "INSERT INTO users (username, email, password_hash, salt) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, passwordHash);
            pstmt.setString(4, salt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário (possível duplicidade): " + e.getMessage());
        }
    }

    public Optional<UserWithCredentials> findByUsernameOrEmail(String login) {
        String sql = "SELECT id, username, email, password_hash, salt, role FROM users WHERE username = ? OR email = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new UserWithCredentials(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("salt"),
                        rs.getString("role")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage());
        }
        return Optional.empty();
    }

    public static class UserWithCredentials {
        public final int id;
        public final String username;
        public final String email;
        public final String passwordHash;
        public final String salt;
        public final String role;

        public UserWithCredentials(int id, String username, String email, String passwordHash, String salt, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.passwordHash = passwordHash;
            this.salt = salt;
            this.role = role;
        }
    }
}
