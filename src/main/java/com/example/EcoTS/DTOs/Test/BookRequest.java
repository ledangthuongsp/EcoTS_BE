package com.example.EcoTS.DTOs.Test;

import com.example.EcoTS.Models.Test.Books;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    private String name;
    private String author;
    private LocalDate publication;
    public Books toBooks() {
        Books book = new Books();
        book.setName(this.name);
        book.setAuthor(this.author);
        book.setPublication(this.publication);
        return book;
    }
}


