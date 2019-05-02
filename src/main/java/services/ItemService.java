
package services;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ItemRepository;
import domain.Item;
import domain.Provider;

@Service
@Transactional
public class ItemService {

	// Managed service
	@Autowired
	private ItemRepository	itemRepository;

	// Supporting service

	@Autowired
	private ProblemService	problemService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	// Simple CRUD methods

	public Item create() {
		final Item i = new Item();
		i.setProvider((Provider) this.actorService.findByPrincipal());

		return i;
	}
	public Item findOne(final int id) {
		Assert.notNull(id);

		return this.itemRepository.findOne(id);
	}

	public Collection<Item> findAll() {
		return this.itemRepository.findAll();
	}

	public Item save(final Item i) {
		Assert.notNull(i);

		//Assertion that the user deleting this social profile has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == i.getProvider().getId());

		final Item saved = this.itemRepository.save(i);

		return saved;
	}

	public void delete(final Item i) {
		Assert.notNull(i);

		//Assertion that the user deleting this social identity has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == i.getProvider().getId());

		this.itemRepository.delete(i);

	}

	//Reconstruct

	public Item reconstruct(final Item i, final BindingResult binding) {
		Item result;

		if (i.getId() == 0)
			result = this.create();
		else
			result = this.findOne(i.getId());

		result.setName(i.getName());
		result.setDescription(i.getDescription());
		result.setLink(i.getLink());
		result.setPicture(i.getPicture());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		//Assertion the user has the correct privilege
		Assert.isTrue(this.actorService.findByPrincipal().getId() == result.getProvider().getId());

		//Assertion to make sure that the pictures are links
		Assert.isTrue(this.problemService.checkPictures(result.getPicture()));

		return result;

	}
	public void flush() {
		this.itemRepository.flush();
	}

	//Time for motion and queries

	//Retrieves a list of items for a certain provider
	public Collection<Item> getItemsForProvider(final int id) {
		return this.itemRepository.getItemsForProvider(id);
	}
}
