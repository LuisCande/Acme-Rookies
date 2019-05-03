
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import security.UserAccount;
import domain.Hacker;
import domain.Message;

@Repository
public interface HackerRepository extends JpaRepository<Hacker, Integer> {
	
	@Query("select h from Hacker h where h.applications.size = (select max(h1.applications.size) from Hacker h1)")
	Collection<Hacker> getMaxApplicationsHackers();
	
	
	@Query("select h from Hacker h where h.userAccount.id = ?1")
	Hacker getHackerByUserAccountId(int id);
}
