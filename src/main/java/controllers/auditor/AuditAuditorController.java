
package controllers.auditor;

import java.util.Collection;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditService;
import services.AuditorService;
import services.PositionService;
import controllers.AbstractController;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@Controller
@RequestMapping("audit/auditor")
public class AuditAuditorController extends AbstractController {

	//Services

	@Autowired
	private AuditService	auditService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private PositionService	positionService;


	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Audit audit;

		audit = this.auditService.create();
		result = this.createEditModelAndView(audit);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int varId) {
		ModelAndView result;
		Audit audit;

		audit = this.auditService.findOne(varId);

		if (audit == null || audit.getFinalMode() == true || audit.getAuditor().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		result = this.createEditModelAndView(audit);

		return result;
	}
	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Audit> audits;
		final Auditor auditor = this.auditorService.findOne(this.actorService.findByPrincipal().getId());

		//QUERY
		audits = this.auditService.auditsFromAuditor(auditor.getId());

		result = new ModelAndView("audit/list");
		result.addObject("audits", audits);
		result.addObject("requestURI", "audit/auditor/list.do");

		return result;
	}

	//Delete GET

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int varId) {
		ModelAndView result;
		final Audit audit = this.auditService.findOne(varId);

		if (audit == null || audit.getFinalMode() == true || this.actorService.findByPrincipal().getId() != audit.getAuditor().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		try {
			this.auditService.delete(audit);
			result = new ModelAndView("redirect:/audit/auditor/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(audit, "audit.commit.error");
		}
		return result;
	}

	//Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Audit audit, final BindingResult binding) {
		ModelAndView result;

		try {
			audit = this.auditService.reconstruct(audit, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.createEditModelAndView(audit, "audit.range.error");
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(audit);
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(audit, "audit.commit.error");
		}

		try {
			this.auditService.save(audit);
			result = new ModelAndView("redirect:/audit/auditor/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(audit, "audit.commit.error");
		}
		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final Audit audit) {
		ModelAndView result;

		result = this.createEditModelAndView(audit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Audit audit, final String messageCode) {
		ModelAndView result;

		final Collection<Position> positions = this.positionService.getPublicPositions();

		result = new ModelAndView("audit/edit");
		result.addObject("audit", audit);
		result.addObject("positions", positions);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "audit/auditor/edit.do");

		return result;

	}

}
