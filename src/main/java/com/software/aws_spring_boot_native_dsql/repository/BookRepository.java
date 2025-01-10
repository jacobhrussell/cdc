package com.software.aws_spring_boot_native_dsql.repository;

import com.software.aws_spring_boot_native_dsql.model.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends PagingAndSortingRepository<BookEntity, UUID>, CrudRepository<BookEntity, UUID> {
}
