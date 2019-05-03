package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.HackerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Curricula;
import domain.Hacker;
import domain.SocialProfile;


@Service
@Transactional
public class HackerService {

	//Managed Repository -----
	
	@Autowired
	private HackerRepository hackerRepository;
	
	//Supporting Services -----
	
	//Simple CRUD methods -----
	
	public Hacker create(UserAccount ua){
		Hacker res = new Hacker();
		res.setApplications(new ArrayList<Application>());
		res.setCurriculas(new ArrayList<Curricula>());
		res.setIsBanned(false);
		res.setIsSpammer(false);
		res.setSocialProfiles(new ArrayList<SocialProfile>());
		res.setUserAccount(ua);
		
		return res;
	}
	
	public Collection<Hacker> findAll(){
		return hackerRepository.findAll();
	}
	
	public Hacker findOne(int Id){
		return hackerRepository.findOne(Id);
	}
	
	public Hacker save(Hacker a){
		
		Hacker saved = hackerRepository.saveAndFlush(a);
		return saved;
	}
	
	public void delete(Hacker a){
		hackerRepository.delete(a);
	}
	
	//Other business methods -----
	
	public Collection<Curricula> getCurriculas(){
		Hacker hacker;
		Collection<Curricula> curriculas;
		
		hacker = this.findByPrincipal();
		curriculas = hacker.getCurriculas();
		
		for(Curricula c:curriculas){
			if(c.getName().contains("Copy of"))
				curriculas.remove(c);
		}
		
		return curriculas;
	}
	
	public Collection<Hacker> getMaxApplicationsHackers(){
		return hackerRepository.getMaxApplicationsHackers();
	}
	
	public Hacker findByPrincipal(){
		return hackerRepository.getHackerByUserAccountId(LoginService.getPrincipal().getId());
	}
}