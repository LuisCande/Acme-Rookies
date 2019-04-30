
package services;

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

import repositories.AuditorRepository;
import security.Authority;
import security.UserAccount;
import domain.Auditor;
import forms.FormObjectAuditor;

@Service
@Transactional
public class AuditorService {

	//Managed repository ---------------------------------

	@Autowired
	private AuditorRepository	auditorRepository;

	//Supporting services --------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	//Simple CRUD Methods --------------------------------

	public Auditor create() {
		final Authority a = new Authority();
		a.setAuthority(Authority.AUDITOR);
		final UserAccount account = new UserAccount();
		account.setAuthorities(Arrays.asList(a));
		account.setBanned(false);
		account.setInactive(false);

		final Auditor auditor = new Auditor();
		auditor.setSpammer(false);
		auditor.setEvaluated(false);
		auditor.setUserAccount(account);

		return auditor;
	}

	public Collection<Auditor> findAll() {
		return this.auditorRepository.findAll();
	}

	public Auditor findOne(final int id) {
		Assert.notNull(id);

		return this.auditorRepository.findOne(id);
	}

	public Auditor save(final Auditor auditor) {
		Assert.notNull(auditor);
		Auditor saved2;

		//Assertion that the email is valid according to the checkUserEmail method.
		Assert.isTrue(this.actorService.checkUserEmail(auditor.getEmail()));

		//Assertion to check that the address isn't just a white space.
		Assert.isTrue(this.actorService.checkAddress(auditor.getAddress()));

		//Assertion that the phone is valid according to the checkPhone method.
		Assert.isTrue(this.actorService.checkPhone(auditor.getPhone()));

		//Checking if the actor is bannable according to the "bannableActors" query.
		if (this.actorService.isBannable(auditor) == true)
			auditor.setSpammer(true);

		if (auditor.getId() != 0) {
			Assert.isTrue(this.actorService.findByPrincipal().getId() == auditor.getId());
			saved2 = this.auditorRepository.save(auditor);
		} else {
			final Auditor saved = this.auditorRepository.save(auditor);
			this.actorService.hashPassword(saved);
			saved2 = this.auditorRepository.save(saved);
		}

		return saved2;
	}

	public void delete(final Auditor auditor) {
		Assert.notNull(auditor);

		//Assertion that the user deleting this auditor has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == auditor.getId());

		this.auditorRepository.delete(auditor);
	}

	public Auditor reconstruct(final FormObjectAuditor foau, final BindingResult binding) {
		final Auditor result = this.create();

		Assert.isTrue(foau.getAcceptedTerms());
		Assert.isTrue(foau.getPassword().equals(foau.getSecondPassword()));

		result.setName(foau.getName());
		result.setSurnames(foau.getSurnames());
		result.setVatNumber(foau.getVatNumber());
		result.setCreditCard(foau.getCreditCard());
		result.setPhoto(foau.getPhoto());
		result.setEmail(foau.getEmail());
		result.setPhone(foau.getPhone());
		result.setAddress(foau.getAddress());
		result.getUserAccount().setUsername(foau.getUsername());
		result.getUserAccount().setPassword(foau.getPassword());

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

		//Assertion that the email is valid according to the checkUserEmail method.
		Assert.isTrue(this.actorService.checkUserEmail(result.getEmail()));

		//Assertion to check that the address isn't just a white space.
		Assert.isTrue(this.actorService.checkAddress(result.getAddress()));

		//Assertion that the phone is valid according to the checkPhone method.
		Assert.isTrue(this.actorService.checkPhone(result.getPhone()));

		return result;

	}

	public Auditor reconstructPruned(final Auditor auditor, final BindingResult binding) {
		Auditor result;

		result = this.auditorRepository.findOne(auditor.getId());

		result.setName(auditor.getName());
		result.setSurnames(auditor.getSurnames());
		result.setVatNumber(auditor.getVatNumber());
		result.setCreditCard(auditor.getCreditCard());
		result.setPhoto(auditor.getPhoto());
		result.setEmail(auditor.getEmail());
		result.setPhone(auditor.getPhone());
		result.setAddress(auditor.getAddress());

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

		//Assertion that the email is valid according to the checkUserEmail method.
		Assert.isTrue(this.actorService.checkUserEmail(result.getEmail()));

		//Assertion to check that the address isn't just a white space.
		Assert.isTrue(this.actorService.checkAddress(result.getAddress()));

		//Assertion that the phone is valid according to the checkPhone method.
		Assert.isTrue(this.actorService.checkPhone(result.getPhone()));

		return result;

	}

	//Other methods
	public void flush() {
		this.auditorRepository.flush();
	}
}
