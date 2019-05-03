package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import utilities.AbstractTest;
import domain.Admin;
import domain.Company;
import domain.CreditCard;
import domain.Hacker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/junit.xml"})
@Transactional
public class ActorServiceTest extends AbstractTest {

	// Managed service --------------------------------------------------
	
	@Autowired
	private ApplicationService appService;
	
	// Support services -------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private HackerService hackerService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private CurriculaService curriculaService;
	
	
	
	

	// Tests ------------------------------------------------------------
	
	@Test
	public void driverHacker(){
		
		UserAccount ua = userAccountService.create();
		Authority auth = new Authority();
		auth.setAuthority(Authority.HACKER);
		ua.getAuthorities().add(auth);
		ua.setUsername("newhacker");
		ua.setPassword("password");		
		Hacker hacker = hackerService.create(ua);
		
		CreditCard credit = creditCardService.create();
		credit.setCVV(123);
		credit.setHolder("me");
		credit.setExpirationDate(new Date(System.currentTimeMillis()+623415234));
		credit.setMake("AMEX");
		credit.setNumber("4576098756783456");
		
		hacker.setName("name");
		hacker.setSurnames("a b");
		hacker.setVatNumber("ASD12341234");
		hacker.setEmail("email@email.com");
		hacker.setPhone("612123456");
		hacker.setCreditCard(credit);
		
		Hacker hacker1 = hackerService.findOne(getEntityId("hacker1"));
		
		System.out.println(hacker1 +" " +hacker);
		
		Object testingData[][] = {
				{hacker, null},
				{hacker1, null},
				{null,NullPointerException.class}
		};
		for(int i=0; i<testingData.length;i++){
			templateHacker((Hacker) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void templateHacker(Hacker hacker, Class<?> expected){
		Class<?> caught = null;
		try{
			
			actorService.registerHacker(hacker);
			
		}catch(Throwable oops){
			oops.printStackTrace();
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void driverCompany(){
		
		UserAccount ua = userAccountService.create();
		Authority auth = new Authority();
		auth.setAuthority(Authority.HACKER);
		ua.getAuthorities().add(auth);
		ua.setUsername("newcompany");
		ua.setPassword("password");		
		Company company = companyService.create(ua);
		
		CreditCard credit = creditCardService.create();
		credit.setCVV(123);
		credit.setHolder("me");
		credit.setExpirationDate(new Date(System.currentTimeMillis()+623415234));
		credit.setMake("AMEX");
		credit.setNumber("2354635425346978");
		
		company.setName("name");
		company.setSurnames("a b");
		company.setVatNumber("ASD12341234");
		company.setEmail("email@email.com");
		company.setPhone("612123456");
		company.setCreditCard(credit);
		company.setCommercialName("asdf");
		
		Company company1 = companyService.findOne(getEntityId("company1"));
		
		System.out.println(company1 +" " +company);
		
		Object testingData[][] = {
				{company, null},
				{company1, null},
				{null,NullPointerException.class}
		};
		for(int i=0; i<testingData.length;i++){
			templateCompany((Company) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void templateCompany(Company company, Class<?> expected){
		Class<?> caught = null;
		try{
			
			actorService.registerCompany(company);
			
		}catch(Throwable oops){
			oops.printStackTrace();
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void driverAdmin(){
		
		UserAccount ua = userAccountService.create();
		Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		ua.getAuthorities().add(auth);
		ua.setUsername("newadmin");
		ua.setPassword("password");		
		Admin admin1 = adminService.create(ua);
		
		CreditCard credit = creditCardService.create();
		credit.setCVV(123);
		credit.setHolder("me");
		credit.setExpirationDate(new Date(System.currentTimeMillis()+623415234));
		credit.setMake("AMEX");
		credit.setNumber("2534746553427456");
		
		admin1.setName("name");
		admin1.setSurnames("a b");
		admin1.setVatNumber("ASD12341234");
		admin1.setEmail("email@email.com");
		admin1.setPhone("612123456");
		admin1.setCreditCard(credit);
		
		Admin admin = adminService.findOne(getEntityId("admin"));
		
		System.out.println(admin +" " +admin1);
		
		Object testingData[][] = {
				{admin, null},
				{admin1, IllegalArgumentException.class},
				{null,NullPointerException.class}
		};
		for(int i=0; i<testingData.length;i++){
			templateAdmin((Admin) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void templateAdmin(Admin admin, Class<?> expected){
		Class<?> caught = null;
		try{
			if(admin.equals(adminService.findOne(getEntityId("admin")))){
				authenticate("admin");
			}else {
				authenticate(null);
			}
			
			actorService.registerAdmin(admin);
			
		}catch(Throwable oops){
			oops.printStackTrace();
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	

}
