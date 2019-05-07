
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.MiscellaneousData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MiscellaneousDataServiceTest extends AbstractTest {

	// System under test: MiscellaneousData ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;


	@Test
	public void MiscellaneousDataPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 94.3% | Covered Instructions 100 | Missed Instructions 6 | Total Instructions 106
			{
				"rookie1", null, "curriculum1", "create", null
			},
			/*
			 * 
			 * Positive test: A rookie registers a new miscellaneousData
			 * Requisite tested: Functional requirement - 17.1. An actor who is authenticated as a rookie must be able
			 * to: Manage his or her curricula , which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage : We created a new miscellaneousData with valid data.
			 * Exception expected: None. A rookie can edit his miscellaneousData.
			 */{
				"rookie1", null, "miscellaneousData1", "edit", null
			},

			/*
			 * Positive test: A rookie edit his miscellaneousData.
			 * Requisite tested: Functional requirement - 17.1. An actor who is authenticated as a rookie must be able
			 * to: Manage his or her curricula , which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage : From 2 editable attributes we tried to edit 1 attribute (text) with valid data.
			 * Exception expected: None. A rookie can edit his miscellaneousDatas.
			 */

			{
				"rookie1", null, "miscellaneousData1", "delete", null
			},
		/*
		 * 
		 * Positive test: A rookie delete his miscellaneousData
		 * Requisite tested: Functional requirement - 17.1. An actor who is authenticated as a rookie must be able
		 * to: Manage his or her curricula , which includes listing, showing, creating, updating, and deleting them.
		 * Data coverage : We tried to delete a miscellaneousData.
		 * Exception expected: None. A rookie can delete his miscellaneousData.
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
	public void MiscellaneousDataNegativeTest() {
		final Object testingData[][] = {
			//Total Sentence Coverage: Coverage 95.1% | Covered Instructions 116 | Missed Instructions 6 | Total Instructions 122
			{
				"rookie1", "", "curriculum1", "createNegative", ConstraintViolationException.class
			},
			/*
			 * Negative test: A rookie tries to create a miscellaneousData
			 * Requisite tested: Functional requirement - 17.1. An actor who is authenticated as a rookie must be able
			 * to: Manage his or her curricula , which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage: We created a miscellaneousData with 1 out of 2 valid parameters.
			 * Exception expected: ConstraintViolationException. Text cannot be blank.
			 */

			{
				"rookie2", null, "miscellaneousData1", "edit", IllegalArgumentException.class
			},

			/*
			 * Negative test: A rookie tries to edit a miscellaneousData that not owns.
			 * Requisite tested: Functional requirement - 17.1. An actor who is authenticated as a rookie must be able
			 * to: Manage his or her curricula , which includes listing, showing, creating, updating, and deleting them.
			 * Data coverage : From 2 editable attributes we tried to edit 1 attribute (Text) with a user that is not the owner.
			 * Exception expected: IllegalArgumentException. A Rookie can not edit miscellaneousDatas from another rookie.
			 */
			{
				"rookie2", null, "miscellaneousData1", "delete", IllegalArgumentException.class
			},
		/*
		 * Negative test: A rookie tries to delete a miscellaneousData that not owns.
		 * Requisite tested: Functional requirement - 17.1. An actor who is authenticated as a rookie must be able
		 * to: Manage his or her curricula , which includes listing, showing, creating, updating, and deleting them.
		 * Data coverage : We tried to delete a miscellaneousData with a user that is not the owner.
		 * Exception expected: IllegalArgumentException. A Rookie can not delete miscellaneousDatas from another rookie.
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

			if (operation.equals("edit")) {
				final MiscellaneousData miscellaneousData = this.miscellaneousDataService.findOne(this.getEntityId(id));
				miscellaneousData.setText("Text");

				this.miscellaneousDataService.save(miscellaneousData);

			} else if (operation.equals("create")) {

				final MiscellaneousData miscellaneousData = this.miscellaneousDataService.create(this.getEntityId(id));

				miscellaneousData.setText("text");
				miscellaneousData.setAttachments("attachments");

				this.miscellaneousDataService.save(miscellaneousData);

			} else if (operation.equals("delete")) {
				final MiscellaneousData miscellaneousData = this.miscellaneousDataService.findOne(this.getEntityId(id));
				this.miscellaneousDataService.delete(miscellaneousData);

			} else if (operation.equals("createNegative")) {
				final MiscellaneousData miscellaneousData = this.miscellaneousDataService.create(this.getEntityId(id));

				miscellaneousData.setText(st);
				miscellaneousData.setAttachments("attachments");

				this.miscellaneousDataService.save(miscellaneousData);
			}
			this.miscellaneousDataService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
