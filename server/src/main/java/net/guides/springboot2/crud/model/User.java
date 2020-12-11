package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Angajat> angajati;

	@JsonBackReference(value = "user-role")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	@JsonBackReference(value = "user-societati")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_societati", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "societate_id"))
	private List<Societate> societati;

	@Column(name = "gen")
	private boolean gen;

	@JsonBackReference(value = "user-notificari")
	@OneToMany(mappedBy = "user")
	private List<Notificare> notificari;

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public String getEmail() {
		return email;
	}

	public List<Angajat> getAngajati() {
		return angajati;
	}

	public String getPassword() {
		return password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public List<Societate> getSocietati() {
		return societati;
	}

	public String getUsername() {
		return username;
	}

	public boolean isGen() {
		return gen;
	}

	// SETTERS
	public void setEmail(String email) {
		this.email = email;
	}

	public void setAngajati(List<Angajat> angajati) {
		this.angajati = angajati;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setSocietati(List<Societate> societati) {
		this.societati = societati;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setGen(boolean gen) {
		this.gen = gen;
	}

	public void addSocietate(Societate newSocietate) {
		this.societati.add(newSocietate);
	}

	public List<Notificare> getNotificari() {
		return notificari;
	}

	public void setNotificari(List<Notificare> notificari) {
		this.notificari = notificari;
	}
}
