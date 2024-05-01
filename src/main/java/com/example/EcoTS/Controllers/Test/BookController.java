package com.example.EcoTS.Controllers.Test;

import com.example.EcoTS.DTOs.Test.BookRequest;
import com.example.EcoTS.Models.Test.Books;
import com.example.EcoTS.Services.TestService.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/api/book-controller")
public class BookController {
    @Autowired
    private BookService booksService;

    @PostMapping("/add")
    public String addBook(@ModelAttribute("bookRequest") BookRequest bookRequest) {
        booksService.addBook(bookRequest.toBooks());
        return "redirect:/books";
    }

    @PutMapping("/update/{id}")
    public String updateBook(@PathVariable long id, @ModelAttribute("bookRequest") BookRequest bookRequest) {
        booksService.updateBook(id, bookRequest.toBooks());
        return "redirect:/books";
    }

    @GetMapping("/author/{author}")
    public String getBooksByAuthor(@PathVariable String author, Model model) {
        List<Books> books = booksService.getBooksByAuthor(author);
        model.addAttribute("books", books);
        return "books-list";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        booksService.deleteBook(id);
        return "redirect:/books";
    }

}
