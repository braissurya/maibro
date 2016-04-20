package com.maibro.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.maibro.model.Upload;

/**
 * Validator Untuk Input Group Policy (gabungan life dan fire)
 * 
 * @author Yusuf
 * @since Feb 4, 2013 (11:29:07 AM)
 *
 */
public class UploadValidator implements Validator {



	public boolean supports(Class clazz) {
		return Upload.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
        Upload upload = (Upload) obj;
        
        
		    if(upload.jenisUpload!=25&&upload.jenisUpload!=12){
		    	if(upload.uploadFile==null||upload.uploadFile.getSize() == 0){
					e.rejectValue("uploadFile", "NotEmpty", new String[]{""}, "");
				}else if(upload.uploadFile.getSize() > 500000){
					e.rejectValue("uploadFile", "","Maksimal upload 500kb");
				}
		    }
		    
		    if(!e.hasErrors()){
				if(upload.jenisUpload==1||upload.jenisUpload==4){
					if(upload.jenisUpload==1)ValidationUtils.rejectIfEmptyOrWhitespace(e, "mst_product_id", "NotEmpty", new String[]{""});
					 ValidationUtils.rejectIfEmptyOrWhitespace(e, "importStartLine", "NotEmpty", new String[]{""});
					if((!upload.uploadFile.getOriginalFilename().toLowerCase().endsWith(".csv"))){
						e.rejectValue("uploadFile", "","Harap upload file dengan format csv (*.csv)");
					}
				}else if(upload.jenisUpload==8||upload.jenisUpload==12){
					ValidationUtils.rejectIfEmptyOrWhitespace(e, "policy.flag_akseptasi", "NotEmpty", new String[]{""});
					ValidationUtils.rejectIfEmptyOrWhitespace(e, "policy.rate", "NotEmpty", new String[]{""});
					ValidationUtils.rejectIfEmptyOrWhitespace(e, "policy.premi", "NotEmpty", new String[]{""});
					ValidationUtils.rejectIfEmptyOrWhitespace(e, "policy.extrapremi", "NotEmpty", new String[]{""});
					
					if(upload.jenisUpload!=25&&upload.jenisUpload!=12)
					if((!upload.uploadFile.getOriginalFilename().toLowerCase().endsWith(".pdf"))){
						//e.rejectValue("uploadFile", "","Harap upload file dengan format pdf (*.pdf)");
					}
					
					if(!e.hasErrors()){
						upload.policy.setTotalpremi(upload.getPolicy().getPremi()+upload.getPolicy().getExtrapremi());
					}
				}else if(upload.jenisUpload==2){
					 ValidationUtils.rejectIfEmptyOrWhitespace(e, "importStartLine", "NotEmpty", new String[]{""});
					 ValidationUtils.rejectIfEmptyOrWhitespace(e, "mst_product_id", "NotEmpty", new String[]{""});
				}
		    }

       
	}

}
