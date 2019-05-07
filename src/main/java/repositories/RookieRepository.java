
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rookie;

@Repository
public interface RookieRepository extends JpaRepository<Rookie, Integer> {

	@Query("select r from Rookie r where r.finder.id=?1")
	Rookie rookieByFinder(int id);

	//The rookies who have made more applications
	@Query("select u.username from Rookie r join r.userAccount u order by ((select count(a) from Application a where a.rookie.id=r.id)*1.) desc")
	Collection<String> rookiesWithMoreApplications();

}
