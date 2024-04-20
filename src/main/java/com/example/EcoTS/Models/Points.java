package com.example.EcoTS.Models;

import com.example.EcoTS.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "points")
@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Points {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private double point;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
}
