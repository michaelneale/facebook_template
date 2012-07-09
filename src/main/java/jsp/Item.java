package jsp;

import java.io.Serializable;
import javax.persistence.*;

// This class stores Item objects which contain a unique name and an optional comment, as well as the author of the item.

@Entity
@Table(name ="items")
public class Item implements Serializable {

	private static final long serialVersionUID = 1084348048495831163L;
	
	@Id 
	@Column(name = "name")
	String name;

	@Column(name = "comment")
	String comment;

	@Column(name = "author")
	String author;
	
	public Item(String name, String comment, String author) {
		this();
		this.name = name;
		this.comment = comment;
		this.author = author;
	}

	public Item() {
		super();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}
}