package com.sec.billing.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sec.billing.Category;
import com.sec.billing.MyNode;
import com.sec.billing.repository.BillTemplateRepository;
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