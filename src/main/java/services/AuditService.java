
package services;

import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditRepository;
import domain.Audit;
import domain.Auditor;

@Service
@Transactional
public class AuditService {

	//Managed service

	@Autowired
	private AuditRepository	auditRepository;

	//Supporting service

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	//Simple CRUD methods

	public Audit create() {
		final Audit a = new Audit();

		a.setAuditor((Auditor) this.actorService.findByPrincipal());
		a.setFinalMode(false);

		return a;
	}

	public Audit findOne(final int id) {
		Assert.notNull(id);

		return this.auditRepository.findOne(id);
	}

	public Collection<Audit> findAll() {
		return this.auditRepository.findAll();
	}

	public Audit save(final Audit a) {
		Assert.notNull(a);

		//Assertion that the user modifying this education record has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == a.getAuditor().getId());

		final Audit saved = this.auditRepository.save(a);

		return saved;
	}

	public void delete(final Audit a) {
		Assert.notNull(a);

		//Assertion that the user modifying this task has the correct privilege.
		Assert.isTrue(a.getFinalMode() == false);

		//Assertion that the user deleting this education record has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == a.getAuditor().getId());

		this.auditRepository.delete(a);
	}

	//Other methods--------------------------

	//Reconstruct

	public Audit reconstruct(final Audit p, final BindingResult binding) {
		Assert.notNull(p);
		Audit result;

		if (p.getId() == 0)
			result = this.create();
		else
			result = this.auditRepository.findOne(p.getId());

		//Assertion that the user modifying this task has the correct privilege.
		Assert.isTrue(result.getFinalMode() == false);

		if (result.getMoment() == null)
			result.setMoment(new Date(System.currentTimeMillis() - 1));

		result.setText(p.getText());
		result.setScore(p.getScore());
		result.setFinalMode(p.getFinalMode());
		result.setPosition(p.getPosition());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		if (result.getScore() < 0 || result.getScore() > 10)
			throw new ConstraintDefinitionException();

		//Assertion that the user modifying this task has the correct privilege.
		Assert.isTrue(this.actorService.findByPrincipal().getId() == result.getAuditor().getId());

		return result;

	}

	//Other methods

	public void flush() {
		this.auditRepository.flush();
	}

	//Motion and queries

	//Retrieves the list of audits for an auditor
	public Collection<Audit> auditsFromAuditor(final int id) {
		return this.auditRepository.auditsFromAuditor(id);
	}

	//Retrieves the list of audits for a certain position
	public Collection<Audit> getAuditsForPosition(final int id) {
		return this.auditRepository.getAuditsForPosition(id);
	}
}
