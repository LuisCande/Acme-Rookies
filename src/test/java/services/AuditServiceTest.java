
package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AuditServiceTest extends AbstractTest {

	// System under test: Audit ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private AuditService	auditService;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private PositionService	positionService;


	@Test
	public void AuditPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 94.3% | Covered Instructions 100 | Missed Instructions 6 | Total Instructions 106

			{
				"auditor1", null, "position4", "create", null
			},
			/*
			 * Positive: A auditor tries to create a audit.
			 * Requisite tested: 3.2. An actor who is authenticated as an auditor must be able to:
			 * Manage his or her audits, which includes listing them, showing them, creating them,
			 * updating, and deleting them. An audit can be updated or deleted as long as it is saved in draft mode.
			 * Data coverage : We created a audit with 5 out of 5 valid parameters.
			 * Exception expected: None. An auditor can create audits.
			 */
			{
				"auditor2", null, "audit3", "edit", null
			},
			/*
			 * Positive test: A auditor edits his audit.
			 * Requisite tested: Functional requirement - 3.2. An actor who is authenticated as an auditor must be able to:
			 * Manage his or her audits, which includes listing them, showing them, creating them,
			 * updating, and deleting them. An audit can be updated or deleted as long as it is saved in draft mode.
			 * Data coverage : From 3 editable attributes we tried to edit 1 attribute (text) with valid data.
			 * Exception expected: None. An auditor can edit his audits.
			 */
			{
				"auditor2", null, "audit3", "delete", null
			},
		/*
		 * Positive test: An auditor deletes his audits.
		 * Requisite tested: Functional requirement - 3.2. An actor who is authenticated as an auditor must be able to:
		 * Manage his or her audits, which includes listing them, showing them, creating them,
		 * updating, and deleting them. An audit can be updated or deleted as long as it is saved in draft mode.
		 * Data coverage : We tried to delete a audit which is not in final mode
		 * Exception expected: None. A auditor can delete his audits.
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
	public void AuditNegativeTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 94.9% | Covered Instructions 112 | Missed Instructions 6 | Total Instructions 118

			{
				"auditor1", null, "position4", "createNegative", ConstraintViolationException.class
			},
			/*
			 * Negative: A auditor tries to create an audit with invalid data.
			 * Requisite tested: 3.2. An actor who is authenticated as an auditor must be able to:
			 * Manage his or her audits, which includes listing them, showing them, creating them,
			 * updating, and deleting them. An audit can be updated or deleted as long as it is saved in draft mode.
			 * Data coverage : We created an audit with 4 out of 5 valid parameters.
			 * Exception expected: ConstraintViolationException. Moment must be past.
			 */
			{
				"auditor2", null, "audit1", "edit", IllegalArgumentException.class
			},
			/*
			 * Negative: A auditor tries to edit an audit that not owns.
			 * Requisite tested: 3.2. An actor who is authenticated as an auditor must be able to:
			 * Manage his or her audits, which includes listing them, showing them, creating them,
			 * updating, and deleting them. An audit can be updated or deleted as long as it is saved in draft mode.
			 * Data coverage : From 3 editable attributes we tried to edit 1 attribute (body) with a user that is not the owner.
			 * Exception expected: IllegalArgumentException. An auditor can not edit audits from another auditor.
			 */
			{
				"auditor1", null, "audit1", "delete", IllegalArgumentException.class
			},
		/*
		 * Negative: A auditor tries to delete an audit that is saved in final mode.
		 * Requisite tested: 3.2. An actor who is authenticated as an auditor must be able to:
		 * Manage his or her audits, which includes listing them, showing them, creating them,
		 * updating, and deleting them. An audit can be updated or deleted as long as it is saved in draft mode.
		 * Data coverage :We tried to delete a audit which is in final mode.
		 * Exception expected: IllegalArgumentException. An auditor can not delete audits which are in final mode.
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
				final Audit audit = this.auditService.create();
				final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				final Date moment = sdf.parse("21/12/2018 12:34");
				audit.setMoment(moment);
				audit.setText("This is the statement ain't it cool?");
				audit.setScore(2.5);
				audit.setFinalMode(false);
				final Position position = this.positionService.findOne(this.getEntityId(id));
				audit.setPosition(position);
				final Auditor auditor = this.auditorService.findOne(this.getEntityId(username));
				audit.setAuditor(auditor);

				this.auditService.save(audit);
			} else if (operation.equals("edit")) {
				final Audit audit = this.auditService.findOne(this.getEntityId(id));
				audit.setText("Testing body edition");

				this.auditService.save(audit);

			} else if (operation.equals("delete")) {
				final Audit audit = this.auditService.findOne(this.getEntityId(id));

				this.auditService.delete(audit);

			} else if (operation.equals("createNegative")) {
				final Audit audit = this.auditService.create();
				final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				final Date moment = sdf.parse("21/12/2020 12:34");
				audit.setMoment(moment);
				audit.setText("This is the statement ain't it cool?");
				audit.setScore(2.5);
				audit.setFinalMode(false);
				final Auditor auditor = this.auditorService.findOne(this.getEntityId(username));
				audit.setAuditor(auditor);
				final Position position = this.positionService.findOne(this.getEntityId(id));
				audit.setPosition(position);

				this.auditService.save(audit);

			}

			this.auditService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
