package com.example.pafProject.repository;

import com.example.pafProject.model.ClassPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassPackageRepository extends MongoRepository<ClassPackage, String> {
}
