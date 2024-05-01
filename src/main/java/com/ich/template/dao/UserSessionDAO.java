package com.ich.template.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ich.template.model.UserSession;

public interface UserSessionDAO  extends CrudRepository<UserSession,Long>,PagingAndSortingRepository<UserSession, Long>{

}
