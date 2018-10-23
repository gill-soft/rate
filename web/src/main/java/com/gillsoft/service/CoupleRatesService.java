package com.gillsoft.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gillsoft.entity.CoupleRates;

@Service
@Component
public interface CoupleRatesService {

	public List<CoupleRates> getAll();

	public CoupleRates save(CoupleRates coupleRate);

}
