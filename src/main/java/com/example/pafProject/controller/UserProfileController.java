package com.example.pafProject.controller;

import com.example.pafProject.model.UserProfile;
import com.example.pafProject.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user_profile")
public class UserProfileController {

    private static final String UPLOAD_DIR = "profile_uploads/";

    @Autowired
    private UserProfileRepository userProfileRepository;

    // CREATE - create a user profile
    @PostMapping
    public ResponseEntity<UserProfile> createUserProfile(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("name") String name,
                                                         @RequestParam("birthdate") String birthdate,
                                                         @RequestParam("skills") String skills,
                                                         @RequestParam("bio") String bio) throws IOException {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        // Parse birthdate and skills
        LocalDate parsedBirthdate = LocalDate.parse(birthdate);
        List<String> skillList = Arrays.asList(skills.split(","));

        UserProfile userProfile = new UserProfile(name, parsedBirthdate, skillList, bio, filePath, file.getContentType());
        UserProfile saved = userProfileRepository.save(userProfile);
        return ResponseEntity.ok(saved);
    }

    // READ - get all user profiles
    @GetMapping
    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    // UPDATE - update a user profile and/or its file
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable String id,
                                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                                         @RequestParam("name") String name,
                                                         @RequestParam("birthdate") String birthdate,
                                                         @RequestParam("skills") String skills,
                                                         @RequestParam("bio") String bio) throws IOException {

        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(id);

        if (!optionalUserProfile.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UserProfile existingProfile = optionalUserProfile.get();
        existingProfile.setName(name);
        existingProfile.setBirthdate(LocalDate.parse(birthdate));
        existingProfile.setSkills(Arrays.asList(skills.split(",")));
        existingProfile.setBio(bio);

        if (file != null) {
            // Delete the old file
            Path oldFilePath = Paths.get(existingProfile.getFilePath());
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }

            // Upload the new file
            String newFilePath = UPLOAD_DIR + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);

            existingProfile.setFilePath(newFilePath);
            existingProfile.setFileType(file.getContentType());
        }

        UserProfile updated = userProfileRepository.save(existingProfile);
        return ResponseEntity.ok(updated);
    }

    // DELETE - delete a user profile and its file
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable String id) throws IOException {

        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(id);

        if (!optionalUserProfile.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UserProfile existingProfile = optionalUserProfile.get();

        // Delete the physical file
        Path filePath = Paths.get(existingProfile.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Delete from MongoDB
        userProfileRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
