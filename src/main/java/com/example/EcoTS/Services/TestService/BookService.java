package com.example.EcoTS.Services.TestService;

import com.example.EcoTS.Models.Test.Books;
import com.example.EcoTS.Repositories.TestRepository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void addBook(Books book) {
        bookRepository.save(book);
    }

    public void updateBook(long id, Books book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Book not found");
        }
    }

    public List<Books> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }
}
