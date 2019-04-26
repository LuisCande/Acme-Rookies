
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "auditor, position")
})
public class Audit extends DomainEntity {

	//Attributes

	private Date		moment;
	private String		text;
	private Double		score;

	//Relationships

	private Position	position;
	private Auditor		auditor;


	//Getters

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public String getText() {
		return this.text;
	}

	@Range(min = 0, max = 10)
	public Double getScore() {
		return this.score;
	}
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Position getPosition() {
		return this.position;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Auditor getAuditor() {
		return this.auditor;
	}

	//Setters
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void setScore(final Double score) {
		this.score = score;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public void setAuditor(final Auditor auditor) {
		this.auditor = auditor;
	}

}
