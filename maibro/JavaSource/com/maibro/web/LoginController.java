package com.maibro.web;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maibro.model.User;

/**
 * Contoh FormController
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:50:34 AM)
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends ParentController{

	//show form pertama kali
	@RequestMapping(method=RequestMethod.GET)
	public String show(@ModelAttribute("user") User user, HttpServletRequest request, HttpSession session) {
		logger.debug("Halaman: LOGIN, method: SHOW");

//		user.setUsername("MAIBRO_IT");
		
		//bila masih logged in, langsung ke home saja
        if (session != null) {
        	User currentUser = (User) session.getAttribute("currentUser");
        	if(currentUser != null) {
            	return "redirect:/home";
        	}
        }

		return "login";
	}
	
	//saat user menekan tombol login, process form
	@RequestMapping(method=RequestMethod.POST)
	public String processForm(@Valid @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException { 
		logger.debug("Halaman: LOGIN, method: PROCESSFORM");

		//validasi dasar di model object, validasi lainnya langsung didalam controller
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			User tmp = dbService.selectUser(user);
			
			if(tmp == null) {
				errors.rejectValue("username", "", "Username tidak terdaftar atau password salah");
			}else{
				//set beberapa item dari object user yg ditarik dari database ke object user yg dipakai untuk login
//				user.setId(tmp.getId());
//				user.setGroup_menu_id(tmp.getGroup_menu_id());
//				user.bank_id=tmp.bank_id;
//				user.bank_jenis = tmp.bank_jenis;
//				user.cab_bank_id=tmp.cab_bank_id;
//				user.cab_bank_jenis = tmp.cab_bank_jenis;
				PropertyUtils.copyProperties(user, tmp);
			}
		}

		//bila ada error, kembalikan ke halaman login
		if (result.hasErrors()) {
			return "login";
		}

		//bila tidak error, lanjut ke login
		dbService.login(user, sessionRegistry, request);
		
		return "redirect:/home";
	}	
	
}
