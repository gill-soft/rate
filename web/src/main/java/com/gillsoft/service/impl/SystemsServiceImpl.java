package com.gillsoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.Systems;
import com.gillsoft.repository.SystemsRepository;
import com.gillsoft.service.SystemsService;

@Service
@Component
public class SystemsServiceImpl implements SystemsService {

    @Autowired
    private SystemsRepository repository;

	@Override
	public List<Systems> getAll() {
		return repository.findAll();
	}

	@Override
	public Systems findOne(Integer id) {
		return repository.findOne(id);
	}

	@Override
	public Systems save(Systems system) {
		return repository.save(system);
	}

	public void delete(Systems system) {
		repository.delete(system);
	}

}
