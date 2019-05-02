
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	//Retrieves a list of items for a certain provider
	@Query("select i from Item i where i.provider.id=?1")
	Collection<Item> getItemsForProvider(int id);
}
