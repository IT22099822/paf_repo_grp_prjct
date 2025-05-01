package com.example.pafProject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "skill_post_shares")
public class SkillPostShare {
    @Id
    private String id;

    private String title;
    private String description;
    private String filePath;
    private String fileType;
    private String category;
    private String tags;

    @CreatedDate
    private Date createdAt;

    // Full Constructor
    public SkillPostShare(String title, String description, String filePath, String fileType, String category, String tags) {
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.fileType = fileType;
        this.category = category;
        this.tags = tags;
        this.createdAt = new Date(); // Fallback in case @CreatedDate doesn't work
    }

    // Getters and Setters
    public String getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
