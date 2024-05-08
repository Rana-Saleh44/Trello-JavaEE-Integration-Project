package model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
public class Card implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long card_id;
	
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
		return card_id;
	}

	public void setId(Long id) {
		this.card_id = id;
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
