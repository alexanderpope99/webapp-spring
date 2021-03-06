package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
  
  @Autowired
  private ApplicationContext context;

  public Object getBean(String name) {
    return context.getBean(name);
  }
}
