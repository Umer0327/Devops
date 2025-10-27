package com.bookstore.catalog.controller;

import com.bookstore.catalog.model.Book;
import com.bookstore.catalog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // Create new book
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // Update book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
            .map(existingBook -> {
                existingBook.setTitle(bookDetails.getTitle());
                existingBook.setAuthor(bookDetails.getAuthor());
                existingBook.setIsbn(bookDetails.getIsbn());
                existingBook.setPrice(bookDetails.getPrice());
                existingBook.setStockQuantity(bookDetails.getStockQuantity());
                existingBook.setDescription(bookDetails.getDescription());
                existingBook.setCategory(bookDetails.getCategory());
                Book updatedBook = bookRepository.save(existingBook);
                return ResponseEntity.ok(updatedBook);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        return bookRepository.findById(id)
            .map(book -> {
                bookRepository.delete(book);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Catalog Service is running");
    }
}