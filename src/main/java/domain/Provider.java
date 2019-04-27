
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Provider extends Actor {

	//Attributes

	private String	makeP;


	//Getters

	@NotBlank
	public String getMakeP() {
		return this.makeP;
	}

	//Setters

	public void setMakeP(final String makeP) {
		this.makeP = makeP;
	}

}
