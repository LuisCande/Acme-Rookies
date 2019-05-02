
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Provider;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProviderServiceTest extends AbstractTest {

	// System under test: Provider ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private PositionService	positionService;


	@Test
	public void ProviderPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 92.9% | Covered Instructions 79 | Missed Instructions 6 | Total Instructions 85

			{
				null, "testProvider1", null, "create", null
			},
			/*
			 * 
			 * Positive test: An user registers as a new provider
			 * Requisite tested: Functional requirement - 9.3. An actor who is not authenticated must be able to:
			 * Register to the system as a provider.
			 * Data coverage : We created a new provider with valid data.
			 * Exception expected: None.
			 */
			{
				"provider1", null, "provider1", "editPositive", null
			}

		/*
		 * Positive test: A provider edit its makeP.
		 * Requisite tested: Functional requirement - 9.3. An actor who is not authenticated must be able to:
		 * Register to the system as a provider.
		 * Data coverage : From 12 editable attributes we tried to edit 1 attributes (makeP) with valid data.
		 * Exception expected: None. A provider can edit his data.
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
	public void ProviderNegativeTest() {
		final Object testingData[][] = {
			//Total Sentence Coverage: Coverage 93.8% | Covered Instructions 91 | Missed Instructions 6 | Total Instructions 97
			{
				"provider1", null, "provider2", "editNegative", IllegalArgumentException.class
			},
			/*
			 * Negative test: A provider tries to edit the another provider personal data.
			 * Requisite tested: Functional requirement 9.3. An actor who is not authenticated must be able to:
			 * Register to the system as a provider.
			 * Data coverage: From 11 editable attributes we tried to edit 1 attribute (makeP) of another user.
			 * Exception expected: IllegalArgumentException A provider cannot edit others personal data.
			 */

			{
				"provider2", "", null, "editNegative1", ConstraintViolationException.class
			},

		/*
		 * Negative test: A provider tries to edit its profile with invalid data.
		 * Requisite tested: Functional requirement -9.3. An actor who is not authenticated must be able to:
		 * Register to the system as a provider.
		 * Data coverage: From 11 editable attributes we tried to edit 1 attributes (makeP) with invalid data.
		 * Exception expected: ConstraintViolationException. MakeP cannot be blank.
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
				final Provider provider = this.providerService.create();

				provider.setName("nombre");
				provider.setSurnames("sname");
				provider.setAddress("calle");
				provider.setPhoto("https://www.photos.com");
				provider.setPhone("666666666");
				provider.getUserAccount().setPassword("test");
				provider.getUserAccount().setUsername(st);
				provider.setEmail("email@email.com");
				provider.setMakeP("Provider Name Test");
				provider.setVatNumber("ATU1412345");
				final CreditCard creditCard1 = new CreditCard();
				creditCard1.setCvv(115);
				creditCard1.setHolder("Test Man");
				creditCard1.setMake("VISA");
				creditCard1.setNumber("5564157826282522");
				creditCard1.setExpMonth(10);
				creditCard1.setExpYear(2030);
				provider.setCreditCard(creditCard1);

				this.providerService.save(provider);

			} else if (operation.equals("editPositive")) {
				final Provider provider = this.providerService.findOne(this.getEntityId(id));
				provider.setMakeP("Thanks god this is a String");

				this.providerService.save(provider);
			} else if (operation.equals("editNegative")) {
				final Provider provider = this.providerService.findOne(this.getEntityId(id));
				provider.setName("Test negative name");
				provider.setSurnames("Test negative surnames");
				provider.setMakeP("Test title");
				provider.setAddress("Test address");
				this.providerService.save(provider);

			} else if (operation.equals("editNegative1")) {
				final Provider provider = this.providerService.findOne(this.getEntityId(username));
				provider.getUserAccount().setUsername(st);
				this.providerService.save(provider);
			}
			this.providerService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
