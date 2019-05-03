package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CompanyService;
import services.CurriculaService;
import services.FinderService;
import services.HackerService;
import services.PositionService;

import controllers.AbstractController;


@Controller
@RequestMapping("/admin/")
public class AdminDashboardController extends AbstractController {
	
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	HackerService hackerService;
	
	@Autowired
	FinderService finderService;
	
	@Autowired
	CurriculaService curriculaService;
	
	//DASHBOARD--------------------------------------------------------
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public ModelAndView dashboard(){
		ModelAndView res;
	
		res = new ModelAndView("admin/dashboard");

		res.addObject("avgPositionsPerCompany", Math.round(positionService.getAvgPositionsPerCompany()*100.0d/100.0d));
		res.addObject("minPositionsPerCompany", positionService.getMinPositionsPerCompany());
		res.addObject("maxPositionsPerCompany", positionService.getMaxPositionsPerCompany());
		res.addObject("stdevPositionsPerCompany", Math.round(positionService.getStdevPositionsPerCompany()*100.0d/100.0d));

		res.addObject("avgApplicationsPerHacker", Math.round(applicationService.getAvgApplicationsPerHacker()*100.0d/100.0d));
		res.addObject("minApplicationsPerHacker", applicationService.getMinApplicationsPerHacker());
		res.addObject("maxApplicationsPerHacker",applicationService.getMaxApplicationsPerHacker());
		res.addObject("stdevApplicationsPerHacker", Math.round(applicationService.getStdevApplicationsPerHacker()*100.0d/100.0d));
		
		res.addObject("avgSalaryPerPosition", Math.round(positionService.getAvgSalaryPerPosition()*100.0d/100.0d));
		res.addObject("minSalaryPerPosition", positionService.getMinSalaryPerPosition());
		res.addObject("maxSalaryPerPosition", positionService.getMaxSalaryPerPosition());
		res.addObject("stdevSalaryPerPosition", Math.round(positionService.getStdevSalaryPerPosition()*100.0d/100.0d));
		
		res.addObject("avgCurriculasPerHacker", Math.round(curriculaService.getAvgCurriculasPerHacker()*100.0d/100.0d));
		res.addObject("minCurriculasPerHacker", curriculaService.getMinCurriculasPerHacker());
		res.addObject("maxCurriculasPerHacker", curriculaService.getMaxCurriculasPerHacker());
		res.addObject("stdevCurriculasPerHacker", Math.round(curriculaService.getStdevCurriculasPerHacker()*100.0d/100.0d));
		
		res.addObject("avgResultsPerFinder", Math.round(finderService.getAvgResultsPerFinder()*100.0d/100.0d));
		res.addObject("minResultsPerFinder", finderService.getMinResultsPerFinder());
		res.addObject("maxResultsPerFinder", finderService.getMaxResultsPerFinder());
		res.addObject("stdevResultsPerFinder", Math.round(finderService.getStdevResultsPerFinder()*100.0d/100.0d));

		res.addObject("maxPositionsCompanies", companyService.getMaxPositionsCompanies());
		res.addObject("maxApplicationsHackers", hackerService.getMaxApplicationsHackers());
		
		res.addObject("bestSalaryPosition", positionService.getBestSalaryPosition());
		res.addObject("worstSalaryPosition", positionService.getWorstSalaryPosition());
		
		res.addObject("ratioEmptyFinders", finderService.getRatioEmptyFinders());


		return res;
	}
	
	// SCORE
	
	//DASHBOARD--------------------------------------------------------
//	@RequestMapping(value="/score", method=RequestMethod.GET)
//	public ModelAndView score(){
//		ModelAndView res;
//		
//		res = new ModelAndView("admin/score");
////		res.addObject("customersScore", customerEndorsementService.getScoreCustomerEndorsement());
////		res.addObject("handyworkersScore", handyWorkerEndorsementService.getScoreHandyWorkerEndorsement());
//		return res;
//	}
	
	
	
	//Helper methods---------------------------------------------------

	
}