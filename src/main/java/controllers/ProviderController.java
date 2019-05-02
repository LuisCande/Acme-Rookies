
package controllers;

import java.util.Collection;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.ProviderService;
import domain.Configuration;
import domain.Provider;
import forms.FormObjectProvider;

@Controller
@RequestMapping("/provider")
public class ProviderController extends AbstractController {

	//Services

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	//Listing all
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Provider> providers;

		providers = this.providerService.findAll();

		result = new ModelAndView("provider/list");
		result.addObject("providers", providers);
		result.addObject("requestURI", "provider/list.do");

		return result;
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		final ModelAndView result;

		final Provider provider = this.providerService.findOne(varId);

		if (provider == null)
			return new ModelAndView("redirect:/welcome/index.do");

		result = new ModelAndView("provider/display");
		result.addObject("provider", provider);
		result.addObject("requestURI", "provider/display.do");

		return result;
	}

	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final FormObjectProvider fop;
		final Configuration config = this.configurationService.findAll().iterator().next();

		fop = new FormObjectProvider();
		fop.setPhone(config.getCountryCode());
		result = this.createEditModelAndView(fop);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		Provider provider;

		provider = (Provider) this.actorService.findByPrincipal();
		Assert.notNull(provider);
		result = this.editModelAndView(provider);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "create")
	public ModelAndView save(final FormObjectProvider fop, final BindingResult binding) {
		ModelAndView result;
		Provider provider;

		try {
			provider = this.providerService.reconstruct(fop, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.createEditModelAndView(fop, "provider.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(fop, "provider.validation.error");
		} catch (final Throwable oops) {
			return this.createEditModelAndView(fop, "provider.reconstruct.error");
		}
		try {
			this.providerService.save(provider);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(fop, "provider.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Provider provider, final BindingResult binding) {
		ModelAndView result;

		try {
			provider = this.providerService.reconstructPruned(provider, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.editModelAndView(provider, "provider.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.editModelAndView(provider);
		} catch (final Throwable oops) {
			return this.editModelAndView(provider, "provider.commit.error");
		}

		try {
			this.providerService.save(provider);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(provider, "provider.commit.error");
		}
		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final FormObjectProvider fop) {
		ModelAndView result;

		result = this.createEditModelAndView(fop, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FormObjectProvider fop, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("provider/create");
		result.addObject("fop", fop);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "provider/create.do");

		return result;
	}

	protected ModelAndView editModelAndView(final Provider provider) {
		ModelAndView result;

		result = this.editModelAndView(provider, null);

		return result;
	}

	protected ModelAndView editModelAndView(final Provider provider, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("provider/edit");
		result.addObject("provider", provider);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "provider/edit.do");

		return result;
	}

}
