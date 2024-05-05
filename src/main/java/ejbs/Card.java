package ejbs;

import javax.persistence.*;


@Entity
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
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
