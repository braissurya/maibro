package com.maibro.web;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.Utils;

/**
 * Controller untuk cancel polis
 * 
 * Flow:
 * 1. MaiBro menekan tombol CANCEL POLIS di VIEWER, sistem me-redirect ke controller untuk meng-cancel
 * 2. Sistem mengupdate status polis menjadi cancel, mencatat history posisi, dan meng-email ke pihak asuransi bahwa ada polis yg di cancel
 * 3. ???
 * 
 * FIXME yang sudah:
 * - 
 * 
 * FIXME yang belum:
 * - 
 * 
 * @author Yusuf
 * @since 11 Mei 2013
 *
 */
@Controller
@RequestMapping("/cancel")
public class CancelPolisController extends ParentController{

	@RequestMapping(value="/{policy_id}", method=RequestMethod.GET) //mapping request GET
	public String show(@PathVariable Integer policy_id, @ModelAttribute("policy") Policy policy) {
		logger.debug("Halaman: CANCEL, method: show");
		Policy tmp = dbService.selectPolicyById(policy_id);
		BeanUtils.copyProperties(tmp, policy);

		return "cancel_polis";
	}

	//saat user menekan tombol cancel polis
	@RequestMapping(value="/{policy_id}", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String cancel(@Valid @ModelAttribute("policy") Policy policy, @PathVariable("policy_id") Integer policy_id, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MessagingException {
		logger.debug("Halaman: CANCEL, method: cancel");

		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "keteranganCancel", "NotEmpty", new String[]{""});
		}
		
		//bila ada error, kembalikan ke halaman cancel polis
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			String message=messageSource.getMessage("ErrorForm", null,null);
			for(String err:Utils.errorBinderToList(result, messageSource)){
				if(!err.trim().equals("harus diisi"))
				message+="<br/>"+err;
			}
			map.put("message",message );
			model.addAllAttributes(map);

			//tarik ulang datanya
			Policy tmp = dbService.selectPolicyById(policy_id);
			BeanUtils.copyProperties(tmp, policy);

			return "cancel_polis";
		}

		//bila tidak ada error lakukan pembatalan polis, lalu kembalikan ke layar, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan = dbService.cancelPolis(policy_id, currentUser, policy.getKeteranganCancel());
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/viewer/view/" + policy_id;//balikin ke layar view
		
	}

}