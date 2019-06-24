package com.gillsoft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.TemporalType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Parameter;
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
	public List<CoupleRates> getRateCouple(Integer coupleId, Date date) {
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
	
	public Map<String, List<Object>> getAllCouplesRatesByOrganization(String organizationId) {
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		Object[] array = null;
		Map<String, List<Object>> map = new HashMap<>();
		try {
			array = em
					.createQuery(
							"SELECT c, cr FROM Couples c, CoupleRates cr WHERE c.organizationId = :p_organization_id AND cr.coupleId = c.id AND (:p_date >= cr.dateStart and (:p_date <= cr.dateEnd or cr.dateEnd is null))")
					.setParameter("p_organization_id", organizationId).setParameter("p_date", new Date())
					.getResultList().toArray();
			if (array.length != 0) {
				map.put("couples", new ArrayList<>());
				map.put("rates", new ArrayList<>());
				for (int i = 0; i < array.length; i++) {
					map.get("couples").add(((Object[]) array[i])[0]);
					map.get("rates").add(((Object[]) array[i])[1]);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			em.close();
		}
		return map;
	}

	public void setRate(CoupleRates coupleRates) throws Exception {
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		try {
			em.getTransaction().begin();
			em.createStoredProcedureQuery("rate.mod_rate")
			.registerStoredProcedureParameter("p_couple_id", Integer.class, ParameterMode.IN)
			.registerStoredProcedureParameter("p_rate", BigDecimal.class, ParameterMode.IN)
			.registerStoredProcedureParameter("p_date_start", Date.class, ParameterMode.IN)
			.registerStoredProcedureParameter("p_date_end", Date.class, ParameterMode.IN)
			.setParameter("p_couple_id", coupleRates.getCoupleId())
			.setParameter("p_rate", coupleRates.getRate())
			.setParameter("p_date_start", coupleRates.getDateStart())
			.setParameter("p_date_end", coupleRates.getDateEnd() == null ? new GregorianCalendar(2099, GregorianCalendar.DECEMBER, 31, 23, 59, 59).getTime() : coupleRates.getDateEnd())
			.execute();
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
