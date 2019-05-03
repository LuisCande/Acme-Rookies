package services;

import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import security.LoginService;
import domain.Application;
import domain.Company;
import domain.Curricula;
import domain.Hacker;
import domain.Position;
import domain.Problem;
import forms.ApplicationCompanyForm;
import forms.ApplicationHackerForm;
//import forms.ApplicationHackerForm;

@Service
@Transactional
public class ApplicationService {

	//Managed Repository ------------------------------------------------------------------------
	
	@Autowired
	private ApplicationRepository appRepository;
	
	//Supporting Services -----------------------------------------------------------------------
	
	@Autowired
	private HackerService hackerService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private PositionService posService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private ProblemService problemService;

	//Simple CRUD methods -----------------------------------------------------------------------
	
	public Application create(Position position){

		Application res; 
		Problem problem;
		
		res = new Application();
		problem = (Problem) problemService.findProblemFinal(position.getCompany().getId()).toArray()[0];
		
		Date current = new Date(System.currentTimeMillis() - 1000);
		Hacker hacker = hackerService.findByPrincipal();
		res.setStatus("PENDING");
		res.setMoment(current);
		res.setHacker(hacker);
		res.setPosition(position);
		res.setProblem(problem);
		
		return res;
	}
	
	public void delete(Application a){
		this.appRepository.delete(a);
		appRepository.flush();
	}
	
	public Application save(Application a){
		Assert.isTrue(LoginService.hasRole("HACKER"));
		Curricula curricula;
		
		if(a.getStatus().equals("SUBMITTED")){
			Date current = new Date(System.currentTimeMillis() - 1000);
			a.setSubmitMoment(current);
			curricula = this.copyCurricula(a.getCurricula());
			a.setCurricula(curricula);
		}

		return appRepository.saveAndFlush(a);
	}
	
	public Application saveC(Application a){
		Assert.isTrue(LoginService.hasRole("COMPANY"));

		return appRepository.saveAndFlush(a);
	}
	
	public Application trueSave(Application a){
		return appRepository.saveAndFlush(a);
	}
	
	public Collection<Application> findAll(){
		return appRepository.findAll();
	}
	
	public Application findOne(int Id){
		return appRepository.findOne(Id);
	}
	
	//Other business methods ----------------------------------------------------------------------------
	
	public Application reconstruct(ApplicationHackerForm appForm,int id, BindingResult binding){
		Application app;
		Hacker hacker;
		
		app = this.findOne(appForm.getId());
		hacker = hackerService.findByPrincipal();
		
		app.setPosition(posService.findOne(id));
		app.setHacker(hacker);
		app.setAnswer(appForm.getAnswer());
		app.setCodeLink(appForm.getCodeLink());
		app.setProblem(appForm.getProblem());
		app.setCurricula(appForm.getCurricula());
		
		validator.validate(appForm, binding);
		if(binding.hasErrors()){
			throw new ValidationException();
		}
		return app;
	}
	
	public Application reconstruct(ApplicationCompanyForm appForm,int posId, int hId, BindingResult binding){
		Application app;
		
		app = this.findOne(appForm.getId());
		
		app.setPosition(posService.findOne(posId));
		app.setHacker(hackerService.findOne(hId));
		app.setStatus(appForm.getStatus());

		validator.validate(app, binding);
		if(binding.hasErrors()){
			throw new ValidationException();
		}
		
		return app;
	}
	
	public Curricula copyCurricula(Curricula a){
		Curricula result, saved;
		
		result = curriculaService.create();
		result.setEducationDatas(a.getEducationDatas());
		result.setHacker(a.getHacker());
		result.setMiscellaneousDatas(a.getMiscellaneousDatas());
		result.setName("Copy of "+a.getName());
		result.setPersonalData(a.getPersonalData());
		result.setPositionDatas(a.getPositionDatas());
		
		saved = curriculaService.save(result);
		
		return saved;
	}
	
	public Collection<Application> findAppByPrincipalHacker(){
		Hacker hacker = hackerService.findByPrincipal();
		return appRepository.findAppByHacker(hacker.getId());
	}
	
	public Collection<Application> findAppByPrincipalHackerAndStatus(String status){
		Hacker hacker = hackerService.findByPrincipal();
		return appRepository.findAppByHackerAndStatus(hacker.getId(),status);
	}
	
	public Collection<Application> findAppByPrincipalCompany(){
		Company company = companyService.findByPrincipal();
		return appRepository.findAppByCompany(company.getId());
	}
	
	public Collection<Application> findAppByPrincipalCompanyAndStatus(String status){
		Company company = companyService.findByPrincipal();
		return appRepository.findAppByCompanyAndStatus(company.getId(),status);
	}
	
	public Double getAvgApplicationsPerHacker(){
		Double res = appRepository.getAvgApplicationsPerHacker();
		if(res==null) res=0d;
		return res;
	}
	
	public Integer getMinApplicationsPerHacker(){
		Integer res = appRepository.getMinApplicationsPerHacker();
		if(res==null)res=0;
		return res;
	}
	
	public Integer getMaxApplicationsPerHacker(){
		Integer res=  appRepository.getMaxApplicationsPerHacker();
		if(res==null)res=0;
		return res;
	}
	
	public Double getStdevApplicationsPerHacker(){
		Double res = appRepository.getStdevApplicationsPerHacker();
		if(res==null)res=0d;
		return res;
	}
	
}
