
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
	
	@Query("select a from Application a where a.hacker.id = ?1 order by a.status")
	Collection<Application> findAppByHacker(int id);
	
	@Query("select a from Application a where a.hacker.id = ?1 and a.status=?2 order by a.status")
	Collection<Application> findAppByHackerAndStatus(int id, String status);
	
	@Query("select a from Application a where a.position.company.id = ?1 order by a.status")
	Collection<Application> findAppByCompany(int id);
	
	@Query("select a from Application a where a.position.company.id = ?1 and a.status=?2 order by a.status")
	Collection<Application> findAppByCompanyAndStatus(int id, String status);
	
	@Query("select avg(h.applications.size) from Hacker h")
	Double getAvgApplicationsPerHacker();
	
	@Query("select min(h.applications.size) from Hacker h")
	Integer getMinApplicationsPerHacker();
	
	@Query("select max(h.applications.size) from Hacker h")
	Integer getMaxApplicationsPerHacker();
	
	@Query("select stddev(h.applications.size) from Hacker h")
	Double getStdevApplicationsPerHacker();
}
