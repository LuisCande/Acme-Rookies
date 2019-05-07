
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

	//The minimum, the maximum, the average, and the standard deviation of the number of items per provider
	@Query("select min((select count(i) from Item i where i.provider.id=p.id)*1.), max((select count(i) from Item i where i.provider.id=p.id)*1.), avg((select count(i) from Item i where i.provider.id=p.id)*1.), stddev((select count(i) from Item i where i.provider.id=p.id)*1.) from Provider p")
	Double[] minMaxAvgStddevItemPerProvider();

	//The top-5 providers in terms of total number of items provided
	@Query("select p.makeP from Provider p order by ((select count(i) from Item i where i.provider.id=p.id)*1.) desc")
	Collection<String> top5ProviderInTermsOfItems();

	//The average, the minimum, the maximum, and the standard deviation of the	number of sponsorships per provider.
	@Query("select avg((select count(s) from Sponsorship s where s.provider.id=p.id)*1.), min((select count(s) from Sponsorship s where s.provider.id=p.id)*1.), max((select count(s) from Sponsorship s where s.provider.id=p.id)*1.), stddev((select count(s) from Sponsorship s where s.provider.id=p.id)*1.) from Provider p")
	Double[] avgMinMaxStddevSponsorshipsPerProvider();

	//The providers who have a number of sponsorships that is at least 10% above the average number of sponsorships per provider
	@Query("select p.makeP from Provider p where ((select count(s) from Sponsorship s where s.provider.id=p.id)*1.)>(select avg((select count(s) from Sponsorship s where s.provider.id=p1.id)*1.1) from Provider p1)")
	Collection<String> providersWith10PerCentMoreSponsorshipsThanAvg();

}
