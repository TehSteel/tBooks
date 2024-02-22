package dev.tehsteel.tbooks.book.book;

import dev.tehsteel.tbooks.book.book.model.Book;
import dev.tehsteel.tbooks.book.book.model.BookCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book")
public class BookController {

	private final BookService bookService;

	@PutMapping("/create")
	public ResponseEntity<String> createBook(@RequestBody final BookCreateRequest request) {
		final Book book = bookService.createBook(request);

		if (book != null) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {

			return new ResponseEntity<>("Book is already registered.", HttpStatus.CONFLICT);
		}
	}
}
