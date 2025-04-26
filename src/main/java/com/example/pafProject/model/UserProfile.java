package com.example.pafProject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "user_profiles")
public class UserProfile {
    @Id
    private String id;

    private String name;
    private LocalDate birthdate;
    private List<String> skills;
    private String bio;
    private String filePath;
    private String fileType;

    // Full Constructor
    public UserProfile(String name, LocalDate birthdate, List<String> skills, String bio, String filePath, String fileType) {
        this.name = name;
        this.birthdate = birthdate;
        this.skills = skills;
        this.bio = bio;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    // Getters and Setters
    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthdate() { return birthdate; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}
