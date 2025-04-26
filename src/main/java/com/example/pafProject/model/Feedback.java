package com.example.pafProject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feedbacks")
public class Feedback {
    @Id
    private String id;

    private String comment;
    private int rating;
    private String category;
    private String filePath;
    private String fileType;

    // Full Constructor
    public Feedback(String comment, int rating, String category, String filePath, String fileType) {
        this.comment = comment;
        this.rating = rating;
        this.category = category;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
