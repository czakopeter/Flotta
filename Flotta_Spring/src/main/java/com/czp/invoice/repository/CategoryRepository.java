package com.czp.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.invoice.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

  List<Category> findAll();

  Category findByName(String category);
}
