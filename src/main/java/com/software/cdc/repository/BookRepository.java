package com.software.cdc.repository;

import com.software.cdc.model.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends PagingAndSortingRepository<BookEntity, UUID>, CrudRepository<BookEntity, UUID> {
}
