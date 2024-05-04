package com.example.EcoTS.Services.TestService;

import com.example.EcoTS.Models.Test.Books;
import com.example.EcoTS.Repositories.TestRepository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Books addBook(Books book) {
        return bookRepository.save(book);
    }
    @Transactional
    public Books updateBook(long id, Books updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setName(updatedBook.getName());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setPublication(updatedBook.getPublication());
                    return bookRepository.save(book);
                }).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Books> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Transactional
    public Books deleteBook(long id) {
        Optional<Books> book = bookRepository.findById(id);
        book.ifPresent(bookRepository::delete);
        return book.orElseThrow(() -> new RuntimeException("Book not found"));
    }
}
