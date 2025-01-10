package com.software.aws_spring_boot_native_dsql.service;

import com.software.aws_spring_boot_native_dsql.model.api.Book;
import com.software.aws_spring_boot_native_dsql.model.entity.BookEntity;
import com.software.aws_spring_boot_native_dsql.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> listBooks(int page, int size, String sortBy) {
        Page<BookEntity> bookEntities = bookRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
        return bookEntities.map(Book::of);
    }

    public Optional<Book> getBookById(String id) {
        System.out.println("Getting book by ID in service");
        return bookRepository.findById(UUID.fromString(id)).map(Book::of);
    }

    public Book createBook(Book book) {
        BookEntity bookToSave = BookEntity.of(book);
        BookEntity savedBook = bookRepository.save(bookToSave);
        return Book.of(savedBook);
    }

    public Optional<Book> updateBook(String id, Book book) {
        return bookRepository.findById(UUID.fromString(id)).map(existing -> {
            existing.setTitle(book.getTitle());
            return Book.of(bookRepository.save(existing));
        });
    }

    public boolean deleteBook(String id) {
        if (bookRepository.existsById(UUID.fromString(id))) {
            bookRepository.deleteById(UUID.fromString(id));
            return true;
        }
        return false;
    }

}
