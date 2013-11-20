package com.maibro.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.maibro.model.GroupPolicy;
import com.maibro.model.MstRate;
import com.maibro.model.Upload;

/**
 * Validator Untuk Input Group Policy (gabungan life dan fire)
 * 
 * @author Yusuf
 * @since Feb 4, 2013 (11:29:07 AM)
 *
 */
public class RateValidator implements Validator {



	public boolean supports(Class clazz) {
		return MstRate.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
		MstRate mstRate = (MstRate) obj;
        
         
		
         ValidationUtils.rejectIfEmptyOrWhitespace(e, "tenor",  "NotEmpty", new String[]{"Tenor"},null);
//         ValidationUtils.rejectIfEmptyOrWhitespace(e, "umur",  "NotEmpty", new String[]{"Umur"},null);
         //         ValidationUtils.rejectIfEmptyOrWhitespace(e, "jenis_bangunan",  "NotEmpty", new String[]{"Jenis Bangunan"},null);
         ValidationUtils.rejectIfEmptyOrWhitespace(e, "rate",  "NotEmpty", new String[]{"Rate"},null);
         ValidationUtils.rejectIfEmptyOrWhitespace(e, "jenis_rate",  "NotEmpty", new String[]{"Rate"},null);
       
	}

}
