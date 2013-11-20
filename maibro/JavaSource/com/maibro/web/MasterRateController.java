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

import com.maibro.model.MstRate;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.Utils;
import com.maibro.validator.RateValidator;
import com.maibro.validator.UploadValidator;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 9:25:42 PM
 * @Description : inputan master rate
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/master/rate")
public class MasterRateController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new RateValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true));
	}
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("JenisBangunan", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=5 and active=1", "keterangan"));
		map.put("JenisRate", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=11 and active=1", "keterangan"));
		map.put("AllRate", dbService.selectDropDown("id", "concat('tenor:',tenor,'; umur:',umur) \"desc\",mst_product_id", "mst_rate", "1=1", "id"));
		return map;
	}
	
	
	@RequestMapping(value="/{uname}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable Integer uname) {
		logger.debug("Halaman: Master Rate, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="id",sort_type=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		totalData=dbService.selectListMstRatePagingCount(search,uname);
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		List<MstRate> listMstRate=dbService.selectListMstRatePaging(search, offset, rowcount, sort, sort_type,uname);
		
		
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
		model.addAttribute("listMstRate", listMstRate);
		model.addAttribute("prod", uname);		
		model.addAttribute("prodName", dbService.selectListMstProduct(uname,null,null,null).get(0).getNama());
		return "master_rate_list";
	}		

	//input baru
	@RequestMapping(value="/new/{uname}", method=RequestMethod.GET)
	public String insert(@ModelAttribute("mstrate") MstRate mstrate,@PathVariable Integer uname,Model model) {
		logger.debug("Halaman:  Master Rate, method: NEW");
		mstrate.setMode("NEW");
		mstrate.setMst_product_id(uname);
		model.addAttribute("prodName", dbService.selectListMstProduct(uname,null,null,null).get(0).getNama());
		
		return "master_rate_edit";
	}
	
	//edit data
		@RequestMapping(value="/edit/{uname}/{prod}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
		public String update(@ModelAttribute("mstrate")MstRate mstrate, @PathVariable Integer uname, Model model,@PathVariable Integer prod) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
			logger.debug("Halaman:  Master Rate, method: EDIT");
			
			//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
			MstRate tmp=dbService.selectListMstRate(uname,prod,null,null,null,null).get(0);
			BeanUtils.copyProperties(tmp, mstrate);
			mstrate.setMode("EDIT");
			model.addAttribute("prodName", dbService.selectListMstProduct(prod,null,null,null).get(0).getNama());
			return "master_rate_edit";
		}
		
		//view data
		@RequestMapping(value="/view/{uname}/{prod}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
		public String view(@ModelAttribute("mstrate") MstRate mstrate, @PathVariable Integer uname, Model model,@PathVariable Integer prod) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
			logger.debug("Halaman:  Master Rate, method: View");
			
			//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
			
			MstRate tmp=dbService.selectListMstRate(uname,prod,null,null,null,null).get(0);
			BeanUtils.copyProperties(tmp, mstrate);
			
			mstrate.setMode("VIEW");
			
			model.addAttribute("prodName", dbService.selectListMstProduct(prod,null,null,null).get(0).getNama());
			return "master_rate_edit";
		}

	//saat user menekan tombol save baik dari layar input maupun layar edit
		@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
		public String save(@Valid @ModelAttribute("mstrate") MstRate mstrate, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
			logger.debug("Halaman:  Master Rate, method: SAVE");
			
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			
			//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
			if (!result.hasErrors()) {
				BindException errors = new BindException(result);
				
	
				if (!result.hasErrors()) {
					
				}
			}

			//bila ada error, kembalikan ke halaman edit
			if (result.hasErrors()) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("messageType", "error");
				map.put("message", messageSource.getMessage("ErrorForm", null,null));
				model.addAllAttributes(map);
				model.addAttribute("prodName", dbService.selectListMstProduct(mstrate.mst_product_id,null,null,null).get(0).getNama());
				return "master_rate_edit";
			}

			//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
			String pesan ="";
			try{
				pesan = dbService.saveMstRate(mstrate, currentUser);
				//balikin ke layar list input
			}catch (Exception e) {
				pesan=messageSource.getMessage("submitfailed"+mstrate.mode, new String[]{"Master Rate",""+mstrate.id},null);

				/*email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada Maibro", Utils.errorExtract(e), null);*/
				
				
			}
			ra.addFlashAttribute("pesan", pesan);
			return "redirect:/master/rate/"+mstrate.mst_product_id; 
			
			
		}
		
		//user menekan tombol delete
		@RequestMapping(value="/delete/{uname}/{prod}", method=RequestMethod.GET)
		public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request,@PathVariable Integer prod) throws MailException, MessagingException {
			logger.debug("Halaman: Master Rate, method: DELETE");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				MstRate mstrate=dbService.selectListMstRate(uname,prod,null,null,null,null).get(0);
				mstrate.setMode("DELETE");
				pesan = dbService.saveMstRate(mstrate, currentUser);
				
			}catch (Exception e) {
				e.printStackTrace();
				pesan=messageSource.getMessage("submitfailedDELETE", new String[]{"Master Rate",""+uname},null);

				/*email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);*/
			
			
			}
			ra.addFlashAttribute("pesan", pesan);
			
			return "redirect:/master/rate/"+prod;
		}
	
}
