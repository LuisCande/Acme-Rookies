package controllers.hacker;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculaService;
import services.HackerService;
import services.PersonalDataService;
import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;
import forms.CurriculaDataForm;

@Controller
@RequestMapping("/curricula/hacker")
public class CurriculaHackerController {
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private HackerService hackerService;
	
	@Autowired
	private PersonalDataService personalDataService;
	
	//show
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int curriculaId) {
		ModelAndView result;
		Hacker hacker = this.hackerService.findByPrincipal();
		
		Curricula curricula = this.curriculaService.findOne(curriculaId);
		Boolean isOwner = false;
		Boolean hasPersonalData = true;
		
		try {
			Hacker logged = hackerService.findByPrincipal();
			if(logged.getId() == hacker.getId()){isOwner=true;}
			if(curricula.getPersonalData() == null){hasPersonalData = false;}
		} catch (Exception e) {
			System.out.println("Cazado hehe.");
			e.printStackTrace();

		}
		
		result = new ModelAndView("curricula/show");
		result.addObject("curricula", curricula);
		result.addObject("personalData", curricula.getPersonalData());
		result.addObject("positionDatas", curricula.getPositionDatas());
		result.addObject("miscellaneousDatas", curricula.getMiscellaneousDatas());
		result.addObject("educationDatas", curricula.getEducationDatas());
		result.addObject("requestURI", "curricula/hacker/show.do");
		result.addObject("hackerIsOwner", isOwner);
		result.addObject("hasPersonalData", hasPersonalData);
		
		return result;
		
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Hacker hacker;
		Hacker logged;
		hacker = this.hackerService.findByPrincipal();
		logged = this.hackerService.findByPrincipal();
		final Collection<Curricula> curriculas = this.curriculaService.findByHacker(logged);
		
		
		result = new ModelAndView("curricula/list");
		result.addObject("curriculas", curriculas);
		result.addObject("hackerIsOwner", hacker.getId() == logged.getId());
		result.addObject("requestURI", "curricula/hacker/list.do");
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;		
		CurriculaDataForm form = new CurriculaDataForm();
				
		result = new ModelAndView("curricula/editForm");
		result.addObject("form", form);
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula curricula;
		Hacker logged = this.hackerService.findByPrincipal();
		
		curricula = this.curriculaService.findOne(curriculaId);
		
		if(curricula.getHacker().equals(logged)) {
			result = this.createEditModelAndView(curricula);
		} else {
			result = new ModelAndView("error/access");
		}
		
		
		return result;
	}
	@RequestMapping(value = "/editForm", method = RequestMethod.POST, params = "save")
	public ModelAndView saveForm(CurriculaDataForm form, BindingResult binding) {
		ModelAndView result;
		
			Hacker logged = hackerService.findByPrincipal();

		if(binding.hasErrors()) {
			result = this.createEditFormModelAndView(form);
		} else {
			try {
				PersonalData data = personalDataService.saveTrue(personalDataService.reconstruct(form, binding));
				Curricula curricula = this.curriculaService.save(curriculaService.reconstruct(form, data, binding));
				logged.getCurriculas().add(curricula);
				this.hackerService.save(logged);
				result = new ModelAndView("curricula/list");
				result.addObject("curriculas", logged.getCurriculas());
				result.addObject("hackerIsOwner", true);
				result.addObject("requestURI", "curricula/hacker/list.do");
			} catch (Throwable oops) {
				result = this.createEditFormModelAndView(form, "curricula.commit.error");
			}
		}
		return result;
	}
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Curricula curricula, BindingResult binding) {
		ModelAndView result;
		Hacker hacker = this.hackerService.findByPrincipal();
		Hacker logged = this.hackerService.findByPrincipal();
		Collection<Curricula> curriculas = this.curriculaService.findByHacker(logged);
		
		if(binding.hasErrors()) {
			result = this.createEditModelAndView(curricula);
		} else {
			try {
				this.curriculaService.save(curricula);
				curriculas.add(curricula);
				this.hackerService.save(logged);
				result = new ModelAndView("curricula/list");
				result.addObject("curriculas", curriculas);
				result.addObject("hackerIsOwner", hacker.getId() == logged.getId());
				result.addObject("requestURI", "curricula/hacker/list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(curricula, "curricula.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int curriculaId) {
		ModelAndView result;
		Hacker logged = this.hackerService.findByPrincipal();
		Hacker hacker = this.hackerService.findByPrincipal();
		Curricula curricula = this.curriculaService.findOne(curriculaId);
		Collection<Curricula> curriculas = this.curriculaService.findByHacker(logged);
		
		if(curricula.getHacker().equals(logged)) {
			try {
				this.curriculaService.delete(curricula);
				result = new ModelAndView("curricula/list");
				result.addObject("curriculas", curriculas);
				result.addObject("hackerIsOwner", hacker.getId() == logged.getId());
				result.addObject("requestURI", "curricula/hacker/list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(curricula, "curricula.commit.error");
			}
		} else {
			result = new ModelAndView("error/access");
		}
		return result;
	}
	
	//Helper methods
	
	protected ModelAndView createEditModelAndView(Curricula curricula) {
		ModelAndView result;
		
		result = this.createEditModelAndView(curricula, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Curricula curricula, String message) {
		ModelAndView result;
		
		result = new ModelAndView("curricula/edit");
		result.addObject("curricula", curricula);
		result.addObject("message", message);
		
		return result;
	}
	
	//for the form
	
	protected ModelAndView createEditFormModelAndView(CurriculaDataForm form) {
		ModelAndView result;
		
		result = this.createEditFormModelAndView(form, null);
		
		return result;
	}
	
	protected ModelAndView createEditFormModelAndView(CurriculaDataForm form, String message) {
		ModelAndView result;
		
		result = new ModelAndView("curricula/edit");
		result.addObject("form", form);
		result.addObject("message", message);
		
		return result;
	}
	
}	
