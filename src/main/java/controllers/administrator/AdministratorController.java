/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Arrays;
import java.util.Collection;

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
import services.AdministratorService;
import services.ApplicationService;
import services.AuditorService;
import services.CompanyService;
import services.ConfigurationService;
import services.CurriculumService;
import services.FinderService;
import services.PositionService;
import services.ProviderService;
import services.RookieService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Auditor;
import domain.Company;
import domain.Configuration;
import forms.FormObjectAdministrator;
import forms.FormObjectAuditor;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	//Services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private AuditorService			auditorService;

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private CurriculumService		curriculumService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ProviderService			providerService;


	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		FormObjectAdministrator foa;

		final Configuration config = this.configurationService.findAll().iterator().next();

		foa = new FormObjectAdministrator();
		foa.setPhone(config.getCountryCode());

		result = this.createEditModelAndView(foa);

		return result;
	}

	//Create auditor
	@RequestMapping(value = "/createAuditor", method = RequestMethod.GET)
	public ModelAndView createAuditor() {
		final ModelAndView result;
		FormObjectAuditor foau;

		final Configuration config = this.configurationService.findAll().iterator().next();

		foau = new FormObjectAuditor();
		foau.setPhone(config.getCountryCode());

		result = this.createEditModelAndView(foau);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		Administrator administrator;
		administrator = (Administrator) this.actorService.findByPrincipal();
		Assert.notNull(administrator);
		result = this.editModelAndView(administrator);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Administrator administrator, final BindingResult binding) {
		ModelAndView result;

		try {
			administrator = this.administratorService.reconstructPruned(administrator, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.editModelAndView(administrator, "administrator.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.editModelAndView(administrator);
		} catch (final Throwable oops) {
			return this.editModelAndView(administrator, "administrator.commit.error");
		}
		try {
			this.administratorService.save(administrator);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(administrator, "administrator.commit.error");
		}
		return result;
	}
	//Create POST
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "create")
	public ModelAndView save(final FormObjectAdministrator foa, final BindingResult binding) {
		ModelAndView result;
		Administrator administrator;

		try {
			administrator = this.administratorService.reconstruct(foa, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.createEditModelAndView(foa, "administrator.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(foa, "administrator.validation.error");
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(foa, "administrator.reconstruct.error");
		}
		try {
			this.administratorService.save(administrator);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(foa, "administrator.commit.error");
		}
		return result;
	}

	//Create POST auditor

	@RequestMapping(value = "/createAuditor", method = RequestMethod.POST, params = "create")
	public ModelAndView saveAuditor(final FormObjectAuditor foau, final BindingResult binding) {
		ModelAndView result;
		Auditor auditor;

		try {
			auditor = this.auditorService.reconstruct(foau, binding);
		} catch (final ConstraintDefinitionException oops) {
			return this.createEditModelAndView(foau, "administrator.expirationDate.error");
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(foau, "administrator.validation.error");
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(foau, "administrator.reconstruct.error");
		}
		try {
			this.auditorService.save(auditor);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(foau, "administrator.commit.error");
		}
		return result;
	}

	//Dashboard

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {
		ModelAndView result;

		result = new ModelAndView("administrator/dashboard");

		result.addObject("avgMinMaxStddevPositionsPerCompany", Arrays.toString(this.positionService.avgMinMaxStddevPositionsPerCompany()));
		result.addObject("avgMinMaxStddevApplicationsPerRookie", Arrays.toString(this.applicationService.avgMinMaxStddevApplicationsPerRookie()));
		result.addObject("companiesWithMoreOfferedPossitions", this.companyService.companiesWithMoreOfferedPossitions());
		result.addObject("rookiesWithMoreApplications", this.rookieService.rookiesWithMoreApplications());
		result.addObject("avgMinMaxStddevOfferedSalaries", Arrays.toString(this.positionService.avgMinMaxStddevOfferedSalaries()));
		result.addObject("bestAndWorstPositions", this.positionService.bestAndWorstPositions());
		result.addObject("minMaxAvgStddevCurriculaPerRookie", Arrays.toString(this.curriculumService.minMaxAvgStddevCurriculaPerRookie()));
		result.addObject("minMaxAvgStddevResultsFinders", Arrays.toString(this.finderService.minMaxAvgStddevResultsFinders()));
		result.addObject("ratioEmptyVersusNonEmptyFinders", this.finderService.ratioEmptyVersusNonEmptyFinders());

		result.addObject("avgMinMaxStddevAuditScorePerPosition", Arrays.toString(this.positionService.avgMinMaxStddevAuditScorePerPosition()));
		result.addObject("avgMinMaxStddevAuditScorePerCompany", Arrays.toString(this.companyService.avgMinMaxStddevAuditScorePerCompany()));
		result.addObject("companiesWithHighAuditScore", this.companyService.companiesWithHighAuditScore());
		result.addObject("avgSalaryOfferedPerPositionWithHighestAvgAuditScore", this.positionService.avgSalaryOfferedPerPositionWithHighestAvgAuditScore());
		result.addObject("minMaxAvgStddevItemPerProvider", Arrays.toString(this.providerService.minMaxAvgStddevItemPerProvider()));
		result.addObject("top5ProviderInTermsOfItems", this.providerService.top5ProviderInTermsOfItems());
		result.addObject("avgMinMaxStddevSponsorshipsPerProvider", Arrays.toString(this.providerService.avgMinMaxStddevSponsorshipsPerProvider()));
		result.addObject("avgMinMaxStddevSponsorshipsPerPosition", Arrays.toString(this.positionService.avgMinMaxStddevSponsorshipsPerPosition()));
		result.addObject("providersWith10PerCentMoreSponsorshipsThanAvg", this.providerService.providersWith10PerCentMoreSponsorshipsThanAvg());

		result.addObject("requestURI", "administrator/dashboard.do");

		return result;
	}

	//Flag spam
	@RequestMapping(value = "/flagSpam", method = RequestMethod.GET)
	public ModelAndView flagSpam() {
		final ModelAndView result;

		this.actorService.flagSpammers();

		result = new ModelAndView("redirect:/administrator/bannableList.do");

		return result;
	}

	//Display actor
	@RequestMapping(value = "/actorDisplay", method = RequestMethod.GET)
	public ModelAndView actorDisplay(@RequestParam final int varId) {
		final ModelAndView result;

		final Actor actor = this.actorService.findOne(varId);

		result = new ModelAndView("actor/display");

		final Company company = (Company) actor;
		if (company.getAuditScore() != null) {
			final Double score = company.getAuditScore();
			result.addObject("score", score);
		}
		result.addObject("actor", actor);

		return result;
	}

	//Compute score
	@RequestMapping(value = "/computeScore", method = RequestMethod.GET)
	public ModelAndView computeScore() {
		final ModelAndView result;

		this.companyService.computeScoreForAll();

		result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	//Listing suspicious actors
	@RequestMapping(value = "/bannableList", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		final Collection<Actor> actors = this.actorService.spammerActors();

		result = new ModelAndView("administrator/bannableList");
		result.addObject("actors", actors);
		result.addObject("requestURI", "administrator/bannableList.do");

		return result;
	}

	//Ban and unban actors

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int varId) {
		final ModelAndView result;
		final Actor actor = this.actorService.findOne(varId);

		final Collection<Actor> actors = this.actorService.bannableActors();

		if (actor.getId() == this.actorService.findByPrincipal().getId()) {
			result = new ModelAndView("administrator/bannableList");
			result.addObject("actors", actors);
			result.addObject("message", "administrator.selfBan.error");
			result.addObject("requestURI", "administrator/bannableList.do");

			return result;
		} else {

			if (actors.contains(actor))
				this.actorService.BanOrUnban(actor.getId());

			result = new ModelAndView("redirect:/administrator/bannableList.do");

			return result;
		}
	}

	//Create auditor protected methods

	protected ModelAndView createEditModelAndView(final FormObjectAuditor foau) {
		ModelAndView result;

		result = this.createEditModelAndView(foau, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FormObjectAuditor foau, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("auditor/create");
		result.addObject("foau", foau);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "administrator/createAuditor.do");

		return result;

	}
	//Ancillary methods

	protected ModelAndView createEditModelAndView(final FormObjectAdministrator foa) {
		ModelAndView result;

		result = this.createEditModelAndView(foa, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FormObjectAdministrator foa, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("administrator/create");
		result.addObject("foa", foa);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "administrator/create.do");

		return result;

	}

	protected ModelAndView editModelAndView(final Administrator administrator) {
		ModelAndView result;

		result = this.editModelAndView(administrator, null);

		return result;
	}

	protected ModelAndView editModelAndView(final Administrator administrator, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("administrator/edit");
		result.addObject("administrator", administrator);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "administrator/edit.do");

		return result;
	}
}
