package com.ich.template.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ich.template.dao.LocalDAO;
import com.ich.template.dto.LocalDto;
import com.ich.template.exception.BadRequestException;
import com.ich.template.model.Local;
import com.ich.template.utility.Mapper;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class LocalService {
	
    @Autowired
    private LocalDAO localDao;
    
    @Autowired
    private Mapper mapper;
	
	
    public List<Local> findAll() {
        List<Local> locals= (List<Local>) localDao.findAll();
        return locals;
    }

    public LocalDto get(Long id) {
		if (!localDao.existsById(id))
			throw new BadRequestException("404", "Not found");
		
    	Local local = localDao.findById(id).get();
    	
    	return (LocalDto) mapper.map( local,LocalDto.class);
    }

    public LocalDto save(LocalDto localDto) {
    	
    	if(localDto.getId() != 0)
    		localDto.setId(0);
    	
    	Local local = new Local();
    	
    /*	local.setName (localDto.getName());
    	local.setData (localDto.getData());*/
    	
    	local = (Local) mapper.map( localDto,Local.class);
    	
    	localDao.save(local);
    	localDto.setId(local.getId());
    	
    	return localDto;
    }
    
    public LocalDto modify(LocalDto localDto) {
    	
		if (!localDao.existsById( Long.valueOf(localDto.getId()) ))
			throw new BadRequestException("404", "Not found");
    	Local local = new Local();
/*    	local.setId(localDto.getId());
    	local.setName (localDto.getName());
    	local.setData (localDto.getData());*/
    	
    	local = (Local) mapper.map( localDto,Local.class);
    	
    	localDao.save(local);
    	localDto.setId(local.getId());
    	
    	return localDto;
    }

    public void delete(Long id) {
		if (!localDao.existsById(id))
			throw new BadRequestException("404", "Not found");
    	localDao.deleteById(id);
    }

}
