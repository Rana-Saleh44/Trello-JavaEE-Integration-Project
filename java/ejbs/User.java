package ejbs;
import java.io.Serializable;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Stateless
@Table(name = "users")
public class User implements Serializable{
	static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@Column(unique = true, nullable = false)
	@Size(min = 15)
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$", message = "Email not valid")
	String email;
	
	@Column(nullable = false)
	@Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
	String password;
	
	@Column(nullable = false)
	@Size(min = 3, message = "Name should be at least 3 characters")
	String name;
	
	@Column(nullable = false)
	String role;
	public String getRole() {
		return role;
	}public void setRole(String role) {
		this.role = role;
	}
	@ManyToMany(mappedBy = "collaborators")
	Set<Board> boards;
	
	public User() {}
	
	public User(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
    
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + "]";
	}
}
