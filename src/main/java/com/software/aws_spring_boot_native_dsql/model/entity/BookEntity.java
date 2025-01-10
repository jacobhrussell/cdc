package com.software.aws_spring_boot_native_dsql.model.entity;

import com.software.aws_spring_boot_native_dsql.model.api.Book;
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
