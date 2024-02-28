
package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
 
    @Autowired
    private BookService bookService;
 
    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }
 
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
 
    @PostMapping
    public BookDTO saveBook(@RequestBody BookDTO bookDTO) {
        bookService.saveBook(bookDTO);
        return bookDTO;
    }
 
    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO updatedBookDTO) {
        bookService.updateBook(id, updatedBookDTO);
        return updatedBookDTO;
    }
 
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "Book with ID " + id + " deleted successfully.";
    }
}