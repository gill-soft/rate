package com.gillsoft.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
	
	private static final Logger LOGGER = LogManager.getLogger(CoupleRatesServiceImpl.class);

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
		try {
			systemCouples = em.createQuery("SELECT c FROM Couples c WHERE c.organizationId = :id").setParameter("id", organizationId).getResultList();
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			em.close();
		}
		return systemCouples;
	}

	@SuppressWarnings("unchecked")
	public List<CoupleRates> getRateCouple(Long coupleId, Date date) {
		List<CoupleRates> coupleRates = null;
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		try {
			coupleRates = em
					.createQuery(
							"SELECT cr FROM CoupleRates cr WHERE cr.coupleId = :p_couple_id AND (:date >= cr.dateStart and (:date <= cr.dateEnd or cr.dateEnd is null))")
					.setParameter("p_couple_id", coupleId)
					.setParameter("date", date)
					.getResultList();
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			em.close();
		}
		return coupleRates;
	}
	
	public Object getAllCouplesRatesByOrganization(String organizationId) {
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		Object array = null;
		try {
			array = em
					.createQuery(
							"SELECT c, cr FROM Couples c, CoupleRates cr WHERE c.organizationId = :p_organization_id AND cr.coupleId = c.id AND (:p_date >= cr.dateStart and (:p_date <= cr.dateEnd or cr.dateEnd is null))")
					.setParameter("p_organization_id", organizationId)
					.setParameter("p_date", new Date())
					.getResultList();
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			em.close();
		}
		return array;
	}

	public void setRate(Long coupleId, BigDecimal rate, Date dateStart) throws Exception {
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		try {
			em.getTransaction().begin();
			em.createStoredProcedureQuery("rate.mod_rate")
					.registerStoredProcedureParameter("p_couple_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_rate", BigDecimal.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_date_start", Date.class, ParameterMode.IN)
					.setParameter("p_couple_id", coupleId).setParameter("p_rate", rate)
					.setParameter("p_date_start", dateStart).execute();
			em.getTransaction().commit();
		} catch (Exception e) {
			try {
				em.getTransaction().rollback();
			} catch (Exception et) { LOGGER.error(et); }
			LOGGER.error(e);
			throw new Exception(e.getMessage(), e);
		} finally {
			em.close();
		}
	}

}
