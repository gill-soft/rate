package com.gillsoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gillsoft.entity.SystemCouples;

@Repository
public interface SystemCouplesRepository extends CrudRepository<SystemCouples, Integer> {

	@Override
	@Query("select s from SystemCouples s")
	List<SystemCouples> findAll();

	@Override
	SystemCouples findOne(Integer systemId);

}