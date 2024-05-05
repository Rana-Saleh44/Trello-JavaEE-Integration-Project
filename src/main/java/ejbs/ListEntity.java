package ejbs;
import java.util.List;
import javax.persistence.*;

@Entity
public class ListEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String name;
	
	@ManyToOne
	@JoinColumn(name ="board_id")
	Board board;
	
	@OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
	List<Card> cards;
	
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
