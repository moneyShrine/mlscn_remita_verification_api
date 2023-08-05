package com.mlscn.rrr.dto;

import lombok.Data;

/**
 * This is used to get data from Remita portal and transfer it only
 * @author USER
 *
 */
@Data
public class RemitaDTO 
{	
	private float amount;
	
	private String RRR;
	
	private String orderId;
	
	private String message;
	
	private String paymentDate;
	
	private String transactiontime;
    
    private String status;

}
