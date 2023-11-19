package ca.sheridancollege.charanit.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {
    private String title;
    private String author;
    private Long isbn;
    private double price;
    private String description;
    private String bookImage;
    private int quantity;
}
