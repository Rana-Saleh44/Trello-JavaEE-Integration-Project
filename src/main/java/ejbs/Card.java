package ejbs;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@Column(unique = true, nullable = false)
	@Size(min = 3)
	String description;
	
	@ManyToOne
	@JoinColumn(name = "lsit_id")
	ListEntity list;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ListEntity getList() {
		return list;
	}

	public void setList(ListEntity list) {
		this.list = list;
	}
	
	
}
