
package controllers.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PositionService;
import controllers.AbstractController;
import domain.Position;

@Controller
@RequestMapping("position/auditor")
public class PositionAuditorController extends AbstractController {

	//Supporting services

	@Autowired
	private PositionService	positionService;


	//Reject

	@RequestMapping(value = "/selfAssign", method = RequestMethod.GET)
	public ModelAndView selfAssign(@RequestParam final int varId) {

		final ModelAndView result = new ModelAndView("position/list");
		final Collection<Position> positions = this.positionService.getPublicPositions();
		final Position position = this.positionService.findOne(varId);

		if (position.getAuditor() != null)
			return new ModelAndView("redirect:/welcome/index.do");

		try {
			this.positionService.selfAssign(varId);
		} catch (final Throwable oops) {
			result.addObject("message", "position.selfAssign.error");
		}

		result.addObject("positions", positions);

		return result;
	}

}
