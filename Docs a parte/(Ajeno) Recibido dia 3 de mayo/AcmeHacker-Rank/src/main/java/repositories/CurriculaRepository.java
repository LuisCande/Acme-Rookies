
package repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curricula;


@Repository
public interface CurriculaRepository extends JpaRepository<Curricula, Integer> {
	
	@Query("select avg(h.curriculas.size) from Hacker h")
	Double getAvgCurriculasPerHacker();

	@Query("select min(h.curriculas.size) from Hacker h")
	Integer getMinCurriculasPerHacker();
	
	@Query("select max(h.curriculas.size) from Hacker h")
	Integer getMaxCurriculasPerHacker();
	
	@Query("select stddev(h.curriculas.size) from Hacker h")
	Double getStdevCurriculasPerHacker();
	
	@Query("select c from Curricula c where c.hacker.id=?1")
	Collection<Curricula> findByHackerId(int id);
	
	@Query("select c from Curricula c join c.positionDatas p where p.id=?1")
	Curricula findByPositionDataId(int id);

	@Query("select c from Curricula c join c.educationDatas e where e.id=?1")
	Curricula findByEducationDataId(int id);
	
	@Query("select c from Curricula c join c.miscellaneousDatas m where m.id=?1")
	Curricula findByMiscellaneousDataId(int id);
	
	@Query("select c from Curricula c where c.personalData.id=?1")
	Curricula findByPersonalDataId(int id);
	
	
}
