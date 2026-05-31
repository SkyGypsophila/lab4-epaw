package epaw.lab4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import epaw.lab4.model.User;
import epaw.lab4.repository.UserRepository;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserService {

    private static UserService instance;
    private UserRepository userRepository;

    private UserService() {
        this.userRepository = UserRepository.getInstance();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";

    public Map<String, String> validate(User user) {
        Map<String, String> errors = new HashMap<>();

        // Validate Name
        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Username cannot be empty.");
        } else if (name.length() < 5 || name.length() > 20) {
            errors.put("name", "Username must be between 5 and 20 characters.");
        } else if (userRepository.existsByUsername(name)) {
            errors.put("name", "Username already exists.");
        }

        // Validate Password
        String password = user.getPassword();
        if (password == null || !password.matches(PASSWORD_REGEX)) {
            errors.put("password",
                    "Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*).");
        }

        // Validate Email
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "The email format is not valid.");
        } else if (userRepository.existsByEmail(user.getEmail())) {
            errors.put("email", "Email already registered.");
        }

        // Validate Nickname
        if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
            errors.put("nickname", "Nickname cannot be empty.");
        } else if (userRepository.existsByNickname(user.getNickname())) {
            errors.put("nickname", "Nickname already taken.");
        }

        // Validate Surname
        if (user.getSurname() == null || user.getSurname().trim().isEmpty()) {
            errors.put("surname", "Surname cannot be empty.");
        }

        // Validate Birth Date
        if (user.getBirthDate() == null || user.getBirthDate().trim().isEmpty()) {
            errors.put("birthDate", "You must enter a birth date.");
        }

        return errors;
    }

    public Map<String, String> register(User user) {
        Map<String, String> errors = validate(user);
        if (errors.isEmpty()) {
            userRepository.save(user);
        }
        return errors;
    }

    public Map<String, String> login(User user) {
        Map<String, String> errors = new HashMap<>();
        if (!userRepository.checkLogin(user)) {
            errors.put("password", "The combination of name and password does not match in our database");
        }
        return errors;
    }

    // Get all users
    public List<User> getAllUsers() {
        Optional<List<User>> users = userRepository.findAll();
        if (users.isPresent())
            return users.get();
        return null;
    }

    // Get followed users
    public List<User> getFollowedUsers(Integer id, Integer start, Integer end) {
        Optional<List<User>> users = userRepository.findFollowed(id, start, end);
        if (users.isPresent())
            return users.get();
        return null;
    }

    // Get unfollowed users
    public List<User> getNotFollowedUsers(Integer id, Integer start, Integer end) {
        Optional<List<User>> users = userRepository.findNotFollowed(id, start, end);
        if (users.isPresent())
            return users.get();
        return null;
    }

    // Follow User
    public void follow(Integer uid, Integer fid) {
        userRepository.followUser(uid, fid);
    }

    // Unfollow User
    public void unfollow(Integer uid, Integer fid) {
        userRepository.unfollowUser(uid, fid);
    }

    // Check if user is banned
    public boolean isBanned(Integer userId) {
        return userRepository.isBanned(userId);
    }

    // Get ban reason
    public String getBanReason(Integer userId) {
        return userRepository.getBanReason(userId);
    }

    // Ban a user
    public void banUser(Integer bannedUserId, Integer adminId, String reason) {
        userRepository.banUser(bannedUserId, adminId, reason);
    }

    // Unban a user
    public void unbanUser(Integer bannedUserId) {
        userRepository.unbanUser(bannedUserId);
    }

    // Get all users with ban status (for admin panel)
    public List<User> getAllUsersWithBanStatus(Integer adminId) {
        Optional<List<User>> users = userRepository.findAllWithBanStatus(adminId);
        if (users.isPresent())
            return users.get();
        return null;
    }

    public String saveProfilePicture(Part filePart, String username) {
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }

        try {
            String fileName = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = username + extension;

            String resourcesDir = "EXTERNAL_RESOURCES";
            Files.createDirectories(Paths.get(resourcesDir));

            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(resourcesDir, newFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
