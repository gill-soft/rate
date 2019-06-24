package com.gillsoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gillsoft.entity.Couples;

@Repository
public interface CouplesRepository extends CrudRepository<Couples, Integer> {

	@Override
	@Query("select s from Couples s")
	List<Couples> findAll();

	@Override
	Couples findOne(Integer coupleId);

	@Override
	void delete(Couples couple);

}