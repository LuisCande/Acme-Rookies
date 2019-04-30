
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Configuration;
import domain.CreditCard;
import domain.Provider;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	// Managed repository
	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	// Supporting service

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;

	@Autowired
	private ConfigurationService	configurationService;


	// Simple CRUD methods

	public Sponsorship create() {
		final Sponsorship ss = new Sponsorship();
		ss.setProvider((Provider) this.actorService.findByPrincipal());
		ss.setCreditCard(new CreditCard());
		ss.setCharge(0.0);
		return ss;
	}

	public Sponsorship findOne(final int id) {
		Assert.notNull(id);

		return this.sponsorshipRepository.findOne(id);
	}

	public Collection<Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	public Sponsorship save(final Sponsorship ss) {
		Assert.notNull(ss);

		final int year = Calendar.getInstance().get(Calendar.YEAR);
		final int month = Calendar.getInstance().get(Calendar.MONTH);

		//Assertion that the user modifying this sponsorship has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == ss.getProvider().getId());

		//Assertion to make sure that the credit card has a valid expiration date.
		if (ss.getCreditCard() != null) {
			Assert.isTrue(ss.getCreditCard().getExpYear() >= year);

			if (ss.getCreditCard().getExpYear() == year)
				Assert.isTrue(ss.getCreditCard().getExpMonth() >= month);
		}

		//Assertion that the position is final and not cancelled
		Assert.isTrue(ss.getPosition().getFinalMode() == true && ss.getPosition().getCancelled() == false);

		final Sponsorship saved = this.sponsorshipRepository.save(ss);
		return saved;
	}

	public void delete(final Sponsorship ss) {
		Assert.notNull(ss);

		//Assertion that the user deleting this sponsorship has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == ss.getProvider().getId());

		this.sponsorshipRepository.delete(ss);
	}

	//Save the acumulated charge calculated when a sponsorship is displayed in a parade
	public Sponsorship saveFromPosition(final Sponsorship s) {
		Assert.notNull(s);
		final Configuration config = this.configurationService.findAll().iterator().next();
		final Double vat = config.getVat();
		final Double fare = config.getFare();

		final Double charge = s.getCharge();
		final Double plusCharge = vat * fare;

		final Double total = charge + (plusCharge);
		s.setCharge(total);

		final Sponsorship saved = this.sponsorshipRepository.save(s);
		return saved;
	}

	public void pay(final Sponsorship ss) {
		Assert.notNull(ss);
		//Assertion that the user activating this sponsorship has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == ss.getProvider().getId());

		ss.setCharge(0.0);

		this.sponsorshipRepository.save(ss);
	}

	public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding) {

		Sponsorship result;
		final int year = Calendar.getInstance().get(Calendar.YEAR);
		final int month = Calendar.getInstance().get(Calendar.MONTH);
		if (sponsorship.getId() == 0)
			result = this.create();
		else
			result = this.sponsorshipRepository.findOne(sponsorship.getId());

		result.setBanner(sponsorship.getBanner());
		result.setTargetPage(sponsorship.getTargetPage());
		result.setCreditCard(sponsorship.getCreditCard());
		result.setPosition(sponsorship.getPosition());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		//Assertion that the user modifying this sponsorship has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == result.getProvider().getId());

		//Assertion to make sure that the credit card has a valid expiration date.
		if (result.getCreditCard() != null) {
			Assert.isTrue(result.getCreditCard().getExpYear() >= year);

			if (result.getCreditCard().getExpYear() == year)
				Assert.isTrue(result.getCreditCard().getExpMonth() >= month);
		}

		//Assertion that the position is final and not cancelled
		Assert.isTrue(result.getPosition().getFinalMode() == true && result.getPosition().getCancelled() == false);

		return result;

	}

	//Other methods

	//Retrieves the sponsorships for a certain provider
	public Collection<Sponsorship> sponsorshipsFromProvider(final int id) {
		return this.sponsorshipRepository.sponsorshipsFromProvider(id);
	}

	//Retrieves the sponsorships for a certain position
	public Collection<Sponsorship> getSponsorshipsByPosition(final int id) {
		return this.sponsorshipRepository.getSponsorshipsByPosition(id);
	}

}
