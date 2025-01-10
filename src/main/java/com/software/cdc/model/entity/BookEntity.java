package com.software.cdc.model.entity;

import com.software.cdc.model.api.Book;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "book")
public class BookEntity {

    @Id()
    @GeneratedValue()
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String title;

    public static BookEntity of(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(book.getTitle());
        return bookEntity;
    }

}
