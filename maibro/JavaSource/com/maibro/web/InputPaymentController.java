package com.maibro.web;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maibro.model.GroupPolicy;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.Utils;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Feb 14, 2013 5:26:28 PM
 * @Description : Input Payment FormController
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/keuangan/input")
public class InputPaymentController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	}

	
	
	@ModelAttribute("reff")
	public Map<String, Object> reff(HttpServletRequest request){
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		String filter_bank=currentUser.getBank_id()==1?"":"and mst_bank_id ="+currentUser.getBank_id();
		map.put("listProductLife", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis in (1,3) "+filter_bank, "nama"));
		map.put("listProductFire", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=2 "+filter_bank, "nama"));
		map.put("AllProduct", dbService.selectDropDown("id", "nama", "mst_product", "active =1", "id"));
		map.put("listBank", dbService.selectDropDown("id", "nama", "mst_bank", "active = 1", "nama"));
		map.put("listRelasi", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=2 and active=1", "keterangan"));
		map.put("listManfaat", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=7 and active=1", "keterangan"));
		map.put("listJenisBangunan", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=5 and active=1", "keterangan"));
		map.put("listAsuransiLife", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=2", "nama"));
		map.put("listAsuransiFire", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=4", "nama"));
		return map;
	}

	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) throws ServletRequestBindingException {
		logger.debug("Halaman: Micro Page, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null;
		String search=null, sort="id",sort_type=null,begdate=null,enddate=null,begdatepaid=null,enddatepaid=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5, 10, 15, 20, 25, 30, 40, 50};
		
		Integer posisi=5;
		Integer jenis_produk=ServletRequestUtils.getIntParameter(request, "jenis_produk",0);
		Integer paid=ServletRequestUtils.getIntParameter(request, "paid",-1);
		
		Integer grouppolis=null;
		Integer jenispolis=null;
		
		
		if(jenis_produk!=0){
			jenispolis=jenis_produk;
		}else{
			grouppolis=null;
			jenispolis=null;
		}
		

		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		String defaultbegdate=null;//"01-"+Utils.convertDateToString(dbService.selectSysdate(), "MM-yyyy");
		String defaultdate=null;//Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		begdate=ServletRequestUtils.getStringParameter(request, "begdate", defaultbegdate);
		enddate=ServletRequestUtils.getStringParameter(request, "enddate", defaultdate);
		begdatepaid=ServletRequestUtils.getStringParameter(request, "begdatepaid", defaultbegdate);
		enddatepaid=ServletRequestUtils.getStringParameter(request, "enddatepaid", defaultdate);
		
		if(Utils.isEmpty(begdate))begdate=null;
		if(Utils.isEmpty(begdatepaid))begdatepaid=null;
			
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		
		//bila user bank (jenis = 1) dan user KC / KCP (jenis = 1 atau 2), maka user hanya bisa melihat yg ada di cabangnya saja
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Integer cab_bank = null;
		Integer asuransi_id = null;
		if(currentUser.getBank_jenis().intValue() == 1 && (currentUser.getCab_bank_jenis().intValue() == 1 || currentUser.getCab_bank_jenis().intValue() == 2)){
			cab_bank = currentUser.getCab_bank_id();
		}else if(currentUser.getBank_jenis().intValue() == 2||currentUser.getBank_jenis().intValue() == 4){
			asuransi_id=currentUser.getBank_id();
		}

		totalData=dbService.selectListPolisPagingCount(grouppolis,jenispolis, search,posisi,begdate,enddate,begdatepaid,enddatepaid,paid, cab_bank,asuransi_id);
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		rowcount=null;
		
		List<Policy> listPolicy = dbService.selectListPolisPaging(grouppolis,jenispolis, search, offset, rowcount, sort, sort_type,posisi,begdate,enddate,begdatepaid,enddatepaid,paid, cab_bank,asuransi_id);
		model.addAttribute("listPolicy", listPolicy);
		
		
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
		
		model.addAttribute("begdate",begdate );
		model.addAttribute("enddate",enddate );
		model.addAttribute("jenis_produk",jenis_produk);
		model.addAttribute("begdatepaid",begdatepaid );
		model.addAttribute("enddatepaid",enddatepaid );
		model.addAttribute("sysdate",Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy") );
		model.addAttribute("paid",paid );
		
		return "input_payment_list";
	}		

	

	//edit data
	@RequestMapping(value="/pay/{id}", method=RequestMethod.GET)
	public String input_payment(@ModelAttribute("policy")Policy policy, @PathVariable Integer id, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Input Payment, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		Policy tmp=dbService.selectPolicy(id,null,null,null);
		BeanUtils.copyProperties(tmp, policy);
		policy.setMode("EDIT");
		policy.setProduct(dbService.selectProduct(policy.getId()));
		return "input_payment_edit";
	}
	
	//view data
	@RequestMapping(value="/viewPay/{id}", method=RequestMethod.GET)
	public String view_payment(@ModelAttribute("policy")Policy policy, @PathVariable Integer id, Model model){ //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Input Payment, method: EDIT");
		
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		Policy tmp=dbService.selectPolicy(id,null,null,null);
		BeanUtils.copyProperties(tmp, policy);
		policy.setMode("EDIT");
		policy.setProduct(dbService.selectProduct(policy.getId()));
		return "input_payment_edit";
	}
	
	//view data
		@RequestMapping(value="/view/{id}", method=RequestMethod.GET)
		public String view(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy, @PathVariable Integer id) {
			logger.debug("Halaman: INPUT, method: View");
			
			Policy policy=dbService.selectPolicy(id,null,null,null);
			GroupPolicy tmp = dbService.selectGroupPolicy(policy.getGroup_policy_id());
			BeanUtils.copyProperties(tmp, groupPolicy);
			groupPolicy.setMode("VIEW");
			groupPolicy.setPagename("keuangan/input");
			groupPolicy.setListHistory(dbService.selectHistoryPolis(id));
			if(policy.getJenis()==1||policy.getJenis()==3){
				groupPolicy.setLifeCheck(true);
				groupPolicy.setPolisMicro(policy);
				groupPolicy.getPolisMicro().setBank(dbService.selectBank(groupPolicy.getPolisMicro().getId()));
				if(groupPolicy.getPolisMicro().getBank() != null){
					groupPolicy.setMst_bank_id(groupPolicy.getPolisMicro().getBank().getMst_bank_id());
					groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisMicro().getBank().getMst_cab_bank_id());
				}
				
				groupPolicy.getPolisMicro().setAgent(dbService.selectAgent(groupPolicy.getPolisMicro().getId()));
				if(groupPolicy.getPolisMicro().getAgent() != null){
					groupPolicy.setNama_agent(groupPolicy.getPolisMicro().getAgent().getNama());
					groupPolicy.setTelp_agent(groupPolicy.getPolisMicro().getAgent().getTelp_hp());
					groupPolicy.setJabatan_agent(groupPolicy.getPolisMicro().getAgent().getJabatan());
				}
				
				groupPolicy.getPolisMicro().setProduct(dbService.selectProduct(groupPolicy.getPolisMicro().getId()));
				groupPolicy.getPolisMicro().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisMicro().getId()));
				groupPolicy.getPolisMicro().setCustomer(dbService.selectCustomer(groupPolicy.getPolisMicro().getCustomer_id()));
				groupPolicy.getPolisMicro().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisMicro().getCustomer().getAddress_id()));
			
			}else if(policy.getJenis()==2){
				groupPolicy.setFireCheck(true);
				groupPolicy.setPolisFire(policy);
				groupPolicy.getPolisFire().setBank(dbService.selectBank(groupPolicy.getPolisFire().getId()));
				if(groupPolicy.getPolisFire().getBank() != null){
					groupPolicy.setMst_bank_id(groupPolicy.getPolisFire().getBank().getMst_bank_id());
					groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisFire().getBank().getMst_cab_bank_id());
				}

				groupPolicy.getPolisFire().setAgent(dbService.selectAgent(groupPolicy.getPolisFire().getId()));
				if(groupPolicy.getPolisFire().getAgent() != null){
					groupPolicy.setNama_agent(groupPolicy.getPolisFire().getAgent().getNama());
					groupPolicy.setTelp_agent(groupPolicy.getPolisFire().getAgent().getTelp_hp());
					groupPolicy.setJabatan_agent(groupPolicy.getPolisFire().getAgent().getJabatan());
				}

				groupPolicy.getPolisFire().setProduct(dbService.selectProduct(groupPolicy.getPolisFire().getId()));
				groupPolicy.getPolisFire().setCustomer(dbService.selectCustomer(groupPolicy.getPolisFire().getCustomer_id()));
				groupPolicy.getPolisFire().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisFire().getCustomer().getAddress_id()));
				groupPolicy.getPolisFire().getCustomer().setAddressInsured(dbService.selectAddress(groupPolicy.getPolisFire().getCustomer().getObjek_pertanggungan()));
			
			}
			
			
			
			return "input_viewer_edit";
		}
	
	
	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save( @ModelAttribute("policy") Policy policy, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) {
		logger.debug("Halaman: INPUT, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		//contoh bila validasi dilakukan langsung didalam controller 
		//(validasi tambahan, selain validasi di PolicyAJKValidator)
		if (!result.hasErrors()) {
			//FIXME result.reject("", "Error nih!");
			BindException errors = new BindException(result);
			 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "product.tgl_paid", "NotEmpty", new String[]{"Tanggal Pembayaran"});
			 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "product.nominal_paid", "NotEmpty", new String[]{"Jumlah Pembayaran"});
		}

		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			model.addAllAttributes(map);
			return "input_payment_edit";
		}

		//bila tidak ada error simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan = dbService.savePayment(policy, currentUser);
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/keuangan/input"; //balikin ke layar list input
		
	}
	
	//saat user menekan tombol save baik dari layar input maupun layar edit
		@RequestMapping(value="/saveAll", method=RequestMethod.POST) //mapping request POST saja ke method ini
		public String save( HttpServletRequest request, Model model, RedirectAttributes ra) {
			logger.debug("Halaman: INPUT, method: SAVE");
			List<String> message=new ArrayList<String>();
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			
			String[] lspaid=ServletRequestUtils.getStringParameters(request, "paid");
			List<Policy> lsPolicy=new ArrayList<Policy>();
			
			if(lspaid.length==0){
				message.add("Tidak ada polis yang di proses");
			}else{
				
				for(String paid:lspaid){
					Policy policy=dbService.selectPolicy(Integer.parseInt(paid), null, null,null);
					policy.setProduct(dbService.selectProduct(policy.getId()));
					String tgl_paid=ServletRequestUtils.getStringParameter(request, "tglpaid_"+paid,null);
					String nominal_paid=ServletRequestUtils.getStringParameter(request, "nominalpaid_"+paid,null);
					if(Utils.isEmpty(tgl_paid)){
						message.add("Tanggal bayar pada No. Spaj "+policy.getSpaj_no()+" belum diisi.");
						break;
					}
					
					if(Utils.isEmpty(nominal_paid)){
						message.add("Jumlah bayar pada No. Spaj "+policy.getSpaj_no()+" belum diisi.");
						break;
					}
					
					
					
					try {
						policy.getProduct().setTgl_paid(Utils.defaultDF.parse(tgl_paid));
					} catch (ParseException e) {
						e.printStackTrace();
						message.add("Format tanggal bayar pada No. Spaj "+policy.getSpaj_no()+" salah");
						break;
					}
					
					
					try {
						Double tempPaid=policy.getProduct().getNominal_paid();
						policy.getProduct().setNominal_paid(Utils.defaultNF.parse(nominal_paid).doubleValue());
						
						if(tempPaid!=null)if(tempPaid.compareTo(policy.getProduct().getNominal_paid())==0)continue;
					} catch (ParseException e) {
						e.printStackTrace();
						message.add("Format jumlah bayar pada No. Spaj "+policy.getSpaj_no()+" salah");
						break;
					}
					
					
					
					policy.getProduct().setNominal_remain(policy.getProduct().getPremi()-policy.getProduct().getNominal_paid());
					
					Integer flag_paid=null;
					if(policy.getProduct().getNominal_remain().compareTo(new Double(0))<0){//lebih bayar
						flag_paid=2;
					}else if(policy.getProduct().getNominal_remain().compareTo(new Double(0))>0){//kurang bayar
						flag_paid=3;
					}else if(policy.getProduct().getNominal_remain().compareTo(new Double(0))==0){//pas
						flag_paid=1;
					}
					
					policy.getProduct().setFlag_paid(flag_paid);
					lsPolicy.add(policy);
				}
			}
			String pesan="";
			//bila ada error, kembalikan ke halaman edit
			if (!message.isEmpty()) {
				
				for(String m:message){
					pesan+=m+"<br/>";
				}
				
			}else{
				
				pesan = dbService.savePayment(lsPolicy, currentUser);
			}

			//bila tidak ada error simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
//			String pesan = dbService.savePayment(policy, currentUser);
			ra.addFlashAttribute("pesan", pesan);
			return "redirect:/keuangan/input"; //balikin ke layar list input
			
		}

	
	//user menekan tombol delete (delete dalam hal ini me-nonaktifkan data saja)
	@RequestMapping(value="/reset/{uname}")
	public String reset(@PathVariable String uname, RedirectAttributes ra,HttpServletRequest request) throws MessagingException {
		logger.debug("Halaman: Keuangan, method: Reset");


		logger.debug("Halaman: Master User, method: DELETE");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Policy policy=dbService.selectPolicy(Integer.parseInt(uname), null, null,null);
			policy.setProduct(dbService.selectProduct(policy.getId()));
			policy.setMode("RESET");
			pesan = dbService.savePayment(policy, currentUser);
			
		}catch (Exception e) {
			e.printStackTrace();
			pesan="Pembayaran gagal di reset";

			email.send(
					props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
					"ERROR pada maibro (Reset Pembayaran)", e.toString());
			
		}
		
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/keuangan/input"; //balikin ke layar list input
	
	}
	
	//user menekan tombol transfer
		@RequestMapping(value="/transfer/{id}", method=RequestMethod.GET)
		public String transfer(@PathVariable Integer id, RedirectAttributes ra, HttpServletRequest request) throws MailException, MessagingException {
			logger.debug("Halaman: Input Payment Transfer, method: transfer");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				Policy policy=dbService.selectPolicy(id,null,null,null);
				policy.setMode("TRANSFER");
				
				if(policy.getJenis()==1)
					pesan=dbService.savePolisAJK(policy, currentUser);
				else if(policy.getJenis()==2)
					pesan=dbService.savePolicyFire(policy, currentUser);
				else if(policy.getJenis()==3)
					pesan=dbService.savePolicyMicro(policy, currentUser);
				
			}catch (Exception e) {
				e.printStackTrace();
				pesan="Polis gagal di transfer";

				/*email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);*/
			
			
			}
			ra.addFlashAttribute("pesan", pesan);
			
			return "redirect:/keuangan/input";
		}
	
	
}
