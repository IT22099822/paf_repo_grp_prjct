package com.example.pafProject.controller;

import com.example.pafProject.model.SkillPostShare;
import com.example.pafProject.repository.SkillPostShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/skill_post_share")
public class SkillPostShareController {

    private static final String UPLOAD_DIR = "skill_uploads/";

    @Autowired
    private SkillPostShareRepository skillPostShareRepository;

    // CREATE - share a skill post
    @PostMapping
    public ResponseEntity<SkillPostShare> shareSkillPost(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("title") String title,
                                                         @RequestParam("description") String description,
                                                         @RequestParam("category") String category,
                                                         @RequestParam("tags") String tags) throws IOException {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        SkillPostShare skillPostShare = new SkillPostShare(title, description, filePath, file.getContentType(), category, tags);
        SkillPostShare saved = skillPostShareRepository.save(skillPostShare);
        return ResponseEntity.ok(saved);
    }

    // READ - get all skill posts
    @GetMapping
    public List<SkillPostShare> getAllSkillPosts() {
        return skillPostShareRepository.findAll();
    }

    // UPDATE - update a skill post and/or its file
    @PutMapping("/{id}")
    public ResponseEntity<SkillPostShare> updateSkillPost(@PathVariable String id,
                                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("description") String description,
                                                          @RequestParam("category") String category,
                                                          @RequestParam("tags") String tags) throws IOException {

        Optional<SkillPostShare> optionalSkillPostShare = skillPostShareRepository.findById(id);

        if (!optionalSkillPostShare.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        SkillPostShare existingPost = optionalSkillPostShare.get();
        existingPost.setTitle(title);
        existingPost.setDescription(description);
        existingPost.setCategory(category);
        existingPost.setTags(tags);

        if (file != null) {
            // Delete the old file
            Path oldFilePath = Paths.get(existingPost.getFilePath());
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }

            // Upload the new file
            String newFilePath = UPLOAD_DIR + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);

            existingPost.setFilePath(newFilePath);
            existingPost.setFileType(file.getContentType());
        }

        SkillPostShare updated = skillPostShareRepository.save(existingPost);
        return ResponseEntity.ok(updated);
    }

    // DELETE - delete a skill post and its file
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillPost(@PathVariable String id) throws IOException {

        Optional<SkillPostShare> optionalSkillPostShare = skillPostShareRepository.findById(id);

        if (!optionalSkillPostShare.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        SkillPostShare existingPost = optionalSkillPostShare.get();

        // Delete the physical file
        Path filePath = Paths.get(existingPost.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Delete from MongoDB
        skillPostShareRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
