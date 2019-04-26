
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Provider extends Actor {

	//Attributes

	private String	make;


	//Getters

	public String getMake() {
		return this.make;
	}

	//Setters

	public void setMake(final String make) {
		this.make = make;
	}

}
