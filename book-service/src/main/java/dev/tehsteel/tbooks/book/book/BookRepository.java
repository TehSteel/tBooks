package dev.tehsteel.tbooks.book.book;

import dev.tehsteel.tbooks.book.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
	Optional<Book> findByName(String name);
}
