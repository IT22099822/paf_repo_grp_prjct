package com.example.pafProject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "class_packages")
public class ClassPackage {
    @Id
    private String id;

    private String title;
    private String description;
    private String category;
    private double price;
    private String filePath;
    private String fileType;
    private String schedule; // e.g., "Self-paced"
    private String duration;

    // Full Constructor
    public ClassPackage(String title, String description, String category, double price, String filePath, String fileType, String schedule, String duration) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.filePath = filePath;
        this.fileType = fileType;
        this.schedule = schedule;
        this.duration = duration;
    }

    // Getters and Setters
    public String getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
