package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.User;
import epaw.lab4.util.DBLogger;

public class UserRepository extends BaseRepository {

    private static UserRepository instance;

    private UserRepository() {
        super();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE name = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByNickname(String nickname) {
        String query = "SELECT COUNT(*) FROM users WHERE nickname = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, nickname);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmailAndNotId(String email, Integer id) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setInt(2, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByNicknameAndNotId(String nickname, Integer id) {
        String query = "SELECT COUNT(*) FROM users WHERE nickname = ? AND id <> ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, nickname);
            statement.setInt(2, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkLogin(User user) {
        String query = "SELECT u.id, u.picture, u.role_id, u.email, u.surname, u.nickname, u.birth_date, u.favorite_game, u.created_at, r.name AS role_name FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE u.name = ? AND u.password = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setPicture(rs.getString("picture"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setRole(rs.getString("role_name"));
                    user.setEmail(rs.getString("email"));
                    user.setSurname(rs.getString("surname"));
                    user.setNickname(rs.getString("nickname"));
                    user.setBirthDate(rs.getString("birth_date"));
                    user.setFavoriteGame(rs.getString("favorite_game"));
                    user.setCreatedAt(rs.getString("created_at"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        String query = "INSERT INTO users (role_id, email, name, surname, nickname, password, picture, birth_date, favorite_game, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (PreparedStatement statement = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, user.getRoleId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getNickname());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getPicture());
            statement.setString(8, user.getBirthDate());
            statement.setString(9, user.getFavoriteGame());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    user.setId(id);
                    DBLogger.append(
                        "INSERT INTO users (id, role_id, email, name, surname, nickname, password, picture, birth_date, favorite_game, created_at, updated_at) VALUES (" +
                        id + ", " + user.getRoleId() + ", " + DBLogger.q(user.getEmail()) + ", " +
                        DBLogger.q(user.getName()) + ", " + DBLogger.q(user.getSurname()) + ", " +
                        DBLogger.q(user.getNickname()) + ", " + DBLogger.q(user.getPassword()) + ", " +
                        DBLogger.q(user.getPicture()) + ", " + DBLogger.q(user.getBirthDate()) + ", " +
                        DBLogger.q(user.getFavoriteGame()) + ", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)"
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        String query = "UPDATE users SET name = ?, nickname = ?, email = ?, favorite_game = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getNickname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFavoriteGame());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
            DBLogger.append(
                "UPDATE users SET name = " + DBLogger.q(user.getName()) +
                ", nickname = " + DBLogger.q(user.getNickname()) +
                ", email = " + DBLogger.q(user.getEmail()) +
                ", favorite_game = " + DBLogger.q(user.getFavoriteGame()) +
                ", updated_at = CURRENT_TIMESTAMP WHERE id = " + user.getId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByName(String name) {
        String query = "SELECT u.*, r.name AS role_name FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE u.name = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findById(Integer id) {
        String query = "SELECT u.*, r.name AS role_name FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE u.id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<User>> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.id, u.name, u.picture, u.nickname, r.name AS role_name FROM users u INNER JOIN roles r ON u.role_id = r.role_id";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setPicture(rs.getString("picture"));
                    user.setNickname(rs.getString("nickname"));
                    user.setRole(rs.getString("role_name"));
                    users.add(user);
                }
                return Optional.of(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Follow a user (follower_id, followee_id)
    public void followUser(Integer followerId, Integer followeeId) {
        String query = "INSERT INTO follows (follower_id, followee_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followeeId);
            statement.executeUpdate();
            DBLogger.append("INSERT INTO follows (follower_id, followee_id) VALUES (" + followerId + ", " + followeeId + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Unfollow a user
    public void unfollowUser(Integer followerId, Integer followeeId) {
        String query = "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followeeId);
            statement.executeUpdate();
            DBLogger.append("DELETE FROM follows WHERE follower_id = " + followerId + " AND followee_id = " + followeeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find users NOT followed by the current user
    public Optional<List<User>> findNotFollowed(Integer id, Integer start, Integer end) {
        String query = "SELECT id, name, picture FROM users WHERE id NOT IN (SELECT followee_id FROM follows WHERE follower_id = ?) AND id <> ? ORDER BY name LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, start);
            statement.setInt(4, end);
            try (ResultSet rs = statement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setPicture(rs.getString("picture"));
                    users.add(user);
                }
                return Optional.of(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Find users followed by the current user
    public Optional<List<User>> findFollowed(Integer id, Integer start, Integer end) {
        String query = "SELECT u.id, u.name, u.picture FROM users u INNER JOIN follows f ON u.id = f.followee_id WHERE f.follower_id = ? ORDER BY u.name LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, start);
            statement.setInt(3, end);
            try (ResultSet rs = statement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setPicture(rs.getString("picture"));
                    users.add(user);
                }
                return Optional.of(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Helper to map a full user from ResultSet
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setRoleId(rs.getInt("role_id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setPicture(rs.getString("picture"));
        user.setRole(rs.getString("role_name"));
        user.setEmail(rs.getString("email"));
        user.setSurname(rs.getString("surname"));
        user.setNickname(rs.getString("nickname"));
        user.setBirthDate(rs.getString("birth_date"));
        user.setFavoriteGame(rs.getString("favorite_game"));
        user.setCreatedAt(rs.getString("created_at"));
        user.setUpdatedAt(rs.getString("updated_at"));
        return user;
    }

    // Check if a user is currently banned
    public boolean isBanned(Integer userId) {
        String query = "SELECT COUNT(*) FROM bans WHERE banned_user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get the ban reason for a user
    public String getBanReason(Integer userId) {
        String query = "SELECT reason FROM bans WHERE banned_user_id = ? ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("reason");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ban a user
    public void banUser(Integer bannedUserId, Integer adminId, String reason) {
        String query = "INSERT INTO bans (banned_user_id, banned_by_admin_id, reason) VALUES (?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, bannedUserId);
            statement.setInt(2, adminId);
            statement.setString(3, reason);
            statement.executeUpdate();
            DBLogger.append("INSERT INTO bans (banned_user_id, banned_by_admin_id, reason) VALUES (" +
                bannedUserId + ", " + adminId + ", " + DBLogger.q(reason) + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Unban a user (remove all ban records)
    public void unbanUser(Integer bannedUserId) {
        String query = "DELETE FROM bans WHERE banned_user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, bannedUserId);
            statement.executeUpdate();
            DBLogger.append("DELETE FROM bans WHERE banned_user_id = " + bannedUserId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find all non-admin users with their ban status (for admin panel)
    public Optional<List<User>> findAllWithBanStatus(Integer adminId) {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.id, u.name, u.email, u.nickname, u.picture, r.name AS role_name, " +
                "(SELECT COUNT(*) FROM bans b WHERE b.banned_user_id = u.id) AS ban_count, " +
                "(SELECT b2.reason FROM bans b2 WHERE b2.banned_user_id = u.id ORDER BY b2.created_at DESC LIMIT 1) AS ban_reason " +
                "FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE u.id <> ? ORDER BY u.name";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, adminId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setNickname(rs.getString("nickname"));
                    user.setPicture(rs.getString("picture"));
                    user.setRole(rs.getString("role_name"));
                    user.setBanned(rs.getInt("ban_count") > 0);
                    user.setBanReason(rs.getString("ban_reason"));
                    users.add(user);
                }
                return Optional.of(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
