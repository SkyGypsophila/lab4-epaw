package epaw.lab4.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int roleId;
	private String name;
	private String password;
	private String picture;
	private String role;

	// Fields from the full schema
	private String email;
	private String surname;
	private String nickname;
	private String birthDate;
	private String favoriteGame;
	private String createdAt;
	private String updatedAt;
	private boolean banned;
	private String banReason;

	public User() {
		super();
	}

	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }

	public int getRoleId() { return roleId; }
	public void setRoleId(int roleId) { this.roleId = roleId; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getPicture() { return picture; }
	public void setPicture(String picture) { this.picture = picture; }

	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getSurname() { return surname; }
	public void setSurname(String surname) { this.surname = surname; }

	public String getNickname() { return nickname; }
	public void setNickname(String nickname) { this.nickname = nickname; }

	public String getBirthDate() { return birthDate; }
	public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

	public String getFavoriteGame() { return favoriteGame; }
	public void setFavoriteGame(String favoriteGame) { this.favoriteGame = favoriteGame; }

	public String getCreatedAt() { return createdAt; }
	public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

	public String getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

	public boolean isBanned() { return banned; }
	public void setBanned(boolean banned) { this.banned = banned; }

	public String getBanReason() { return banReason; }
	public void setBanReason(String banReason) { this.banReason = banReason; }
}
