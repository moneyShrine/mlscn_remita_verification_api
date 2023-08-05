package com.mlscn.rrr.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name="rrr_verification", schema="mlscn_rrr")
public class Remita 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native", strategy="native")
	private Long id;
	
	private String rrr;
	
	private String date_used;
	
	private String beneficiary;
	
	private String purpose_of_payment;

	private float amount_used;

	public Remita(String rrr, String date_used, String beneficiary, String purpose_of_payment, float amount_used) {
		super();
		this.rrr = rrr;
		this.date_used = date_used;
		this.beneficiary = beneficiary;
		this.purpose_of_payment = purpose_of_payment;
		this.amount_used = amount_used;
	}

}
