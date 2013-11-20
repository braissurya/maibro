package com.maibro.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.maibro.model.GroupPolicy;
import com.maibro.service.DbService;

/**
 * Validator Untuk Input Group Policy (gabungan life dan fire)
 * 
 * @author Yusuf
 * @since Feb 4, 2013 (11:29:07 AM)
 *
 */
@Component
public class GroupPolicyValidator implements Validator {

	@Autowired
	protected DbService dbService;
	
	@Autowired
	protected PolicyAJKValidator policyAJKValidator;

	@Autowired
	protected PolicyFireValidator policyFireValidator;
	
	@Autowired
	protected PolicyMicroValidator policyMicroValidator;
	
	@Autowired
	protected PolicyMicroAJKValidator policyMicroAJKValidator;
	
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public void setPolicyAJKValidator(PolicyAJKValidator policyAJKValidator) {
		this.policyAJKValidator = policyAJKValidator;
	}

	public void setPolicyFireValidator(PolicyFireValidator policyFireValidator) {
		this.policyFireValidator = policyFireValidator;
	}

	public void setPolicyMicroValidator(PolicyMicroValidator policyMicroValidator) {
		this.policyMicroValidator = policyMicroValidator;
	}
	
	
	public void setPolicyMicroAJKValidator(	PolicyMicroAJKValidator policyMicroAJKValidator) {
		this.policyMicroAJKValidator = policyMicroAJKValidator;
	}

	public boolean supports(Class clazz) {
		return GroupPolicy.class.equals(clazz);
	}
	
	/**
	 * ini buat 
	 * 
	 */
	public void validate(Object obj, Errors e) {
		
        GroupPolicy groupPolicy = (GroupPolicy) obj;
        

    	 if(groupPolicy.isMicroCheck()){
    		 if(groupPolicy.isLifeCheck()){
    			//bank
 	    		ValidationUtils.rejectIfEmptyOrWhitespace(e, "mst_bank_id", "NotEmpty", new String[]{""});
 	    		ValidationUtils.rejectIfEmptyOrWhitespace(e, "mst_cab_bank_id", "NotEmpty", new String[]{""});
 	
 	    		//set beberapa nilai
 	    		groupPolicy.getPolisMicro().getAgent().setNama(groupPolicy.getNama_agent());
 	    		groupPolicy.getPolisMicro().getAgent().setTelp_hp(groupPolicy.getTelp_agent());
 	    		groupPolicy.getPolisMicro().getAgent().setJabatan(groupPolicy.getJabatan_agent());
 	    		groupPolicy.getPolisMicro().getBank().setMst_bank_id(groupPolicy.getMst_bank_id());
 	    		groupPolicy.getPolisMicro().getBank().setMst_cab_bank_id(groupPolicy.getMst_cab_bank_id());
 	    		
 	            //Bila ada AJK (Life), validasi
 	    		e.pushNestedPath("polisMicro");
                ValidationUtils.invokeValidator(this.policyMicroAJKValidator, groupPolicy.getPolisMicro(), e);
                e.popNestedPath();
    		 }else{
	            e.pushNestedPath("polisMicro");
	            ValidationUtils.invokeValidator(this.policyMicroValidator, groupPolicy.getPolisMicro(), e);
	            e.popNestedPath();
    		 }
    		 
        }else{
        	//agent
    		ValidationUtils.rejectIfEmptyOrWhitespace(e, "nama_agent", "NotEmpty", new String[]{""});
    		ValidationUtils.rejectIfEmptyOrWhitespace(e, "telp_agent", "NotEmpty", new String[]{""});
    		ValidationUtils.rejectIfEmptyOrWhitespace(e, "jabatan_agent", "NotEmpty", new String[]{""});
    		//bank
    		ValidationUtils.rejectIfEmptyOrWhitespace(e, "mst_bank_id", "NotEmpty", new String[]{""});
    		ValidationUtils.rejectIfEmptyOrWhitespace(e, "mst_cab_bank_id", "NotEmpty", new String[]{""});

    		//set beberapa nilai
    		groupPolicy.getPolisAJK().getAgent().setNama(groupPolicy.getNama_agent());
    		groupPolicy.getPolisAJK().getAgent().setTelp_hp(groupPolicy.getTelp_agent());
    		groupPolicy.getPolisAJK().getAgent().setJabatan(groupPolicy.getJabatan_agent());
    		groupPolicy.getPolisAJK().getBank().setMst_bank_id(groupPolicy.getMst_bank_id());
    		groupPolicy.getPolisAJK().getBank().setMst_cab_bank_id(groupPolicy.getMst_cab_bank_id());
    		groupPolicy.getPolisFire().getAgent().setNama(groupPolicy.getNama_agent());
    		groupPolicy.getPolisFire().getAgent().setTelp_hp(groupPolicy.getTelp_agent());
    		groupPolicy.getPolisFire().getAgent().setJabatan(groupPolicy.getJabatan_agent());
    		groupPolicy.getPolisFire().getBank().setMst_bank_id(groupPolicy.getMst_bank_id());
    		groupPolicy.getPolisFire().getBank().setMst_cab_bank_id(groupPolicy.getMst_cab_bank_id());
    		
            //Bila tidak ada satupun yang dipilih (Life vs Fire), kasih pesan error
            if(!groupPolicy.isLifeCheck() && !groupPolicy.isFireCheck()){
            	e.reject("", "Silahkan pilih minimal satu (1) asuransi untuk melanjutkan");
            }
            
            //Bila ada AJK (Life), validasi
            if(groupPolicy.isLifeCheck()){
                e.pushNestedPath("polisAJK");
                ValidationUtils.invokeValidator(this.policyAJKValidator, groupPolicy.getPolisAJK(), e);
                e.popNestedPath();
            }

            //Bila ada Fire, validasi
            if(groupPolicy.isFireCheck()){
                e.pushNestedPath("polisFire");
                ValidationUtils.invokeValidator(this.policyFireValidator, groupPolicy.getPolisFire(), e);
                e.popNestedPath();
            }
    	}
        
	}

}
