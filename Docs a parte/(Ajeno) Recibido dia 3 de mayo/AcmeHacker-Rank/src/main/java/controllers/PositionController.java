package controllers;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Position;
import forms.PositionFinderForm;

import services.PositionService;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {
	
	@Autowired
	private PositionService positionService;
	
	// List -----------------------------------------------------------------
	
	@RequestMapping(value="/list", method= RequestMethod.GET)
	public ModelAndView filter(@RequestParam(required=false) Integer companyId) {
		ModelAndView result;
		PositionFinderForm form = new PositionFinderForm();
		if(companyId!=null){
			form.setCompanyId(companyId);
		}
		result = createEditModelAndView(form);	
		return result;
	}

	@RequestMapping(value="/list", method= RequestMethod.POST, params = "list")
	public ModelAndView list(PositionFinderForm form, final BindingResult binding) {
		ModelAndView result;
		if(binding.hasErrors()){
			result = createEditModelAndView(form);
		} else {
			try {
				form.setMoment(new Date());
				result = createEditModelAndView(form);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = createEditModelAndView(form, "position.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/list", method= RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(PositionFinderForm form, final BindingResult binding) {
		ModelAndView result;
			try {
				form.setMoment(null);
				form.setKeyword(null);
				result = createEditModelAndView(form);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = createEditModelAndView(form, "position.commit.error");
			}
		return result;
	}
		
		// Show --------------------------------------------------------------------

			@RequestMapping(value = "/show", method = RequestMethod.GET)
			public ModelAndView show(@RequestParam final int positionId) {

				ModelAndView result;

				Position position = positionService.findOne(positionId);

				result = new ModelAndView("position/show");
				result.addObject("position", position);
				result.addObject("requestURI", "position/show.do");

				return result;
			}
			
	// MODEL AND VIEW FILTER
	protected ModelAndView createEditModelAndView(PositionFinderForm positionFinderForm){
		ModelAndView res;
		res = createEditModelAndView(positionFinderForm, null);
		return res;
	}
	protected ModelAndView createEditModelAndView(PositionFinderForm positionFinderForm, String messageCode){
		ModelAndView res;
		Collection<Position> positions = new HashSet<Position>();
		res = new ModelAndView("position/list");
		
		if(positionFinderForm.getMoment() == null || positionFinderForm.getKeyword() == null || positionFinderForm.getKeyword().equals("")){
			if(positionFinderForm.getCompanyId()==null)positions.addAll(positionService.findPositionsIsFinal());
			else{
				positions.addAll(positionService.findPublishedPositionsByCompany(positionFinderForm.getCompanyId()));
			}
		}else{
			if(positionFinderForm.getCompanyId()==null) positions.addAll(positionService.searchPositions(positionFinderForm.getKeyword()));
			else{
				System.out.println("keyow " + positionFinderForm.getKeyword() + " comp " + positionFinderForm.getCompanyId());
				positions.addAll(positionService.searchPositionsWCompany(positionFinderForm.getKeyword(),positionFinderForm.getCompanyId()));
			}
		}				
		res.addObject("positionFinderForm", positionFinderForm);
		res.addObject("positions", positions);
		res.addObject("requestURI", "position/list.do");
		res.addObject("message", messageCode);

		return res;
	}
}
