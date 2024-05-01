package com.example.EcoTS.Controllers.Test;

import com.example.EcoTS.DTOs.Test.BookRequest;
import com.example.EcoTS.DTOs.Test.BookResponse;
import com.example.EcoTS.Models.Test.Books;
import com.example.EcoTS.Services.TestService.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
@RequestMapping("/book-controller")
public class BookController {
    @Autowired
    private BookService booksService;

    @PostMapping("/add")
    @ResponseBody
    public BookResponse addBook(@RequestBody BookRequest bookRequest) {
        Books book = booksService.addBook(bookRequest.toBooks());
        return new BookResponse(book);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public BookResponse updateBook(@PathVariable long id, @RequestBody BookRequest bookRequest) {
        Books updatedBook = booksService.updateBook(id, bookRequest.toBooks());
        return new BookResponse(updatedBook);
    }

    @GetMapping("/author/{author}")
    @ResponseBody
    public List<BookResponse> getBooksByAuthor(@PathVariable String author) {
        List<Books> books = booksService.getBooksByAuthor(author);
        return books.stream().map(BookResponse::new).collect(Collectors.toList());
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public BookResponse deleteBook(@PathVariable long id) {
        Books book = booksService.deleteBook(id);
        return new BookResponse(book);
    }
}

