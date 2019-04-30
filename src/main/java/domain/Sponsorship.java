
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	//Attributes

	private String		banner;
	private String		targetPage;
	private CreditCard	creditCard;
	private Double		charge;

	//Relationships

	private Provider	provider;
	private Position	position;


	//Getters

	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	@NotBlank
	@URL
	public String getTargetPage() {
		return this.targetPage;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Provider getProvider() {
		return this.provider;
	}

	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Position getPosition() {
		return this.position;
	}

	@NotNull
	public Double getCharge() {
		return this.charge;
	}
	//Setters

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public void setTargetPage(final String targetPage) {
		this.targetPage = targetPage;
	}

	public void setProvider(final Provider provider) {
		this.provider = provider;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public void setCharge(final Double charge) {
		this.charge = charge;
	}

}
