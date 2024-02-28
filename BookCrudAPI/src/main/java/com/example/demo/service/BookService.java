package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.BookDTO;

public interface BookService {
	List<BookDTO> getAllBooks();
	BookDTO getBookById(Long id);
	void saveBook(BookDTO bookDTO);
	void updateBook(Long id, BookDTO updatedBookDTO);
	void deleteBook(Long id);
}
