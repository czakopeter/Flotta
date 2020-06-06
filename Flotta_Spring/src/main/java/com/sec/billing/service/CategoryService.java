package com.sec.billing.service;

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
    return categoryRepository.findAll();
  }

  public void save(String category) {
    Category c = categoryRepository.findByName(category);
    if(c == null) {
      categoryRepository.save(new Category(category));
    }
  }

  public Category findById(long id) {
    return categoryRepository.findOne(id);
  }
}