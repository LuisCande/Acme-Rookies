
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import services.ProviderService;
import domain.Item;
import domain.Provider;

@Controller
@RequestMapping("/item")
public class ItemController extends AbstractController {

	//Services

	@Autowired
	private ItemService		itemService;

	@Autowired
	private ProviderService	providerService;


	//Listing by provider
	@RequestMapping(value = "/listByProvider", method = RequestMethod.GET)
	public ModelAndView listByPosition(@RequestParam final int varId) {
		final ModelAndView result;
		final Collection<Item> items;

		final Provider p = this.providerService.findOne(varId);

		if (p == null)
			return new ModelAndView("redirect:/welcome/index.do");

		items = this.itemService.getItemsForProvider(varId);

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("requestURI", "item/listByProvider.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Item> items;
		final Boolean canEdit = false;

		items = this.itemService.findAll();

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("canEdit", canEdit);
		result.addObject("requestURI", "item/list.do");

		return result;
	}

	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		final ModelAndView result;

		final Item item = this.itemService.findOne(varId);

		if (item == null)
			return new ModelAndView("redirect:/welcome/index.do");

		result = new ModelAndView("item/display");
		result.addObject("item", item);
		result.addObject("requestURI", "item/display.do");

		return result;
	}

}
