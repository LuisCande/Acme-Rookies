
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(name = "`Item`")
public class Item extends DomainEntity {

	//Attributes

	private String		name;
	private String		description;
	private String		link;
	private String		picture;

	//Relationships

	private Provider	provider;


	//Getters

	@NotBlank
	public String getName() {
		return this.name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	@URL
	@NotBlank
	public String getLink() {
		return this.link;
	}

	public String getPicture() {
		return this.picture;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Provider getProvider() {
		return this.provider;
	}

	//Setters

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public void setProvider(final Provider provider) {
		this.provider = provider;
	}

}
