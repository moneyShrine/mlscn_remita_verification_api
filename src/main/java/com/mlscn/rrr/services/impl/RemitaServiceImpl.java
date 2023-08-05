package com.mlscn.rrr.services.impl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mlscn.rrr.businesscomponent.RemitaComponent;
import com.mlscn.rrr.model.Remita;
import com.mlscn.rrr.repository.RemitaRepository;
import com.mlscn.rrr.services.RemitaServices;

@Service
public class RemitaServiceImpl implements RemitaServices
{
	@Autowired
	private RemitaRepository remitaRepository;
	@Autowired
	private RemitaComponent remitaComponent;


	
	public Page<Remita> getRemitaPageByPage(int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber - 1, 50);
		
		return remitaRepository.findAllByOrderByRrr(pageable);
	}	
	

	public Page<Remita> getRemitaPage(int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber - 1, 50);
		
		return remitaRepository.findAll(pageable);
	}	
	
	@Override
	public Collection<Remita> getAll() {	
		return remitaRepository.findAll();
	}

	
	@Override
	public Optional<Remita> getById(Long id) 
	{
		return remitaRepository.findById(id);
	}

	@Override
	public void add(String file_path) {

		for(Remita remita : remitaComponent.readJSONFile(file_path)) {
			remitaRepository.save(remita);
		}
	}

	@Override
	public Remita update(Remita verification) {
		//Do nothing here because it is not needed		
		return null;
	}

	@Override
	public void deleteById(Long id) {
		//Do nothing here because it is not needed
	}

	@Override
	public Remita add(Remita verification) {
		return null;
	}
	
	@Override
	public Collection<Remita> getByRrrCode(String rrr) {
		return remitaRepository.findByRrr(rrr);
	}
	
	

}
