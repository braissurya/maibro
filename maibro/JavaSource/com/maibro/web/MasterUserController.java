package com.maibro.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maibro.model.User;
import com.maibro.model.User;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.Utils;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 9:25:42 PM
 * @Description : inputan master user
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/admin/user")
public class MasterUserController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true));
	}
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("AllGroupMenu", dbService.selectDropDown("id", "nama", "group_menu", "active=1 group by nama", " nama "));
		map.put("listBank", dbService.selectDropDown("id", "nama", "mst_bank", "active = 1", "nama"));
		map.put("AllUser", dbService.selectDropDown("id", "group_menu_id \"desc\" ,username", "user", "active = 1", "username"));
		map.put("AllGroupProduct", dbService.selectDropDown("jenis", "keterangan", "mst_master", "active = 1 and id = 3", "keterangan"));
		map.put("AllGroupProduct", dbService.selectDropDown("jenis", "keterangan", "mst_master", "active = 1 and id=3", "keterangan"));
		return map;
	}
	
	
	@RequestMapping(value="/{uname}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable Integer uname) {
		logger.debug("Halaman: Master User, method: SHOW");
		if(uname==-1)uname=null;
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="id",sort_type=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		totalData=dbService.selectListUserPagingCount(search,uname);
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		List<User> listUser=dbService.selectListUserPaging(search, offset, rowcount, sort, sort_type, uname);
		
		
		model.addAttribute("listNumRows", listNumRows);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalData", totalData);
		model.addAttribute("halfPage", (new Double(totalPage/2)).intValue());
		List<Integer>pages=new ArrayList<Integer>();
		for (int i = 0; i < totalPage; i++) {
			pages.add(i+1);
		}
		model.addAttribute("pages", pages);
		model.addAttribute("page", page);
		model.addAttribute("rowcount", rowcount);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		model.addAttribute("sort_type", sort_type);
		model.addAttribute("listUser", listUser);
		if(uname==null)uname=-1;
		model.addAttribute("groupmenuid", uname);		
		model.addAttribute("groupmenuName",uname==-1?"": dbService.selectListGroupMenu(uname, null, 1, 1).get(0).getNama());
		return "master_user_list";
	}		

	//input baru
	@RequestMapping(value="/new/{uname}", method=RequestMethod.GET)
	public String insert(@ModelAttribute("user") User user,@PathVariable Integer uname,Model model) {
		logger.debug("Halaman:  Master User, method: NEW");
		if(uname==-1)uname=null;
		user.setMode("NEW");
		user.setGroup_menu_id(uname);
		user.setPassword("123BCD");
		if(uname==null)uname=-1;
		model.addAttribute("groupmenuid", uname);
		model.addAttribute("groupmenuName", uname==-1?"":dbService.selectListGroupMenu(uname, null, 1, 1).get(0).getNama());
		
		return "master_user_edit";
	}
	
	//edit data
	@RequestMapping(value="/edit/{uname}/{prod}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(@ModelAttribute("user")User user, @PathVariable Integer uname, Model model,@PathVariable Integer prod) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master User, method: EDIT");
		if(prod==-1)prod=null;
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		User tmp=dbService.selectAllUser(prod, uname).get(0);
		BeanUtils.copyProperties(tmp, user);
		user.setMode("EDIT");
		if(prod==null)prod=-1;
		model.addAttribute("groupmenuName", prod==-1?"":dbService.selectListGroupMenu(prod, null, 1, 1).get(0).getNama());
		model.addAttribute("groupmenuid", prod);
		return "master_user_edit";
	}
		
		//view data
	@RequestMapping(value="/view/{uname}/{prod}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(@ModelAttribute("user") User user, @PathVariable Integer uname, Model model,@PathVariable Integer prod) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master User, method: View");
		if(prod==-1)prod=null;
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		
		User tmp=dbService.selectAllUser(prod, uname).get(0);
		BeanUtils.copyProperties(tmp, user);
		
		user.setMode("VIEW");
		if(prod==null)prod=-1;
		model.addAttribute("groupmenuName",  prod==-1?"":dbService.selectListGroupMenu(prod, null, 1, 1).get(0).getNama());
		model.addAttribute("groupmenuid", prod);
		return "master_user_edit";
	}

	//saat user menekan tombol save baik dari layar input maupun layar edit
		@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
		public String save(@Valid @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
			logger.debug("Halaman:  Master User, method: SAVE");
			
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			
			//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
			if (!result.hasErrors()) {
				BindException errors = new BindException(result);
				
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "", messageSource.getMessage("NotEmpty", new String[]{""},null));
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "group_menu_id", "", messageSource.getMessage("NotEmpty", new String[]{""},null));
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bank_id", "", messageSource.getMessage("NotEmpty", new String[]{""},null));
				if (!result.hasErrors()) {
					
				}
			}

			//bila ada error, kembalikan ke halaman edit
			if (result.hasErrors()) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("messageType", "error");
				map.put("message", messageSource.getMessage("ErrorForm", null,null));
				model.addAllAttributes(map);
				
				return "master_user_edit";
			}

			//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
			String pesan ="";
			try{
				pesan = dbService.saveUser(user, currentUser);
				//balikin ke layar list input
			}catch (Exception e) {
				pesan=messageSource.getMessage("submitfailed"+user.mode, new String[]{"Master User",""+user.id},null);

				/*email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada Maibro", Utils.errorExtract(e), null);*/
				
				
			}
			ra.addFlashAttribute("pesan", pesan);
			Integer groupid=ServletRequestUtils.getIntParameter(request, "groupid",-1);
			return "redirect:/admin/user/"+groupid; 
			
			
		}
		
		//user menekan tombol delete
		@RequestMapping(value="/delete/{uname}/{prod}", method=RequestMethod.GET)
		public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request,@PathVariable Integer prod) throws MailException, MessagingException {
			logger.debug("Halaman: Master User, method: DELETE");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				if(prod==-1)prod=null;
				User user=dbService.selectAllUser(prod,uname).get(0);
				user.setMode("DELETE");
				pesan = dbService.saveUser(user, currentUser);
				
			}catch (Exception e) {
				e.printStackTrace();
				pesan=messageSource.getMessage("submitfailedDELETE", new String[]{"Master User",""+uname},null);

				/*email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);*/
			
			
			}
			ra.addFlashAttribute("pesan", pesan);
			if(prod==null)prod=-1;
			return "redirect:/admin/user/"+prod;
		}
		
		//user menekan tombol delete
		@RequestMapping(value="/reset/{uname}/{prod}", method=RequestMethod.GET)
		public String reset(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request,@PathVariable Integer prod) throws MailException, MessagingException {
			logger.debug("Halaman: Master User, method: DELETE");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				if(prod==-1)prod=null;
				User user=dbService.selectAllUser(prod,uname).get(0);
				user.setMode("RESET");
				pesan = dbService.saveUser(user, currentUser);
				
			}catch (Exception e) {
				e.printStackTrace();
				pesan="Master User : Password gagal di reset";

				/*email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);*/
			
			
			}
			ra.addFlashAttribute("pesan", pesan);
			if(prod==null)prod=-1;
			return "redirect:/admin/user/"+prod;
		}
	
}
