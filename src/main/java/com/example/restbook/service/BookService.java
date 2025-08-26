package com.example.restbook.service;

import com.example.restbook.dto.BookDto;
import com.example.restbook.entity.Book;
import com.example.restbook.entity.Category;
import com.example.restbook.repository.BookRepository;
import com.example.restbook.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }
    
    /**
     * Get all books with caching
     */
    @Cacheable(value = "allBooks")
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Find book by title and author with caching
     * Cache key is combination of title and author
     */
    @Cacheable(value = "books", key = "#title + '_' + #author")
    public Optional<BookDto> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author)
                .map(this::convertToDto);
    }
    
    /**
     * Find all books by category name with caching
     * Cache key is the category name
     */
    @Cacheable(value = "booksByCategory", key = "#categoryName")
    public List<BookDto> findByCategoryName(String categoryName) {
        return bookRepository.findByCategoryName(categoryName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new book
     * Invalidates caches for book search, category search, and all books
     */
    @CacheEvict(value = {"books", "booksByCategory", "allBooks"}, allEntries = true, beforeInvocation = true)
    public BookDto createBook(BookDto bookDto) {
        // Check if book already exists
        if (bookRepository.existsByTitleAndAuthor(bookDto.getTitle(), bookDto.getAuthor())) {
            throw new RuntimeException("Book with title '" + bookDto.getTitle() + 
                    "' and author '" + bookDto.getAuthor() + "' already exists");
        }
        
        // Get or create category
        Category category = getOrCreateCategory(bookDto.getCategoryName());
        
        // Create book
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setDescription(bookDto.getDescription());
        book.setCategory(category);
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }
    
    /**
     * Update book information
     * Invalidates caches for book search, category search, and all books
     */
    @CacheEvict(value = {"books", "booksByCategory", "allBooks"}, allEntries = true, beforeInvocation = true)
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        
        // Get or create category
        Category category = getOrCreateCategory(bookDto.getCategoryName());
        
        // Update book fields
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setDescription(bookDto.getDescription());
        book.setCategory(category);
        
        Book updatedBook = bookRepository.save(book);
        return convertToDto(updatedBook);
    }
    
    /**
     * Delete book by ID
     * Invalidates caches for book search, category search, and all books
     */
    @CacheEvict(value = {"books", "booksByCategory", "allBooks"}, allEntries = true, beforeInvocation = true)
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
    
    /**
     * Get or create category by name
     */
    private Category getOrCreateCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category(categoryName);
                    return categoryRepository.save(newCategory);
                });
    }
    
    /**
     * Convert Book entity to BookDto
     */
    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setDescription(book.getDescription());
        dto.setCategoryName(book.getCategory().getName());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }
}
