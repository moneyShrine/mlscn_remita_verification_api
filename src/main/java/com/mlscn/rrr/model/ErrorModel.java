package com.mlscn.rrr.model;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.mlscn.rrr.dto.RemitaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Component
public class ErrorModel 
{
	private Integer currentPage;
    
	private Integer totalPages;
	
    private Long totalItems;
		
	private boolean errorExist;
	
	private String errorMsg;
	
	private Remita remita;
	
	private Collection<Remita> remita_list;
	
	private Collection<RemitaDTO> remita_dto_list;
}
