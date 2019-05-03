package controllers.actor;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AdminService;
import services.ApplicationService;
import services.CompanyService;
import services.CreditCardService;
import services.CurriculaService;
import services.FinderService;
import services.HackerService;
import services.MessageService;
import services.PositionService;
import services.ProblemService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Application;
import domain.Company;
import domain.CreditCard;
import domain.Curricula;
import domain.EducationData;
import domain.Finder;
import domain.Hacker;
import domain.MiscellaneousData;
import domain.Position;
import domain.PositionData;
import domain.Problem;
import domain.SocialProfile;

@Controller
@RequestMapping("actor/")
public class DeleteActorController extends AbstractController {

	// Services ----------------------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private AdminService adminService;
		
	@Autowired
	private FinderService finderService;

	@Autowired
	private HackerService hackerService;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private SocialProfileService socialProfileService;

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CurriculaService curriculaService;
	
//DELETE ALL DATA -----------------------------------------------------------------
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteActor(){
		ModelAndView res;
		Collection <Authority> auths = LoginService.getPrincipal().getAuthorities();
		Authority hackerAuth = new Authority();
		Authority companyAuth = new Authority();
		
		hackerAuth.setAuthority(Authority.HACKER);
		companyAuth.setAuthority(Authority.COMPANY);

		if (auths.contains(hackerAuth)) {
			res = this.deleteHacker();
		}else if(auths.contains(companyAuth)){
			res = this.deleteCompany();
		}else{
			res = new ModelAndView("error/access");
		}
		
		return res;
	}
	
	//Company -------------------------------------------------------------------
		@RequestMapping(value = "/deleteCompanyData", method = RequestMethod.GET)
		public ModelAndView deleteCompany() {
			
			ModelAndView res = new ModelAndView("redirect:/j_spring_security_logout");

			Company c = companyService.findByPrincipal();
			for (Application a : applicationService.findAppByPrincipalCompany()) {
//				a.setProblem(null);
//				a.setPosition(null);
				a.setCurricula(null);
				Application saved = applicationService.trueSave(a);
				applicationService.delete(saved);
				System.out.println("deleted "+ saved );
			}
			for (Problem p : problemService.findProblemByCompany(c.getId())) {
				problemService.delete(p);
				System.out.println("deleted" + p);
			}
			if (c.getSocialProfiles().size() != 0) {
				SocialProfile[] socials = new SocialProfile[c.getSocialProfiles().size()];
				Integer cont = 0;
				for (SocialProfile sp : c.getSocialProfiles()) {
					socials[cont] = sp;
					cont++;
				}
				
				for (int i = 0; i < socials.length; i++) {
					socialProfileService.delete(socials[i]);
					System.out.println("deleted "+ socials[i]);
				}
			}

			for (Position p : c.getPositions()) {
				for (Problem p1: problemService.findProblemByPosition(p)) {
						problemService.delete(p1);
						System.out.println("deleted "+ p1);
				}
				positionService.trueDelete(p);
			}
			positionService.flush();

			CreditCard credit = c.getCreditCard();
			System.out.println(credit);
			
			c.setCreditCard(null);
			companyService.flush();
	//		Company saved = companyService.save(c);
			if(credit != null){
				creditCardService.delete(credit);
				System.out.println("deleted "+ credit);
			}


			UserAccount ua = c.getUserAccount();	
			
			companyService.delete(c);
			userAccountService.delete(ua);

			return res;
		}
		
		//Company -------------------------------------------------------------------
				@RequestMapping(value = "/deleteHackerData", method = RequestMethod.GET)
				public ModelAndView deleteHacker() {
					
					ModelAndView res = new ModelAndView("redirect:/j_spring_security_logout");

					Hacker h = hackerService.findByPrincipal();
					
					
					h.getCreditCard();
					h.getUserAccount();
					
					if (h.getSocialProfiles().size() != 0) {
						SocialProfile[] socials = new SocialProfile[h.getSocialProfiles().size()];
						Integer cont = 0;
						for (SocialProfile sp : h.getSocialProfiles()) {
							socials[cont] = sp;
							cont++;
						}
						
						for (int i = 0; i < socials.length; i++) {
							socialProfileService.delete(socials[i]);
							System.out.println("deleted "+ socials[i]);
						}
					}

					Collection<Application> apps = h.getApplications();
					for (Application a : apps) {
						a.setProblem(null);
						a.setPosition(null);
						a.setCurricula(null);
						//Application saved = applicationService.trueSave(a);
						applicationService.delete(a);
						System.out.println("deleted "+ a );
					}


					for (Curricula c : h.getCurriculas()) {
						c.setEducationDatas(new ArrayList<EducationData>());
						c.setMiscellaneousDatas(new ArrayList<MiscellaneousData>());
						c.setPositionDatas(new ArrayList<PositionData>());
						c.setPersonalData(null);
						c.setHacker(null);
						
						//Curricula saved = curriculaService.save(c);
						
						curriculaService.delete(c);
						
						System.out.println("deleted " + c);
					}
					
					CreditCard credit = h.getCreditCard();
					System.out.println(credit);
					
					h.setCreditCard(null);
			//		Hacker saved = hackerService.save(h);
					if(credit != null){
						creditCardService.delete(credit);
						System.out.println("deleted "+ credit);
					}

					UserAccount ua = h.getUserAccount();	
					Finder f = finderService.findByHacker(h);
					finderService.delete(f);
					System.out.println("deleted "+ f);
				
					hackerService.delete(h);
					userAccountService.delete(ua);

					return res;
				}

}