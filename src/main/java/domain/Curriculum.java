
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.PROPERTY)
public class Curriculum extends DomainEntity {

	//Attributes
	private Rookie	rookie;


	//Getters
	@ManyToOne(optional = false)
	public Rookie getRookie() {
		return this.rookie;
	}

	//Setters
	public void setRookie(final Rookie rookie) {
		this.rookie = rookie;
	}
}
