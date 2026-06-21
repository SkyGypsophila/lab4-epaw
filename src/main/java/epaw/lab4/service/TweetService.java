package epaw.lab4.service;

import epaw.lab4.repository.TweetRepository;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import epaw.lab4.model.Tweet;
import jakarta.servlet.http.Part;

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
	
	public void add(Tweet tweet, Part filePart, String imgDir) {
		tweetRepository.save(tweet);
		String ext = filePart != null ? getExtension(filePart.getSubmittedFileName()) : null;
		if (filePart != null && filePart.getSize() > 0 && ext != null && tweet.getId() != null) {
			String rel = "img/tweets/" + tweet.getId() + ext;
			try {
				Path dest = Paths.get(imgDir, tweet.getId() + ext);
				Files.createDirectories(dest.getParent());
				try (InputStream in = filePart.getInputStream()) {
					Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
				}
				tweetRepository.updateImage(tweet.getId(), rel);
				tweet.setImage(rel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void add(Tweet tweet) {
		add(tweet, null, null);
	}

	public void delete(Integer id, Integer uid, String webappRoot) {
		Tweet t = tweetRepository.findById(id).orElse(null);
		if (t != null && t.getImage() != null && webappRoot != null) {
			deleteImageFile(t.getImage(), webappRoot);
		}
		tweetRepository.delete(id, uid);
	}

	public void delete(Integer id, Integer uid) {
		delete(id, uid, null);
	}

	public void deleteUnrestricted(Integer id, String webappRoot) {
		Tweet t = tweetRepository.findById(id).orElse(null);
		if (t != null && t.getImage() != null && webappRoot != null) {
			deleteImageFile(t.getImage(), webappRoot);
		}
		tweetRepository.deleteUnrestricted(id);
	}

	public void deleteUnrestricted(Integer id) {
		deleteUnrestricted(id, null);
	}

	public void update(Integer id, String content, Part filePart, String imgDir, boolean removeImage) {
		tweetRepository.update(id, content);
		String webappRoot = imgDir != null ? Paths.get(imgDir).getParent().getParent().toString() : null;
		if (removeImage) {
			Tweet t = tweetRepository.findById(id).orElse(null);
			if (t != null && t.getImage() != null) {
				deleteImageFile(t.getImage(), webappRoot);
			}
			tweetRepository.removeImage(id);
		} else if (filePart != null && filePart.getSize() > 0 && imgDir != null) {
			String ext = getExtension(filePart.getSubmittedFileName());
			if (ext == null) return;
			Tweet t = tweetRepository.findById(id).orElse(null);
			if (t != null && t.getImage() != null) {
				deleteImageFile(t.getImage(), webappRoot);
			}
			String rel = "img/tweets/" + id + ext;
			try {
				Path dest = Paths.get(imgDir, id + ext);
				Files.createDirectories(dest.getParent());
				try (InputStream in = filePart.getInputStream()) {
					Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
				}
				tweetRepository.updateImage(id, rel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void update(Integer id, String content) {
		update(id, content, null, null, false);
	}

	public void removeTweetImage(Integer tweetId, String webappRoot) {
		Tweet t = tweetRepository.findById(tweetId).orElse(null);
		if (t != null && t.getImage() != null && webappRoot != null) {
			deleteImageFile(t.getImage(), webappRoot);
		}
		tweetRepository.removeImage(tweetId);
	}

	private void deleteImageFile(String relativePath, String webappRoot) {
		if (relativePath == null || webappRoot == null) return;
		try {
			Path p = Paths.get(webappRoot, relativePath.replace("/", java.io.File.separator));
			Files.deleteIfExists(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getExtension(String filename) {
		if (filename == null) return null;
		int dot = filename.lastIndexOf('.');
		if (dot < 0) return null;
		String ext = filename.substring(dot).toLowerCase();
		if (ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".svg")) return ext;
		return null;
	}

	public Tweet getTweetById(Integer id) {
		return tweetRepository.findById(id).orElse(null);
	}

	public List<Tweet> getTweetsByUser(Integer uid, Integer start, Integer end) {
		return getTweetsByUser(uid, uid, start, end);
	}

	public List<Tweet> getTweetsByUser(Integer uid, Integer viewerId, Integer start, Integer end) {
		Optional<List<Tweet>> tweets = tweetRepository.findByUser(uid, viewerId, start, end);
		if (tweets.isPresent()) {
			List<Tweet> list = tweets.get();
			for (Tweet t : list) {
				t.setComments(tweetRepository.findReplies(t.getId(), viewerId));
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