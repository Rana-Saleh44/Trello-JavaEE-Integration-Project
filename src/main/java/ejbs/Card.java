package ejbs;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@Column(nullable = false)
	@Size(min = 3)
	String description;
	
	@Column
	String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@ManyToOne
	@JoinColumn(name = "list_id")
	ListEntity list;
	
	@ManyToOne
    @JoinColumn(name = "collaborator_id")
    User collaborator;
	public User getCollaborator() {
		return collaborator;
	}
	public void setCollaborator(User collaborator) {
		this.collaborator = collaborator;
	}

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
