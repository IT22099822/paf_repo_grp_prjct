package com.example.pafProject.controller;

import com.example.pafProject.model.ClassPackage;
import com.example.pafProject.repository.ClassPackageRepository;
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
@RequestMapping("/api/class_package")
public class ClassPackageController {

    private static final String UPLOAD_DIR = "class_package_uploads/";

    @Autowired
    private ClassPackageRepository classPackageRepository;

    // CREATE - create a class package
    @PostMapping
    public ResponseEntity<ClassPackage> createClassPackage(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("title") String title,
                                                           @RequestParam("description") String description,
                                                           @RequestParam("category") String category,
                                                           @RequestParam("price") double price,
                                                           @RequestParam("schedule") String schedule,
                                                           @RequestParam("duration") String duration) throws IOException {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        ClassPackage classPackage = new ClassPackage(title, description, category, price, filePath, file.getContentType(), schedule, duration);
        ClassPackage saved = classPackageRepository.save(classPackage);
        return ResponseEntity.ok(saved);
    }

    // READ - get all class packages
    @GetMapping
    public List<ClassPackage> getAllClassPackages() {
        return classPackageRepository.findAll();
    }

    // UPDATE - update a class package and/or its file
    @PutMapping("/{id}")
    public ResponseEntity<ClassPackage> updateClassPackage(@PathVariable String id,
                                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                                           @RequestParam("title") String title,
                                                           @RequestParam("description") String description,
                                                           @RequestParam("category") String category,
                                                           @RequestParam("price") double price,
                                                           @RequestParam("schedule") String schedule,
                                                           @RequestParam("duration") String duration) throws IOException {

        Optional<ClassPackage> optionalClassPackage = classPackageRepository.findById(id);

        if (!optionalClassPackage.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ClassPackage existingPackage = optionalClassPackage.get();
        existingPackage.setTitle(title);
        existingPackage.setDescription(description);
        existingPackage.setCategory(category);
        existingPackage.setPrice(price);
        existingPackage.setSchedule(schedule);
        existingPackage.setDuration(duration);

        if (file != null) {
            // Delete the old file
            Path oldFilePath = Paths.get(existingPackage.getFilePath());
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }

            // Upload the new file
            String newFilePath = UPLOAD_DIR + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);

            existingPackage.setFilePath(newFilePath);
            existingPackage.setFileType(file.getContentType());
        }

        ClassPackage updated = classPackageRepository.save(existingPackage);
        return ResponseEntity.ok(updated);
    }

    // DELETE - delete a class package and its file
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassPackage(@PathVariable String id) throws IOException {

        Optional<ClassPackage> optionalClassPackage = classPackageRepository.findById(id);

        if (!optionalClassPackage.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ClassPackage existingPackage = optionalClassPackage.get();

        // Delete the physical file
        Path filePath = Paths.get(existingPackage.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Delete from MongoDB
        classPackageRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
