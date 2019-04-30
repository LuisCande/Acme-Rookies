
package controllers;

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
import services.AuditorService;
import domain.Auditor;

@Controller
@RequestMapping("/auditor")
public class AuditorController extends AbstractController {

	//Services

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private ActorService	actorService;


	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		final ModelAndView result;

		final Auditor auditor = this.auditorService.findOne(varId);

		if (auditor == null)
			return new ModelAndView("redirect:/welcome/index.do");

		result = new ModelAndView("auditor/display");
		result.addObject("auditor", auditor);
		result.addObject("requestURI", "auditor/display.do");

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		Auditor auditor;

		auditor = (Auditor) this.actorService.findByPrincipal();
		Assert.notNull(auditor);
		result = this.editModelAndView(auditor);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Auditor auditor, final BindingResult binding) {
		ModelAndView result;

		try {
			auditor = this.auditorService.reconstructPruned(auditor, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.editModelAndView(auditor, "auditor.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.editModelAndView(auditor);
		} catch (final Throwable oops) {
			return this.editModelAndView(auditor, "auditor.commit.error");
		}

		try {
			this.auditorService.save(auditor);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(auditor, "auditor.commit.error");
		}
		return result;
	}

	//Ancillary methods

	protected ModelAndView editModelAndView(final Auditor auditor) {
		ModelAndView result;

		result = this.editModelAndView(auditor, null);

		return result;
	}

	protected ModelAndView editModelAndView(final Auditor auditor, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("auditor/edit");
		result.addObject("auditor", auditor);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "auditor/edit.do");

		return result;
	}

}
