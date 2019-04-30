
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	//Retrieves the sponsorships for a certain provider
	@Query("select ss from Sponsorship ss where ss.provider.id=?1")
	Collection<Sponsorship> sponsorshipsFromProvider(int id);

	//Retrieves the sponsorships for a certain position
	@Query("select ss from Sponsorship ss where ss.position.id =?1")
	Collection<Sponsorship> getSponsorshipsByPosition(int id);
}
