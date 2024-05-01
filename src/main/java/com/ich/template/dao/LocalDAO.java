package com.ich.template.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ich.template.model.Local;


public interface LocalDAO extends CrudRepository<Local,Long>,PagingAndSortingRepository<Local, Long>{
/*	public List<Local> findAll();
	public Local findById(int id);
	public Void save(Local local);
	public Void delete(int id);*/
}
