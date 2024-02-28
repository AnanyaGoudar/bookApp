package com.example.demo.service;
 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao. DataIntegrityViolationException;
import com.example.demo.entity.Book;
import com.example.demo.dto.BookDTO;
import com.example.demo.repository.BookRepository;
 
@Service
public class BookServiceImpl implements BookService {
 
    @Autowired
    private BookRepository bookRepository;
 
    @Override
    public List<BookDTO> getAllBooks() {
    	List<Book> books = bookRepository.findAll();
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
 
    @Override
    public BookDTO getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.map(this::convertToDTO).orElse(null);
    }
 
    @Override
    public void saveBook(BookDTO bookDTO) {
    	try {
    		if(bookDTO.getId()==null) {
    			Book existingBook = bookRepository.findByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor());
	    		if(existingBook == null) {
	    			Book book = convertToEntity(bookDTO);
	                bookRepository.save(book);
	                System.out.println("Book saved successfully!!");
	    		}else {
	        		throw new IllegalArgumentException("Book with same title and author already exists");
	        	}
	    	}else {
	    		throw new IllegalArgumentException("Book should not have an id");
	    	}
    	}catch(Exception e) {
    		System.out.println("Error saving book: "+e.getMessage());
    		throw e;
    	}
    }
 
    @Override
    public void updateBook(Long id, BookDTO updatedBookDTO) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isPresent()) {
        	Book existingBook = optionalBook.get();
        	BeanUtils.copyProperties(updatedBookDTO, existingBook);
        	bookRepository.save(existingBook);
        }else {
        	throw new IllegalArgumentException("Book not found with ID: "+id);
    	}
    }
 
    @Override
    public void deleteBook(Long id) {
    	Optional<Book> optionalBook = bookRepository.findById(id);
    	if(optionalBook.isPresent()) {
    		bookRepository.deleteById(id);
    	}else {
    		throw new IllegalArgumentException("Book not found with ID: "+id);
    	}       
    }
    
    private BookDTO convertToDTO(Book book){
    	BookDTO bookDTO = new BookDTO();
    	BeanUtils.copyProperties(book, bookDTO);
    	return bookDTO;	
    }
    
    private List<BookDTO> convertToDTOList(List<Book> books){
    	return books.stream(). map(this::convertToDTO).toList();
    }
    
    private Book convertToEntity(BookDTO bookDTO){
    	Book book = new Book();
    	BeanUtils.copyProperties(bookDTO, book);
    	return book;	
    }
}