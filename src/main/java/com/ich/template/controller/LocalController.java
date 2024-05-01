package com.ich.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ich.template.dto.LocalDto;
import com.ich.template.dto.ResponseStatus;
import com.ich.template.exception.BadRequestException;
import com.ich.template.service.LocalService;


@RestController
@RequestMapping("/local")
public class LocalController {

	@Autowired
	private LocalService localService;
	
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<?> getAll(){

        return new ResponseEntity<Object>(localService.findAll(), HttpStatus.OK);
    }
    
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/id/{localId}", produces = "application/json")
    public ResponseEntity<?> getLocal(@PathVariable Long localId){
		 
		try {
			return new ResponseEntity<Object>(localService.get(localId), HttpStatus.OK);
		} catch (BadRequestException e) {
			return new ResponseEntity<Object>(new ResponseStatus(e.getCodigo(), e.getMensaje()),
					HttpStatus.BAD_REQUEST);			
		}
    }
	
	@PostMapping(value = "/new", produces = "application/json")
    public ResponseEntity<?> save(@RequestBody LocalDto localDto){
		 return new ResponseEntity<Object>(localService.save(localDto), HttpStatus.OK);		 
    }
	
	@PostMapping(value = "/update", produces = "application/json")
    public ResponseEntity<?> modify(@RequestBody LocalDto localDto){
		 
		try {
			return new ResponseEntity<Object>(localService.modify(localDto), HttpStatus.OK);
		} catch (BadRequestException e) {
			return new ResponseEntity<Object>(new ResponseStatus(e.getCodigo(), e.getMensaje()),
					HttpStatus.BAD_REQUEST);			
		}
    }
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/delete/{localId}", produces = "application/json")
    public ResponseEntity<?> deleteLocal(@PathVariable Long localId){
		 
		try {
			localService.delete(localId);
			return new ResponseEntity<Object>(null, HttpStatus.OK) ;
		} catch (BadRequestException e) {
			return new ResponseEntity<Object>(new ResponseStatus(e.getCodigo(), e.getMensaje()),
					HttpStatus.BAD_REQUEST);			
		}
    }
}
