package com.gillsoft.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.Couples;
import com.gillsoft.repository.CouplesRepository;
import com.gillsoft.service.CouplesService;

@Service
@Component
public class CouplesServiceImpl implements CouplesService {

	private static final Logger LOGGER = LogManager.getLogger(CouplesServiceImpl.class);

    @Autowired
    private CouplesRepository repository;

    @Autowired
    protected LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Override
	public List<Couples> getAll() {
		return repository.findAll();
	}

	@Override
	public Couples save(Couples couple) {
		return repository.save(couple);
	}

	@Override
	public void delete(Integer coupleId) {
		Couples couple = repository.findOne(coupleId);
		if (couple != null) {
			repository.delete(couple);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Couples> getOrganizationCouples(String organizationId) {
		List<Couples> systemCouples = null;
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		systemCouples = em.createQuery("SELECT c FROM Couples c WHERE c.organizationId = :organizationId")
				.setParameter("organizationId", organizationId).getResultList();
		em.close();
		return systemCouples;
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> getOrganizationAll(String organizationId) {
		EntityManager em = entityManagerFactory.getObject().createEntityManager();
		ArrayList<Object[]> array = null;
		Map<String, List<Object>> map = new HashMap<>();
		try {
			/*Query query = em.createNamedQuery("OrganizationAllQuery");
	        //query.setParameter(1, new Date());
	        //query.setParameter(2, new Date());
	        query.setParameter(1, organizationId);
	        array = (ArrayList<Object[]>)query.getResultList();
			if (array != null && !array.isEmpty()) {
				map.put("couples", new ArrayList<>());
				map.put("couple_rates", new ArrayList<>());
				map.put("system_couples", new ArrayList<>());
				map.put("systems", new ArrayList<>());
				array.forEach(objectArray -> {
					map.get("couples").add(objectArray[0]);
					map.get("system_couples").add(objectArray[1]);
					map.get("systems").add(objectArray[2]);
					map.get("couple_rates").add(objectArray[3]);
				});
			}*/
			Query query = em.createNamedQuery("OrganizationRatesQuery");
	        query.setParameter(1, organizationId);
	        array = (ArrayList<Object[]>)query.getResultList();
			if (array != null && !array.isEmpty()) {
				map.put("couples", new ArrayList<>());
				map.put("couple_rates", new ArrayList<>());
				array.forEach(objectArray -> {
					map.get("couples").add(objectArray[0]);
					map.get("couple_rates").add(objectArray[1]);
				});
			}
			query = em.createNamedQuery("OrganizationSystemsQuery");
	        query.setParameter(1, organizationId);
	        array = (ArrayList<Object[]>)query.getResultList();
			if (array != null && !array.isEmpty()) {
				map.put("systems", new ArrayList<>());
				map.put("system_couples", new ArrayList<>());
				array.forEach(objectArray -> {
					map.get("systems").add(objectArray[0]);
					map.get("system_couples").add(objectArray[1]);
				});
			}
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			em.close();
		}
		return map;
	}

}
