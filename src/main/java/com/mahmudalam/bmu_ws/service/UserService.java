package com.mahmudalam.bmu_ws.service;

import com.mahmudalam.bmu_ws.dto.request.ChangePasswordRequest;
import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.User;
import com.mahmudalam.bmu_ws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse<Page<User>> getAllUsers(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userRepository.findAll(pageable);
            return new UserResponse<>(true, users, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch users: " + e.getMessage());
        }
    }

    public UserResponse<User> getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> new UserResponse<>(true, user, null))
                    .orElse(new UserResponse<>(false, null, "User not found with ID: " + id));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to retrieve userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> createUser(User createdUser) {
        try {
            createdUser.setEmail(createdUser.getEmail().toLowerCase());
            createdUser.setPassword(passwordEncoder.encode(createdUser.getPassword()));
            User created = userRepository.save(createdUser);
            return new UserResponse<>(true, created, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to create userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> putUpdateUser(Long id, User updatedUser) {
        try {
            return userRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setEmail(updatedUser.getEmail().toLowerCase());
                        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                        existingUser.setFirstName(updatedUser.getFirstName());
                        existingUser.setLastName(updatedUser.getLastName());
                        existingUser.setPhone(updatedUser.getPhone());
                        existingUser.setAvatar(updatedUser.getAvatar());
                        existingUser.setIsActive(updatedUser.getIsActive());
                        existingUser.setDob(updatedUser.getDob());
                        existingUser.setGender(updatedUser.getGender());
                        existingUser.setAddress(updatedUser.getAddress());
                        existingUser.setRole(updatedUser.getRole());

                        userRepository.save(existingUser);

                        return new UserResponse<>(true, existingUser, null);
                    }).orElse(new UserResponse<>(false, null, "User not found to update"));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> patchUpdateUser(Long id, User updatedUser) {
        try {
            return userRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setEmail(updatedUser.getEmail() != null ? updatedUser.getEmail().toLowerCase() : existingUser.getEmail().toLowerCase());
                        existingUser.setPassword(updatedUser.getPassword() != null ? passwordEncoder.encode(updatedUser.getPassword()) : existingUser.getPassword());
                        existingUser.setFirstName(updatedUser.getFirstName() != null ? updatedUser.getFirstName() : existingUser.getFirstName());
                        existingUser.setLastName(updatedUser.getLastName() != null ? updatedUser.getLastName() : existingUser.getLastName());
                        existingUser.setPhone(updatedUser.getPhone() != null ? updatedUser.getPhone() : existingUser.getPhone());
                        existingUser.setAvatar(updatedUser.getAvatar() != null ? updatedUser.getAvatar() : existingUser.getAvatar());
                        existingUser.setIsActive(updatedUser.getIsActive() != null ? updatedUser.getIsActive() : existingUser.getIsActive());
                        existingUser.setDob(updatedUser.getDob() != null ? updatedUser.getDob() : existingUser.getDob());
                        existingUser.setGender(updatedUser.getGender() != null ? updatedUser.getGender() : existingUser.getGender());
                        existingUser.setAddress(updatedUser.getAddress() != null ? updatedUser.getAddress() : existingUser.getAddress());
                        existingUser.setRole(updatedUser.getRole() != null ? updatedUser.getRole() : existingUser.getRole());

                        userRepository.save(existingUser);
                        return new UserResponse<>(true, existingUser, null);
                    })
                    .orElse(new UserResponse<>(false, null, "User not found to update"));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> getUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return new UserResponse<>(false, null, "User not found");
            }
            return new UserResponse<>(true, user, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch user details: " + e.getMessage());
        }
    }

    public UserResponse<User> putUpdateProfile(String email, User updatedUser) {
        try {
            User existingUser = userRepository.findByEmail(email);
            if (existingUser == null) {
                return new UserResponse<>(false, null, "User not found");
            }

            existingUser.setEmail(updatedUser.getEmail().toLowerCase());
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAvatar(updatedUser.getAvatar());
            existingUser.setIsActive(updatedUser.getIsActive());
            existingUser.setDob(updatedUser.getDob());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setAddress(updatedUser.getAddress());

            userRepository.save(existingUser);
            return new UserResponse<>(true, existingUser, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update profile: " + e.getMessage());
        }
    }

    public UserResponse<User> patchUpdateProfile(String email, User updatedUser) {
        try {
            User existingUser = userRepository.findByEmail(email);
            if (existingUser == null) {
                return new UserResponse<>(false, null, "User not found");
            }

            if (updatedUser.getEmail() != null)
                existingUser.setEmail(updatedUser.getEmail().toLowerCase());
            if (updatedUser.getPassword() != null)
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            if (updatedUser.getFirstName() != null)
                existingUser.setFirstName(updatedUser.getFirstName());
            if (updatedUser.getLastName() != null)
                existingUser.setLastName(updatedUser.getLastName());
            if (updatedUser.getPhone() != null)
                existingUser.setPhone(updatedUser.getPhone());
            if (updatedUser.getAvatar() != null)
                existingUser.setAvatar(updatedUser.getAvatar());
            if (updatedUser.getIsActive() != null)
                existingUser.setIsActive(updatedUser.getIsActive());
            if (updatedUser.getDob() != null)
                existingUser.setDob(updatedUser.getDob());
            if (updatedUser.getGender() != null)
                existingUser.setGender(updatedUser.getGender());
            if (updatedUser.getAddress() != null)
                existingUser.setAddress(updatedUser.getAddress());

            userRepository.save(existingUser);
            return new UserResponse<>(true, existingUser, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update profile: " + e.getMessage());
        }
    }

    public UserResponse<String> changeActiveStatus(Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> {
                        user.setIsActive(!user.getIsActive());

                        userRepository.save(user);
                        return new UserResponse<>(true, "User active status: " + user.getIsActive(), null);
                    })
                    .orElse(new UserResponse<>(false, null, "User not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to change active status: " + e.getMessage());
        }
    }

    public UserResponse<String> changeRole(Long id, String strRole) {
        try {
            User.Role role = User.Role.valueOf(strRole.toUpperCase());
            return userRepository.findById(id)
                    .map(user -> {
                        user.setRole(role);

                        userRepository.save(user);
                        return new UserResponse<>(true, user.getFirstName() + " " + user.getLastName() + "'s role changed to " + user.getRole(), null);
                    })
                    .orElse(new UserResponse<>(false, null, "User not found."));
        } catch (IllegalArgumentException e) {
            String validRoles = Arrays.stream(User.Role.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            return new UserResponse<>(false, null, "Invalid role: " + strRole + ". Valid roles are: " + validRoles);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to change role: " + e.getMessage());
        }
    }

    public UserResponse<String> changePassword(String email, ChangePasswordRequest request) {
        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return new UserResponse<>(false, null, "New Password and Confirm Password do not match.");
            }

            User user = userRepository.findByEmail(email);
            if (user == null) {
                return new UserResponse<>(false, null, "User not found.");
            }

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return new UserResponse<>(false, null, "Old password is incorrect.");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return new UserResponse<>(true, "Password changed successfully.", null);

        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to change password: " + e.getMessage());
        }
    }

    public UserResponse<String> changeAvatar(String email, MultipartFile file) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return new UserResponse<>(false, null, "User not found.");
            }

            if (file.isEmpty()) {
                return new UserResponse<>(false, null, "File is empty.");
            }

            // Create directory if not exists
//            String uploadDir = "uploads/avatars/";
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "avatars" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.exists() && !dir.mkdirs()) {
                return new UserResponse<>(false, null, "Failed to create upload directory.");
            }

            // Unique filename: userID_timestamp_originalFilename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = "avatar_" + user.getId() + "_" + System.currentTimeMillis() + fileExtension;
            String filePath = uploadDir + newFileName;

            // Save file locally
            File dest = new File(filePath);
            file.transferTo(dest);

            // Save relative path or absolute URL
            user.setAvatar("/" + filePath.replace("\\", "/"));  // Use relative path for serving later
            userRepository.save(user);

            return new UserResponse<>(true, "Avatar updated successfully.", null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to upload avatar: " + e.getMessage());
        }
    }

    public UserResponse<String> resetPassword(Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> {
                        user.setPassword(passwordEncoder.encode("1234"));
                        userRepository.save(user);

                        return new UserResponse<>(true, "Password reset successfully.", null);
                    })
                    .orElse(new UserResponse<>(false, null, "User not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to password reset: " + e.getMessage());
        }
    }
}