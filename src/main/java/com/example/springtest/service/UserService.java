package com.example.springtest.service;

import com.example.springtest.dto.UserRequest;
import com.example.springtest.dto.UserResponse;
import com.example.springtest.entity.User;
import com.example.springtest.exception.ResourceNotFoundException;
import com.example.springtest.mapper.UserMapper;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Create a new user
     */
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating new user with email: {}", userRequest.getEmail());
        
        validateUserRequest(userRequest);
        checkEmailUniqueness(userRequest.getEmail());
        
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        User savedUser = userRepository.save(user);
        log.info("User created with id: {} and UUID: {}", savedUser.getId(), savedUser.getUuid());
        
        return userMapper.toResponse(savedUser);
    }

    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);
        User user = findUserById(id);
        return userMapper.toResponse(user);
    }

    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);
        User user = findUserByEmail(email);
        return userMapper.toResponse(user);
    }

    /**
     * Get user by UUID
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByUuid(String uuid) {
        log.debug("Fetching user with UUID: {}", uuid);
        User user = findUserByUuid(uuid);
        return userMapper.toResponse(user);
    }

    /**
     * Update an existing user
     */
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        log.info("Updating user with id: {}", id);
        
        validateUserRequest(userRequest);
        User user = findUserById(id);
        
        // Check if email is being changed and if so, check uniqueness
        if (!user.getEmail().equals(userRequest.getEmail())) {
            checkEmailUniqueness(userRequest.getEmail());
        }

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        User updatedUser = userRepository.save(user);
        log.info("User with id: {} updated successfully", id);
        
        return userMapper.toResponse(updatedUser);
    }

    /**
     * Delete a user
     */
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User user = findUserById(id);
        userRepository.delete(user);
        log.info("User with id: {} deleted successfully", id);
    }

    // ====================== Private Helper Methods ======================

    /**
     * Find user by ID or throw exception
     */
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Find user by email or throw exception
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Find user by UUID or throw exception
     */
    private User findUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with UUID: " + uuid));
    }

    /**
     * Validate user request
     */
    private void validateUserRequest(UserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("User request cannot be null");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Check if email already exists
     */
    private void checkEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
    }

    /**
     * Validate email format (basic validation)
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
