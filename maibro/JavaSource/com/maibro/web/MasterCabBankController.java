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

import com.maibro.model.MstCabBank;
import com.maibro.model.User;
import com.maibro.utils.Utils;

/**
 * 
 * @author 		: Rudy Hermawan
 * @since		: Jan 30, 2013 9:25:42 PM
 * @Description : inputan master cabang bank
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/master/cabbank")
public class MasterCabBankController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true));
	}
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("AllBank", dbService.selectDropDown("id", "nama", "mst_bank", "active =1", "nama"));
		map.put("AllCabBank", dbService.selectDropDown("id", "mst_bank_id \"desc\", nama", "mst_cab_bank", "active =1", "nama"));
		map.put("AllCabBankInduk", dbService.selectDropDown("id", "nama", "mst_cab_bank", "active =1 and jenis=1", "nama"));
		map.put("JenisCabBank",Utils.lstJenisCabBank());
		return map;
	}
	
	
	@RequestMapping(value="/{uname}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable Integer uname) {
		logger.debug("Halaman: Master Cabang Bank, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="id",sort_type=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		totalData=dbService.selectListMstCabBankPagingCount(search, uname);//FIXME: query count data
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		List<MstCabBank> listMstBank=dbService.selectListMstCabBankPaging(search, offset, rowcount, sort, sort_type, uname);
		
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
		model.addAttribute("listMstCabBank", listMstBank);
		model.addAttribute("bank", uname);		
		model.addAttribute("bankName", dbService.selectListMstBank(uname).get(0).getNama());
		
		return "master_cabbank_list";
	}		
	
	//input baru
	@RequestMapping(value="/new/{uname}", method=RequestMethod.GET)
	public String insert(@ModelAttribute("mstcabbank") MstCabBank mstcabbank,@PathVariable Integer uname,Model model) {
		logger.debug("Halaman:  Master Cabang Bank, method: NEW");
		mstcabbank.setMode("NEW");
		mstcabbank.setMst_bank_id(uname);
		model.addAttribute("bankName", dbService.selectListMstBank(uname).get(0).getNama());

		return "master_cabbank_edit";
	}
	
	//edit data
	@RequestMapping(value="/edit/{uname}/{bank}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(@ModelAttribute("mstcabbank")MstCabBank mstcabbank, @PathVariable Integer uname, Model model,@PathVariable Integer bank) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Cabang Bank, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		MstCabBank tmp=dbService.selectListMstCabBank(uname, bank,null).get(0);
		BeanUtils.copyProperties(tmp, mstcabbank);
		mstcabbank.setMode("EDIT");
		model.addAttribute("bankName", dbService.selectListMstBank(bank).get(0).getNama());
		return "master_cabbank_edit";
	}
		
	//view data
	@RequestMapping(value="/view/{uname}/{bank}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(@ModelAttribute("mstcabbank") MstCabBank mstcabbank, @PathVariable Integer uname, Model model,@PathVariable Integer bank) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Cabang Bank, method: View");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		
		MstCabBank tmp=dbService.selectListMstCabBank(uname, bank,null).get(0);
		BeanUtils.copyProperties(tmp, mstcabbank);
		
		mstcabbank.setMode("VIEW");
		
		model.addAttribute("bankName", dbService.selectListMstBank(bank).get(0).getNama());		
		return "master_cabbank_edit";
	}

	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("mstcabbank") MstCabBank mstcabbank, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Master Cabang Bank, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mst_bank_id", "", messageSource.getMessage("NotEmpty", new String[]{""},null));
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nama", "", messageSource.getMessage("NotEmpty", new String[]{""},null));
			if (!result.hasErrors()) {
				
			}
		}

		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			model.addAllAttributes(map);
			
			return "master_cabbank_edit";
		}

		//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{
			pesan = dbService.saveMstCabBank(mstcabbank, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed"+mstcabbank.mode, new String[]{"Master Cabang Bank",""+mstcabbank.id},null);

//			email.send(
//					props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
//					"ERROR pada maibro (Master Cab Bank)", e.toString());
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/master/cabbank/"+mstcabbank.mst_bank_id; 	
	}
		
	//user menekan tombol delete
	@RequestMapping(value="/delete/{uname}/{bank}", method=RequestMethod.GET)
	public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request,@PathVariable Integer bank) throws MailException, MessagingException {
		logger.debug("Halaman: Master Perusahaan, method: DELETE");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			MstCabBank mstcabbank=dbService.selectListMstCabBank(uname, bank,null).get(0);
			mstcabbank.setMode("DELETE");
			pesan = dbService.saveMstCabBank(mstcabbank, currentUser);
			
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailedDELETE", new String[]{"Master Cabang Bank",""+uname},null);

//			email.send(
//					props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
//					"ERROR pada maibro (Delete Master Cab Bank)", e.toString());
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/master/cabbank/"+bank;
	}
}
