package controllers.hacker;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curricula;
import domain.Hacker;
import domain.MiscellaneousData;
import forms.MiscellaneousDataForm;

import services.CurriculaService;
import services.HackerService;
import services.MiscellaneousDataService;

@Controller
@RequestMapping("/curricula/miscellaneousData")
public class MiscellaneousDataHackerController {

	@Autowired
	private HackerService hackerService;

	@Autowired
	private MiscellaneousDataService miscellaneousDataService;

	@Autowired
	private CurriculaService curriculaService;


	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result;
		MiscellaneousData miscellaneousData;
		MiscellaneousDataForm form;

		miscellaneousData = this.miscellaneousDataService.create();
		form = this.miscellaneousDataService.construct(miscellaneousData);
		
		form.setCurriculaId(curriculaId);
		
		result = new ModelAndView();
		result.addObject("miscellaneousDataForm", form);
		result.addObject("id", miscellaneousData.getId());
		result.addObject("curriculaId", form.getCurriculaId());
		

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousDataId) {
		ModelAndView result;
		MiscellaneousData miscellaneousData;

		Hacker hacker = this.hackerService.findByPrincipal();
		miscellaneousData = this.miscellaneousDataService.findOne(miscellaneousDataId);
		Curricula curricula = this.curriculaService.findByMiscellaneousData(miscellaneousData);
		MiscellaneousDataForm form = this.miscellaneousDataService.construct(miscellaneousData);
		Assert.notNull(miscellaneousData);
		
		form.setCurriculaId(curricula.getId());

		if (curricula.getHacker().equals(hacker)) {
			result = this.createEditModelAndView(form);
		} else {
			result = new ModelAndView("error/access");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("miscellaneousDataForm")@Valid MiscellaneousDataForm form, BindingResult binding) {
		ModelAndView result;
		Hacker logged = this.hackerService.findByPrincipal();
		MiscellaneousData miscellaneousData;
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(form);
		} else {
			try {
				miscellaneousData = this.miscellaneousDataService.reconstruct(form, binding);
				Curricula curricula = this.curriculaService.findOne(form.getCurriculaId());
				if(form.getId()!=0){
					this.miscellaneousDataService.saveTrue(miscellaneousData);
				}else{
					this.miscellaneousDataService.save(miscellaneousData, curricula);
				}
				
				result = new ModelAndView("curricula/show");
				result.addObject("curricula", curricula);
				result.addObject("personalData", curricula.getPersonalData());
				result.addObject("positionDatas", curricula.getPositionDatas());
				result.addObject("miscellaneousDatas", curricula.getMiscellaneousDatas());
				result.addObject("educationDatas", curricula.getEducationDatas());
				result.addObject("hackerIsOwner", curricula.getHacker().equals(logged));
				result.addObject("hasPersonalData", curricula.getPersonalData() == null);
				result.addObject("hackerId", logged.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(form, "miscellaneousData.commit.error");
			}
		}
		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int miscellaneousDataId) {
		ModelAndView result;
		final Hacker logged = this.hackerService.findByPrincipal();
		final MiscellaneousData miscellaneousData = this.miscellaneousDataService.findOne(miscellaneousDataId);
		final Curricula curricula = this.curriculaService.findByMiscellaneousData(miscellaneousData);

		if (curricula.getHacker().equals(logged)) {
			try {
				System.out.println("borrando");
				this.miscellaneousDataService.delete(miscellaneousData, curricula);
				System.out.println("borrado");
				result = new ModelAndView("curricula/show");
				result.addObject("curricula", curricula);
				result.addObject("personalData", curricula.getPersonalData());
				result.addObject("positionDatas", curricula.getPositionDatas());
				result.addObject("miscellaneousDatas", curricula.getMiscellaneousDatas());
				result.addObject("educationDatas", curricula.getEducationDatas());
				result.addObject("hackerIsOwner", curricula.getHacker().equals(logged));
				result.addObject("hasPersonalData", curricula.getPersonalData() == null);
				result.addObject("hackerId", logged.getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("curricula/show");
				result.addObject("curricula", curricula);
				result.addObject("personalData", curricula.getPersonalData());
				result.addObject("positionDatas", curricula.getPositionDatas());
				result.addObject("miscellaneousDatas", curricula.getMiscellaneousDatas());
				result.addObject("educationDatas", curricula.getEducationDatas());
				result.addObject("hackerIsOwner", curricula.getHacker().equals(logged));
				result.addObject("hasPersonalData", curricula.getPersonalData() == null);
				result.addObject("hackerId", logged.getId());
			}
		} else {
			result = new ModelAndView("error/access");
		}
		return result;
	}
	
	//Ancillary methods
	protected ModelAndView createEditModelAndView(final MiscellaneousDataForm form) {
		ModelAndView result;
		result = this.createEditModelAndView(form, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousDataForm form, final String messageCode) {
		ModelAndView result;
		result = new ModelAndView("curricula/miscellaneousData/create");
		result.addObject("miscellaneousDataForm", form);
		result.addObject("message", messageCode);
		result.addObject("curriculaId", form.getCurriculaId());
		result.addObject("id", form.getId());

		return result;
	}

}
