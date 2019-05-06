
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

import repositories.ProviderRepository;
import security.Authority;
import security.UserAccount;
import domain.Provider;
import forms.FormObjectProvider;

@Service
@Transactional
public class ProviderService {

	//Managed repository ---------------------------------

	@Autowired
	private ProviderRepository	providerRepository;

	//Supporting services --------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	//Simple CRUD Methods --------------------------------

	public Provider create() {
		final Authority a = new Authority();
		a.setAuthority(Authority.PROVIDER);
		final UserAccount account = new UserAccount();
		account.setAuthorities(Arrays.asList(a));
		account.setBanned(false);
		account.setInactive(false);

		final Provider provider = new Provider();
		provider.setSpammer(false);
		provider.setEvaluated(false);
		provider.setUserAccount(account);

		return provider;
	}

	public Collection<Provider> findAll() {
		return this.providerRepository.findAll();
	}

	public Provider findOne(final int id) {
		Assert.notNull(id);

		return this.providerRepository.findOne(id);
	}

	public Provider save(final Provider provider) {
		Assert.notNull(provider);
		Provider saved2;

		//Assertion that the email is valid according to the checkUserEmail method.
		Assert.isTrue(this.actorService.checkUserEmail(provider.getEmail()));

		//Assertion to check that the address isn't just a white space.
		Assert.isTrue(this.actorService.checkAddress(provider.getAddress()));

		//Assertion that the phone is valid according to the checkPhone method.
		Assert.isTrue(this.actorService.checkPhone(provider.getPhone()));

		//Checking if the actor is bannable according to the "bannableActors" query.
		if (this.actorService.isBannable(provider) == true)
			provider.setSpammer(true);

		if (provider.getId() != 0) {
			Assert.isTrue(this.actorService.findByPrincipal().getId() == provider.getId());
			saved2 = this.providerRepository.save(provider);
		} else {
			final Provider saved = this.providerRepository.save(provider);
			this.actorService.hashPassword(saved);
			saved2 = this.providerRepository.save(saved);
		}

		return saved2;
	}

	public void delete(final Provider provider) {
		Assert.notNull(provider);

		//Assertion that the user deleting this provider has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == provider.getId());

		this.providerRepository.delete(provider);
	}

	public Provider reconstruct(final FormObjectProvider fop, final BindingResult binding) {
		final Provider result = this.create();

		Assert.isTrue(fop.getAcceptedTerms());
		Assert.isTrue(fop.getPassword().equals(fop.getSecondPassword()));

		result.setName(fop.getName());
		result.setSurnames(fop.getSurnames());
		result.setVatNumber(fop.getVatNumber());
		result.setCreditCard(fop.getCreditCard());
		result.setPhoto(fop.getPhoto());
		result.setEmail(fop.getEmail());
		result.setPhone(fop.getPhone());
		result.setAddress(fop.getAddress());
		result.setMakeP(fop.getMakeP());
		result.getUserAccount().setUsername(fop.getUsername());
		result.getUserAccount().setPassword(fop.getPassword());

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

	public Provider reconstructPruned(final Provider provider, final BindingResult binding) {
		Provider result;

		result = this.providerRepository.findOne(provider.getId());

		result.setName(provider.getName());
		result.setSurnames(provider.getSurnames());
		result.setVatNumber(provider.getVatNumber());
		result.setCreditCard(provider.getCreditCard());
		result.setPhoto(provider.getPhoto());
		result.setEmail(provider.getEmail());
		result.setPhone(provider.getPhone());
		result.setAddress(provider.getAddress());
		result.setMakeP(provider.getMakeP());

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
		this.providerRepository.flush();
	}

	//The minimum, the maximum, the average, and the standard deviation of the number of items per provider
	public Double[] minMaxAvgStddevItemPerProvider() {
		return this.providerRepository.minMaxAvgStddevItemPerProvider();
	}

	//The top-5 providers in terms of total number of items provided
	public Collection<String> top5ProviderInTermsOfItems() {
		Collection<String> results = new ArrayList<>();
		final Collection<String> providers = this.providerRepository.top5ProviderInTermsOfItems();
		final int maxResults = 5;
		if (providers.size() > maxResults)
			results = new ArrayList<String>(((ArrayList<String>) providers).subList(0, maxResults));
		else
			results = providers;
		return results;
	}

	//The average, the minimum, the maximum, and the standard deviation of the	number of sponsorships per provider.
	public Double[] avgMinMaxStddevSponsorshipsPerProvider() {
		return this.providerRepository.avgMinMaxStddevSponsorshipsPerProvider();
	}

	//The providers who have a number of sponsorships that is at least 10% above the average number of sponsorships per provider
	public Collection<String> providersWith10PerCentMoreSponsorshipsThanAvg() {
		return this.providerRepository.providersWith10PerCentMoreSponsorshipsThanAvg();
	}

}
