
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RookieRepository;
import security.Authority;
import security.UserAccount;
import domain.Rookie;
import forms.FormObjectRookie;

@Service
@Transactional
public class RookieService {

	//Managed repository ---------------------------------

	@Autowired
	private RookieRepository	rookieRepository;

	//Supporting services --------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	//Simple CRUD Methods --------------------------------

	public Rookie create() {
		final Authority a = new Authority();
		a.setAuthority(Authority.ROOKIE);
		final UserAccount account = new UserAccount();
		account.setAuthorities(Arrays.asList(a));
		account.setBanned(false);
		account.setInactive(false);

		final Rookie rookie = new Rookie();
		rookie.setSpammer(false);
		rookie.setEvaluated(false);
		rookie.setUserAccount(account);

		return rookie;
	}

	public Collection<Rookie> findAll() {
		return this.rookieRepository.findAll();
	}

	public Rookie findOne(final int id) {
		Assert.notNull(id);

		return this.rookieRepository.findOne(id);
	}

	public Rookie save(final Rookie rookie) {
		Assert.notNull(rookie);
		Rookie saved2;

		//Assertion that the email is valid according to the checkAdminEmail method.
		Assert.isTrue(this.actorService.checkUserEmail(rookie.getEmail()));

		//Assertion to check that the address isn't just a white space.
		Assert.isTrue(this.actorService.checkAddress(rookie.getAddress()));

		//Assertion that the phone is valid according to the checkPhone method.
		Assert.isTrue(this.actorService.checkPhone(rookie.getPhone()));

		//Checking if the actor is bannable according to the "bannableActors" query.
		if (this.actorService.isBannable(rookie) == true)
			rookie.setSpammer(true);

		if (rookie.getId() != 0) {
			Assert.isTrue(this.actorService.findByPrincipal().getId() == rookie.getId());
			saved2 = this.rookieRepository.save(rookie);
		} else {
			final Rookie saved = this.rookieRepository.save(rookie);
			this.actorService.hashPassword(saved);
			saved2 = this.rookieRepository.save(saved);
		}

		return saved2;
	}

	public void delete(final Rookie rookie) {
		Assert.notNull(rookie);

		//Assertion that the user deleting this actor has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == rookie.getId());

		this.rookieRepository.delete(rookie);
	}

	public Rookie reconstruct(final FormObjectRookie foh, final BindingResult binding) {
		final Rookie result = this.create();

		Assert.isTrue(foh.getAcceptedTerms());
		Assert.isTrue(foh.getPassword().equals(foh.getSecondPassword()));

		result.setName(foh.getName());
		result.setSurnames(foh.getSurnames());
		result.setVatNumber(foh.getVatNumber());
		result.setCreditCard(foh.getCreditCard());
		result.setPhoto(foh.getPhoto());
		result.setEmail(foh.getEmail());
		result.setPhone(foh.getPhone());
		result.setAddress(foh.getAddress());
		result.getUserAccount().setUsername(foh.getUsername());
		result.getUserAccount().setPassword(foh.getPassword());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		final int year = Calendar.getInstance().get(Calendar.YEAR);
		final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		//Assertion to make sure that the credit card has a valid expiration date.
		if (result.getCreditCard() != null) {
			if (result.getCreditCard().getExpYear() < year)
				throw new ConstraintDefinitionException();
			if (result.getCreditCard().getExpYear() == year && result.getCreditCard().getExpMonth() < month)
				throw new ConstraintDefinitionException();
		}

		//Assertion that the email is valid according to the checkAdminEmail method.
		Assert.isTrue(this.actorService.checkUserEmail(result.getEmail()));

		//Assertion to check that the address isn't just a white space.
		Assert.isTrue(this.actorService.checkAddress(result.getAddress()));

		//Assertion that the phone is valid according to the checkPhone method.
		Assert.isTrue(this.actorService.checkPhone(result.getPhone()));

		return result;

	}

	public Rookie reconstructPruned(final Rookie rookie, final BindingResult binding) {
		Rookie result;

		result = this.rookieRepository.findOne(rookie.getId());

		result.setName(rookie.getName());
		result.setSurnames(rookie.getSurnames());
		result.setVatNumber(rookie.getVatNumber());
		result.setCreditCard(rookie.getCreditCard());
		result.setPhoto(rookie.getPhoto());
		result.setEmail(rookie.getEmail());
		result.setPhone(rookie.getPhone());
		result.setAddress(rookie.getAddress());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		final int year = Calendar.getInstance().get(Calendar.YEAR);
		final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		//Assertion to make sure that the credit card has a valid expiration date.
		if (result.getCreditCard() != null) {
			if (result.getCreditCard().getExpYear() < year)
				throw new ConstraintDefinitionException();
			if (result.getCreditCard().getExpYear() == year && result.getCreditCard().getExpMonth() < month)
				throw new ConstraintDefinitionException();
		}

		//Assertion the user has the correct privilege
		Assert.isTrue(this.actorService.findByPrincipal().getId() == result.getId());

		//Assertion that the email is valid according to the checkAdminEmail method.
		Assert.isTrue(this.actorService.checkUserEmail(result.getEmail()));

		//Assertion to check that the address isn't just a white space.
		Assert.isTrue(this.actorService.checkAddress(result.getAddress()));

		//Assertion that the phone is valid according to the checkPhone method.
		Assert.isTrue(this.actorService.checkPhone(result.getPhone()));

		return result;

	}

	//Other methods
	public void flush() {
		this.rookieRepository.flush();
	}

	public Rookie rookieByFinder(final int id) {
		return this.rookieRepository.rookieByFinder(id);
	}

	//The rookies who have made more applications
	public Collection<String> rookiesWithMoreApplications() {
		Collection<String> results = new ArrayList<>();
		final Collection<String> rookies = this.rookieRepository.rookiesWithMoreApplications();
		final int maxResults = 1;
		if (rookies.size() > maxResults)
			results = new ArrayList<String>(((ArrayList<String>) rookies).subList(0, maxResults));
		else
			results = rookies;
		return results;
	}

}
