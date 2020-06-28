package com.sec.billing.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.billing.Category;
import com.sec.billing.repository.CategoryRepository;

@Service
public class CategoryService {

  private CategoryRepository categoryRepository;

  @Autowired
  public void setCategoryRepository(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<Category> findAll() {
    List<Category> result = categoryRepository.findAll();
    Collections.sort(result);
    return result;
  }

  public boolean save(String category) {
    Category c = categoryRepository.findByName(category);
    if(c == null) {
      categoryRepository.save(new Category(category));
      return true;
    }
    return false;
  }

  public Category findById(long id) {
    return categoryRepository.findOne(id);
  }
}