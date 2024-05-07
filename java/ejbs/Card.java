package ejbs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
	
	@OneToMany(mappedBy = "card") //1
    private Set<Comment> comments;
	
	/*@Column
	String comment;
	public String getComment() {
		return comment;
	}*/
	
	public void setComment(Set<Comment> comment) {
		this.comments = comment;
	}
	public void addComment(Comment comment) { //2
        comments.add(comment);
        comment.setCard(this); // Ensure bidirectional relationship is maintained
    }
	
	public Set<User> getCommenters() {
        Set<User> commenters = new HashSet<>(); //3
        for (Comment comment : comments) {
            commenters.add(comment.getCommenter());
        }
        return commenters;
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
