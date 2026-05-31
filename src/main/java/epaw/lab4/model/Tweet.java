package epaw.lab4.model;

import java.sql.Timestamp;

public class Tweet implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;        // maps to tweet_id in DB
	private int uid;       // maps to user_id in DB
	private String uname;  // joined from users.name
	private String upicture; // joined from users.picture
	private Integer parentId;
	private Integer gameDiscussionId;
	private Timestamp postDateTime; // maps to created_at in DB
	private String content;
	private int likeCount;
	private boolean liked; // true if current user has liked this tweet

	public Tweet() {
	}

	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }

	public int getUid() { return this.uid; }
	public void setUid(int uid) { this.uid = uid; }

	public String getUname() { return this.uname; }
	public void setUname(String uname) { this.uname = uname; }

	public String getUpicture() { return this.upicture; }
	public void setUpicture(String upicture) { this.upicture = upicture; }

	public Integer getParentId() { return parentId; }
	public void setParentId(Integer parentId) { this.parentId = parentId; }

	public Integer getGameDiscussionId() { return gameDiscussionId; }
	public void setGameDiscussionId(Integer gameDiscussionId) { this.gameDiscussionId = gameDiscussionId; }

	public Timestamp getPostDateTime() { return this.postDateTime; }
	public void setPostDateTime(Timestamp postDateTime) { this.postDateTime = postDateTime; }

	public String getContent() { return this.content; }
	public void setContent(String content) { this.content = content; }

	public int getLikeCount() { return likeCount; }
	public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

	public boolean isLiked() { return liked; }
	public void setLiked(boolean liked) { this.liked = liked; }
}
