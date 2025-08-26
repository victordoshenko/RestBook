package com.example.restbook.repository;

import com.example.restbook.entity.Book;
import com.example.restbook.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Find book by title and author
     */
    Optional<Book> findByTitleAndAuthor(String title, String author);
    
    /**
     * Find all books by category
     */
    List<Book> findByCategory(Category category);
    
    /**
     * Find all books by category name using JPQL
     */
    @Query("SELECT b FROM Book b JOIN b.category c WHERE c.name = :categoryName")
    List<Book> findByCategoryName(@Param("categoryName") String categoryName);
    
    /**
     * Check if book exists by title and author
     */
    boolean existsByTitleAndAuthor(String title, String author);
}
