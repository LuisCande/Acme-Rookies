
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	//The companies that have offered more positions
	@Query("select c.name from Company c order by ((select count(p) from Position p where p.finalMode=true and p.company.id=c.id)*1.) desc")
	Collection<String> companiesWithMoreOfferedPossitions();

	//The average, the minimum, the maximum, and the standard deviation of the audit score of the companies that are registered in the system
	@Query("select avg(c.auditScore), min(c.auditScore), max(c.auditScore), stddev(c.auditScore) from Company c")
	Double[] avgMinMaxStddevAuditScorePerCompany();

	//The companies with the highest audit score.
	@Query("select u.username from Company c join c.userAccount u order by (c.auditScore) desc")
	Collection<String> companiesWithHighAuditScore();
}
