package com.mlscn.rrr.services;

import java.util.Collection;

import com.mlscn.rrr.model.Remita;


public interface RemitaServices extends VerificationServices <Remita>{
	void add(String file_path);
	
	Collection<Remita> getByRrrCode(String rrr);
}
