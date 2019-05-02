
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	//Attributes

	private String				systemName;
	private String				banner;
	private String				welcomeEN;
	private String				welcomeES;
	private Collection<String>	spamWords;
	private String				countryCode;
	private Integer				expireFinderMinutes;
	private Integer				maxFinderResults;
	private Double				vat;
	private Double				fare;
	private Boolean				nameAnnounced;


	//Getters

	@NotBlank
	public String getSystemName() {
		return this.systemName;
	}

	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	@NotNull
	public String getWelcomeEN() {
		return this.welcomeEN;
	}

	@NotNull
	public String getWelcomeES() {
		return this.welcomeES;
	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	@NotBlank
	public String getCountryCode() {
		return this.countryCode;
	}

	@Range(min = 60, max = 1440)
	public Integer getExpireFinderMinutes() {
		return this.expireFinderMinutes;
	}

	@Range(min = 10, max = 100)
	public Integer getMaxFinderResults() {
		return this.maxFinderResults;
	}

	@Min(0)
	@NotNull
	public Double getVat() {
		return this.vat;
	}

	@Min(0)
	@NotNull
	public Double getFare() {
		return this.fare;
	}

	@NotNull
	public Boolean getNameAnnounced() {
		return this.nameAnnounced;
	}

	//Setters
	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public void setWelcomeEN(final String welcomeEN) {
		this.welcomeEN = welcomeEN;
	}

	public void setWelcomeES(final String welcomeES) {
		this.welcomeES = welcomeES;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	public void setExpireFinderMinutes(final Integer expireFinderMinutes) {
		this.expireFinderMinutes = expireFinderMinutes;
	}

	public void setMaxFinderResults(final Integer maxFinderResults) {
		this.maxFinderResults = maxFinderResults;
	}

	public void setVat(final Double vat) {
		this.vat = vat;
	}

	public void setFare(final Double fare) {
		this.fare = fare;
	}

	public void setNameAnnounced(final Boolean nameAnnounced) {
		this.nameAnnounced = nameAnnounced;
	}

}
