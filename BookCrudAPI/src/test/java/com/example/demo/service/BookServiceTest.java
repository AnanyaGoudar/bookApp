package com.example.demo.service;
 
import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
 
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
 
public class BookServiceTest {
 
    @Mock
    private BookRepository bookRepository;
 
    @InjectMocks
    private BookServiceImpl bookService;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testGetAllBooks() {
        Mockito.when(bookRepository.findAll()).thenReturn(Arrays.asList(
                new Book("Book1", "Author1", 20.0),
                new Book("Book2", "Author2", 25.0)
        ));
        List<BookDTO> result = bookService.getAllBooks();
        assertEquals(2, result.size());
        assertEquals("Book1", result.get(0).getTitle());
        assertEquals("Author2", result.get(1).getAuthor());
    }
 
    @Test
    void testGetBookById() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book("Book1", "Author1", 20.0)));
        BookDTO result = bookService.getBookById(1L);
        assertEquals("Book1", result.getTitle());
        assertEquals("Author1", result.getAuthor());
        assertEquals(20.0, result.getPrice());
    }
 
    @Test
    void testSaveBook() {
        Mockito.when(bookRepository.findByTitleAndAuthor("Book1", "Author1")).thenReturn(null);
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(new Book("Book1", "Author1", 20.0));
        BookDTO bookDTO = new BookDTO(null, "Book1", "Author1", 20.0);
        bookService.saveBook(bookDTO);
        Mockito.verify(bookRepository, Mockito.times(1)).save(any(Book.class));
        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.argThat(book ->
                "Book1".equals(book.getTitle()) && "Author1".equals(book.getAuthor()) && 20.0 == book.getPrice()));
    }
 
    @Test
    void testUpdateBook() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book("Book1", "Author1", 20.0)));
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(new Book("Book1", "Author1", 30.0));
        BookDTO updatedBookDTO = new BookDTO(null, "NewTitle", "NewAuthor", 30.0);
        bookService.updateBook(1L, updatedBookDTO);
        Mockito.verify(bookRepository, Mockito.times(1)).save(any(Book.class));
        Mockito.verify(bookRepository).save(Mockito.argThat(book ->
                "NewTitle".equals(book.getTitle()) && "NewAuthor".equals(book.getAuthor()) && 30.0 == book.getPrice()));
    }
 
    @Test
    void testDeleteBook() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book("Book1", "Author1", 20.0)));
        bookService.deleteBook(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(anyLong());
        Mockito.verify(bookRepository).deleteById(Mockito.eq(1L));
    }
 
    @Test
    void testSaveBookWithExistingTitleAndAuthor() {
        Mockito.when(bookRepository.findByTitleAndAuthor("Book1", "Author1"))
                .thenReturn(new Book("Book1", "Author1", 20.0));
        BookDTO bookDTO = new BookDTO(null, "Book1", "Author1", 30.0);
        assertThrows(IllegalArgumentException.class, () -> bookService.saveBook(bookDTO));
    }
 
    @Test
    void testUpdateBookNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        BookDTO updatedBookDTO = new BookDTO(null, null, null, 30.0);
        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, updatedBookDTO));
    }
 
    @Test
    void testDeleteBookNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(1L));
    }
}