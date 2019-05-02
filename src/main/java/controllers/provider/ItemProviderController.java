
package controllers.provider;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ItemService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Item;
import domain.Provider;

@Controller
@RequestMapping("item/provider")
public class ItemProviderController extends AbstractController {

	//Services

	@Autowired
	private ItemService		itemService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ProviderService	providerService;


	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Item item;

		item = this.itemService.create();
		result = this.createEditModelAndView(item);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int varId) {
		ModelAndView result;
		Item item;

		item = this.itemService.findOne(varId);

		if (item == null || item.getProvider().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		result = this.createEditModelAndView(item);

		return result;
	}
	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Item> items;
		final Provider provider = this.providerService.findOne(this.actorService.findByPrincipal().getId());

		//QUERY
		items = this.itemService.getItemsForProvider(provider.getId());

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("requestURI", "item/provider/list.do");

		return result;
	}

	//Delete GET

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int varId) {
		ModelAndView result;
		final Item item = this.itemService.findOne(varId);

		if (item == null || this.actorService.findByPrincipal().getId() != item.getProvider().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		try {
			this.itemService.delete(item);
			result = new ModelAndView("redirect:/item/provider/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(item, "item.commit.error");
		}
		return result;
	}

	//Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Item item, final BindingResult binding) {
		ModelAndView result;

		try {
			item = this.itemService.reconstruct(item, binding);
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(item);
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(item, "item.commit.error");
		}

		try {
			this.itemService.save(item);
			result = new ModelAndView("redirect:/item/provider/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(item, "item.commit.error");
		}
		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final Item item) {
		ModelAndView result;

		result = this.createEditModelAndView(item, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Item item, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("item/edit");
		result.addObject("item", item);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "item/provider/edit.do");

		return result;

	}

}
