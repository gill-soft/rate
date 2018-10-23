package com.gillsoft.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.SystemCouples;

@Service
@Component
public interface SystemCouplesService {

	public List<SystemCouples> getAll();

	public SystemCouples save(SystemCouples station);

}
