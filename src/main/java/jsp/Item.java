package jsp;

import java.io.Serializable;
import javax.persistence.*;


@Entity
public class Item implements Serializable {

	private static final long serialVersionUID = 1084348048495831163L;
	
	@Id String name;
	String comment;
	
	public Item(String name, String comment) {
		super();
		this.name = name;
		this.comment = comment;
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
}