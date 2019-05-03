
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
import domain.Sponsorship;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	// System under test: Sponsorship ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private ProviderService		providerService;
	@Autowired
	private SponsorshipService	sponsorshipService;
	@Autowired
	private PositionService		positionService;


	@Test
	public void SponsorshipPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 94.3% | Covered Instructions 100 | Missed Instructions 6 | Total Instructions 106
			{
				"provider1", null, "position5", "create", null
			}
			/*
			 * Positive test: A provider creates a sponsorship.
			 * Requisite tested: Functional requirement 13.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her sponsorships, which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage : We created a sponsorship with a valid position (positon5) and a valid VISA credit card.
			 * Exception expected: None.A Provider can create sponsorships.
			 */
			, {
				"provider1", null, "sponsorship1", "edit", null
			},
			/*
			 * Positive: A provider edits his sponsorship.
			 * Requisite tested: Functional requirement 13.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her sponsorships, which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage :From 4 editable atributes we tried to edit 1 atribute (banner) with valid data.
			 * Exception expected: None. A Provider can edit his sponsorships.
			 */
			{
				"provider1", null, "sponsorship1", "delete", null
			},
		/*
		 * Positive test: A provider deletes his sponsorship.
		 * Requisite tested: Functional requirement 13.1. An actor who is authenticated as a provider must be able to:
		 * Manage his or her sponsorships, which includes listing, showing, creating, updating, and deleting them.
		 * Data coverage : A provider deletes his sponsorship
		 * Exception expected: None. A provider can delete his sponsorships.
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
	public void SponsorshipNegativeTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 94.9% | Covered Instructions 112 | Missed Instructions 6 | Total Instructions 118
			{
				"provider1", null, "position1", "createNegative", IllegalArgumentException.class
			},
			/*
			 * Negative: A provider tries to create a sponsorship for a position which is not in final mode
			 * Requisite tested: Functional requirement 13.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her sponsorships, which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage : We tried to create a sponsorship with valid atributes, but for an invalid position
			 * Exception expected: IllegalArgumentException.Position must be in final mode.
			 */
			{
				"provider1", null, "sponsorship1", "editNegative", ConstraintViolationException.class
			},
			/*
			 * Negative: A provider tries to edit a sponsorship with invalid data.
			 * Requisite tested: Functional requirement 13.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her sponsorships, which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage : From 4 editable attributes we tried to edit 1 attribute (banner) with invalid data.
			 * Exception expected: ConstraintViolationException .The Sponsorship Banner must be a valid url.
			 */
			{
				"provider2", null, "sponsorship1", "delete", IllegalArgumentException.class
			},
		/*
		 * Negative: A sponsor tries to delete a sponsorship that not owns.
		 * Requisite tested: Functional requirement 13.1. An actor who is authenticated as a provider must be able to:
		 * Manage his or her sponsorships, which includes listing, showing, creating, updating, and deleting them.
		 * Data coverage : From 9 editable attributes we tried to edit 1 attribute (banner).
		 * Exception expected: IllegalArgumentException. A Sponsor can not edit sponsorships from another sponsor.
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
				final Sponsorship s = this.sponsorshipService.create();
				final CreditCard c = new CreditCard();
				c.setHolder("test");
				c.setMake("VISA");
				c.setNumber("4167363478835187");
				c.setExpMonth(8);
				c.setExpYear(2020);
				c.setCvv(123);

				s.setBanner("http://www.test.com");
				s.setTargetPage("http://www.test.com");
				s.setCreditCard(c);
				s.setPosition(this.positionService.findOne(this.getEntityId(id)));

				this.sponsorshipService.save(s);

			} else if (operation.equals("edit")) {
				final Sponsorship sponsorship = this.sponsorshipService.findOne(this.getEntityId(id));
				sponsorship.setBanner("https://www.test.com");

				this.sponsorshipService.save(sponsorship);

			} else if (operation.equals("delete")) {
				final Sponsorship sponsorship = this.sponsorshipService.findOne(this.getEntityId(id));
				this.sponsorshipService.delete(sponsorship);

			} else if (operation.equals("editNegative")) {
				final Sponsorship sponsorship = this.sponsorshipService.findOne(this.getEntityId(id));
				sponsorship.setBanner(" ");

				this.sponsorshipService.save(sponsorship);

			} else if (operation.equals("createNegative")) {
				final Sponsorship s = this.sponsorshipService.create();
				final CreditCard c = new CreditCard();
				c.setHolder("test");
				c.setMake("VISA");
				c.setNumber("4167363478835187");
				c.setExpMonth(8);
				c.setExpYear(2020);
				c.setCvv(123);

				s.setBanner("http://www.test.com");
				s.setTargetPage("http://www.test.com");
				s.setCreditCard(c);
				s.setPosition(this.positionService.findOne(this.getEntityId(id)));

				this.sponsorshipService.save(s);
			}
			this.providerService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
