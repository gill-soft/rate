package com.gillsoft.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.Couples;

@Service
@Component
public interface CouplesService {

	public List<Couples> getAll();

	public Couples save(Couples couple);

	public void delete(Integer coupleId);
	
}
