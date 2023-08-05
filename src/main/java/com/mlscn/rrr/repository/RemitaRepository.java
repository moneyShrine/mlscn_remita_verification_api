package com.mlscn.rrr.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mlscn.rrr.model.Remita;

@Repository
public interface RemitaRepository extends JpaRepository<Remita, Long>
{
	Collection<Remita> findByRrr(String rrr);
	
	Page<Remita> findAll(Pageable pageable);

	//Collection<Remita> findAllByGroupByRrr();
	Page<Remita> findAllByOrderByRrr(Pageable pageable);
	

}
