
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
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.RookieService;
import domain.Configuration;
import domain.Rookie;
import forms.FormObjectRookie;

@Controller
@RequestMapping("rookie")
public class RookieController extends AbstractController {

	//Services

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final FormObjectRookie foh;
		final Configuration config = this.configurationService.findAll().iterator().next();

		foh = new FormObjectRookie();
		foh.setPhone(config.getCountryCode());
		result = this.createEditModelAndView(foh);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		Rookie rookie;

		rookie = (Rookie) this.actorService.findByPrincipal();
		Assert.notNull(rookie);
		result = this.editModelAndView(rookie);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "create")
	public ModelAndView save(final FormObjectRookie foh, final BindingResult binding) {
		ModelAndView result;
		Rookie rookie;

		try {
			rookie = this.rookieService.reconstruct(foh, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.createEditModelAndView(foh, "rookie.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(foh, "rookie.validation.error");
		} catch (final Throwable oops) {
			return this.createEditModelAndView(foh, "rookie.reconstruct.error");
		}
		try {
			this.rookieService.save(rookie);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(foh, "rookie.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Rookie rookie, final BindingResult binding) {
		ModelAndView result;

		try {
			rookie = this.rookieService.reconstructPruned(rookie, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.editModelAndView(rookie, "rookie.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.editModelAndView(rookie);
		} catch (final Throwable oops) {
			return this.editModelAndView(rookie, "rookie.commit.error");
		}

		try {
			this.rookieService.save(rookie);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(rookie, "rookie.commit.error");
		}
		return result;
	}

	//List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Rookie> rookies;

		rookies = this.rookieService.findAll();

		result = new ModelAndView("rookie/list");
		result.addObject("rookies", rookies);
		result.addObject("requestURI", "rookie/list.do");

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final FormObjectRookie foh) {
		ModelAndView result;

		result = this.createEditModelAndView(foh, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FormObjectRookie foh, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("rookie/create");
		result.addObject("foh", foh);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "rookie/create.do");

		return result;
	}

	protected ModelAndView editModelAndView(final Rookie rookie) {
		ModelAndView result;

		result = this.editModelAndView(rookie, null);

		return result;
	}

	protected ModelAndView editModelAndView(final Rookie rookie, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("rookie/edit");
		result.addObject("rookie", rookie);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "rookie/edit.do");

		return result;
	}

}
