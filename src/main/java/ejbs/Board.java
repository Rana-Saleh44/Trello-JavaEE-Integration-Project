package ejbs;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(unique = true, nullable = false)
	@Size(min = 3)
	String name ;
	
	@ManyToOne
	@JoinColumn( name = "team_leader_id")
	User teamLeader;
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	List <ListEntity> lists;
	
	@ManyToMany
	@JoinTable(
			name = "board_collaborators",
			joinColumns = @JoinColumn(name = "board_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	Set<User> collaborators;
	public Board() {
		this.lists = new ArrayList<>();
		this.collaborators = new HashSet<>();
	}

	public void setCollaborators(Set<User> collaborators) {
		this.collaborators = collaborators;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public User getTeamLeader() {
		return teamLeader;
	}
	public void setTeamLeader(User teamLeader) {
		this.teamLeader = teamLeader;
	}
	public List<ListEntity> getList(){
		return lists;
	}
	public void setLists(List<ListEntity> lists) {
		this.lists = lists;
	}

	public Set<User> getCollaborators() {
		return collaborators;
	}

}
