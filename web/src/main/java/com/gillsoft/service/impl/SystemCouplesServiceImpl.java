package com.gillsoft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.Couples;
import com.gillsoft.entity.SystemCouples;
import com.gillsoft.repository.CouplesRepository;
import com.gillsoft.repository.SystemCouplesRepository;
import com.gillsoft.service.SystemCouplesService;

@Service
@Component
public class SystemCouplesServiceImpl implements SystemCouplesService {

    @Autowired
    private SystemCouplesRepository repository;

    @Autowired
    private CouplesRepository repositoryCouples;

    @Autowired
    protected LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Override
	public List<SystemCouples> getAll() {
		return repository.findAll();
	}

	@Override
	public SystemCouples save(SystemCouples system) {
		return repository.save(system);
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, List<Couples>> getAllSystemCouples(String organizationId) {
		final List<SystemCouples> systemCouples = new ArrayList<>();
		Map<Integer, List<Couples>> couplesMap = new HashMap<>();
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		systemCouples.addAll(em
				.createQuery(
						"SELECT sc FROM SystemCouples sc WHERE sc.coupleId IN (SELECT c.id FROM Couples c WHERE c.organizationId = :organizationId)")
				.setParameter("organizationId", organizationId).getResultList());
		if (systemCouples != null && !systemCouples.isEmpty()) {
			List<Couples> couples = em.createQuery("SELECT c FROM Couples c WHERE c.organizationId = :organizationId")
					.setParameter("organizationId", organizationId).getResultList();
			if (couples != null && !couples.isEmpty()) {
				systemCouples.stream().forEach(sc -> {
					if (!couplesMap.containsKey(sc.getSystemId())) {
						couplesMap.put(sc.getSystemId(),
								couples.stream()
										.filter(f -> systemCouples.stream()
												.filter(scf -> scf.getSystemId().equals(sc.getSystemId())
														&& scf.getCoupleId().equals(f.getId()))
												.count() != 0)
										.map(m -> m).collect(Collectors.toList()));
					}
				});
			}
		}
		em.close();
		return couplesMap;
	}

	public void deleteSystemCouple(String organizationId, Integer coupleId) {
		Map<Integer, List<Couples>> systemCouples = getAllSystemCouples(organizationId);
		if (systemCouples != null && !systemCouples.isEmpty()) {
			systemCouples.entrySet().stream()
					.forEach(es -> es.getValue().stream().filter(f -> f.getId().equals(coupleId)).forEach(couple -> {
						repository.delete(new SystemCouples(es.getKey(), couple.getId()));
						repositoryCouples.delete(couple);
					}));
		}
	}

	@SuppressWarnings("unchecked")
	public List<Couples> getSystemCouples(Integer id) {
		List<Couples> couples = null;
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		couples = em.createQuery("SELECT c FROM Couples c WHERE c.id IN (SELECT sc.coupleId FROM SystemCouples sc WHERE sc.systemId = :id)").setParameter("id", id).getResultList();
		em.close();
		return couples;
	}

	public void updateRate(Integer systemId, String currencyFrom, String currencyTo, BigDecimal rate, Date dateStart) {
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		em.createStoredProcedureQuery("rate.mod_system_rate")
				.registerStoredProcedureParameter("p_system_id", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_currency_from", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_currency_to", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_rate", BigDecimal.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_date_start", Date.class, ParameterMode.IN)
				.setParameter("p_system_id", systemId).setParameter("p_currency_from", currencyFrom)
				.setParameter("p_currency_to", currencyTo).setParameter("p_rate", rate)
				.setParameter("p_date_start", dateStart).execute();
		em.close();
	}

}
