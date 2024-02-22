package dev.tehsteel.tbooks.book.book.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private String author;

	/* Whenever the book was added to the site */
	private Date addDate;
	
	/* Whenever the book was published */
	private Date creationDate;
}
