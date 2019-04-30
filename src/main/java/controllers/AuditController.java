
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import domain.Audit;

@Controller
@RequestMapping("/audit")
public class AuditController extends AbstractController {

	//Services

	@Autowired
	private AuditService	auditService;


	//Listing by audit

	@RequestMapping(value = "/listByPosition", method = RequestMethod.GET)
	public ModelAndView listByPosition(@RequestParam final int varId) {
		final ModelAndView result;
		final Collection<Audit> audits;

		audits = this.auditService.getAuditsForPosition(varId);

		result = new ModelAndView("audit/list");
		result.addObject("audits", audits);
		result.addObject("requestURI", "audit/list.do");

		return result;
	}

	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		final ModelAndView result;

		final Audit audit = this.auditService.findOne(varId);

		if (audit == null)
			return new ModelAndView("redirect:/welcome/index.do");

		result = new ModelAndView("audit/display");
		result.addObject("audit", audit);
		result.addObject("requestURI", "audit/display.do");

		return result;
	}

}
