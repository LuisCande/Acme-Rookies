
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import security.UserAccount;
import domain.Company;
import domain.Hacker;
import domain.Message;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	
	@Query("select c from Company c where c.positions.size = (select max(c1.positions.size) from Company c1)")
	Collection<Company> getMaxPositionsCompanies();

	@Query("select h from Company h where h.userAccount.id = ?1")
	Company getCompanyByUserAccountId(int id);
	
}
