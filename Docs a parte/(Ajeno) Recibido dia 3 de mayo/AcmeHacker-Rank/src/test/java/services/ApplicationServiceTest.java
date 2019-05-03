package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Application;
import domain.Curricula;
import domain.Hacker;
import domain.Position;
import domain.Problem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/junit.xml"})
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	// Managed service --------------------------------------------------
	
	@Autowired
	private ApplicationService appService;
	
	// Support services -------------------------------------------------
	
	@Autowired
	private PositionService posService;
	
	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private HackerService hackerService;
	
	@Autowired
	private CurriculaService curriculaService;
	
	

	// Tests ------------------------------------------------------------
	
	@Test
	public void testCreate() {
		
		Position pos;
		Application app;
		
		authenticate("hacker1");
		
		pos = posService.findOne(getEntityId("position1"));
		app = appService.create(pos);
		
		Assert.notNull(app.getHacker());
		Assert.notNull(app.getPosition());
		Assert.notNull(app.getStatus());
		Assert.notNull(app.getMoment());
		Assert.notNull(app.getProblem());
		
		Assert.isNull(app.getAnswer());
		Assert.isNull(app.getCodeLink());
		Assert.isNull(app.getCurricula());
		Assert.isNull(app.getSubmitMoment());
		
		unauthenticate();
		
	}

	@Test
	public void testSave() {

		Application app, saved;
		Position pos;
		Hacker hacker;
		Problem problem;
		
		authenticate("hacker1");
		
		pos = posService.findOne(getEntityId("position1"));
		app = appService.create(pos);
		hacker = hackerService.findByPrincipal();
		problem = problemService.findOne(getEntityId("problem1"));
		
		Date current = new Date(System.currentTimeMillis() - 1000);
		
		app.setAnswer("Estoy preparado para esto");
		app.setCodeLink("http://www.github.com/user/mycode.php");
		app.setCurricula((Curricula) hacker.getCurriculas().toArray()[0]);
		app.setProblem(problem);
		app.setStatus("SUBMITTED");
		app.setSubmitMoment(current);
		
		saved = appService.save(app);
		Assert.isTrue(appService.findAll().contains(saved));
		
		unauthenticate();
	}
	
	// Functional testing -----------------------------------------------------------
	
	// RF 9.3
	// Guardar una aplicación un usuario que no sea ni Hacker ni Company.
	@Test(expected = IllegalArgumentException.class)
	public void testSaveAdmin(){
		
		authenticate("admin");
		
		Application app;
		
		app = appService.create(posService.findOne(getEntityId("position1")));
		
		app.setStatus("REJECTED");
		
		appService.save(app);
		
		unauthenticate();

	}
	
	// RF 10.1
	@Test
	public void testUpdateApp(){
		
		Application app, result;
		Hacker hacker;
		
		authenticate("hacker3");
		
		app = appService.findOne(getEntityId("application5"));
		hacker = hackerService.findByPrincipal();
		
		app.setAnswer("preparado");
		app.setCodeLink("http://www.minu.es/profile=124/photo.png");
		app.setCurricula((Curricula)curriculaService.findByHacker(hacker).toArray()[0]);
	
		result = appService.save(app);
		
		unauthenticate();
		
		
		authenticate("company1");
		
		result.setStatus("ACCEPTED");
		
		unauthenticate();
		
	}

}
