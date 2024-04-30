package com.example.eshop.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

//    @Length(min = 1, max = 3)
    @NotBlank(message = "Description is mandatory")
    @Size(min = 100, max = 10000, message = "Description must be less than 2000 characters")
    private String description;

    private String shortDescription;

    private boolean deleted;

    @Positive(message = "Price must be positive")
    private Double price;

    @Range(min = 0, max = 100, message = "Quantity limited")
    private Integer quantity;

    private Instant createAt;

    @ManyToOne
    private Category category;

}