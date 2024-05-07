package App;

import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ejbs.Card;
import ejbs.Messaging;
import ejbs.User;
import java.util.List;
import java.util.Set;

@Stateless
public class MessagingService {

    @PersistenceContext(unitName = "hello")
    EntityManager entityManager;

    public void sendNotification(Card card, User assignee, Set<User> commenters) {
        // Create a new notification for assignee
        Messaging assigneeNotification = new Messaging();
        assigneeNotification.setMessage("Changes made to card: " + card.getId());
        assigneeNotification.setRecipient(assignee);
        assigneeNotification.setCard(card);

        
        for (User commenter : commenters) { // Create notifications for commenters
            Messaging commenterNotification = new Messaging();
            commenterNotification.setMessage("Changes made to card: " + card.getId());
            commenterNotification.setRecipient(commenter);
            commenterNotification.setCard(card);
            entityManager.persist(commenterNotification);
        }
        
        

        // Persist assignee notification
        entityManager.persist(assigneeNotification);
    }
}
