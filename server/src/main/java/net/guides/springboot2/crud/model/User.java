package net.guides.springboot2.crud.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_angajat", referencedColumnName = "idpersoana")
	private Angajat id_angajat;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_societati", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "societate_id"))
	private Set<Societate> societati = new HashSet<>();

	@Column(name = "gen")
	private boolean gen;

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	// GETTERS
	public String getEmail() {
		return email;
	}
	public Angajat getId_angajat() {
		return id_angajat;
	}
	public String getPassword() {
		return password;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public Set<Societate> getSocietati() {
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
	public void setId_angajat(Angajat id_angajat) {
		this.id_angajat = id_angajat;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public void setSocietati(Set<Societate> societati) {
		this.societati = societati;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setGen(boolean gen) {
		this.gen = gen;
	}
}
