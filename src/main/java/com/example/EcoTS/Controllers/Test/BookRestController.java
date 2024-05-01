package com.example.EcoTS.Controllers.Test;

import com.example.EcoTS.Models.Test.Books;
import com.example.EcoTS.Services.TestService.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/book-rest-controller")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Books> addBook(@RequestBody Books book) {
        Books savedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable long id, @RequestBody Books book) {
        Books updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/find/{author}")
    public ResponseEntity<List<Books>> getBooksByAuthor(@PathVariable String author) {
        List<Books> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Books> deleteBook(@PathVariable long id) {
        Books deletedBook = bookService.deleteBook(id);
        return ResponseEntity.ok(deletedBook);
    }
}
