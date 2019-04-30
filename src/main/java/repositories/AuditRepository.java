
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {

	//Retrieves the list of audits for an auditor
	@Query("select a from Audit a where a.auditor.id=?1")
	Collection<Audit> auditsFromAuditor(int id);

	//Retrieves the list of audits for a certain position
	@Query("select a from Audit a where a.position.id=?1")
	Collection<Audit> getAuditsForPosition(int id);
}
