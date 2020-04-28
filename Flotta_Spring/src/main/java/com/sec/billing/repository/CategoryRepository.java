package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

  List<Category> findAll();

  Category findByName(String category);
}
