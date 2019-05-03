package controllers.actor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.FinderService;
import controllers.AbstractController;
import domain.Admin;
import domain.Company;
import domain.Finder;
import domain.Hacker;
import forms.RegisterCompanyForm;
import forms.RegisterHackerAdminForm;

@Controller
@RequestMapping("actor/")
public class ActorCreateController extends AbstractController {

	// Services ----------------------------------------------------------------

	// @Autowired
	// private UserAccountService userAccountService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FinderService finderService;

	// Constructors ------------------------------------------------------------

	public ActorCreateController() {
		super();
	}

	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/registerHacker", method = RequestMethod.GET)
	public ModelAndView registerHacker() {
		RegisterHackerAdminForm form = new RegisterHackerAdminForm();
		return this.createRegisterHackerAdminModelAndView(form, "HACKER");
	}

	@RequestMapping(value = "/registerCompany", method = RequestMethod.GET)
	public ModelAndView registerCompany() {
		RegisterCompanyForm form = new RegisterCompanyForm();
		return this.createRegisterCompanyModelAndView(form);
	}

	@RequestMapping(value = "/registerAdmin", method = RequestMethod.GET)
	public ModelAndView createAdmin() {
		if(LoginService.hasRole("ADMIN")){
			RegisterHackerAdminForm form = new RegisterHackerAdminForm();
			return this.createRegisterHackerAdminModelAndView(form, "HACKER");
		}else{
			return new ModelAndView("error/access");
		}
		
	}
	// SAVES-------------------------------------------------------

	// HACKER
	@RequestMapping(value = "/registerHacker", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(@ModelAttribute("registerForm") RegisterHackerAdminForm registerForm, final BindingResult binding) {
		Boolean expired = false;
		Boolean passMatch = false;
		
		Calendar c = Calendar.getInstance();		
		System.out.println("el año es: " + c.get(Calendar.YEAR)+ " y el mes es: "+ c.get(Calendar.MONTH));
		if(registerForm.getExpirationYear()== c.get(Calendar.YEAR) && registerForm.getExpirationMonth()<=c.get(Calendar.MONTH)){
			expired = true;
		}
		
		if(registerForm.getPassword().equals(registerForm.getPassword2())){passMatch=true;}
		
		
			Hacker hacker = this.actorService.reconstructHacker(registerForm, binding);
			
			if (binding.hasErrors() || expired || !passMatch) {
				System.out.println(binding);
				ModelAndView res = this.createRegisterHackerAdminModelAndView(registerForm, "HACKER");
				res.addObject("isExpired", expired);
				res.addObject("passMatch", passMatch);
				return res;
			} else {
			try {
				Hacker h = actorService.registerHacker(hacker);
				
				Finder f = finderService.create();
				f.setHacker(h);
				finderService.save(f);

				return new ModelAndView("redirect:/");
			
		} catch (final Throwable oops) {
			oops.printStackTrace();
			RegisterHackerAdminForm form = new RegisterHackerAdminForm();
			return this.createRegisterHackerAdminModelAndView(form, "HACKER");
			}
		}
	}

	// COMPANY
	@RequestMapping(value = "/registerCompany", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(@ModelAttribute("registerForm") RegisterCompanyForm registerForm, final BindingResult binding) {

		Boolean expired = false;
		//Bugged code, should be false.
		Boolean passMatch = true;
		
		Calendar c = Calendar.getInstance();		
		System.out.println("el año es: " + c.get(Calendar.YEAR)+ " y el mes es: "+ c.get(Calendar.MONTH));
		if(registerForm.getExpirationYear()== c.get(Calendar.YEAR) && registerForm.getExpirationMonth()<=c.get(Calendar.MONTH)){
			expired = true;
		}
		
		//Bugged code:
		//if(registerForm.getPassword().equals(registerForm.getPassword2())){passMatch=true;}
		
		Company company = this.actorService.reconstructCompany(registerForm, binding);
		
		if (binding.hasErrors() || expired || !passMatch) {
			System.out.println(binding);
			ModelAndView res = this.createRegisterCompanyModelAndView(registerForm);
			res.addObject("isExpired", expired);
			res.addObject("passMatch", passMatch);
			return res;
		} else {
			try {
				actorService.registerCompany(company);
				return new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				oops.printStackTrace();
				RegisterCompanyForm form = new RegisterCompanyForm();
				return this.createRegisterCompanyModelAndView(form);
			}
		}
	}

	// ADMIN
	@RequestMapping(value = "/registerAdmin", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAdmin(@ModelAttribute("registerForm") RegisterHackerAdminForm registerForm,final BindingResult binding) {
		
		Boolean expired = false;
		Boolean passMatch = false;
		
		Calendar c = Calendar.getInstance();		
		System.out.println("el año es: " + c.get(Calendar.YEAR)+ " y el mes es: "+ c.get(Calendar.MONTH));
		if(registerForm.getExpirationYear()== c.get(Calendar.YEAR) && registerForm.getExpirationMonth()<=c.get(Calendar.MONTH)){
			expired = true;
		}
		
		if(registerForm.getPassword().equals(registerForm.getPassword2())){passMatch=true;}
		
		Admin admin = this.actorService.reconstructAdmin(registerForm,binding);
		
		// Configuration config = configurationService.find();
		if (binding.hasErrors()|| expired || !passMatch) {
			ModelAndView res = this.createRegisterHackerAdminModelAndView(registerForm, "ADMIN");
			res.addObject("isExpired", expired);
			res.addObject("passMatch", passMatch);
			return res;
		} else {
			try {
				actorService.registerAdmin(admin);
				return new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				oops.printStackTrace();
				RegisterHackerAdminForm form = new RegisterHackerAdminForm();
				return this.createRegisterHackerAdminModelAndView(form, "ADMIN");
			}
		}
	}

	protected ModelAndView createRegisterHackerAdminModelAndView(RegisterHackerAdminForm form, String actor) {
		ModelAndView result;
		result = this.createRegisterHackerAdminModelAndView(form, actor, null);
		return result;
	}
	
	protected ModelAndView createRegisterCompanyModelAndView(RegisterCompanyForm form) {
		ModelAndView result;
		result = this.createRegisterCompanyModelAndView(form, null);
		return result;
	}

	protected ModelAndView createRegisterHackerAdminModelAndView(RegisterHackerAdminForm form, String actor, String messageCode) {
		ModelAndView res;
		Date d = new Date();
		Collection <Integer> months = new ArrayList<>();
		Collection <Integer> years = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			months.add(i+1);
			years.add(d.getYear()+i+1900);
		}

		if (actor == "HACKER") {
			System.out.println("Creo las vista de registrar hacker");

			res = new ModelAndView("actor/registerHacker");
			res.addObject("registerForm", form);

		} else{ //ADMIN
			System.out.println("Creo las vista de registrar admin");

			res = new ModelAndView("actor/registerAdmin");
			res.addObject("registerForm", form);
			
		}
		res.addObject("message", messageCode);
		res.addObject("months", months);
		res.addObject("years", years);
		return res;
	}
	
	protected ModelAndView createRegisterCompanyModelAndView(RegisterCompanyForm form, String messageCode) {
		ModelAndView res;
		
		Date d = new Date();
		Collection <Integer> months = new ArrayList<>();
		Collection <Integer> years = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			months.add(i+1);
			years.add(d.getYear()+i+1900);
		}
			
		System.out.println("Creo las vista de registrar company");
		res = new ModelAndView("actor/registerCompany");
		res.addObject("registerForm", form);
		
		res.addObject("months", months);
		res.addObject("years", years);
		res.addObject("message", messageCode);
		return res;
	}
	

}
