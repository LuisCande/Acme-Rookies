
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCard;
import domain.Hacker;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {


}
