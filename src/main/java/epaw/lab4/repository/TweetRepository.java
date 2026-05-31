package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.Tweet;

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

    public void save(Tweet tweet) {
        String query = "INSERT INTO tweets (user_id, content, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweet.getUid());
            statement.setString(2, tweet.getContent());
            statement.setTimestamp(3, tweet.getPostDateTime());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Delete existing tweet - only if it belongs to the user */
    public void delete(Integer tweetId, Integer userId) {
        String query = "DELETE FROM tweets WHERE tweet_id = ? AND user_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, tweetId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Get tweets from a user, with like count and liked-by-viewer status */
    public Optional<List<Tweet>> findByUser(Integer userId, Integer start, Integer end) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.tweet_id, t.user_id, t.created_at, t.content, u.name, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id) AS like_count, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id AND l.user_id = ?) AS liked_by_user " +
                "FROM tweets t INNER JOIN users u ON t.user_id = u.id " +
                "WHERE t.user_id = ? ORDER BY t.created_at DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);  // viewer = tweet owner in this case
            statement.setInt(2, userId);
            statement.setInt(3, start);
            statement.setInt(4, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tweets.add(mapTweet(rs));
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /* Get tweets from all users that a given user follows, with like info */
    public Optional<List<Tweet>> findByFollowedUsers(Integer followerId, Integer start, Integer end) {
        List<Tweet> tweets = new ArrayList<>();
        String query = "SELECT t.tweet_id, t.user_id, t.created_at, t.content, u.name, u.picture, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id) AS like_count, " +
                "(SELECT COUNT(*) FROM likes l WHERE l.tweet_id = t.tweet_id AND l.user_id = ?) AS liked_by_user " +
                "FROM tweets t " +
                "INNER JOIN users u ON t.user_id = u.id " +
                "INNER JOIN follows f ON t.user_id = f.followee_id " +
                "WHERE f.follower_id = ? " +
                "ORDER BY t.created_at DESC LIMIT ?, ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId);  // viewer = follower
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

    /* Check if a user has liked a tweet */
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

    /* Like a tweet */
    public void likeTweet(Integer userId, Integer tweetId) {
        String query = "INSERT INTO likes (user_id, tweet_id) VALUES (?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Unlike a tweet */
    public void unlikeTweet(Integer userId, Integer tweetId) {
        String query = "DELETE FROM likes WHERE user_id = ? AND tweet_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Helper to map common tweet fields from ResultSet */
    private Tweet mapTweet(ResultSet rs) throws SQLException {
        Tweet tweet = new Tweet();
        tweet.setId(rs.getInt("tweet_id"));
        tweet.setUid(rs.getInt("user_id"));
        tweet.setPostDateTime(rs.getTimestamp("created_at"));
        tweet.setContent(rs.getString("content"));
        tweet.setUname(rs.getString("name"));
        tweet.setLikeCount(rs.getInt("like_count"));
        tweet.setLiked(rs.getInt("liked_by_user") > 0);
        return tweet;
    }
}
