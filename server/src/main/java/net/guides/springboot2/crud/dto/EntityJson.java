package net.guides.springboot2.crud.dto;

import java.util.List;

public class EntityJson {
  private String name;
  private List<String> attributeNames;


  public EntityJson() {
  }

  public EntityJson(String name, List<String> attributeNames) {
    this.name = name;
    this.attributeNames = attributeNames;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getAttributeNames() {
    return this.attributeNames;
  }

  public void setAttributeNames(List<String> attributeNames) {
    this.attributeNames = attributeNames;
  }

}
