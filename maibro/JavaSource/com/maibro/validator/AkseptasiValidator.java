package com.maibro.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.maibro.model.Policy;
import com.maibro.service.DbService;

/**
 * Validator Untuk Input Group Policy (gabungan life dan fire)
 * 
 * @author Yusuf
 * @since Feb 4, 2013 (11:29:07 AM)
 *
 */
@Component
public class AkseptasiValidator implements Validator {

	@Autowired
	protected DbService dbService;
	
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	

	public boolean supports(Class clazz) {
		return Policy.class.equals(clazz);
	}
	
	/**
	 * ini buat 
	 * 
	 */
	public void validate(Object obj, Errors e) {
		
        Policy policy = (Policy) obj;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "spaj_no", "NotEmpty", new String[]{"No SPAJ"});
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "flag_akseptasi", "NotEmpty", new String[]{"Akseptasi"});
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "tgl_aksep", "NotEmpty", new String[]{"Tanggal Akseptasi"});
//        ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.rate", "NotEmpty", new String[]{"Rate"});
//        ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.premi_pokok", "NotEmpty", new String[]{"Premi Pokok"});
//        ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.premi_extra", "NotEmpty", new String[]{"Premi Extra"});
//        ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.biaya_admin", "NotEmpty", new String[]{"Biaya Admin"});
//        ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.premi", "NotEmpty", new String[]{"Premi Netto"});
		
        
        if(!e.hasErrors()){
        	if(policy.getFlag_akseptasi()==1){
                if(policy.getJenis()==1|policy.getJenis()==3){
                	ValidationUtils.rejectIfEmptyOrWhitespace(e, "policy_no", "NotEmpty", new String[]{"No Polis"});
         	    }
        	}else{
        		if(policy.getFlag_akseptasi()!=2)e.rejectValue("flag_akseptasi", null, "Kolom Akseptasi harus berisi 1: aksep  atau 2: ditolak");
        	}
        	
        	Policy temp=dbService.selectPolicy(null,null,null,policy.getSpaj_no());
        	
        	if(temp==null){
        		e.rejectValue("spaj_no", null, "No Spaj tidak ditemukan");
        	}else{
        		policy.setId(temp.getId());
        	}
        }
    	
        
	}

}
