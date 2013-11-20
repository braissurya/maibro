package com.maibro.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
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

import com.maibro.model.Claim;
import com.maibro.model.DropDown;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.EncryptUtils;
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
@RequestMapping("/klaim")
public class InputClaimController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	}
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(HttpServletRequest request){
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		String bank="";
		if(currentUser.getBank_jenis()==1)bank="and jenis in (1,4)";
		
		map.put("listPosisi", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id = 13 and active = 1 "+bank, "jenis"));
		return map;
	}
	
	
	@RequestMapping(value="/{pagename}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable String pagename) {
		logger.debug("Halaman: Input Klaim, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="id",sort_type=null,begdate=null,enddate=null;
		Integer posisi=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		String defaultbegdate=null;//"01-"+Utils.convertDateToString(dbService.selectSysdate(), "MM-yyyy");
		String defaultdate=null;//Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		begdate=ServletRequestUtils.getStringParameter(request, "begdate", defaultbegdate);
		enddate=ServletRequestUtils.getStringParameter(request, "enddate", defaultdate);		
		
		posisi = ServletRequestUtils.getIntParameter(request, "posisi",0);
		if(posisi==0)posisi=null;
		
		/*if(pagename.equals("input")){
			if(posisi==0)posisi=-1;
		}else if(pagename.equals("proses")){
			if(posisi==0)posisi=-2;
		}*/
		
		//bila user bank (jenis = 1) dan user KC / KCP (jenis = 1 atau 2), maka user hanya bisa melihat yg ada di cabangnya saja
		Integer cab_bank = null;
		Integer asuransi_id = null;
		if(currentUser.getBank_jenis().intValue() == 1 && (currentUser.getCab_bank_jenis().intValue() == 1 || currentUser.getCab_bank_jenis().intValue() == 2)){
			cab_bank = currentUser.getCab_bank_id();
		}else if(currentUser.getBank_jenis().intValue() == 2||currentUser.getBank_jenis().intValue() == 4){
			asuransi_id=currentUser.getBank_id();
		}
		
		Integer groupproduct=currentUser.getMst_product_id();
		
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		totalData=dbService.selectListClaimPagingCount(search, posisi, begdate, enddate,cab_bank,asuransi_id,groupproduct);
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		List<Claim> listClaim=dbService.selectListClaimPaging(search, offset, rowcount, sort, sort_type, posisi, defaultbegdate, enddate,cab_bank,asuransi_id,groupproduct);
		
		
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
		model.addAttribute("listClaim", listClaim);
		model.addAttribute("posisi", posisi);
		
		return "input_claim_list";
	}		

	//input baru
	@RequestMapping(value="/new", method={RequestMethod.GET, RequestMethod.POST})
	public String insert(@ModelAttribute("claim") Claim claim,Model model,HttpServletRequest request, BindingResult result) {
		logger.debug("Halaman:  Master User, method: NEW");
		
		claim.setMode("NEW");
		claim.setPagename("input");
		
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj", null);
		
		if (request.getParameter("cari")!=null) {
			BindException errors = new BindException(result);
			if(!Utils.isEmpty(spaj))claim.spaj_no=spaj;
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "spaj_no", "", messageSource.getMessage("NotEmpty", new String[]{"No SPAJ"},null));
			if (!result.hasErrors()) {
				if(claim.spaj_no!=null){
					Policy policy=dbService.selectPolicy(null, null, null, claim.spaj_no);
					if(policy==null){
						errors.rejectValue("spaj_no", null, "NO SPAJ tidak ditemukan");			
					}else if(policy.getFlag_akseptasi()==null){
						errors.rejectValue("spaj_no", null, "Maaf No SPAJ belum Aktif");			
					}else if(!(policy.getFlag_akseptasi()==1&policy.getTgl_paid()!=null)){
						errors.rejectValue("spaj_no", null, "Maaf No SPAJ belum Aktif");			
					}else{
						
						claim.policy_id=policy.getId();
						claim.namacustomer=policy.getDebitur();
						List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
						for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\3")){
							HashMap<String, Object> add=new HashMap<String, Object>();
							add.put("key", dd.key);
							add.put("encrypt", EncryptUtils.encodeURL("3~"+dd.key));
							daftarfile.add(add);
						}
						model.addAttribute("daftarFile",daftarfile);
						model.addAttribute("asuransi", 3);
						model.addAttribute("kelengkapan_klaim", Utils.lstFormClaim(true));
						model.addAttribute("file_category", Utils.lstFormClaim(false));
					}
				}
			}
		}
		
		
		
	
		return "input_claim_edit";
	}
	
	//edit data
	@RequestMapping(value="/edit/{id}/{pagename}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(@ModelAttribute("claim")Claim claim, @PathVariable Integer id,@PathVariable String pagename, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Input Klaim, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		Claim tmp=dbService.selectClaim(id);
		BeanUtils.copyProperties(tmp, claim);
		claim.setMode("EDIT");
		claim.setPagename(pagename);
		List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
		for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\3")){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dd.key);
			add.put("encrypt", EncryptUtils.encodeURL("3~"+dd.key));
			daftarfile.add(add);
		}
		model.addAttribute("daftarFile",daftarfile);
		
		daftarfile=new ArrayList<HashMap<String,Object>>();
		
		List<DropDown> lstFile=Utils.listFilesInDirectory(props.getProperty("dir.klaim.upload")+"\\"+claim.id+"\\");
		
		for(DropDown dl:Utils.lstFormClaim(false)){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dl.key);
			add.put("value", dl.value);
			add.put("desc", dl.desc);
			List<HashMap<String,Object>> tmplt=new ArrayList<HashMap<String,Object>>();
			for(DropDown dd:  lstFile){
				if(dd.key.contains(dl.value)){
					HashMap<String, Object> addFile=new HashMap<String, Object>();
					addFile.put("key", dd.key);
					//System.out.println(claim.id+"~"+dd.key);
					addFile.put("encrypt", EncryptUtils.encodeURL(claim.id+"~"+dd.key));
					tmplt.add(addFile);
				}					
			}
			add.put("lsfile", tmplt);
			daftarfile.add(add);
		}
		
		
		
		model.addAttribute("daftarFileKlaim",daftarfile);
		
		model.addAttribute("asuransi", 3);
		model.addAttribute("kelengkapan_klaim", Utils.lstFormClaim(true));
		model.addAttribute("file_category", Utils.lstFormClaim(false));
		return "input_claim_edit";
	}
	
	
	//edit data
	@RequestMapping(value="/validasi/{id}/{pagename}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String validasi(@ModelAttribute("claim")Claim claim, @PathVariable Integer id,@PathVariable String pagename, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Input Klaim, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		Claim tmp=dbService.selectClaim(id);
		BeanUtils.copyProperties(tmp, claim);
		claim.setMode("VALIDASI");
		claim.setPagename(pagename);
		List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
		for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\3")){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dd.key);
			add.put("encrypt", EncryptUtils.encodeURL("3~"+dd.key));
			daftarfile.add(add);
		}
		model.addAttribute("daftarFile",daftarfile);
		
		daftarfile=new ArrayList<HashMap<String,Object>>();
		
		List<DropDown> lstFile=Utils.listFilesInDirectory(props.getProperty("dir.klaim.upload")+"\\"+claim.id+"\\");
		
		for(DropDown dl:Utils.lstFormClaim(false)){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dl.key);
			add.put("value", dl.value);
			add.put("desc", dl.desc);
			List<HashMap<String,Object>> tmplt=new ArrayList<HashMap<String,Object>>();
			for(DropDown dd:  lstFile){
				if(dd.key.contains(dl.value)){
					HashMap<String, Object> addFile=new HashMap<String, Object>();
					addFile.put("key", dd.key);
					//System.out.println(claim.id+"~"+dd.key);
					addFile.put("encrypt", EncryptUtils.encodeURL(claim.id+"~"+dd.key));
					tmplt.add(addFile);
				}					
			}
			add.put("lsfile", tmplt);
			daftarfile.add(add);
		}
		
		
		
		model.addAttribute("daftarFileKlaim",daftarfile);
		
		model.addAttribute("asuransi", 3);
		model.addAttribute("kelengkapan_klaim", Utils.lstFormClaim(true));
		model.addAttribute("file_category", Utils.lstFormClaim(false));
		return "input_claim_edit";
	}
	
	
	@RequestMapping(value="/akseptasi/{id}/{pagename}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String akseptasi(@ModelAttribute("claim")Claim claim, @PathVariable Integer id,@PathVariable String pagename, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Input Klaim, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		Claim tmp=dbService.selectClaim(id);
		BeanUtils.copyProperties(tmp, claim);
		claim.setMode("AKSEPTASI");
		claim.setPagename(pagename);
		List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
		for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\3")){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dd.key);
			add.put("encrypt", EncryptUtils.encodeURL("3~"+dd.key));
			daftarfile.add(add);
		}
		model.addAttribute("daftarFile",daftarfile);
		
		daftarfile=new ArrayList<HashMap<String,Object>>();
		
		List<DropDown> lstFile=Utils.listFilesInDirectory(props.getProperty("dir.klaim.upload")+"\\"+claim.id+"\\");
		
		for(DropDown dl:Utils.lstFormClaim(false)){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dl.key);
			add.put("value", dl.value);
			add.put("desc", dl.desc);
			List<HashMap<String,Object>> tmplt=new ArrayList<HashMap<String,Object>>();
			for(DropDown dd:  lstFile){
				if(dd.key.contains(dl.value)){
					HashMap<String, Object> addFile=new HashMap<String, Object>();
					addFile.put("key", dd.key);
					//System.out.println(claim.id+"~"+dd.key);
					addFile.put("encrypt", EncryptUtils.encodeURL(claim.id+"~"+dd.key));
					tmplt.add(addFile);
				}					
			}
			add.put("lsfile", tmplt);
			daftarfile.add(add);
		}
		
		
		
		model.addAttribute("daftarFileKlaim",daftarfile);
		
		model.addAttribute("asuransi", 3);
		model.addAttribute("kelengkapan_klaim", Utils.lstFormClaim(true));
		model.addAttribute("file_category", Utils.lstFormClaim(false));
		return "input_claim_edit";
	}
		
		//view data
	@RequestMapping(value="/view/{id}/{pagename}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(@ModelAttribute("claim")Claim claim, @PathVariable Integer id,@PathVariable String pagename, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Input Klaim, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		Claim tmp=dbService.selectClaim(id);
		BeanUtils.copyProperties(tmp, claim);
		claim.setMode("VIEW");
		claim.setPagename(pagename);
		List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
		for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\3")){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dd.key);
			add.put("encrypt", EncryptUtils.encodeURL("3~"+dd.key));
			daftarfile.add(add);
		}
		model.addAttribute("daftarFile",daftarfile);
		
		daftarfile=new ArrayList<HashMap<String,Object>>();
		
		List<DropDown> lstFile=Utils.listFilesInDirectory(props.getProperty("dir.klaim.upload")+"\\"+claim.id+"\\");
		
		for(DropDown dl:Utils.lstFormClaim(false)){
			HashMap<String, Object> add=new HashMap<String, Object>();
			add.put("key", dl.key);
			add.put("value", dl.value);
			add.put("desc", dl.desc);
			List<HashMap<String,Object>> tmplt=new ArrayList<HashMap<String,Object>>();
			for(DropDown dd:  lstFile){
				if(dd.key.contains(dl.value)){
					HashMap<String, Object> addFile=new HashMap<String, Object>();
					addFile.put("key", dd.key);
					//System.out.println(claim.id+"~"+dd.key);
					addFile.put("encrypt", EncryptUtils.encodeURL(claim.id+"~"+dd.key));
					tmplt.add(addFile);
				}					
			}
			add.put("lsfile", tmplt);
			daftarfile.add(add);
		}
		
		
		
		model.addAttribute("daftarFileKlaim",daftarfile);
		
		model.addAttribute("asuransi", 3);
		model.addAttribute("kelengkapan_klaim", Utils.lstFormClaim(true));
		model.addAttribute("file_category", Utils.lstFormClaim(false));
		return "input_claim_edit";
	}

	//saat user menekan tombol save baik dari layar input maupun layar edit
		@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
		public String save(@ModelAttribute("claim") Claim claim, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
			logger.debug("Halaman:  Master User, method: SAVE");
			
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			
			//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
			if (!result.hasErrors()) {
				BindException errors = new BindException(result);
				
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jumlah_klaim", "", messageSource.getMessage("NotEmpty", new String[]{"Jumlah Klaim"},null));
				
				if(claim.mode.equals("VALIDASI")){
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "statusClaim", "", messageSource.getMessage("NotEmpty", new String[]{""},null));
				}else if(claim.mode.equals("AKSEPTASI")){
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "statusClaim", "", messageSource.getMessage("NotEmpty", new String[]{""},null));
				}
				
				if (!result.hasErrors()) {
					if(claim.mode.equals("AKSEPTASI")){
						if(claim.statusClaim==1)
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jumlah_bayar", "", messageSource.getMessage("NotEmpty", new String[]{"Jumlah Bayar"},null));
					}
					
					if(claim.uploadFile.getSize() == 0 && 	claim.mode.equals("NEW")){
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fileCategory", "", messageSource.getMessage("NotEmpty", new String[]{"Kategori File"},null));
						
						errors.rejectValue("uploadFile", "NotEmpty", new String[]{""}, "");
					}else if(claim.uploadFile.getSize() != 0){	
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fileCategory", "", messageSource.getMessage("NotEmpty", new String[]{"Kategori File"},null));
						
						if (!result.hasErrors()) {
							String filetype=claim.uploadFile.getOriginalFilename();
						
							if(!filetype.toLowerCase().contains(".gif")&&!filetype.toLowerCase().contains(".pdf")){
								errors.rejectValue("uploadFile",null, "Harap upload logo dengan format *.gif atau *.pdf");
							}else if(claim.uploadFile.getSize() > 500000){
								errors.rejectValue("uploadFile",null, "Maksimal upload 500kb");
							}
						}
					}
				}
			}

			//bila ada error, kembalikan ke halaman edit
			if (result.hasErrors()) {
				List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
				for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\3")){
					HashMap<String, Object> add=new HashMap<String, Object>();
					add.put("key", dd.key);
					add.put("encrypt", EncryptUtils.encodeURL("3~"+dd.key));
					daftarfile.add(add);
				}
				model.addAttribute("daftarFile",daftarfile);
				model.addAttribute("asuransi", 3);
				model.addAttribute("kelengkapan_klaim", Utils.lstFormClaim(true));
				model.addAttribute("file_category", Utils.lstFormClaim(false));
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("messageType", "error");
				map.put("message", messageSource.getMessage("ErrorForm", null,null));
				model.addAllAttributes(map);
				
				return "input_claim_edit";
			}

			//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
			String pesan ="";
			try{
				pesan = dbService.saveClaim(claim, currentUser);
				
				if(claim.uploadFile.getSize() != 0){
					//simpan FIle
					String []s=claim.uploadFile.getOriginalFilename().split("\\.");
					//nama file yang ingin disimpan
					String filename=claim.fileCategory+"_"+claim.id;
					//jumlah coloumn data yang ingin di proses
					
					
					//buat directory di server bila belum ada
					String path = props.getProperty("dir.klaim.upload")+"\\"+claim.id+"\\";
					File directory = new File(path);
					if(!directory.exists()) directory.mkdirs();
					
					boolean repeat=true;
					Integer count=1;
					
					do{			
						if(count==1){
							if(Utils.isFileExist(path + filename+"."+s[1])){
//								filename+="_"+count;
								
							}else{
								repeat=false;
								filename+="_"+count;
							}
						}else{
							if(Utils.isFileExist(path + filename+"_"+count+"."+s[1])){
//								filename+="_"+count;
								
							}else{
								repeat=false;
								filename+="_"+count;
							}
						}
						
						count++;
					}while(repeat);
					
					filename+="."+s[1];
					//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
					Date sekarang = dbService.selectSysdate();
					File file = new File(path + filename);
					FileUtils.writeByteArrayToFile(file, claim.uploadFile.getBytes());
			
				}
				//balikin ke layar list input
			}catch (Exception e) {
				pesan="Proses Klaim Gagal dilakukan";

				email.send(
						props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
						"ERROR pada maibro (Proses Klaim)", e.toString());
				
			}
			ra.addFlashAttribute("pesan", pesan);
			return "redirect:/klaim/"+claim.pagename; 
			
			
		}
		
		
		
		//user menekan tombol transfer
		@RequestMapping(value="/tranfer/{id}/{pagename}", method=RequestMethod.GET)
		public String transfer(@PathVariable Integer id, RedirectAttributes ra, HttpServletRequest request,@PathVariable String pagename) throws MailException, MessagingException {
			logger.debug("Halaman: Micro Transfer, method: transfer");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				Claim claim=dbService.selectClaim(id);
				claim.setMode("TRANSFER");			
				
				pesan=dbService.saveClaim(claim, currentUser);
			}catch (Exception e) {
				e.printStackTrace();
				pesan="Polis gagal di transfer";

				email.send(
						props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
						"ERROR pada maibro (Transfer Klaim)", e.toString());
			}
			ra.addFlashAttribute("pesan", pesan);
			
			return "redirect:/klaim/"+pagename;
		}
		
		
		//user menekan tombol delete file
		@RequestMapping(value="/deleteFileKlaim/{id}/{pagename}", method=RequestMethod.GET)
		public String deleteFileKlaim(@PathVariable Integer id, RedirectAttributes ra, HttpServletRequest request,@PathVariable String pagename) throws MailException, MessagingException {
			logger.debug("Halaman: Micro Transfer, method: transfer");
			String pesan ="";
			try{
				//currently logged in user
				String location=ServletRequestUtils.getStringParameter(request, "loc",null);
				if(!Utils.isEmpty(location)) {
					location=EncryptUtils.decrypt(location.replace(" ", "+"));					
					String [] split=location.split("~");
					Utils.deleteFile(props.getProperty("dir.klaim.upload")+"\\"+split[0], split[1], null);
					pesan="File Klaim "+split[1]+" berhasil di hapus";
				}else{
				
					pesan="File Klaim tidak ditemukan";
				}
			}catch (Exception e) {
				e.printStackTrace();
				pesan="File gagal di delete";

				email.send(
						props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
						"ERROR pada maibro (Proses Delete File Klaim)", e.toString());
				
			}
			ra.addFlashAttribute("pesan", pesan);
			
			return "redirect:/klaim/edit/"+id+"/"+pagename;
		}
	
}
