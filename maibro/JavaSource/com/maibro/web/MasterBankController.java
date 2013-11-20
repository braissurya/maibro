package com.maibro.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
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

import com.maibro.model.MstBank;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.Utils;

/**
 * 
 * @author 		: Rudy Hermawan
 * @since		: Jan 30, 2013 9:25:42 PM
 * @Description : inputan Master Perusahaan
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/master/bank")
public class MasterBankController extends ParentController {

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
		map.put("JenisBank",Utils.lstJenisBank());
		return map;
	}
	
	
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {
		logger.debug("Halaman: Master Perusahaan, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="id",sort_type=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		totalData=dbService.selectListMstBankPagingCount(search);//FIXME: query count data
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		List<MstBank> listMstBank=dbService.selectListMstBankPaging(search, offset, rowcount, sort, sort_type);
		
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
		model.addAttribute("listMstBank", listMstBank);
		
		return "master_bank_list";
	}		
	
	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(@ModelAttribute("mstbank") MstBank mstbank) {
		logger.debug("Halaman:  Master Perusahaan, method: NEW");
		mstbank.setMode("NEW");
		return "master_bank_edit";
	}
	
	//edit data
	@RequestMapping(value="/edit/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(@ModelAttribute("mstbank")MstBank mstbank, @PathVariable Integer uname, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Perusahaan, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		MstBank tmp=dbService.selectListMstBank(uname).get(0);
		BeanUtils.copyProperties(tmp, mstbank);
		mstbank.setMode("EDIT");
		return "master_bank_edit";
	}
		
	//view data
	@RequestMapping(value="/view/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(@ModelAttribute("mstbank") MstBank mstbank, @PathVariable Integer uname) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Perusahaan, method: View");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		
		MstBank tmp=dbService.selectListMstBank(uname).get(0);
		BeanUtils.copyProperties(tmp, mstbank);
		
		mstbank.setMode("VIEW");
		
		
		return "master_bank_edit";
	}

	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("mstbank") MstBank mstbank, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Master Perusahaan, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nama", "", messageSource.getMessage("NotEmpty", new String[]{"Nama Bank"},null));
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jenis", "", messageSource.getMessage("NotEmpty", new String[]{"Jenis"},null));
			if (!result.hasErrors()) {
				if(mstbank.getJenis()==2||mstbank.getJenis()==4){
				
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "diskon_premi", "", messageSource.getMessage("NotEmpty", new String[]{"Diskon Premi"},null));
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "diskon_brokerage", "", messageSource.getMessage("NotEmpty", new String[]{"Diskon Broker"},null));
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "diskon_komisi", "", messageSource.getMessage("NotEmpty", new String[]{"Diskon Komisi"},null));
					
					
				}
				
				if(mstbank.uploadFile.getSize() == 0 && 	mstbank.mode.equals("NEW")){
					//errors.rejectValue("uploadFile", "NotEmpty", new String[]{""}, "");
				}else if(mstbank.uploadFile.getSize() != 0){				
					String filetype=mstbank.uploadFile.getOriginalFilename();
					if(!filetype.toLowerCase().contains(".gif")){
						errors.rejectValue("uploadFile",null, "Harap upload logo dengan format *.gif");
					}
					
				}
				
				if(mstbank.uploadFileTTD.getSize() == 0 && 	mstbank.mode.equals("NEW")){
					//errors.rejectValue("uploadFile", "NotEmpty", new String[]{""}, "");
				}else if(mstbank.uploadFileTTD.getSize() != 0){				
					String filetype=mstbank.uploadFileTTD.getOriginalFilename();
					if(!filetype.toLowerCase().contains(".gif")){
						errors.rejectValue("uploadFileTTD",null, "Harap upload logo dengan format *.gif");
					}
					
				}
				
				if(mstbank.uploadFileCap.getSize() == 0 && 	mstbank.mode.equals("NEW")){
					//errors.rejectValue("uploadFile", "NotEmpty", new String[]{""}, "");
				}else if(mstbank.uploadFileCap.getSize() != 0){				
					String filetype=mstbank.uploadFileCap.getOriginalFilename();
					if(!filetype.toLowerCase().contains(".gif")){
						errors.rejectValue("uploadFileCap",null, "Harap upload logo dengan format *.gif");
					}
					
				}
			}
		}

		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			model.addAllAttributes(map);
			
			return "master_bank_edit";
		}

		//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{
			pesan = dbService.saveMstBank(mstbank, currentUser);
			
			if(mstbank.uploadFile.getSize() != 0){
				//simpan logo
				//nama file yang ingin disimpan
				String filename=+mstbank.id+".gif";
				//jumlah coloumn data yang ingin di proses
				
				
				//buat directory di server bila belum ada
				String path = props.getProperty("upload.company.logo")+"\\";
				File directory = new File(path);
				if(!directory.exists()) directory.mkdirs();
				
				//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
				Date sekarang = dbService.selectSysdate();
				File file = null;
				//copy file ke server
				
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, mstbank.uploadFile.getBytes());
		
			}
			
			if(mstbank.uploadFileTTD.getSize() != 0){
				//simpan logo
				//nama file yang ingin disimpan
				String filename=+mstbank.id+"_ttd.gif";
				//jumlah coloumn data yang ingin di proses
				
				
				//buat directory di server bila belum ada
				String path = props.getProperty("upload.company.logo")+"\\";
				File directory = new File(path);
				if(!directory.exists()) directory.mkdirs();
				
				//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
				Date sekarang = dbService.selectSysdate();
				File file = null;
				//copy file ke server
				
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, mstbank.uploadFileTTD.getBytes());
		
			}
			
			if(mstbank.uploadFileCap.getSize() != 0){
				//simpan logo
				//nama file yang ingin disimpan
				String filename=+mstbank.id+"_cap.gif";
				//jumlah coloumn data yang ingin di proses
				
				
				//buat directory di server bila belum ada
				String path = props.getProperty("upload.company.logo")+"\\";
				File directory = new File(path);
				if(!directory.exists()) directory.mkdirs();
				
				//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
				Date sekarang = dbService.selectSysdate();
				File file = null;
				//copy file ke server
				
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, mstbank.uploadFileCap.getBytes());
		
			}
			//balikin ke layar list input
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed"+mstbank.mode, new String[]{"Master Perusahaan",""+mstbank.id},null);
			e.printStackTrace();
//			email.send(
//					props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
//					"ERROR pada maibro (Input Master Bank)", e.toString());
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/master/bank"; 	
	}
		
	//user menekan tombol delete
	@RequestMapping(value="/delete/{uname}", method=RequestMethod.GET)
	public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request) throws MailException, MessagingException {
		logger.debug("Halaman: Master Perusahaan, method: DELETE");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			MstBank mstbank=dbService.selectListMstBank(uname).get(0);
			mstbank.setMode("DELETE");
			pesan = dbService.saveMstBank(mstbank, currentUser);
			
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailedDELETE", new String[]{"Master Perusahaan",""+uname},null);

			/*email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada E-Accounting", Utils.errorExtract(e), null);*/
		
		
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/master/bank";
	}
}
