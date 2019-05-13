
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	//Retrieves a list of all positions for a certain company
	@Query("select p from Position p where p.company.id=?1")
	Collection<Position> getAllPositionsForCompany(int id);

	//Retrieves a list of positions with final mode = true and not cancelled
	@Query("select p from Position p where p.finalMode='1' and p.cancelled='0'")
	Collection<Position> getPublicPositions();

	//Retrieves a list of positions given a certain problem
	@Query("select distinct p from Position p join p.problems pr where pr.id=?1")
	Collection<Position> getPositionsOfAProblem(int id);

	//Positdions which a rookie can do applications
	@Query("select p from Position p where p.finalMode=true and p.cancelled=false and p not in (select a.position from Application a where a.rookie.id=?1 and a.status!='3')")
	Collection<Position> positionsForRequestsByRookie(int id);

	//Retrieves a list of positions with final mode = true and not cancelled for a certain company
	@Query("select p from Position p where p.finalMode='1' and p.cancelled='0' and p.company.id=?1")
	Collection<Position> getPublicPositionsForCompany(int id);

	//The average, the minimum, the maximum, and the standard deviation of the number of positions per company.
	@Query("select avg((select count(p) from Position p where p.company.id=c.id)*1.0), min((select count(p) from Position p where p.company.id=c.id)*1.0), max((select count(p) from Position p where p.company.id=c.id)*1.0), stddev((select count(p) from Position p where p.company.id=c.id)*1.0) from Company c")
	Double[] avgMinMaxStddevPositionsPerCompany();

	//The average, the minimum, the maximum, and the standard deviation of the salaries offered.
	@Query("select avg(p.offeredSalary), min(p.offeredSalary), max(p.offeredSalary), stddev(p.offeredSalary) from Position p where p.finalMode=true")
	Double[] avgMinMaxStddevOfferedSalaries();

	//The best position in terms of salary
	@Query("select p.title from Position p where p.finalMode=true order by p.offeredSalary desc")
	Collection<String> bestPositions();

	//The worst position in terms of salary
	@Query("select p.title from Position p where p.finalMode=true order by p.offeredSalary asc")
	Collection<String> worstPositions();

	//Search positions 
	@Query("select p from Position p join p.company c where p.finalMode='1' and p.cancelled='0' and (p.title like %?1% or p.description like %?1% or p.requiredSkills like %?1% or p.requiredTech like %?1% or p.requiredProfile like %?1% or p.description like %?1% or c.commercialName like %?1%)")
	Collection<Position> searchPosition(String keyWord);

	//The average, the minimum, the maximum, and the standard deviation of the audit score of the positions stored in the system
	@Query("select avg((select avg(a.score) from Audit a where a.position.id=p.id)*1.), min((select avg(a.score) from Audit a where a.position.id=p.id)*1.), max((select avg(a.score) from Audit a where a.position.id=p.id)*1.), stddev((select avg(a.score) from Audit a where a.position.id=p.id)*1.) from Position p")
	Double[] avgMinMaxStddevAuditScorePerPosition();

	//The average salary offered by the positions that have the highest average audit score
	@Query("select avg(p.offeredSalary) from Position p where (select avg(a1.score) from Audit a1 where a1.position.id=p.id)>(select avg((select avg(a.score) from Audit a where a.position.id=p.id)*1.) from Position p)")
	Double avgSalaryOfferedPerPositionWithHighestAvgAuditScore();

	//The average, the minimum, the maximum, and the standard deviation of the number of sponsorships per position
	@Query("select avg((select count(s) from Sponsorship s where s.position.id=p.id)*1.), min((select count(s) from Sponsorship s where s.position.id=p.id)*1.), max((select count(s) from Sponsorship s where s.position.id=p.id)*1.), stddev((select count(s) from Sponsorship s where s.position.id=p.id)*1.) from Position p")
	Double[] avgMinMaxStddevSponsorshipsPerPosition();

	//Returns the available positions for a certain auditor
	@Query("select p from Position p where p.auditor.id=?1")
	Collection<Position> getPositionsForAuditor(int id);
}
