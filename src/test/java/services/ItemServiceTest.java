
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Item;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ItemServiceTest extends AbstractTest {

	// System under test: Item ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private ProviderService	providerService;
	@Autowired
	private ItemService		itemService;


	@Test
	public void ItemPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 94.3% | Covered Instructions 100 | Missed Instructions 6 | Total Instructions 106
			{
				"provider1", "Test item", null, "create", null
			},
			/*
			 * Positive test: A provider creates an item.
			 * Requisite tested: 10.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
			 * Data coverage : We created a item with an accepted parade (parade3) and a valid VISA credit card.
			 * Exception expected: None.A Provider can create items.
			 */
			{
				"provider1", null, "item1", "edit", null
			},
			/*
			 * Positive: A sponsor tries to create a item
			 * Requisite tested: 10.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
			 * Data coverage :From 4 editable atributes we tried to edit 1 atribute (banner) with valid data.
			 * Exception expected: None. A Provider can edit his items.
			 */
			{
				"provider1", null, "item1", "delete", null
			},
		/*
		 * Positive test: A provider deletes his item.
		 * Requisite tested: 10.1. An actor who is authenticated as a provider must be able to:
		 * Manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
		 * Data coverage : A provider deletes his item
		 * Exception expected: None. A provider can delete his items.
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
	public void ItemNegativeTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 94.9% | Covered Instructions 112 | Missed Instructions 6 | Total Instructions 118
			{
				"provider1", " ", null, "create", ConstraintViolationException.class
			},
			/*
			 * Negative: A provider tries to create an item with invalid daTA
			 * Requisite tested: 10.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
			 * Data coverage : We tried to create a item with an invalid name
			 * Exception expected: ConstraintViolationException.class. Name cannot be blank
			 */
			{
				"provider2", null, "item1", "editNegative", IllegalArgumentException.class
			},
			/*
			 * Negative: A provider tries to edit an item from another provider.
			 * Requisite tested: 10.1. An actor who is authenticated as a provider must be able to:
			 * Manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
			 * Data coverage : We tried to edit an item with an invalid user
			 * Exception expected: IllegalArgumentException. A provider can not edit items from another provider.
			 */
			{
				"provider2", null, "item1", "delete", IllegalArgumentException.class
			},
		/*
		 * Negative: A provider tries to delete an item that not owns.
		 * Requisite tested: 10.1. An actor who is authenticated as a provider must be able to:
		 * Manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
		 * Data coverage : We tried to delete an item with an invalid user.
		 * Exception expected: IllegalArgumentException. A provider can not edit items from another provider.
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
				final Item item = this.itemService.create();

				item.setName(st);
				item.setDescription("test description");
				item.setLink("http://www.test.com");
				item.setPicture("http://www.test.com");

				this.itemService.save(item);

			} else if (operation.equals("edit")) {
				final Item item = this.itemService.findOne(this.getEntityId(id));
				item.setLink("https://www.test.com");

				this.itemService.save(item);

			} else if (operation.equals("delete")) {
				final Item item = this.itemService.findOne(this.getEntityId(id));
				this.itemService.delete(item);

			} else if (operation.equals("editNegative")) {
				final Item item = this.itemService.findOne(this.getEntityId(id));
				item.setName("Test negative man");

				this.itemService.save(item);

			}
			this.providerService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
