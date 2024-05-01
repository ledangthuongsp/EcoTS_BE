package com.example.EcoTS.DTOs.Test;

import com.example.EcoTS.Models.Test.Books;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponse {
    private long id;
    private String name;
    private String author;
    private LocalDate publication;

    public BookResponse(Books book) {
        this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.publication = book.getPublication();
    }
}
