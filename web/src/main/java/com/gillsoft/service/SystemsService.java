package com.gillsoft.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.Systems;

@Service
@Component
public interface SystemsService {

	public List<Systems> getAll();

	public Systems findOne(Integer stationId);

	public Systems save(Systems station);

}
