package com.maibro.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.com.bytecode.opencsv.CSVReader;

import com.google.gson.Gson;
import com.maibro.model.DropDown;
import com.maibro.model.GroupMenu;
import com.maibro.model.GroupPolicy;
import com.maibro.model.History;
import com.maibro.model.MstProduct;
import com.maibro.model.MstRate;
import com.maibro.model.Policy;
import com.maibro.model.Product;
import com.maibro.model.Upload;
import com.maibro.model.User;
import com.maibro.utils.EncryptUtils;
import com.maibro.utils.Utils;
import com.maibro.validator.AkseptasiValidator;
import com.maibro.validator.GroupPolicyValidator;
import com.maibro.validator.PolicyMicroValidator;
import com.maibro.validator.RateValidator;
import com.maibro.validator.UploadValidator;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Feb 6, 2013 2:04:18 PM
 * @Description : MultiActionController untuk upload page
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
public class UploadController extends ParentController{
	
	
	

	@Autowired
	protected GroupPolicyValidator groupPolicyValidator;
	
	@Autowired
	protected AkseptasiValidator akseptasiValidator;
	

	public void setGroupPolicyValidator(GroupPolicyValidator groupPolicyValidator) {
		this.groupPolicyValidator = groupPolicyValidator;
	}

	
	public void setAkseptasiValidator(AkseptasiValidator akseptasiValidator) {
		this.akseptasiValidator = akseptasiValidator;
	}


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new UploadValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	}
	
	//Reference Data
	@ModelAttribute("reff")
	public Map<String, Object> reff(HttpServletRequest request){
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listProduct", dbService.selectDropDown("id", "nama", "mst_product", "active=1", "nama"));
		
		String filter_bank=currentUser.getBank_id()==1?"":"and mst_bank_id ="+currentUser.getBank_id();
		map.put("listProductLife", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=3 "+filter_bank, "nama"));
		map.put("listProductFire", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=2 "+filter_bank, "nama"));
		map.put("listProductKPRLife", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=1 "+filter_bank, "nama"));
		map.put("listBank", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=1", "nama"));
		map.put("listRelasi", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=2 and active=1", "keterangan"));
		map.put("listManfaat", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=7 and active=1", "keterangan"));
		map.put("JenisRate", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=11 and active=1", "keterangan"));
		
		map.put("listFileUpload", dbService.selectDropDown("createdate", "keterangan", "history", "jenis=2", "createdate desc"));
		map.put("listFileUploadkprLife", dbService.selectDropDown("createdate", "keterangan", "history", "jenis=5", "createdate desc"));
		map.put("listFileUploadkprFire", dbService.selectDropDown("createdate", "keterangan", "history", "jenis=6", "createdate desc"));
		map.put("listFileUploadmicro", dbService.selectDropDown("createdate", "keterangan", "history", "jenis=7", "createdate desc"));
		return map;
	}

	
	@RequestMapping(value= {"/micro/input/upload"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String micropolis(@ModelAttribute("upload")Upload upload,HttpServletRequest request) {
		logger.debug("Halaman: Micro Upload");
		upload.setJenisUpload(1);
		upload.setImportStartLine(2);
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,3,currentUser.getBank_id(),null);
		if(!lsProduk.isEmpty())
		upload.setMst_product_id(lsProduk.get(0).id);
		return "upload_micro_polis";
	}

	//contoh upload file
	@RequestMapping(value= {"/micro/input/upload"}, method=RequestMethod.POST) //mapping request saat user submit /upload ke function ini (POST)
	public String processMicroPolis(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra) throws IOException{
		logger.debug("Halaman: PROCESS Micro Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
		
			//nama file yang ingin disimpan
			String filename=Utils.convertDateToString(dbService.selectSysdate(), "ddMMyyyyHHmmss")+".csv";
			//jumlah coloumn data yang ingin di proses
			Integer colomnSize=19;
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.micro.polis")+"\\"+currentUser.bank_id+"\\"+currentUser.cab_bank_id+"\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
			Date sekarang = dbService.selectSysdate();
			File file = null;
			//copy file ke server
			try {
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
			
			
			//baca filenya
			//baca filenya
			CSVReader reader = null;
			
			if(errorMessage.isEmpty()){
				try {
					
					reader = new CSVReader(new FileReader(file), ',');
					
					int colCount1 = colomnSize; //jumlah kolom harus 19 dealref, nama, tempatlahir, tgllahir, tglrealisasi, pekerjaan, lamabulan, rateasuransi, premiasuransi, plafondkredit, goldebitur, kodecabang, username, alamat1, alamat2, alamat3, alamat4, alamat5, kodepos
					int colCount2 = 0;
					String[] nextLine;
					int baris=1;
					List<GroupPolicy> lsGroupPolis=new ArrayList<GroupPolicy>();
					
					while ((nextLine = reader.readNext()) != null) {
						if(baris>=upload.importStartLine){ //bila belum start baris, skip saja
							colCount2 = nextLine.length;
							
							if(colCount2 < colCount1){ //bila ada kolom yg error, kasih pesan
								errorMessage.add(" (filename= " + upload.uploadFile.getOriginalFilename() + ")\n Jumlah kolom pada baris ke-"+baris+" tidak sama dengan requirement");
								break;
							}else{ //selain itu tambahkan daftar insert
								GroupPolicy groupPolicy=new GroupPolicy();
								groupPolicy.microCheck=true;
								groupPolicy.createby=currentUser.id;
								groupPolicy.mode="NEW";
								
								try{
									groupPolicy.polisMicro=new Policy(nextLine[0], nextLine[1], nextLine[2], Utils.convertStringToDate2(Utils.reformatDate_ddmmyyyy(nextLine[3], "/"), "dd/MM/yyyy"), Utils.convertStringToDate2(Utils.reformatDate_ddmmyyyy(nextLine[4], "-"), "dd/MM/yyyy"),
											nextLine[5], Integer.parseInt(nextLine[6]), new Double(nextLine[7]), new Double(nextLine[8]), new Double(nextLine[9]), nextLine[10], nextLine[11], nextLine[12], nextLine[13], 
											nextLine[14], nextLine[15], nextLine[16], nextLine[17], nextLine[18]);
									groupPolicy.polisMicro.getProduct().mst_product_id=upload.mst_product_id;
									groupPolicy.polisMicro.getBank().mst_bank_id=currentUser.bank_id;
									groupPolicy.setFileUpload(upload.uploadFile.getOriginalFilename());
								}catch (ParseException e) {
									e.printStackTrace();
									errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n Format kolom salah pada baris ke-"+baris+" :\n"+e.getMessage()) ;
									break;
								}
								
								//validasi
								 
								 DataBinder binder = new DataBinder(groupPolicy);
								 binder.setValidator(this.groupPolicyValidator);
								 binder.validate();
								 
								 if (binder.getBindingResult().hasErrors()) {
									 errorMessage.add(" (filename= " + upload.uploadFile.getOriginalFilename() + ")\n pada baris ke-"+baris+"<br/>");
									 errorMessage.addAll(Utils.errorBinderToList(binder.getBindingResult(), messageSource));
									 break;
								 }else{//kalau tidak ada error add ke list
									 lsGroupPolis.add(groupPolicy);
								 }
								 
								 
							}
							
						}
						baris++;
					}
					
					
					
					//kalo tidak ada error 
					if (!result.hasErrors()&&errorMessage.isEmpty()) {
						//save
						dbService.saveGroupPolicy(2,lsGroupPolis, currentUser,2);
						
						
						
						//kasih pesan
						String pesan = "File [" + upload.uploadFile.getOriginalFilename() + "] berhasil diupload, jumlah data yang diproses = "+(baris-1);
						ra.addFlashAttribute("pesan", pesan);
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") File tidak dapat dibackup.\n") ;
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") File gagal diproses") ;
				}catch (DataAccessException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") Gagal di simpan ke database") ;
				}
				
				
			}
			
			//tutup file nya
			try {reader.close();} catch (IOException e) {e.printStackTrace();}
			
			
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_micro_polis";
		}
		
		
		return "redirect:/micro/upload";
	}
	
	
	@RequestMapping(value= {"/rate/upload/{prod_id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String rate(@ModelAttribute("upload")Upload upload,@PathVariable Integer prod_id) {
		logger.debug("Halaman: Rate Upload");
		upload.setJenisUpload(2);
		upload.setImportStartLine(2);
		upload.setMst_product_id(prod_id);
		return "upload_rate";
	}

	//contoh upload file
	@RequestMapping(value= {"/rate/uploadProses/{prod_id}"}, method={RequestMethod.GET, RequestMethod.POST}) //mapping request saat user submit /upload ke function ini (POST)
	public String processRate(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable Integer prod_id) throws IOException{
		logger.debug("Halaman: PROCESS Rate Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
		
			//nama file yang ingin disimpan
			String filename=Utils.convertDateToString(dbService.selectSysdate(), "ddMMyyyyHHmmss")+".csv";
			//jumlah coloumn data yang ingin di proses
			Integer colomnSize=4;
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.rate.polis")+"\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
			Date sekarang = dbService.selectSysdate();
			File file = null;
			//copy file ke server
			try {
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
		
			//baca filenya
			CSVReader reader = null;
			
			if(errorMessage.isEmpty()){
				try {
					
					reader = new CSVReader(new FileReader(file), ',');
					
					int colCount1 = colomnSize; //jumlah kolom harus 4  tenor, umur, jenis_bangunan, rate
					int colCount2 = 0;
					String[] nextLine;
					int baris=1;
					List<MstRate> lsMstRate=new ArrayList<MstRate>();
					
					while ((nextLine = reader.readNext()) != null) {
						if(baris>=upload.importStartLine){ //bila belum start baris, skip saja
							colCount2 = nextLine.length;
							
							if(colCount2 < colCount1){ //bila ada kolom yg error, kasih pesan
								errorMessage.add(" (filename= " + upload.uploadFile.getOriginalFilename() + ")\n Jumlah kolom pada baris ke-"+baris+" tidak sama dengan requirement");
								break;
							}else{ //selain itu tambahkan daftar insert
								MstRate mstRate=new MstRate();
								
								try{
									mstRate=new MstRate(Utils.isEmpty(nextLine[0])?null:Integer.parseInt(nextLine[0]), Utils.isEmpty(nextLine[1])?null:Integer.parseInt(nextLine[1]), Utils.isEmpty(nextLine[2])?null:Integer.parseInt(nextLine[2]), Utils.isEmpty(nextLine[3])?null:new Double(nextLine[3]), upload.mst_product_id);
									mstRate.createby=currentUser.id;
									mstRate.jenis_rate=1;//dianggap rate biasa( bukan single rate)
									mstRate.mode="NEW";
								}catch (Exception e) {
									e.printStackTrace();
									errorMessage.add( "(filename= " + upload.uploadFile.getOriginalFilename() + ")\n Format kolom salah pada baris ke-"+baris+" :\n"+e.getMessage()) ;
									break;
								}
								
								//validasi
								 
								 DataBinder binder = new DataBinder(mstRate);
								 binder.setValidator(new RateValidator());
								 binder.validate();
								 
								 if (binder.getBindingResult().hasErrors()) {
									 errorMessage.add(" (filename= " + upload.uploadFile.getOriginalFilename() + ")\n pada baris ke-"+baris+"<br/>");
									 errorMessage.addAll(Utils.errorBinderToList(binder.getBindingResult(), messageSource));
									 break;
								 }else{//kalau tidak ada error add ke list
									 lsMstRate.add(mstRate);
								 }
								 
								 
							}
							
						}
						baris++;
					}
					
					//kalo tidak ada error 
					if (!result.hasErrors()&&errorMessage.isEmpty()) {
						//save
						dbService.saveMstRate(lsMstRate, currentUser);
						//kasih pesan
						String pesan = "File [" + upload.uploadFile.getOriginalFilename() + "] berhasil diupload, jumlah data yang diproses = "+(baris-1);
						ra.addFlashAttribute("pesan", pesan);
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") File tidak dapat dibackup.\n") ;
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") File gagal diproses") ;
				}catch (DataAccessException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") Gagal di simpan ke database") ;
				}
				
			}
			
			//tutup file nya
			try {reader.close();} catch (IOException e) {e.printStackTrace();}
			
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_rate";
		}
		
		
		return "redirect:/master/rate/"+upload.mst_product_id;
	}
	
	@RequestMapping(value= {"/{product}/upload/kps/{id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String microKps(@ModelAttribute("upload")Upload upload,HttpServletRequest request,Model model,@PathVariable Integer id,@PathVariable String product) {
		logger.debug("Halaman: Rate Upload");
		upload.setJenisUpload(8);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Integer jenis=null;
		String namapage="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload KPS Micro Life";
			upload.setJenisUpload(12);
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload KPS KPR Life";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload KPS KPR Fire";
		}
		
		Integer filter_bank=currentUser.getBank_id()==1?null:+currentUser.getBank_id();
		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,jenis,filter_bank,null);
		if(!lsProduk.isEmpty())
		upload.setMst_product_id(lsProduk.get(0).id);
		
		upload.setPolicy(dbService.selectPolicy(id, null, null, null));
		upload.getPolicy().setTgl_aksep(dbService.selectSysdate());
		model.addAttribute("spaj_no",upload.getPolicy().getSpaj_no() );
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		return "upload_kps";
	}

	//contoh upload file
	@RequestMapping(value= {"/{product}/upload/kps/{id}"}, method=RequestMethod.POST) //mapping request saat user submit /upload ke function ini (POST)
	public String processMicroKps(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable Integer id,@PathVariable String product) throws IOException{
		logger.debug("Halaman: PROCESS Rate Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
//		logger.debug(upload.uploadFile.getContentType());
//		logger.debug(upload.uploadFile.getOriginalFilename());
		
		Integer jenis=null;
		String namapage="";
		String redirect="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload KPS Micro Life";
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload KPS KPR Life";
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload KPS KPR Fire";
			redirect="kpr";
		}
		
		model.addAttribute("spaj_no", dbService.selectPolicy(id, null, null, null).getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
			Policy tmpPolicy=dbService.selectPolicy(id, null, null, null);
			tmpPolicy.setBank(dbService.selectBank(id));
			//upload.policy=dbService.selectPolicy(id, null, null, null);
			//upload.policy.setBank(dbService.selectBank(id));
			//nama file yang ingin disimpan
			String filename="KPS_"+tmpPolicy.getSpaj_no()+".pdf";
//			String []s=upload.uploadFile.getOriginalFilename().split("\\.");
//			String filetype=s[1];
			//jumlah coloumn data yang ingin di proses
			
			
			
			//buat directory di server bila belum ada
			
			if(upload.jenisUpload!=25){
				String path = props.getProperty("upload.polis")+"\\"+tmpPolicy.getBank().mst_bank_id+"\\"+tmpPolicy.getBank().mst_cab_bank_id+"\\"+tmpPolicy.getSpaj_no()+"\\";
				File directory = new File(path);
				if(!directory.exists()) directory.mkdirs();
				
				
				//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
		
				File file = null;
				//copy file ke server
				try {
					file = new File(path + filename);
					FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
				} 
			}
				
			//kalo tidak ada error 
			if (!result.hasErrors()&&errorMessage.isEmpty()) {
				//save
				//currently logged in user
				Policy policy=upload.policy;
				policy.setJenis(jenis);
				policy.setProduct(new Product());
				policy.getProduct().rate=policy.getRate();
				policy.getProduct().premi_pokok=policy.getPremi();
				policy.getProduct().premi=policy.getTotalpremi();
				policy.getProduct().premi_extra=policy.getExtrapremi();
				policy.setMode("AKSEPTASI");
				String pesan = "";
				if(upload.jenisUpload!=25)pesan="File [" + upload.uploadFile.getOriginalFilename() + "] berhasil diupload &";
				if(policy.getJenis()==1)
					pesan+=dbService.savePolisAJK(policy, currentUser);
				else if(policy.getJenis()==2)
					pesan+=dbService.savePolicyFire(policy, currentUser);
				else if(policy.getJenis()==3)
					pesan+=dbService.savePolicyMicro(policy, currentUser);
				
				 /*policy=dbService.selectPolicy(id,null,null,null);
				policy.setMode("TRANSFER");
				if(policy.getJenis()==1)
					pesan+=dbService.savePolisAJK(policy, currentUser);
				else if(policy.getJenis()==2)
					pesan+=dbService.savePolicyFire(policy, currentUser);
				else if(policy.getJenis()==3)
					pesan+=dbService.savePolicyMicro(policy, currentUser);
				
				pesan+=" ke Posisi Payment";*/
				//kasih pesan
				
				ra.addFlashAttribute("pesan", pesan);
			}
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_kps";
		}
		
		return "redirect:/"+redirect+"/kps";
	}
	
	@RequestMapping(value= {"/micro/upload/klaim"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String microKlaim(@ModelAttribute("upload")Upload upload,@PathVariable Integer id) {
		logger.debug("Halaman: Klaim Upload");
		upload.setJenisUpload(3);
		upload.setImportStartLine(1);
		upload.setMst_product_id(id);
		return "upload_rate";
	}

	//contoh upload file
	@RequestMapping(value= {"/micro/uploadProses/klaim/{id}"}, method={RequestMethod.GET, RequestMethod.POST}) //mapping request saat user submit /upload ke function ini (POST)
	public String processmicroKlaim(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable Integer id) throws IOException{
		logger.debug("Halaman: PROCESS Klaim Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
		
			//nama file yang ingin disimpan
			String filename=upload.uploadFile.getOriginalFilename();
			//jumlah coloumn data yang ingin di proses
			Integer colomnSize=4;
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.micro.polis")+"\\"+currentUser.bank_id+"\\"+currentUser.cab_bank_id+"\\"+upload.policy.getId()+"\\klaim\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
			Date sekarang = dbService.selectSysdate();
			File file = null;
			//copy file ke server
			try {
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
		
			
			
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_rate";
		}
		
		
		return "redirect:/micro/upload/kps/"+id;
	}
	
	
	@RequestMapping(value= {"/{product}/akseptasi/upload"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String akseptasi(@ModelAttribute("upload")Upload upload,HttpServletRequest request,Model model,@PathVariable String product) {
		logger.debug("Halaman: Akseptasi Upload "+product);
		upload.setJenisUpload(4);
		upload.setImportStartLine(2);
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Integer jenis=null;
		String namapage="";
		String redirect="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload Akseptasi Micro Life";
			 redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload Akseptasi KPR Life";
			 redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload Akseptasi KPR Fire";
			 redirect="kpr";
		}	
		
		Integer filter_bank=currentUser.getBank_id()==1?null:+currentUser.getBank_id();
		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,jenis,filter_bank,null);
		if(!lsProduk.isEmpty())
		upload.setMst_product_id(lsProduk.get(0).id);
		
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("redirect", redirect);
		model.addAttribute("jenis", jenis);
		
		return "upload_akseptasi";
	}

	//contoh upload file
	@RequestMapping(value= {"/{product}/akseptasi/upload"}, method=RequestMethod.POST) //mapping request saat user submit /upload ke function ini (POST)
	public String prosesAkseptasi(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable String product) throws IOException{
		logger.debug("Halaman: PROCESS Akseptasi Upload "+product);
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
		List<String> errorMessage = new ArrayList<String>();
		Integer jenis=null;
		String namapage="";
		String redirect="";
		Integer colomnSize=19;
		
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload Akseptasi Micro Life";
			colomnSize=29;
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload Akseptasi KPR Life";
			colomnSize=29;
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			colomnSize=35;
			namapage="Upload Akseptasi KPR Fire";
			redirect="kpr";
		}
		
		if(!result.hasErrors()){
		
			//nama file yang ingin disimpan
			String filename=Utils.convertDateToString(dbService.selectSysdate(), "ddMMyyyyHHmmss")+".csv";
			//jumlah coloumn data yang ingin di proses
			
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.akseptasi")+"\\"+product+"\\"+currentUser.bank_id+"\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
			Date sekarang = dbService.selectSysdate();
			File file = null;
			//copy file ke server
			try {
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
			
			
			
						
			//baca filenya			
			CSVReader reader = null;
		
			
			if(errorMessage.isEmpty()){
				try {
					
					reader = new CSVReader(new FileReader(file), ',');
					
					int colCount1 = colomnSize; 
					int colCount2 = 0;
					String[] nextLine;
					int baris=1;
					List<GroupPolicy> lsGroupPolis=new ArrayList<GroupPolicy>();
					
					while ((nextLine = reader.readNext()) != null) {
						if(baris>=upload.importStartLine){ //bila belum start baris, skip saja
							colCount2 = nextLine.length;
							
							if(colCount2 < colCount1){ //bila ada kolom yg error, kasih pesan
								errorMessage.add(" (filename= " + upload.uploadFile.getOriginalFilename() + ")\n Jumlah kolom pada baris ke-"+baris+" tidak sama dengan requirement");
								break;
							}else{ //selain itu tambahkan daftar insert
								GroupPolicy groupPolicy=new GroupPolicy();
								if(jenis==1){
									groupPolicy.lifeCheck=true;
								}else if(jenis==2){
									groupPolicy.fireCheck=true;
								}else if(jenis==3){
									groupPolicy.microCheck=true;
								}
								
								groupPolicy.createby=currentUser.id;
								groupPolicy.mode="AKSEPTASI";
								
								try{
									if(jenis==1 | jenis == 3)
									groupPolicy.polisMicro=new Policy(nextLine[1], Utils.isEmpty(nextLine[20])?null:Integer.parseInt(nextLine[20]),nextLine[21], Utils.isEmpty(nextLine[22])?null:Utils.convertStringToDate2(Utils.reformatDate_ddmmyyyy(nextLine[22], "/"), "dd/MM/yyyy"), Utils.isEmpty(nextLine[23])?null:new Double(nextLine[23]), Utils.isEmpty(nextLine[24])?null:new Double(nextLine[24]),Utils.isEmpty(nextLine[25])?null:new Double( nextLine[25]),null, Utils.isEmpty(nextLine[26])?null:new Double(nextLine[26]), nextLine[27]);
									else groupPolicy.polisMicro=new Policy(nextLine[1], Utils.isEmpty(nextLine[26])?null:Integer.parseInt(nextLine[26]),nextLine[27], Utils.isEmpty(nextLine[28])?null:Utils.convertStringToDate2(Utils.reformatDate_ddmmyyyy(nextLine[28], "/"), "dd/MM/yyyy"), Utils.isEmpty(nextLine[29])?null:new Double(nextLine[29]), Utils.isEmpty(nextLine[30])?null:new Double(nextLine[30]),Utils.isEmpty(nextLine[31])?null:new Double( nextLine[31]),null, Utils.isEmpty(nextLine[32])?null:new Double(nextLine[32]), nextLine[33]);
									
									groupPolicy.polisMicro.getProduct().mst_product_id=upload.mst_product_id;
									groupPolicy.polisMicro.getBank().mst_bank_id=currentUser.bank_id;
									groupPolicy.polisMicro.setJenis(jenis);
									groupPolicy.setFileUpload(upload.uploadFile.getOriginalFilename());
								}catch (ParseException e) {
									e.printStackTrace();
									errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n Format kolom salah pada baris ke-"+baris+" :\n"+e.getMessage()) ;
									break;
								}
								
								//validasi
								 
								 DataBinder binder = new DataBinder(groupPolicy.polisMicro);
								 binder.setValidator(this.akseptasiValidator);
								 binder.validate();
								 
								 if (binder.getBindingResult().hasErrors()) {
									 errorMessage.add(" (filename= " + upload.uploadFile.getOriginalFilename() + ")\n pada baris ke-"+baris+"<br/>");
									 errorMessage.addAll(Utils.errorBinderToList(binder.getBindingResult(), messageSource));
									 break;
								 }else{//kalau tidak ada error add ke list
									 lsGroupPolis.add(groupPolicy);
								 }
								 
								 
							}
							
						}
						baris++;
					}
					
					
					
					//kalo tidak ada error 
					if (!result.hasErrors()&&errorMessage.isEmpty()) {
						//save
						dbService.saveGroupPolicy(2,lsGroupPolis, currentUser,4+jenis);
						
						//kasih pesan
						String pesan = "File [" + upload.uploadFile.getOriginalFilename() + "] berhasil diupload, jumlah data yang diproses = "+(baris-1);
						ra.addFlashAttribute("pesan", pesan);
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") File tidak dapat dibackup.\n") ;
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") File gagal diproses") ;
				}catch (DataAccessException e) {
					e.printStackTrace();
					errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ") Gagal di simpan ke database") ;
				}
				
				
			}
			
			//tutup file nya
			try {reader.close();} catch (IOException e) {e.printStackTrace();}
			
			
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			map.put("redirect", redirect);
			map.put("product", product);
			map.put("namapage", namapage);
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_akseptasi";
		}
		
		
		return "redirect:/"+redirect+"/akseptasi";
	}
	
	@RequestMapping(value= {"/{product}/upload/spaj/{id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String uploadSPAJ(@ModelAttribute("upload")Upload upload,HttpServletRequest request,Model model,@PathVariable Integer id,@PathVariable String product) {
		logger.debug("Halaman: SPAJ Upload");
		upload.setJenisUpload(9);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Integer jenis=null;
		String redirect="";
		String namapage="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload SPAJ Micro Life";
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload SPAJ KPR Life";
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload SPAK KPR Fire";
			redirect="kpr";
		}
		
		Integer filter_bank=currentUser.getBank_id()==1?null:+currentUser.getBank_id();
		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,jenis,filter_bank,null);
		if(!lsProduk.isEmpty())
		upload.setMst_product_id(lsProduk.get(0).id);
		
		model.addAttribute("spaj_no", dbService.selectPolicy(id, null, null, null).getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("redirect", redirect);
		model.addAttribute("jenis", jenis);
		return "upload_spaj";
	}

	//contoh upload file
	@RequestMapping(value= {"/{product}/upload/spaj/{id}"}, method=RequestMethod.POST) //mapping request saat user submit /upload ke function ini (POST)
	public String processUploadSPAJ(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable Integer id,@PathVariable String product) throws IOException{
		logger.debug("Halaman: PROCESS SPAJ Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
		Integer jenis=null;
		String namapage="";
		String redirect="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload SPAJ Micro Life";
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload SPAJ KPR Life";
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload SPAJ KPR Fire";
			redirect="kpr";
		}
		
		model.addAttribute("spaj_no", dbService.selectPolicy(id, null, null, null).getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
			upload.policy=dbService.selectPolicy(id, null, null, null);
			upload.policy.setBank(dbService.selectBank(id));
			//nama file yang ingin disimpan
			String filename="SPAJ_"+upload.policy.getSpaj_no();
			if(product.equals("kprfire")){
				filename="SPAK_"+upload.policy.getSpaj_no();
			}
			String []s=upload.uploadFile.getOriginalFilename().split("\\.");
//			String filetype=s[1];
			//jumlah coloumn data yang ingin di proses
			
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.polis")+"\\"+upload.policy.getBank().mst_bank_id+"\\"+upload.policy.getBank().mst_cab_bank_id+"\\"+upload.policy.getSpaj_no()+"\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
	
			File file = null;
			//copy file ke server
			try {
				file = new File(path + filename+"."+s[1]);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
			//kalo tidak ada error 
			if (!result.hasErrors()&&errorMessage.isEmpty()) {
				//save
				//currently logged in user
				
				Policy policy=dbService.selectPolicy(id,null,null,null);
				
				Policy tmp=new Policy();
				tmp.setId(policy.getId());
				tmp.setTgl_upload_spaj(dbService.selectSysdate());
				tmp.setFiletype_spaj(s[1]);
				dbService.updatePolicy(tmp);
				dbService.insertHistory(new History(1, "Upload SPAJ File :"+filename, currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
				
				String pesan="Upload SPAJ Berhasil";
				
				if(product.equals("kprfire")){
					pesan="Upload SPAK Berhasil";
				}
				
				ra.addFlashAttribute("pesan", pesan);
			}
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_spaj";
		}
		
		return "redirect:/"+redirect+"/input";
	}
	
	@RequestMapping(value= {"/{product}/upload/kuesioner/{id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
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
		return "upload_kuesioner";
	}

	//contoh upload file
	@RequestMapping(value= {"/{product}/upload/kuesioner/{id}"}, method=RequestMethod.POST) //mapping request saat user submit /upload ke function ini (POST)
	public String processUploadkuesioner(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable Integer id,@PathVariable String product) throws IOException{
		logger.debug("Halaman: PROCESS Kuesioner Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
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
		
		model.addAttribute("spaj_no", dbService.selectPolicy(id, null, null, null).getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
			upload.policy=dbService.selectPolicy(id, null, null, null);
			upload.policy.setBank(dbService.selectBank(id));
			//nama file yang ingin disimpan
			String filename="KUESIONER_"+upload.policy.getSpaj_no();
			String []s=upload.uploadFile.getOriginalFilename().split("\\.");
//			String filetype=s[1];
			//jumlah coloumn data yang ingin di proses
			
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.polis")+"\\"+upload.policy.getBank().mst_bank_id+"\\"+upload.policy.getBank().mst_cab_bank_id+"\\"+upload.policy.getSpaj_no()+"\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			boolean repeat=true;
			Integer count=1;
			
			do{			
				if(count==1){
					if(Utils.isFileExist(path + filename+"."+s[1])){
//						filename+="_"+count;
						
					}else{
						repeat=false;
						//filename+="_"+count;
					}
				}else{
					if(Utils.isFileExist(path + filename+"_"+count+"."+s[1])){
//						filename+="_"+count;
						
					}else{
						repeat=false;
						filename+="_"+count;
					}
				}
				
				count++;
			}while(repeat);
			
			filename+="."+s[1];
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
	
			File file = null;
			
			//copy file ke server
			try {
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
			//kalo tidak ada error 
			if (!result.hasErrors()&&errorMessage.isEmpty()) {
				//save
				//currently logged in user
				
				Policy policy=dbService.selectPolicy(id,null,null,null);
				
				Policy tmp=new Policy();
				tmp.setId(policy.getId());
				tmp.setTgl_upload_kuesioner(dbService.selectSysdate());
				tmp.setFiletype_kuesioner(s[1]);
				dbService.updatePolicy(tmp);
				dbService.insertHistory(new History(1, "Upload Kuesioner File :"+filename, currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
				
				String pesan="Upload Kuesioner Berhasil";
				
				
				ra.addFlashAttribute("pesan", pesan);
			}
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_kuesioner";
		}
		
		return "redirect:/"+redirect+"/input";
	}
	
	@RequestMapping(value= {"/{product}/upload/ktp/{id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String uploadKTP(@ModelAttribute("upload")Upload upload,HttpServletRequest request,Model model,@PathVariable Integer id,@PathVariable String product) {
		logger.debug("Halaman: KTP Upload");
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
		return "upload_ktp";
	}

	//contoh upload file
	@RequestMapping(value= {"/{product}/upload/ktp/{id}"}, method=RequestMethod.POST) //mapping request saat user submit /upload ke function ini (POST)
	public String processUploadKTP(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable Integer id,@PathVariable String product) throws IOException{
		logger.debug("Halaman: PROCESS KTP Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
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
		
		model.addAttribute("spaj_no", dbService.selectPolicy(id, null, null, null).getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
			upload.policy=dbService.selectPolicy(id, null, null, null);
			upload.policy.setBank(dbService.selectBank(id));
			//nama file yang ingin disimpan
			String filename="KTP_"+upload.policy.getSpaj_no();
			String []s=upload.uploadFile.getOriginalFilename().split("\\.");
//			String filetype=s[1];
			//jumlah coloumn data yang ingin di proses
			
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.polis")+"\\"+upload.policy.getBank().mst_bank_id+"\\"+upload.policy.getBank().mst_cab_bank_id+"\\"+upload.policy.getSpaj_no()+"\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			boolean repeat=true;
			Integer count=1;
			
			do{			
				if(count==1){
					if(Utils.isFileExist(path + filename+"."+s[1])){
//						filename+="_"+count;
						
					}else{
						repeat=false;
						//filename+="_"+count;
					}
				}else{
					if(Utils.isFileExist(path + filename+"_"+count+"."+s[1])){
//						filename+="_"+count;
						
					}else{
						repeat=false;
						filename+="_"+count;
					}
				}
				
				count++;
			}while(repeat);
			
			filename+="."+s[1];
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
	
			File file = null;
			
			//copy file ke server
			try {
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
			//kalo tidak ada error 
			if (!result.hasErrors()&&errorMessage.isEmpty()) {
				//save
				//currently logged in user
				
				Policy policy=dbService.selectPolicy(id,null,null,null);
				
				Policy tmp=new Policy();
				tmp.setId(policy.getId());
				tmp.setTgl_upload_ktp(dbService.selectSysdate());
				tmp.setFiletype_ktp(s[1]);
				dbService.updatePolicy(tmp);
				dbService.insertHistory(new History(1, "Upload KTP File :"+filename, currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
				
				String pesan="Upload KTP Berhasil";
				
				
				ra.addFlashAttribute("pesan", pesan);
			}
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_ktp";
		}
		
		return "redirect:/"+redirect+"/input";
	}
	
	@RequestMapping(value= {"/{product}/upload/noclaim/{id}"}, method=RequestMethod.GET) //mapping request saat baru masuk /upload ke function ini (GET)
	public String uploadNoClaim(@ModelAttribute("upload")Upload upload,HttpServletRequest request,Model model,@PathVariable Integer id,@PathVariable String product) {
		logger.debug("Halaman: Subject To No Claim Upload");
		upload.setJenisUpload(112);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Integer jenis=null;
		String namapage="";
		String redirect="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload Subject To No Claim Micro Life";
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload Subject To No Claim KPR Life";
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload Subject To No Claim KPR Fire";
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
			if(dd.key.startsWith("NoClaim_"))
			daftarfile.add(add);
		}
		model.addAttribute("daftarFile",daftarfile);
		return "upload_noclaim";
	}

	//contoh upload file
	@RequestMapping(value= {"/{product}/upload/noclaim/{id}"}, method=RequestMethod.POST) //mapping request saat user submit /upload ke function ini (POST)
	public String processUploadNoClaim(HttpServletRequest request,@Valid @ModelAttribute("upload")Upload upload,BindingResult result, Model model, RedirectAttributes ra,@PathVariable Integer id,@PathVariable String product) throws IOException{
		logger.debug("Halaman: PROCESS Subject To No Claim Upload");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//test saja, berhasil diupload tidak
		logger.debug(upload.uploadFile.getContentType());
		logger.debug(upload.uploadFile.getOriginalFilename());
		
		Integer jenis=null;
		String namapage="";
		String redirect="";
		if(product.equals("micro")){
			jenis=3;
			namapage="Upload Subject To No Claim Micro Life";
			redirect="micro";
		}else if(product.equals("kprlife")){
			jenis=1;
			namapage="Upload Subject To No Claim KPR Life";
			redirect="kpr";
		}else if(product.equals("kprfire")){
			jenis=2;
			namapage="Upload Subject To No Claim KPR Fire";
			redirect="kpr";
		}
		
		model.addAttribute("spaj_no", dbService.selectPolicy(id, null, null, null).getSpaj_no());
		model.addAttribute("product", product);
		model.addAttribute("namapage", namapage);
		model.addAttribute("jenis", jenis);
		
		List<String> errorMessage = new ArrayList<String>();
		if(!result.hasErrors()){
			upload.policy=dbService.selectPolicy(id, null, null, null);
			upload.policy.setBank(dbService.selectBank(id));
			//nama file yang ingin disimpan
			String filename="NoClaim_"+upload.policy.getSpaj_no();
			String []s=upload.uploadFile.getOriginalFilename().split("\\.");
//			String filetype=s[1];
			//jumlah coloumn data yang ingin di proses
			
			
			//buat directory di server bila belum ada
			String path = props.getProperty("upload.polis")+"\\"+upload.policy.getBank().mst_bank_id+"\\"+upload.policy.getBank().mst_cab_bank_id+"\\"+upload.policy.getSpaj_no()+"\\";
			File directory = new File(path);
			if(!directory.exists()) directory.mkdirs();
			boolean repeat=true;
			Integer count=1;
			
			do{			
				if(count==1){
					if(Utils.isFileExist(path + filename+"."+s[1])){
//						filename+="_"+count;
						
					}else{
						repeat=false;
						//filename+="_"+count;
					}
				}else{
					if(Utils.isFileExist(path + filename+"_"+count+"."+s[1])){
//						filename+="_"+count;
						
					}else{
						repeat=false;
						filename+="_"+count;
					}
				}
				
				count++;
			}while(repeat);
			
			filename+="."+s[1];
			
			//buat file di directory yg sudah dibuat diatas, dan diisi dengan data yang sama dgn yg diupload
	
			File file = null;
			
			//copy file ke server
			try {
				file = new File(path + filename);
				FileUtils.writeByteArrayToFile(file, upload.uploadFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage.add( " (filename= " + upload.uploadFile.getOriginalFilename() + ")\n"+Utils.errorExtract(e)) ;
			} 
				
			//kalo tidak ada error 
			if (!result.hasErrors()&&errorMessage.isEmpty()) {
				//save
				//currently logged in user
				
				Policy policy=dbService.selectPolicy(id,null,null,null);
				
				Policy tmp=new Policy();
				tmp.setId(policy.getId());
				tmp.setTgl_upload_no_klaim(dbService.selectSysdate());
				tmp.setFiletype_klaim(s[1]);
				dbService.updatePolicy(tmp);
				dbService.insertHistory(new History(1, "Upload Subject To No Claim File :"+filename, currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
				
				String pesan="Upload Subject To No Claim Berhasil";
				
				
				ra.addFlashAttribute("pesan", pesan);
			}
		}
		
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()||!errorMessage.isEmpty()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			//errorMessage.addAll(Utils.errorBinderToList(result, messageSource));
			map.put("errorList", errorMessage);
			model.addAllAttributes(map);
			return "upload_noclaim";
		}
		
		return "redirect:/"+redirect+"/input";
	}
	
	
}