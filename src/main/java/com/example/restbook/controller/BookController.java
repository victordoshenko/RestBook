package com.example.restbook.controller;

import com.example.restbook.dto.BookDto;
import com.example.restbook.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {
    
    private final BookService bookService;
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    /**
     * Get all books
     * GET /api/books
     */
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    /**
     * Find book by title and author
     * GET /api/books/search?title={title}&author={author}
     */
    @GetMapping("/search")
    public ResponseEntity<BookDto> findByTitleAndAuthor(
            @RequestParam String title,
            @RequestParam String author) {
        
        Optional<BookDto> book = bookService.findByTitleAndAuthor(title, author);
        
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Find all books by category name
     * GET /api/books/category/{categoryName}
     */
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<BookDto>> findByCategoryName(
            @PathVariable String categoryName) {
        
        List<BookDto> books = bookService.findByCategoryName(categoryName);
        return ResponseEntity.ok(books);
    }
    
    /**
     * Create a new book
     * POST /api/books
     */
    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        try {
            BookDto createdBook = bookService.createBook(bookDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update book information
     * PUT /api/books/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDto bookDto) {
        
        try {
            BookDto updatedBook = bookService.updateBook(id, bookDto);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete book by ID
     * DELETE /api/books/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
