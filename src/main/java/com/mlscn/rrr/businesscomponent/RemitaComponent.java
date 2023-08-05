package com.mlscn.rrr.businesscomponent;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.*;
import org.json.simple.parser.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.hash.Hashing;
import com.mlscn.rrr.dto.RemitaDTO;
import com.mlscn.rrr.model.Remita;

@Component
public class RemitaComponent 
{
	private final String merchantId = "628044757";
	private final String apiKey = "805439";

	
	public ArrayList<Remita> readJSONFile(String jsonFilePath) 
	{
		ArrayList<Remita> remita_list = new ArrayList<>();
		JSONParser jsonParser = new JSONParser();		
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(jsonFilePath));
			
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			for (int i = 0; i <jsonArray.size(); i++) {
				Object element = jsonArray.get(i);
				
				JSONObject jsonObj = (JSONObject) element;
				
				String rrr = (String)jsonObj.get("rr_number");

  	         	String date_used = (String)jsonObj.get("updated_at");
  	         	
  	         	JSONObject user =  (JSONObject)jsonObj.get("user");
	  	        JSONObject fee =  (JSONObject)jsonObj.get("fee");
	  	        
	  	        String beneficiary = (String)user.get("username");
	  	        String purpose_of_payment = (String)fee.get("name");
	  	        String amount_used_str = (String)fee.get("amount").toString();
	  	        float amount_used = Float.valueOf(amount_used_str);
	  	        
	  	        
	  	        Remita remita = new Remita(rrr, date_used, beneficiary, purpose_of_payment, amount_used);
	  	        remita_list.add(remita);
	  	        
			}
		} catch(Exception e) {
	         e.printStackTrace();
	    }
		
		return remita_list;		
	}
	
	
	/**
	 * Reads Remita details from Remita portal using RRR code and 
	 * returns Error message if the rrr message is not Successful
	 * @param rrr
	 * @return
	 */
	public RemitaDTO readFromPortal(String rrr) 
	{
		RemitaDTO generatedRRR;
		if(rrr == null) {
			return null;
		}
		if(rrr.length() < 7) {
			return null;
		}
		try {
			RestTemplate restTemplate = new RestTemplate();
			String toHash = rrr + apiKey + merchantId;				
			String apiHash = Hashing.sha512().hashString(toHash, StandardCharsets.UTF_8).toString();
			String remitaConfirmRRRURL = "https://login.remita.net/remita/exapp/api/v1/send/api/echannelsvc/"+merchantId+"/"+rrr+"/"+apiHash+"/status.reg"; 
			
			generatedRRR = restTemplate.getForObject(remitaConfirmRRRURL, RemitaDTO.class);		
			
			String generated_msg = generatedRRR.getMessage();
			
			if(!generated_msg.equals("Successful")) {
				RemitaDTO remitaDTO = new RemitaDTO();
				remitaDTO.setMessage("No payment done");
				
				return remitaDTO;
			}
			
		}catch(Exception e) {
			RemitaDTO remitaDTO = new RemitaDTO();
			remitaDTO.setMessage("Internal Server Error");
			return remitaDTO;
		}
		return generatedRRR;
	}
	
	/**
	 * THe remita_db_list can be coming directly from an API
	 * If amount_used is greater than amount_paid alert Fraud
	 * @param rrr
	 * @param remita_db_list
	 * @return
	 */
	public RemitaDTO validateRemitaCode(String rrr, Collection<Remita> remita_db_list)
	{
		RemitaDTO remitaResponseMsg = new RemitaDTO();		
		RemitaDTO remitaFromPortal = readFromPortal(rrr);		
		if(remitaFromPortal == null) {//This takes care of NULL value or RRR Code that is less than 7 Character long
			return null;
		}
		if(remitaFromPortal.getMessage().equals("Internal Server Error")) {
			remitaResponseMsg.setMessage("Invalid");
			return remitaResponseMsg;
		}
		
		float amount_paid = remitaFromPortal.getAmount();
		float amount_used = 0;
			
		//Values present here are "id, rrr, date_used, beneficiary, purpose_of_payment, amount_used"
		for(Remita remita_db : remita_db_list) {
			amount_used = amount_used + remita_db.getAmount_used();
		}
		
		if(amount_used > amount_paid) {
			remitaResponseMsg.setMessage("Fraud");
			return remitaResponseMsg;
		}
		
		remitaResponseMsg.setMessage("Verified");
		return remitaResponseMsg;
	}

}
