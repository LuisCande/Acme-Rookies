
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Application;
import domain.CreditCard;
import domain.Rookie;
import domain.Status;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RookieServiceTest extends AbstractTest {

	// System under test: Rookie ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private RookieService		rookieService;

	@Autowired
	private ApplicationService	applicationService;


	@Test
	public void RookiePositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 92.9% | Covered Instructions 79 | Missed Instructions 6 | Total Instructions 85

			{
				null, "testRookie1", null, "create", null
			},
			/*
			 * 
			 * Positive test: An user registers as a new rookie
			 * Requisite tested: Functional requirement - 7.1. An actor who is not authenticated must be able to:Register to the system as a rookie or a rookie
			 * Data coverage : We created a new rookie with valid data.
			 * Exception expected: None. A rookie can edit his data.
			 */{
				"rookie1", null, "application1", "editPositive", null
			}

		/*
		 * Positive test: A rookie update an application.
		 * Requisite tested: Functional requirement - 10.1 An actor who is authenticated as rookie must be able to manage his or her applications,
		 * wich includes listing them grouped by status, showing them, creating them, and updating them.
		 * Data coverage : From 5 editable attributes we tried to edit 1 attribute (status) with valid data.
		 * Exception expected: None. A rookie can edit his applications.
		 */

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void RookieNegativeTest() {
		final Object testingData[][] = {
			//Total Sentence Coverage: Coverage 93.8% | Covered Instructions 91 | Missed Instructions 6 | Total Instructions 97
			{
				"rookie1", null, "rookie2", "editNegative", IllegalArgumentException.class
			},
			/*
			 * Negative test: A rookie tries to edit the another rookie personal data.
			 * Requisite tested: Functional requirement - An actor who is authenticated must be able to edit his personal data.
			 * Data coverage: From 9 editable attributes we tried to edit 3 attributes (name, surnames, address) of another user.
			 * Exception expected: IllegalArgumentException A rookie cannot edit others personal data.
			 */

			{
				"rookie2", "", null, "editNegative1", ConstraintViolationException.class
			},

		/*
		 * Negative test: A rookie tries to edit its profile with invalid data.
		 * Requisite tested: Functional requirement - An actor who is authenticated as a rookie must be able to manage
		 * their parades, which includes listing, showing, creating, updating, and deleting them.
		 * Data coverage: From 9 editable attributes we tried to edit 1 attributes (username).
		 * Exception expected: IllegalArgumentException A rookie cannot edit with invalid data.
		 */
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void template(final String username, final String st, final String id, final String operation, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			if (operation.equals("create")) {
				final Rookie rookie = this.rookieService.create();

				rookie.setName("nombre");
				rookie.setSurnames("sname");
				rookie.setAddress("calle");
				rookie.setPhoto("https://www.photos.com");
				rookie.setPhone("666666666");
				rookie.getUserAccount().setPassword("test");
				rookie.getUserAccount().setUsername(st);
				rookie.setEmail("email@email.com");
				rookie.setVatNumber("ATU1412345");
				final CreditCard creditCard1 = new CreditCard();
				creditCard1.setCvv(115);
				creditCard1.setHolder("Test Man");
				creditCard1.setMake("VISA");
				creditCard1.setNumber("5564157826282522");
				creditCard1.setExpMonth(10);
				creditCard1.setExpYear(2030);
				rookie.setCreditCard(creditCard1);

				this.rookieService.save(rookie);

			} else if (operation.equals("editPositive")) {
				final Application application = this.applicationService.findOne(this.getEntityId(id));
				application.setStatus(Status.SUBMITTED);

				this.applicationService.save(application);
			} else if (operation.equals("editNegative")) {
				final Rookie rookie = this.rookieService.findOne(this.getEntityId(id));
				rookie.setName("Test negative name");
				rookie.setSurnames("Test negative surnames");
				rookie.setAddress("Test address");
				this.rookieService.save(rookie);

			} else if (operation.equals("editNegative1")) {
				final Rookie rookie = this.rookieService.findOne(this.getEntityId(username));
				rookie.getUserAccount().setUsername(st);
				this.rookieService.save(rookie);
			}
			this.rookieService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
