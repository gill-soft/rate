package com.gillsoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gillsoft.entity.CoupleRates;
import com.gillsoft.entity.CoupleRatesId;

@Repository
public interface CoupleRatesRepository extends CrudRepository<CoupleRates, CoupleRatesId> {

	@Override
	@Query("select s from CoupleRates s")
	List<CoupleRates> findAll();

	@Override
	CoupleRates findOne(CoupleRatesId key);

}