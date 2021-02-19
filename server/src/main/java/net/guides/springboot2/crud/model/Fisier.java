package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "fisier")
public class Fisier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@JsonBackReference(value = "fisier-societate")
	@OneToOne(mappedBy = "imagine", fetch = FetchType.LAZY)
	private Societate societati;

  private String nume;

  private String tip;

  @Lob
  private byte[] data;

  public Fisier() {
  }

  public Fisier(String nume, String tip, byte[] data) {
    this.nume = nume;
    this.tip = tip;
    this.data = data;
  }

  public int getId() {
    return id;
  }

  public String getNume() {
    return nume;
  }

  public void setNume(String nume) {
    this.nume = nume;
  }

  public String getType() {
    return tip;
  }

  public void setType(String tip) {
    this.tip = tip;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}
