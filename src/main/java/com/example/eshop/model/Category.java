package com.example.eshop.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z-]*$", message = "Name must contain only English letters and hyphen")
    private String name;

//    todo а как решить подругому ? типо разобраться с сесией как действовать
//    @OneToMany(mappedBy = "category")
//    private List<Product> products;

}