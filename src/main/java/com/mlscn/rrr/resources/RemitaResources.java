package com.mlscn.rrr.resources;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.mlscn.rrr.businesscomponent.RemitaComponent;
import com.mlscn.rrr.dto.RemitaDTO;
import com.mlscn.rrr.model.ErrorModel;
import com.mlscn.rrr.model.Remita;
import com.mlscn.rrr.services.impl.RemitaServiceImpl;



@RestController
@RequestMapping(value="/mlscn-remita")
public class RemitaResources 
{
	@Autowired
	private RemitaServiceImpl remitaServiceImpl;
	@Autowired
	private RemitaComponent remitaBusinessComponent;
	@Autowired
	private ErrorModel errorModel;
	
	@GetMapping(value="/test")
	public ResponseEntity<ErrorModel> test() 
	{
		errorModel.setErrorExist(false);
		errorModel.setErrorMsg("Congratulations no error exist");
		
		ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(value="/db-list")
	public ResponseEntity<ErrorModel> readListFromDB(@RequestHeader(value="clientId")String clientId) 
	{
		if(!clientId.equals("@Phronesis1303FORnCsLm")) {
			errorModel.setErrorExist(true);
			errorModel.setErrorMsg("Access Denied. You are not suppose to be here!");
			ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.UNAUTHORIZED);
			return response;
		}
		errorModel.setErrorExist(false);
		errorModel.setErrorMsg("Congratulations no error exist");
		errorModel.setRemita_list(remitaServiceImpl.getAll());
		
		ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(value="/db-list/{pageNumber}")
	public ResponseEntity<ErrorModel> readListByPage(@PathVariable("pageNumber") int currentPage, 
			@RequestHeader(value="clientId")String clientId) 
	{
		if(!clientId.equals("@Phronesis1303FORnCsLm")) {
			errorModel.setErrorExist(true);
			errorModel.setErrorMsg("Access Denied. You are not suppose to be here!");
			ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.UNAUTHORIZED);
			return response;
		}
		Page<Remita> page = remitaServiceImpl.getRemitaPageByPage(currentPage);
	    int totalPages = page.getTotalPages();
	    long totalItems = page.getTotalElements();
	    Collection<Remita> remita_list = page.getContent();

	    errorModel.setCurrentPage(currentPage);
	    errorModel.setTotalItems(totalItems);	    
	    errorModel.setTotalPages(totalPages);
		errorModel.setErrorExist(false);
		errorModel.setErrorMsg("Congratulations no error exist");
		errorModel.setRemita_list(remita_list);		
		ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.OK);
		return response;
	}
	
	
	/**
	 * Firstly get RRR code, then check that this RRR Code exist with Remita
	 * and then get relevant information from Remita before double checking to 
	 * see if the Remita Code exist in database an the amount that was used from 
	 * the code and the balance left.
	 * @param clientId
	 * @param rrr
	 * @return
	 */
	@GetMapping(value="/verify-remita")
	public ResponseEntity<ErrorModel> verifySingleRemitaRRR(@RequestHeader(value="clientId")String clientId, @RequestParam String rrr)
	{
		if(!clientId.equals("@Phronesis1303FORnCsLm")) {
			errorModel.setErrorExist(true);
			errorModel.setErrorMsg("Unauthorize Access!");
			ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		Collection<Remita> remita_list = remitaServiceImpl.getByRrrCode(rrr);
		RemitaDTO verified_remita_rrr = remitaBusinessComponent.validateRemitaCode(rrr, remita_list);
		if(verified_remita_rrr == null) {//This takes care of NULL value or RRR Code that is less than 7 Character long
			errorModel.setErrorExist(true);
			errorModel.setErrorMsg("Invalid Remita Code!");
			errorModel.setRemita(null);
			errorModel.setRemita_list(null);
			errorModel.setRemita_dto_list(null);
			ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.BAD_REQUEST);
			return response;
		}else if(verified_remita_rrr.getMessage().equals("Verified")) {
			errorModel.setErrorExist(false);
			errorModel.setErrorMsg("Verified");
			errorModel.setRemita_list(remita_list);
			ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.OK);
			return response;
		}else if(verified_remita_rrr.getMessage().equals("Invalid")) {
			errorModel.setErrorExist(true);
			errorModel.setErrorMsg("Remita Code does not exist!");
			errorModel.setRemita(null);
			errorModel.setRemita_list(null);
			errorModel.setRemita_dto_list(null);
			ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.BAD_REQUEST);
			return response;
		}
		
		errorModel.setErrorExist(true);
		errorModel.setErrorMsg("Fraud");
		errorModel.setRemita_list(remita_list);
		ResponseEntity<ErrorModel> response = new ResponseEntity<ErrorModel>(errorModel, HttpStatus.EXPECTATION_FAILED);
		return response;
	}
	
	
	@PostMapping(value="/add-rrr")
	public String add_rrr() {
		String file_path = "C:\\Users\\USER\\Documents\\mlscn\\rrr-verification\\rrr-verification.json";
		remitaServiceImpl.add(file_path);
		
		return "Remita saved";
	}

}
