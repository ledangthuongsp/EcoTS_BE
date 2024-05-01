package com.example.EcoTS.Repositories.TestRepository;

import com.example.EcoTS.Models.Test.Books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {
    List<Books> findByAuthor(String author);
}
