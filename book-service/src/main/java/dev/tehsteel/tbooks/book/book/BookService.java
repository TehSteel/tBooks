package dev.tehsteel.tbooks.book.book;

import dev.tehsteel.tbooks.book.book.model.Book;
import dev.tehsteel.tbooks.book.book.model.BookCreateRequest;
import dev.tehsteel.tbooks.book.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {
	private final BookRepository bookRepository;
	private final CacheService cacheService;

	public Book createBook(final BookCreateRequest request) {
		log.debug("Creating new user by: {}", request.name());

		if (bookRepository.findByName(request.name()).isPresent()) {
			log.debug("Book is already created by email: {}", request.name());
			return null;
		}

		final Book book = new Book();

		book.setName(request.name());
		book.setDescription(request.description());
		book.setAuthor(request.author());
		book.setCreationDate(request.creationDate());
		book.setAddDate(new Date());

		bookRepository.save(book);
		log.debug("Book {} inserted into the repository", book.getName());

		cacheService.insertValue("books", book.getName(), book);
		log.debug("Book {} cached", book.getName());

		return book;
	}

	public Book getBookByName(final String name) {
		Book book = cacheService.getCachedData("books", name, Book.class);
		if (book != null) {
			log.debug("User found in cache: {}", book.getName());
			return book;
		}

		log.debug("Book not found in cache. Querying repository...");
		book = bookRepository.findByName(name).orElse(null);

		if (book != null) {
			log.debug("Book found in repository: {}", book.getName());
			cacheService.insertValue("books", book.getName(), book);
		} else {
			log.debug("Book not found in repository.");
		}

		return book;
	}
}
