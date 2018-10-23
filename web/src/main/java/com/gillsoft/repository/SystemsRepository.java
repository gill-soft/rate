package com.gillsoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gillsoft.entity.Systems;

@Repository
public interface SystemsRepository extends CrudRepository<Systems, Integer> {

	@Override
	@Query("select s from Systems s")
	List<Systems> findAll();

	@Override
	Systems findOne(Integer id);

}