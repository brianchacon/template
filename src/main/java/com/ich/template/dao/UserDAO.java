package com.ich.template.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ich.template.model.User;

public interface UserDAO extends CrudRepository<User,Long>,PagingAndSortingRepository<User, Long>{

	Optional<User> findByEmail(String email);
}
