package com.bookstore.catalog.repository;

import com.bookstore.catalog.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
    // Add custom queries if needed
}