package com.gillsoft.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.CoupleRates;
import com.gillsoft.entity.Couples;
import com.gillsoft.repository.CoupleRatesRepository;
import com.gillsoft.service.CoupleRatesService;

@Service
@Component
public class CoupleRatesServiceImpl implements CoupleRatesService {

    @Autowired
    private CoupleRatesRepository repository;

    @Autowired
    protected LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Override
	public List<CoupleRates> getAll() {
		return repository.findAll();
	}

	@Override
	public CoupleRates save(CoupleRates coupleRate) {
		return repository.save(coupleRate);
	}

	@SuppressWarnings("unchecked")
	public List<Couples> getAllCouplesByOrganization(String organizationId) {
		List<Couples> systemCouples = null;
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		systemCouples = em.createQuery("SELECT c FROM Couples c WHERE c.organizationId = :id").setParameter("id", organizationId).getResultList();
		em.close();
		return systemCouples;
	}

	@SuppressWarnings("unchecked")
	public List<CoupleRates> getRateCouple(String currencyFrom, String currencyTo, Date date) {
		List<CoupleRates> coupleRates = null;
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		coupleRates = em
				.createQuery(
						"SELECT cr FROM CoupleRates cr WHERE cr.coupleId IN (SELECT c FROM Couples c WHERE c.currencyFrom = :currencyFrom AND c.currencyTo = :currencyTo) AND (:date >= cr.dateStart and (:date <= cr.dateEnd or cr.dateEnd is null))")
				.setParameter("currencyFrom", currencyFrom).setParameter("currencyTo", currencyTo)
				.setParameter("date", date).getResultList();
		em.close();
		return coupleRates;
	}

	public void setRate(Integer coupleId, BigDecimal rate, Date dateStart) throws Exception {
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		try {
		em.createStoredProcedureQuery("rate.mod_rate")
				.registerStoredProcedureParameter("p_couple_id", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_rate", BigDecimal.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_date_start", Date.class, ParameterMode.IN)
				.setParameter("p_couple_id", coupleId).setParameter("p_rate", rate)
				.setParameter("p_date_start", dateStart).execute();
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			em.close();
		}
	}

}
