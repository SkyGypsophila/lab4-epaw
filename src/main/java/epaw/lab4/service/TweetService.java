package epaw.lab4.service;

import epaw.lab4.repository.TweetRepository;
import java.util.List;
import java.util.Optional;
import epaw.lab4.model.Tweet;

public class TweetService {
	
	private static TweetService instance;
	private TweetRepository tweetRepository;
	
	private TweetService() {
        this.tweetRepository = TweetRepository.getInstance();
    }
	
	public static synchronized TweetService getInstance() {
		if (instance == null) {
			instance = new TweetService();
		}
		return instance;
	}
	
	public void add(Tweet tweet) {
		tweetRepository.save(tweet);	
	}
	
	public void delete(Integer id, Integer uid) {
		tweetRepository.delete(id, uid);
	}

	public List<Tweet> getTweetsByUser(Integer uid, Integer start, Integer end) {
		Optional<List<Tweet>> tweets = tweetRepository.findByUser(uid, start, end);
		if (tweets.isPresent()) {
			List<Tweet> list = tweets.get();
			// Por cada tweet, le asignamos sus comentarios
			for (Tweet t : list) {
				t.setComments(tweetRepository.findReplies(t.getId(), uid));
			}
			return list;
		}
		return null;
	}

	public List<Tweet> getFeedTweets(Integer userId, Integer start, Integer end) {
		Optional<List<Tweet>> tweets = tweetRepository.findByFollowedUsers(userId, start, end);
		if (tweets.isPresent()) {
			List<Tweet> list = tweets.get();
			for (Tweet t : list) {
				t.setComments(tweetRepository.findReplies(t.getId(), userId));
			}
			return list;
		}
		return null;
	}

	public List<Tweet> getGlobalTweets(Integer userId, Integer start, Integer end) {
		Optional<List<Tweet>> tweets = tweetRepository.findAllGlobalTweets(userId, start, end);
		if (tweets.isPresent()) {
			List<Tweet> list = tweets.get();
			for (Tweet t : list) {
				t.setComments(tweetRepository.findReplies(t.getId(), userId));
			}
			return list;
		}
		return null;
	}

	public void toggleLike(Integer userId, Integer tweetId) {
		if (tweetRepository.isLikedByUser(userId, tweetId)) {
			tweetRepository.unlikeTweet(userId, tweetId);
		} else {
			tweetRepository.likeTweet(userId, tweetId);
		}
	}
}