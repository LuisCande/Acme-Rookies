package services;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Curricula;
import domain.EducationData;
import domain.Hacker;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;
import forms.CurriculaDataForm;

import repositories.CurriculaRepository;
import security.LoginService;

@Service
@Transactional
public class CurriculaService {

	// Managed Repository
	@Autowired
	private CurriculaRepository curriculaRepository;

	// Supporting services
	@Autowired
	private PersonalDataService personalDataService;

	@Autowired
	private HackerService hackerService;

	@Autowired
	private EducationDataService educationDataService;

	@Autowired
	private MiscellaneousDataService miscellaneousDataService;

	@Autowired
	private PositionDataService positionDataService;
	
	@Autowired
	private Validator validator;

	// Constructor
	public CurriculaService() {
		super();
	}

	
	@SuppressWarnings("unchecked")
	public Curricula create() {
		Curricula result;
		Hacker hacker = this.hackerService.findByPrincipal();
		Collection<MiscellaneousData> miscellaneousDatas;
		Collection<EducationData> educationDatas;
		Collection<PositionData> positionDatas;

		result = new Curricula();
		miscellaneousDatas = Collections.EMPTY_SET;
		educationDatas = Collections.EMPTY_SET;
		positionDatas = Collections.EMPTY_SET;

		result.setPersonalData(null);
		result.setMiscellaneousDatas(miscellaneousDatas);
		result.setEducationDatas(educationDatas);
		result.setPositionDatas(positionDatas);

		result.setHacker(hacker);

		return result;
	}
	
	public Curricula reconstruct(CurriculaDataForm form,PersonalData data ,BindingResult binding ){
		
		Curricula res = this.create();
		//Creamos la tarjeta de credito:
		res.setName(form.getName());

		res.setPersonalData(data);

		validator.validate(form, binding);		
			
		return res;
	
	}

	public Curricula save(final Curricula curricula) {
		Assert.isTrue(LoginService.hasRole("HACKER"));
		Assert.notNull(curricula);
		Curricula result;
	
		result = this.curriculaRepository.saveAndFlush(curricula);
		
		this.flush();
		

		return result;
	}

	public void delete(final Curricula curricula) {
		Assert.notNull(curricula);
		Assert.isTrue(curricula.getId() != 0);
		Assert.isTrue(this.curriculaRepository.exists(curricula.getId()));
		
		this.curriculaRepository.delete(curricula);
	}

	public Collection<Curricula> findAll() {
		Collection<Curricula> result;
		result = this.curriculaRepository.findAll();

		return result;
	}
	
	public Curricula findOne(final Integer curriculaId) {
		Curricula result;
		
		result = this.curriculaRepository.findOne(curriculaId);
		
		return result;
	}

	public void flush() {
		this.curriculaRepository.flush();
	}

	public Curricula findByPersonalData(final PersonalData personalData) {
		Assert.notNull(personalData);
		Curricula result;
		result = this.curriculaRepository.findByPersonalDataId(personalData.getId());

		return result;
	}

	public Curricula findByMiscellaneousData(final MiscellaneousData miscellaneousData) {
		Assert.notNull(miscellaneousData);
		Curricula result;
		result = this.curriculaRepository.findByMiscellaneousDataId(miscellaneousData.getId());

		return result;
	}

	public Curricula findByEducationData(final EducationData educationData) {
		Assert.notNull(educationData);
		Curricula result;
		result = this.curriculaRepository.findByEducationDataId(educationData.getId());

		return result;
	}

	public Curricula findByPositionData(final PositionData positionData) {
		Assert.notNull(positionData);
		Curricula result;
		result = this.curriculaRepository.findByPositionDataId(positionData.getId());

		return result;
	}

	public Collection<Curricula> findByHacker(final Hacker hacker) {
		Assert.notNull(hacker);
		Collection<Curricula> result;
		result = this.curriculaRepository.findByHackerId(hacker.getId());

		return result;
	}
	
	public Double getAvgCurriculasPerHacker(){
		Double res = curriculaRepository.getAvgCurriculasPerHacker();
		if(res==null)res=0d;
		return res;
	}

	public Integer getMinCurriculasPerHacker(){
		Integer res = curriculaRepository.getMinCurriculasPerHacker();
		if(res==null) res=0;
		return res;
	}
	
	public Integer getMaxCurriculasPerHacker(){
		Integer res = curriculaRepository.getMaxCurriculasPerHacker();
		if(res==null)res=0;
		return res;
	}
	
	public Double getStdevCurriculasPerHacker(){
		Double res = curriculaRepository.getStdevCurriculasPerHacker();
		if(res==null)res=0d;
		return res;
	}
	
	public Curricula emptyCurricula(Curricula c){
		Collection<EducationData> ed = c.getEducationDatas();
		for (int i=0;i<ed.size();i++) {
			EducationData cn = (EducationData) ed.toArray()[i];
			educationDataService.delete(cn,c);
			educationDataService.flush();
		}

		Collection<MiscellaneousData> mi = c.getMiscellaneousDatas();
		for (int i=0;i<mi.size();i++) {
			MiscellaneousData cn = (MiscellaneousData) mi.toArray()[i];
			miscellaneousDataService.delete(cn,c);
			miscellaneousDataService.flush();
		}

		Collection<PositionData> po = c.getPositionDatas();
		for (int i=0;i<po.size();i++) {
			PositionData cn = (PositionData) po.toArray()[i];
			positionDataService.delete(cn,c);
			positionDataService.flush();
		}

		PersonalData pe = c.getPersonalData();
		c.setPersonalData(null);
		Curricula res = this.save(c);
		this.flush();
		personalDataService.delete(pe);

		return res;
	}

}
