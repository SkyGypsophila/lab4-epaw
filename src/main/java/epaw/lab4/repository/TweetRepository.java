package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.Tweet;
import epaw.lab4.util.DBLogger;

public class TweetRepository extends BaseRepository {

    private static TweetRepository instance;

    private TweetRepository() {
        super();
    }

    public static synchronized TweetRepository getInstance() {
        if (instance == null) {
            instance = new TweetRepository();
        }
        return instance;
    }

    private static final java.text.SimpleDateFormat TS_FMT =
        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void save(Tweet tweet) {
        String query = "INSERT INTO tweets (user_id, content, image, created_at, parent_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, tweet.getUid());
            statement.setString(2, tweet.getContent());
            String tsStr = tweet.getPostDateTime() != null
                ? TS_FMT.format(tweet.getPostDateTime()) : null;
            statement.setString(3, tweet.getImage());
            statement.setString(4, tsStr);

            boolean hasParent = tweet.getParentId() != null && tweet.getParentId() > 0;
            if (hasParent) {
                statement.setInt(5, tweet.getParentId());
            } else {
                statement.setNull(5, java.sql.Types.INTEGER);
            }
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    tweet.setId(id);
                    String parentSql = hasParent ? String.valueOf(tweet.getParentId()) : "NULL";
                    DBLogger.append(
                        "INSERT INTO tweets (tweet_id, user_id, content, image, created_at, parent_id) VALUES (" +
                        id + ", " + tweet.getUid() + ", " + DBLogger.q(tweet.getContent()) +
                        ", " + DBLogger.q(tweet.getImage()) + ", " + DBLogger.q(tsStr) + ", " + parentSql + ")"
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer tweetId, Integer userId) {
        String query = "DELETE FROM tweets WHERE tweet_id = ? AND user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweetId);
            statement.setInt(2, userId);
            statement.executeUpdate();
            DBLogger.append("DELETE FROM tweets WHERE tweet_id = " + tweetId + " AND user_id = " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUnrestricted(Integer tweetId) {
        String query = "DELETE FROM tweets WHERE tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweetId);
            statement.executeUpdate();
            DBLogger.append("DELETE FROM tweets WHERE tweet_id = " + tweetId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Integer tweetId, String content) {
        String query = "UPDATE tweets SET content = ? WHERE tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, content);
            statement.setInt(2, tweetId);
            statement.executeUpdate();
            DBLogger.append("UPDATE tweets SET content = " + DBLogger.q(content) + " WHERE tweet_id = " + tweetId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Tweet> findById(Integer tweetId) {
        String query = "SELECT t.tweet_id, t.user_id, t.created_at, t.content, t.image, u.name, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id) AS like_count, " +
                "0 AS liked_by_user, " +
                "(SELECT COUNT(*) FROM bans WHERE banned_user_id = t.user_id) AS is_banned " +
                "FROM tweets t INNER JOIN users u ON t.user_id = u.id " +
                "WHERE t.tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweetId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapTweet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // AÑADIDO: AND t.parent_id IS NULL (Solo tweets principales)
    public Optional<List<Tweet>> findByUser(Integer userId, Integer start, Integer end) {
        return findByUser(userId, userId, start, end);
    }

    // SOBRECARGADO: Para poder ver los tweets de otro usuario con el estado de like de un visualizador
    public Optional<List<Tweet>> findByUser(Integer authorId, Integer viewerId, Integer start, Integer end) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.tweet_id, t.user_id, t.created_at, t.content, t.image, u.name, u.picture, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id) AS like_count, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id AND l.user_id = ?) AS liked_by_user, " +
                "(SELECT COUNT(*) FROM bans WHERE banned_user_id = t.user_id) AS is_banned " +
                "FROM tweets t INNER JOIN users u ON t.user_id = u.id " +
                "WHERE t.user_id = ? AND t.parent_id IS NULL ORDER BY t.created_at DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, viewerId); 
            statement.setInt(2, authorId);
            statement.setInt(3, start);
            statement.setInt(4, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = mapTweet(rs);
                    tweet.setUpicture(rs.getString("picture"));
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // AÑADIDO: AND t.parent_id IS NULL (Solo tweets principales)
    public Optional<List<Tweet>> findByFollowedUsers(Integer followerId, Integer start, Integer end) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.tweet_id, t.user_id, t.created_at, t.content, t.image, u.name, u.picture, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id) AS like_count, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id AND l.user_id = ?) AS liked_by_user, " +
                "(SELECT COUNT(*) FROM bans WHERE banned_user_id = t.user_id) AS is_banned " +
                "FROM tweets t " +
                "INNER JOIN users u ON t.user_id = u.id " +
                "INNER JOIN follows f ON t.user_id = f.followee_id " +
                "WHERE f.follower_id = ? AND t.parent_id IS NULL " +
                "ORDER BY t.created_at DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId); 
            statement.setInt(2, followerId);
            statement.setInt(3, start);
            statement.setInt(4, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = mapTweet(rs);
                    tweet.setUpicture(rs.getString("picture"));
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // NUEVO METODO PARA EL GLOBAL FEED
    public Optional<List<Tweet>> findAllGlobalTweets(Integer viewerId, Integer start, Integer end) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.tweet_id, t.user_id, t.created_at, t.content, t.image, u.name, u.picture, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id) AS like_count, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id AND l.user_id = ?) AS liked_by_user, " +
                "(SELECT COUNT(*) FROM bans WHERE banned_user_id = t.user_id) AS is_banned " +
                "FROM tweets t INNER JOIN users u ON t.user_id = u.id " +
                "WHERE t.parent_id IS NULL ORDER BY t.created_at DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, viewerId);
            statement.setInt(2, start);
            statement.setInt(3, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = mapTweet(rs);
                    tweet.setUpicture(rs.getString("picture"));
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // NUEVO METODO: Buscar respuestas (comentarios) de un tweet
    public List<Tweet> findReplies(Integer parentId, Integer viewerId) {
        List<Tweet> replies = new ArrayList<>();
        String query = "SELECT t.tweet_id, t.user_id, t.created_at, t.content, t.image, u.name, u.picture, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id) AS like_count, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id AND l.user_id = ?) AS liked_by_user, " +
                "(SELECT COUNT(*) FROM bans WHERE banned_user_id = t.user_id) AS is_banned " +
                "FROM tweets t INNER JOIN users u ON t.user_id = u.id " +
                "WHERE t.parent_id = ? ORDER BY t.created_at ASC";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, viewerId);
            statement.setInt(2, parentId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = mapTweet(rs);
                    tweet.setUpicture(rs.getString("picture"));
                    replies.add(tweet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return replies;
    }

    public boolean isLikedByUser(Integer userId, Integer tweetId) {
        String query = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
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

    public void likeTweet(Integer userId, Integer tweetId) {
        String query = "INSERT INTO likes (user_id, tweet_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            statement.executeUpdate();
            DBLogger.append("INSERT INTO likes (user_id, tweet_id) VALUES (" + userId + ", " + tweetId + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unlikeTweet(Integer userId, Integer tweetId) {
        String query = "DELETE FROM likes WHERE user_id = ? AND tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            statement.executeUpdate();
            DBLogger.append("DELETE FROM likes WHERE user_id = " + userId + " AND tweet_id = " + tweetId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateImage(Integer tweetId, String imagePath) {
        String query = "UPDATE tweets SET image = ? WHERE tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, imagePath);
            statement.setInt(2, tweetId);
            statement.executeUpdate();
            DBLogger.append("UPDATE tweets SET image = " + DBLogger.q(imagePath) + " WHERE tweet_id = " + tweetId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeImage(Integer tweetId) {
        String query = "UPDATE tweets SET image = NULL WHERE tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweetId);
            statement.executeUpdate();
            DBLogger.append("UPDATE tweets SET image = NULL WHERE tweet_id = " + tweetId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Tweet mapTweet(ResultSet rs) throws SQLException {
        Tweet tweet = new Tweet();
        tweet.setId(rs.getInt("tweet_id"));
        tweet.setUid(rs.getInt("user_id"));
        tweet.setPostDateTime(rs.getTimestamp("created_at"));
        tweet.setContent(rs.getString("content"));
        tweet.setUname(rs.getString("name"));
        tweet.setLikeCount(rs.getInt("like_count"));
        tweet.setLiked(rs.getInt("liked_by_user") > 0);
        tweet.setBanned(rs.getInt("is_banned") > 0);
        tweet.setImage(rs.getString("image"));
        return tweet;
    }
}