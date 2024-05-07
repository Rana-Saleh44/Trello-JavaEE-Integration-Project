package ejbs;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

	@Entity
	@Table(name = "notifications")
	public class Messaging implements Serializable {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String message;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User recipient;

	    @ManyToOne
	    @JoinColumn(name = "card_id")
	    private Card card;

	    public Messaging() {}

	    public Messaging(String message, User recipient, Card card) {
	        this.message = message;
	        this.recipient = recipient;
	        this.card = card;
	    }
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public User getRecipient() {
	        return recipient;
	    }

	    public void setRecipient(User recipient) {
	        this.recipient = recipient;
	    }

	    public Card getCard() {
	        return card;
	    }

	    public void setCard(Card card) {
	        this.card = card;
	    }
		    
	}



