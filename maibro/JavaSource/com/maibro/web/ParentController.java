package com.maibro.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.maibro.model.User;
import com.maibro.service.DbService;
import com.maibro.utils.Emailer;
import com.maibro.utils.SessionRegistry;
import com.maibro.utils.Utils;

/**
 * Abstract ParentController sebagai parent dari semua controller
 * Cuman untuk meletakkan reference data saja dan beberapa variable
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:50:34 AM)
 *
 */
public abstract class ParentController {

	protected static Logger logger = Logger.getLogger(ParentController.class);
	
	@Autowired
	protected SessionRegistry sessionRegistry;
	
	@Autowired
	protected MessageSource messageSource;
		
	@Autowired
	protected DbService dbService;
	
	@Autowired
	protected Emailer email;

	@Autowired
	protected Properties props;
		
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("company")
	public Map<String, Object> company(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", messageSource.getMessage("company.name", null, null));
		map.put("address", messageSource.getMessage("company.address", null, null).replaceAll("\\n", "<br>"));
		map.put("copyright", messageSource.getMessage("company.copyright", new String[]{Utils.getCopyrightYears(dbService.selectSysdate())}, null));
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		if(currentUser!=null){
			if(currentUser.getBank_jenis()==1)
				map.put("rekening",dbService.selectListMstProduct(null, null, currentUser.getBank_id(), currentUser.getMst_product_id()));
			else if(currentUser.getBank_jenis()==3)
				map.put("rekening",dbService.selectListMstProduct(null, null, null, currentUser.getMst_product_id()));
		}
		String pageCSS="default.css";
		if(request.getRequestURI().toLowerCase().contains("kpr")){
			pageCSS="kpr.css";
		}else if(request.getRequestURI().toLowerCase().contains("micro")){
			pageCSS="micro.css";
		}
		
		map.put("pageCSS", pageCSS);
		
		return map;
	}
	
	
	
}
