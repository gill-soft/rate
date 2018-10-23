package com.gillsoft.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

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

    @Autowired
    private CouplesRepository repository;

    @Autowired
    protected LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Override
	public List<Couples> getAll() {
		return repository.findAll();
	}

	@Override
	public Couples save(Couples system) {
		return repository.save(system);
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

}
