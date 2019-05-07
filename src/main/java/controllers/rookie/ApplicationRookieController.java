
package controllers.rookie;

import java.util.Collection;

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
import services.ApplicationService;
import services.CurriculumService;
import services.MessageService;
import services.PositionService;
import controllers.AbstractController;
import domain.Application;
import domain.Curriculum;
import domain.Position;
import domain.Status;

@Controller
@RequestMapping("application/rookie")
public class ApplicationRookieController extends AbstractController {

	//Services

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CurriculumService	curriculumService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private MessageService		messageService;


	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Application> applications;

		applications = this.applicationService.applicationsOfARookieOrderedByStatus(this.actorService.findByPrincipal().getId());

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);
		result.addObject("requestURI", "application/rookie/list.do");

		return result;
	}

	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		final ModelAndView result;

		final Application application = this.applicationService.findOne(varId);
		Assert.notNull(application);

		if (application.getRookie().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		result = new ModelAndView("application/display");
		result.addObject("application", application);
		result.addObject("requestURI", "application/rookie/display.do");

		return result;
	}

	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Application application;

		application = this.applicationService.create();
		result = this.createEditModelAndView(application);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int varId) {
		final ModelAndView result;
		Application application;
		application = this.applicationService.findOne(varId);

		if (application.getRookie().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		result = this.createEditModelAndView(application);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Application application, final BindingResult binding) {
		ModelAndView result;

		//Assertion the application has changed its status from pending to submitted to send the message notification
		Boolean pending = false;
		final Application app = this.applicationService.findOne(application.getId());
		if (application.getId() != 0 && app.getStatus() == Status.PENDING)
			pending = true;

		try {
			application = this.applicationService.reconstruct(application, binding);
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(application);
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(application, "application.commit.error");
		}

		try {
			final Application saved = this.applicationService.save(application);
			//Sending message to company if status has changed to submitted
			if (pending && saved.getStatus() == Status.SUBMITTED)
				this.messageService.applicationStatusNotification(application);

			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}
		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Application application, final String messageCode) {
		ModelAndView result;

		final Collection<Curriculum> curriculums = this.curriculumService.getCurriculumsForRookie(this.actorService.findByPrincipal().getId());
		final Collection<Position> positions = this.positionService.positionsForRequestsByRookie(this.actorService.findByPrincipal().getId());

		result = new ModelAndView("application/edit");
		result.addObject("application", application);
		result.addObject("curriculums", curriculums);
		result.addObject("positions", positions);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "application/rookie/edit.do");

		return result;

	}
}
