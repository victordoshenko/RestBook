package com.example.restbook.config;

import com.example.restbook.entity.Book;
import com.example.restbook.entity.Category;
import com.example.restbook.repository.BookRepository;
import com.example.restbook.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public DataInitializer(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create categories
        Category fiction = new Category("Fiction");
        Category nonFiction = new Category("Non-Fiction");
        Category science = new Category("Science");
        Category history = new Category("History");
        
        categoryRepository.save(fiction);
        categoryRepository.save(nonFiction);
        categoryRepository.save(science);
        categoryRepository.save(history);
        
        // Create books
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", fiction);
        book1.setIsbn("978-0743273565");
        book1.setPublicationYear(1925);
        book1.setDescription("A story of the fabulously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.");
        
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", fiction);
        book2.setIsbn("978-0446310789");
        book2.setPublicationYear(1960);
        book2.setDescription("The story of young Scout Finch and her father Atticus in a racially divided Alabama town.");
        
        Book book3 = new Book("A Brief History of Time", "Stephen Hawking", science);
        book3.setIsbn("978-0553380163");
        book3.setPublicationYear(1988);
        book3.setDescription("A popular science book about cosmology and the universe.");
        
        Book book4 = new Book("The Art of War", "Sun Tzu", history);
        book4.setIsbn("978-0140439199");
        book4.setPublicationYear(-500);
        book4.setDescription("An ancient Chinese text on military strategy and tactics.");
        
        Book book5 = new Book("1984", "George Orwell", fiction);
        book5.setIsbn("978-0451524935");
        book5.setPublicationYear(1949);
        book5.setDescription("A dystopian novel about totalitarianism and surveillance society.");
        
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
        
        System.out.println("Sample data initialized successfully!");
    }
}
