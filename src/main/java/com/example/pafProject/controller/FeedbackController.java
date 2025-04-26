package com.example.pafProject.controller;

import com.example.pafProject.model.Feedback;
import com.example.pafProject.repository.FeedbackRepository;
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
@RequestMapping("/api/feedback")
public class FeedbackController {

    private static final String UPLOAD_DIR = "feedback_uploads/";

    @Autowired
    private FeedbackRepository feedbackRepository;

    // CREATE - create feedback
    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestParam("file") MultipartFile file,
            @RequestParam("comment") String comment,
            @RequestParam("rating") int rating,
            @RequestParam("category") String category) throws IOException {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists())
            uploadDir.mkdirs();

        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        Feedback feedback = new Feedback(comment, rating, category, filePath, file.getContentType());
        Feedback saved = feedbackRepository.save(feedback);
        return ResponseEntity.ok(saved);
    }

    // READ - get all feedbacks
    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    // UPDATE - update feedback and/or its file
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable String id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("comment") String comment,
            @RequestParam("rating") int rating,
            @RequestParam("category") String category) throws IOException {

        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);

        if (!optionalFeedback.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Feedback existingFeedback = optionalFeedback.get();
        existingFeedback.setComment(comment);
        existingFeedback.setRating(rating);
        existingFeedback.setCategory(category);

        if (file != null) {
            // Delete the old file
            Path oldFilePath = Paths.get(existingFeedback.getFilePath());
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }

            // Upload the new file
            String newFilePath = UPLOAD_DIR + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);

            existingFeedback.setFilePath(newFilePath);
            existingFeedback.setFileType(file.getContentType());
        }

        Feedback updated = feedbackRepository.save(existingFeedback);
        return ResponseEntity.ok(updated);
    }

    // DELETE - delete feedback and its file
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String id) throws IOException {

        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);

        if (!optionalFeedback.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Feedback existingFeedback = optionalFeedback.get();

        // Delete the physical file
        Path filePath = Paths.get(existingFeedback.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Delete from MongoDB
        feedbackRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
