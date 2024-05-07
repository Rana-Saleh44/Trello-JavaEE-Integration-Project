package ejbs;

import java.util.Set;

public class CardDetails {
	
	 private Set<Comment> comments;

	    public Set<Comment> getComments() {
	        return comments;
	    }

	    public void setComments(Set<Comment> comments) {
	        this.comments = comments;
	    }	
	
	private String description;
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
}
