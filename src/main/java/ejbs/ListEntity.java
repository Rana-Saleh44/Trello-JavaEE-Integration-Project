package ejbs;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class ListEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long list_id;
	
	@Column(unique = true, nullable = false)
	@Size(min = 3)
	String name;
	
	@ManyToOne
	@JoinColumn(name ="board_id")
	Board board;
	
	@OneToMany(mappedBy = "list")
	List<Card> cards;
	public ListEntity() {
		this.cards = new ArrayList<>();
	}
	
	public Long getId() {
		return this.list_id;
	}
	public void setId(Long id) {
		this.list_id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public List<Card> getCards(){
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
}
