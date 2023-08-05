package com.mlscn.rrr.services;

import java.util.Collection;
import java.util.Optional;

public interface VerificationServices <Verification>
{
	Collection<Verification> getAll();
	
	Optional<Verification> getById(Long id);
	
	Verification add(Verification verification);	

	Verification update(Verification verification);
	
	void deleteById(Long id);

}
