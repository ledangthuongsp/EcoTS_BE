package com.example.EcoTS.Controllers.Test;

import com.example.EcoTS.Models.Test.Books;
import com.example.EcoTS.Services.TestService.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-rest-controller")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody Books book) {
        bookService.addBook(book);
        return new ResponseEntity<>("Book added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable long id, @RequestBody Books book) {
        bookService.updateBook(id, book);
        return new ResponseEntity<>("Book updated successfully", HttpStatus.OK);
    }

    @GetMapping("/{author}")
    public ResponseEntity<List<Books>> getBooksByAuthor(@PathVariable String author) {
        List<Books> books = bookService.getBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
    }
}
