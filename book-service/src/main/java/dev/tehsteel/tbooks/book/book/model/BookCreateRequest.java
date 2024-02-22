package dev.tehsteel.tbooks.book.book.model;

import java.util.Date;

public record BookCreateRequest(String name, String description, String author, Date creationDate) {
}
