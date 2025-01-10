package com.software.aws_spring_boot_native_dsql.model.api;

import com.software.aws_spring_boot_native_dsql.model.entity.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private UUID id;

    private String title;

    public static Book of(BookEntity bookEntity) {
        return Book.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .build();
    }

}
