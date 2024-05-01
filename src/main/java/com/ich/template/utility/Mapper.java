package com.ich.template.utility;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper {


	ModelMapper mapper = new ModelMapper();
	
	public Object map(Object source, Class<?> destination){
		
		return mapper.map(source, destination);
	}
}
