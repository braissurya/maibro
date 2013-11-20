package com.maibro.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.maibro.model.DropDown;
import com.maibro.model.MstProduct;
import com.maibro.model.Upload;
import com.maibro.model.User;
import com.maibro.utils.EncryptUtils;
import com.maibro.utils.Utils;

/**
 * MultiActionController untuk fungsi2 dasar
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:49:33 AM)
 *
 */
@Controller
public class MainController extends ParentController{

	@RequestMapping({"/", "/home"})
	public String home() throws MessagingException {
		logger.debug("Halaman: HOME");
		 
		return "home";
	}
	
	@RequestMapping("/admin/console")
	public String console(Model model) {
		logger.debug("Halaman: CONSOLE");
		
		model.addAttribute("userMap", sessionRegistry.getUserMap());
		
		return "console";
	}

	@RequestMapping({"/err/{toggle}"})
	public String err(@PathVariable Integer toggle) throws MessagingException {
		dbService.updateTestDB(toggle);
		return "redirect:/";
	}
	
	@RequestMapping({ "/logout", "/keluar" })
	public String logout(HttpServletRequest request) {
		logger.debug("Halaman: LOGOUT");
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		sessionRegistry.kick(currentUser, request.getSession(false));
		
		return "redirect:/login";
	}
	
	@RequestMapping("/changepass")
	public String changepass(HttpServletRequest request, @ModelAttribute("user") User user, BindingResult result, Model model, RedirectAttributes ra) {
		logger.debug("Halaman: CHANGE PASSWORD");
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		
		if(request.getParameter("newPassword") != null){
			ValidationUtils.rejectIfEmpty(result, "newPassword", "NotEmpty", new String[]{""});
			ValidationUtils.rejectIfEmpty(result, "confirmPassword", "NotEmpty", new String[]{""});
			if (!result.hasErrors()) {
				User tmp = dbService.selectUser(user);
				if(tmp == null) {
					result.rejectValue("password", "", "Password salah");
				}else if(!user.getNewPassword().equals(user.getConfirmPassword())){
					result.rejectValue("password", "", "Password baru tidak sama.");
				}else if(user.getNewPassword().length() < 6 || user.getNewPassword().length() > 20){
					result.rejectValue("password", "", "Password baru harus diantara 6-20 karakter.");
				}
			}
			
			//bila ada error, kembalikan ke halaman edit
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
				return "changepass";
			}

			//bila tidak ada error simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
			String pesan = dbService.changePassword(user);
			ra.addFlashAttribute("pesan", pesan);
			return "redirect:/changepass";
		}

		return "changepass";
	}
	
	@RequestMapping("/openfile/{tipe}")
	public String view(HttpServletRequest request, HttpServletResponse response,@PathVariable Integer tipe) throws Exception{
		String location=request.getParameter("loc");
		if(!Utils.isEmpty(location)) {
			location=EncryptUtils.decrypt(location.replace(" ","+"));
			if (tipe==1) {//download formulir
				String [] split=location.split("~");
				location=props.getProperty("dir.klaim.formulir")+"\\"+split[0]+"\\"+split[1];
			}else if (tipe==2) {//download formulir
				String [] split=location.split("~");
				location=props.getProperty("dir.klaim.upload")+"\\"+split[0]+"\\"+split[1];
			}else if (tipe==3) {//download kuesioner & sppa
				String [] split=location.split("~");//bank id, cab bank_id, spaj no, filename
				
				location=props.getProperty("upload.polis")+"\\"+split[0]+"\\"+split[1]+"\\"+split[2]+"\\"+split[3];
			}
							
			File file = new File(location);	
			Utils.downloadFile(location, "", response, "inline");			
		}
		return null;
	}	
	
	@RequestMapping("/deletefile/{tipe}")
	public String deleteFire(HttpServletRequest request, HttpServletResponse response,@PathVariable Integer tipe) throws Exception{
		String location=request.getParameter("loc");
		if(!Utils.isEmpty(location)) {
			location=EncryptUtils.decrypt(location.replace(" ","+"));
			 if (tipe==3) {//download kuesioner & sppa
				String [] split=location.split("~");//bank id, cab bank_id, spaj no, filename

				Utils.deleteFile(props.getProperty("upload.polis")+"\\"+split[0]+"\\"+split[1]+"\\"+split[2], split[3], null);
				try {
					ServletOutputStream os = response.getOutputStream();
					os.print("<script>alert('File "+split[3]+" berhasil dihapus');history.go(-1);</script>");
					os.close();
					return null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			 
			
			
				
		}
		return null;
	}	
	
	@RequestMapping(value="/klaim/formulir", method={RequestMethod.POST,RequestMethod.GET})
	public String downloadFormulirKlaim(HttpServletRequest request, Model model) throws Exception{
		String asuransi = ServletRequestUtils.getStringParameter(request, "asuransi", null);
		if(asuransi!=null){
			List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
			for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\"+asuransi)){
				HashMap<String, Object> add=new HashMap<String, Object>();
				add.put("key", dd.key);
				add.put("encrypt", EncryptUtils.encodeURL(asuransi+"~"+dd.key));
				daftarfile.add(add);
			}
			model.addAttribute("daftarFile",daftarfile);
			model.addAttribute("asuransi", asuransi);
		}
		
		model.addAttribute("lsasuransi", dbService.selectDropDown("id", "nama", "mst_bank", "jenis in (2,4) and active=1", "nama"));
		return "downloadFormulirClaim";
	}	
	
	
	
	/**
	 * Method khusus untuk return data dalam bentuk JSON
	 * Biasanya perlu untuk ajax (misalnya autocomplete, atau dynamic dropdown)
	 * 
	 * @author Yusuf
	 * @since Jan 30, 2013 (6:08:30 PM)
	 *
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/json/{tipe}/{param}")
	public String json(HttpServletResponse response, @PathVariable String tipe, @PathVariable String param) throws IOException{
		response.setContentType("application/json");
		
		List<DropDown> result = new ArrayList<DropDown>();
		
		if("bank".equalsIgnoreCase(tipe)){
			result = dbService.selectDropDown("id", "nama", "mst_cab_bank", "mst_bank_id = '"+param+"' and active = 1", "nama");
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(result));
		out.close();
		
		return null;
	}
	
	/**
	 * Method khusus untuk hitung premi dalam bentuk ajax
	 * 
	 * @author Yusuf
	 * @since 7 Mar 2013
	 *
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws ServletRequestBindingException 
	 * @throws ParseException 
	 */
	@RequestMapping("/hitungpremi")
	public String hitungpremi(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json");

		Map<String, Object> result;
		
		try {
			int produk = ServletRequestUtils.getRequiredIntParameter(request, "produk");
			double plafon = Utils.defaultNF.parse(ServletRequestUtils.getStringParameter(request, "plafon", "0")).doubleValue();
			int ins = ServletRequestUtils.getIntParameter(request, "ins", 0);
			int usia = ServletRequestUtils.getIntParameter(request, "usia", 0);
			int jenisbangunan = ServletRequestUtils.getIntParameter(request, "jenisbangunan", 0);

			if(produk == 1) { //KPR Life
				result = Utils.hitungPremiKPRLife(dbService, usia, plafon, produk, ins);
				
			}else if(produk == 2) { //KPR Fire
				result = Utils.hitungPremiKPRFire(dbService, plafon, produk, ins, jenisbangunan);
				
			}else if(produk == 3) { //Micro Life
				result = Utils.hitungPremiMicroLife(dbService, plafon, produk, ins);
				
			}else{ //belum diimplementasi perhitungannya.
				result = new HashMap<String, Object>();
				result.put("errm", "Perhitungan untuk produk ini belum diimplementasi. Silahkan konfirmasi dengan pihak MaiBro.");
			}
		} catch (ServletRequestBindingException e) {
			result = new HashMap<String, Object>();
			result.put("errm", "Perhitungan gagal dijalankan. Harap pastikan semua kolom sudah terisi dengan lengkap dan benar.");
		} catch (ParseException e) {
			result = new HashMap<String, Object>();
			result.put("errm", "Perhitungan gagal dijalankan. Harap pastikan semua kolom sudah terisi dengan lengkap dan benar.");
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(result));
		out.close();
		
		return null;
	}
	
	@RequestMapping("/imagegenerator/{attach}/{id}/{fileName}.{format}")
	public String imagegenerator(HttpServletRequest request,HttpServletResponse response,@PathVariable String id, @PathVariable String fileName,@PathVariable String format, @PathVariable String attach) throws Exception{
		String destDir=request.getServletContext().getRealPath("/static/img/")+"\\";
		fileName+="."+format;
		if(id!=null){
			if(id.equals("logo")){
				 destDir=props.getProperty("upload.company.logo")+"\\";
				File file=new File(destDir+"\\"+fileName);
				if(file.exists()){
					try{
						Utils.downloadFile(destDir, fileName, response, attach);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}else{
					
					try{
						 destDir=request.getServletContext().getRealPath("/static/img/")+"\\";
						fileName="noimage.gif";
						Utils.downloadFile(destDir, fileName, response, attach);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			
		}else{

			try{
				destDir=request.getServletContext().getRealPath("/static/img/")+"\\";
				fileName="noimage.gif";
				Utils.downloadFile(destDir, fileName, response, attach);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 
		return null;
	}
	
	@RequestMapping("/calc")
	public String kalkulatorpremi(HttpServletRequest request, Model model) {
		logger.debug("Halaman: KALKULATOR PREMI");
		
		model.addAttribute("listProduk", dbService.selectDropDown("id", "nama", "mst_product", "active=1", "id"));
		model.addAttribute("listJenisBangunan", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=5 and active=1", "id"));
		
		return "kalkulatorpremi";
	}
	
	@RequestMapping(value= {"/{product}/ktp/{id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String viewKTP(@ModelAttribute("upload") Upload upload,HttpServletRequest request,Model model,@PathVariable Integer id,@PathVariable String product) {
		logger.debug("Halaman: KTP view");
		
		upload.setJenisUpload(11);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Integer jenis=null;
		String namapage="";
		String redirect="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload KTP Micro Life";
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload KTP KPR Life";
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload KTP KPR Fire";
			redirect="kpr";
		}
		
		Integer filter_bank=currentUser.getBank_id()==1?null:+currentUser.getBank_id();
		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,jenis,filter_bank,null);
		if(!lsProduk.isEmpty())
		upload.setMst_product_id(lsProduk.get(0).id);
		
		upload.setPolicy(dbService.selectPolicy(id, null, null, null));
		upload.getPolicy().setBank(dbService.selectBank(upload.getPolicy().getId()));
		
		model.addAttribute("spaj_no",upload.policy.getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		model.addAttribute("redirect", redirect);
		
		List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
		for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("upload.polis")+"\\"+upload.policy.getBank().mst_bank_id+"\\"+upload.policy.getBank().mst_cab_bank_id+"\\"+upload.policy.getSpaj_no()+"\\")){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dd.key);
			add.put("encrypt", EncryptUtils.encodeURL(upload.policy.getBank().mst_bank_id+"~"+upload.policy.getBank().mst_cab_bank_id+"~"+upload.policy.getSpaj_no()+"~"+dd.key));
			if(dd.key.startsWith("KTP_"))
			daftarfile.add(add);
		}
		model.addAttribute("daftarFile",daftarfile);
		return "view_ktp";
	}
	
	
	@RequestMapping(value= {"/{product}/kuesioner/{id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String uploadKuesioner(@ModelAttribute("upload")Upload upload,HttpServletRequest request,Model model,@PathVariable Integer id,@PathVariable String product) {
		logger.debug("Halaman: kuesioner Upload");
		upload.setJenisUpload(10);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Integer jenis=null;
		String namapage="";
		String redirect="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload Kuesioner Micro Life";
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload Kuesioner KPR Life";
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload Kuesioner KPR Fire";
			redirect="kpr";
		}
		
		Integer filter_bank=currentUser.getBank_id()==1?null:+currentUser.getBank_id();
		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,jenis,filter_bank,null);
		if(!lsProduk.isEmpty())
		upload.setMst_product_id(lsProduk.get(0).id);
		
		upload.setPolicy(dbService.selectPolicy(id, null, null, null));
		upload.getPolicy().setBank(dbService.selectBank(upload.getPolicy().getId()));
		
		model.addAttribute("spaj_no",upload.policy.getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		model.addAttribute("redirect", redirect);
		
		List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
		for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("upload.polis")+"\\"+upload.policy.getBank().mst_bank_id+"\\"+upload.policy.getBank().mst_cab_bank_id+"\\"+upload.policy.getSpaj_no()+"\\")){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dd.key);
			add.put("encrypt", EncryptUtils.encodeURL(upload.policy.getBank().mst_bank_id+"~"+upload.policy.getBank().mst_cab_bank_id+"~"+upload.policy.getSpaj_no()+"~"+dd.key));
			if(dd.key.startsWith("KUESIONER_"))
			daftarfile.add(add);
		}
		model.addAttribute("daftarFile",daftarfile);
		return "view_kuesioner";
	}
}