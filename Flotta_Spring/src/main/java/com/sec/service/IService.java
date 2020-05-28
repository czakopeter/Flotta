package com.sec.service;

import java.util.List;

public interface  IService<E> {
  
  public boolean save(E element);
  
  public E findById(long id);
}
