
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "commercialName")
})
public class Company extends Actor {

	//Attributes
	private String	commercialName;
	private Double	auditScore;


	//Getters
	@NotBlank
	public String getCommercialName() {
		return this.commercialName;
	}

	@Min(0)
	@Max(1)
	public Double getAuditScore() {
		return this.auditScore;
	}

	//Setters
	public void setCommercialName(final String commercialName) {
		this.commercialName = commercialName;
	}

	public void setAuditScore(final Double auditScore) {
		this.auditScore = auditScore;
	}

}
