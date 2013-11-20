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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.maibro.model.Customer;
import com.maibro.model.GroupPolicy;
import com.maibro.model.Menu;
import com.maibro.model.MstBank;
import com.maibro.model.MstCabBank;
import com.maibro.model.MstProduct;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.Utils;
import com.maibro.validator.GroupPolicyValidator;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Feb 7, 2013 10:27:17 PM
 * @Description :Input Group Polis (Polis Life (Micro) digabung)
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/micro")
public class InputMicroController extends ParentController {
	
	@Autowired
	protected GroupPolicyValidator groupPolicyValidator;

	public void setGroupPolicyValidator(GroupPolicyValidator groupPolicyValidator) {
		this.groupPolicyValidator = groupPolicyValidator;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(this.groupPolicyValidator);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	}

	//Reference Data
	@ModelAttribute("reff")
	public Map<String, Object> reff(HttpServletRequest request){

		Map<String, Object> map = new HashMap<String, Object>();

		//bila user bank (jenis = 1), maka user hanya bisa melihat banknya saja, termasuk produknya juga
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(currentUser.getBank_jenis().intValue() == 1){
			map.put("listProductLife", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=3 and mst_bank_id = " + currentUser.getBank_id(), "nama"));

			map.put("listBank", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=1 and id = " + currentUser.getBank_id(), "nama"));
		}else{
			map.put("listProductLife", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=3", "nama"));
			map.put("listBank", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=1", "nama"));
		}

		map.put("listRelasi", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=2 and active=1", "keterangan"));
		map.put("listManfaat", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=7 and active=1", "keterangan"));
		map.put("listAsuransiLife", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=2", "nama"));
		map.put("listAsuransiFire", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=4", "nama"));
		map.put("listStatusAksep", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=14 and active=1", "keterangan"));
		return map;
	}

	//Show Form
	@RequestMapping(value="/{pagename}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable String pagename) {
		logger.debug("Halaman: Micro Page, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null;
		String search=null, sort="id",sort_type=null,begdate=null,enddate=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5, 10, 15, 20, 25, 30, 40, 50};
		
		Integer posisi=null;
		String namaMenu="";
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		for(Menu mn:currentUser.listMenu){
			if(mn.link.contains("micro/"+pagename)){
				namaMenu=mn.nama;
			}
		}
		
		if(pagename.equals("input")|pagename.equals("upload")){
			posisi=1;
		}else if(pagename.equals("validasi")){
			posisi=2;
		}else if(pagename.equals("akseptasi")){
			posisi=3;
		}else if(pagename.equals("klaim")){
			posisi=99;
		}else if(pagename.equals("kps")){
			posisi=4;
		}else if(pagename.equals("cetak")){
			posisi=6;
		}
		
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		String defaultbegdate=null;//"01-"+Utils.convertDateToString(dbService.selectSysdate(), "MM-yyyy");
		String defaultdate=null;//Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		begdate=ServletRequestUtils.getStringParameter(request, "begdate", defaultbegdate);
		enddate=ServletRequestUtils.getStringParameter(request, "enddate", defaultdate);
		
		if(Utils.isEmpty(begdate))begdate=null;
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		
		//bila user bank (jenis = 1) dan user KC / KCP (jenis = 1 atau 2), maka user hanya bisa melihat yg ada di cabangnya saja
		Integer cab_bank = null;
		Integer asuransi_id = null;
		if(currentUser.getBank_jenis().intValue() == 1 && (currentUser.getCab_bank_jenis().intValue() == 1 || currentUser.getCab_bank_jenis().intValue() == 2)){
			cab_bank = currentUser.getCab_bank_id();
		}else if(currentUser.getBank_jenis().intValue() == 2||currentUser.getBank_jenis().intValue() == 4){
			asuransi_id=currentUser.getBank_id();
		}

		if(!pagename.equals("klaim")){
			totalData=dbService.selectListPolisPagingCount(2,3, search,posisi,begdate,enddate,null,null,null, cab_bank,asuransi_id);
			totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
			page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
			
			if(page<1) page = 1;
			if(page>totalPage) page = totalPage;
			int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
			
			if(offset<0)offset=0;
			
			List<Policy> listPolicy = dbService.selectListPolisPaging(2,3, search, offset, rowcount, sort, sort_type,posisi,begdate,enddate,null,null,null, cab_bank,asuransi_id);
			model.addAttribute("listPolicy", listPolicy);
		}
		
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
		
		model.addAttribute("namaMenu",namaMenu);
		model.addAttribute("pagename",pagename );
		model.addAttribute("begdate",begdate );
		model.addAttribute("enddate",enddate );
		
		
		if(request.getParameter("download_akseptasi")!=null){
			String download="redirect:/report/print/report_akseptasi?jenis=3";
			if(!Utils.isEmpty(begdate)&!Utils.isEmpty(enddate)){
				download+="&begdate="+begdate+"&enddate="+enddate;
			}
			return download;
		}
		
		return "input_micro_list";
	}		

	//input baru
	@RequestMapping(value="/new/{pagename}", method=RequestMethod.GET)
	public String insert(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy,@PathVariable String pagename,HttpServletRequest request) {
		logger.debug("Halaman: INPUT, method: NEW");
		groupPolicy.setMode("NEW");
		groupPolicy.setPagename(pagename);
		//set beberapa nilai default
		Date sysdate = dbService.selectSysdate();
		
		//set beberapa nilai default
		groupPolicy.setMicroCheck(true);
		groupPolicy.setLifeCheck(true);
		/*groupPolicy.getPolisMicro().getCustomer().setQ1(true);
		groupPolicy.getPolisMicro().getCustomer().setQ2(false);
		groupPolicy.getPolisMicro().getCustomer().setQ3(false);
		groupPolicy.getPolisMicro().getCustomer().setQ4(false);
		groupPolicy.getPolisMicro().getCustomer().setQ5(false);*/
		groupPolicy.getPolisMicro().setBeg_date(sysdate);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//cab bank otomatis dari user
		groupPolicy.setMst_bank_id(currentUser.getBank_id());
		groupPolicy.setMst_cab_bank_id(currentUser.getCab_bank_id());
		groupPolicy.getPolisMicro().getBank().setMst_bank_id(currentUser.getBank_id());
		groupPolicy.getPolisMicro().getBank().setNamaBank(dbService.selectListMstBank(currentUser.getBank_id()).get(0).nama);
		groupPolicy.getPolisMicro().getBank().setMst_cab_bank_id(currentUser.getCab_bank_id());
		
		List<MstCabBank> lsCabbank=dbService.selectListMstCabBank(currentUser.getCab_bank_id(), currentUser.getBank_id(),null);
		if(!lsCabbank.isEmpty())
		groupPolicy.getPolisMicro().getBank().setNamaCabang(lsCabbank.get(0).nama);
		
		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,3,groupPolicy.getMst_bank_id(),null);
		if(!lsProduk.isEmpty()) 
		groupPolicy.getPolisMicro().getProduct().setMst_product_id(lsProduk.get(0).id);
		
		
		return "input_micro_edit";
	}

	//edit data
	@RequestMapping(value="/edit/{id}/{pagename}", method=RequestMethod.GET)
	public String update(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy, @PathVariable Integer id,@PathVariable String pagename)  {
		logger.debug("Halaman: INPUT, method: EDIT");
		Policy policy=dbService.selectPolicy(id,null,null,null);
		GroupPolicy tmp = dbService.selectGroupPolicy(policy.getGroup_policy_id());
		BeanUtils.copyProperties(tmp, groupPolicy);
		groupPolicy.setMode("EDIT");
		groupPolicy.setPagename(pagename);
		groupPolicy.setMicroCheck(true);
		groupPolicy.setLifeCheck(true);
		groupPolicy.setPolisMicro(policy);
		groupPolicy.getPolisMicro().setBank(dbService.selectBank(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setProduct(dbService.selectProduct(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setAgent(dbService.selectAgent(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setCustomer(dbService.selectCustomer(groupPolicy.getPolisMicro().getCustomer_id()));
		groupPolicy.getPolisMicro().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisMicro().getCustomer().getAddress_id()));		
		
		groupPolicy.setMst_bank_id(groupPolicy.getPolisMicro().getBank().mst_bank_id);
		groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisMicro().getBank().mst_cab_bank_id);
		if(groupPolicy.getPolisMicro().getAgent()!=null){
			groupPolicy.setNama_agent(groupPolicy.getPolisMicro().getAgent().nama);
			groupPolicy.setTelp_agent(groupPolicy.getPolisMicro().getAgent().telp_hp);
			groupPolicy.setJabatan_agent(groupPolicy.getPolisMicro().getAgent().getJabatan());
		}
		
		return "input_micro_edit";
	}
	
	//validsi data
	@RequestMapping(value="/validasi/{id}/{pagename}", method=RequestMethod.GET)
	public String validasi(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy, @PathVariable Integer id,@PathVariable String pagename) {
		logger.debug("Halaman: INPUT, method: EDIT");
		Policy policy=dbService.selectPolicy(id,null,null,null);
		GroupPolicy tmp = dbService.selectGroupPolicy(policy.getGroup_policy_id());
		BeanUtils.copyProperties(tmp, groupPolicy);
		groupPolicy.setMode("VALIDASI");
		groupPolicy.setPagename(pagename);
		groupPolicy.setMicroCheck(true);
		groupPolicy.setLifeCheck(true);
		groupPolicy.setPolisMicro(policy);
		groupPolicy.getPolisMicro().setBank(dbService.selectBank(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setProduct(dbService.selectProduct(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setAgent(dbService.selectAgent(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setCustomer(dbService.selectCustomer(groupPolicy.getPolisMicro().getCustomer_id()));
		groupPolicy.getPolisMicro().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisMicro().getCustomer().getAddress_id()));
		
		groupPolicy.setMst_bank_id(groupPolicy.getPolisMicro().getBank().mst_bank_id);
		groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisMicro().getBank().mst_cab_bank_id);
		if(groupPolicy.getPolisMicro().getAgent()!=null){
			groupPolicy.setNama_agent(groupPolicy.getPolisMicro().getAgent().nama);
			groupPolicy.setTelp_agent(groupPolicy.getPolisMicro().getAgent().telp_hp);
			groupPolicy.setJabatan_agent(groupPolicy.getPolisMicro().getAgent().getJabatan());
		}
		
		return "input_micro_edit";
	}
	
	//view data
	@RequestMapping(value="/view/{id}/{pagename}", method=RequestMethod.GET)
	public String view(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy, @PathVariable Integer id,@PathVariable String pagename) {
		logger.debug("Halaman: INPUT, method: View");
		
		Policy policy=dbService.selectPolicy(id,null,null,null);
		GroupPolicy tmp = dbService.selectGroupPolicy(policy.getGroup_policy_id());
		BeanUtils.copyProperties(tmp, groupPolicy);
		groupPolicy.setMode("VIEW");
		groupPolicy.setPagename(pagename);
		groupPolicy.setMicroCheck(true);
		groupPolicy.setLifeCheck(true);
		groupPolicy.setPolisMicro(policy);
		groupPolicy.getPolisMicro().setBank(dbService.selectBank(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setProduct(dbService.selectProduct(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setAgent(dbService.selectAgent(groupPolicy.getPolisMicro().getId()));
		groupPolicy.getPolisMicro().setCustomer(dbService.selectCustomer(groupPolicy.getPolisMicro().getCustomer_id()));
		groupPolicy.getPolisMicro().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisMicro().getCustomer().getAddress_id()));
		
		groupPolicy.setMst_bank_id(groupPolicy.getPolisMicro().getBank().mst_bank_id);
		groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisMicro().getBank().mst_cab_bank_id);
		if(groupPolicy.getPolisMicro().getAgent()!=null){
			groupPolicy.setNama_agent(groupPolicy.getPolisMicro().getAgent().nama);
			groupPolicy.setTelp_agent(groupPolicy.getPolisMicro().getAgent().telp_hp);
			groupPolicy.setJabatan_agent(groupPolicy.getPolisMicro().getAgent().getJabatan());
		}

		return "input_micro_edit";
	}
	
	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save/{pagename}", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("groupPolicy") GroupPolicy groupPolicy, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra,@PathVariable String pagename) {
		logger.debug("Halaman: INPUT, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			
			if(groupPolicy.mode.equals("VALIDASI")){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "polisMicro.asuransi_id", "NotEmpty", new String[]{""});
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
			return "input_micro_edit";
		}

		//bila tidak ada error simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)

		String pesan = dbService.saveGroupPolicy(2, groupPolicy, currentUser);
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/micro/"+pagename; //balikin ke layar list input
		
	}

	
	//user menekan tombol transfer
	@RequestMapping(value="/tranfer/{id}/{pagename}", method=RequestMethod.GET)
	public String transfer(@PathVariable Integer id, RedirectAttributes ra, HttpServletRequest request,@PathVariable String pagename) throws MailException, MessagingException {
		logger.debug("Halaman: Micro Transfer, method: transfer");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Policy policy=dbService.selectPolicy(id,null,null,null);
			policy.setMode("TRANSFER");			
			
			pesan=dbService.savePolicyMicro(policy, currentUser);
			if(pesan.toLowerCase().contains("berhasil")){
				if(policy.getPosisi()==4){
					MstBank mbank=dbService.selectListMstBank(policy.getAsuransi_id()).get(0);
					Customer cs=dbService.selectCustomer(policy.getCustomer_id());
					String message=	"Kepada Yth :\n" +
									""+mbank.nama+"\n\n" +
									"Dengan hormat,\n" +
									"Berikut notifikasi pendaftaran Asuransi Micro Life dari Maibro.com dengan data sebagai berikut :\n\n" +
									"No. SPAJ : "+policy.getSpaj_no()+"\n" +
									"Kreditur : "+policy.getNamabank()+" "+policy.getNamacabbank()+"\n" +
									"Debitur : "+policy.getDebitur()+"\n" +
									"Tanggal Lahir : "+Utils.defaultDF.format(cs.getTgl_lahir())+"\n" +
									"Plafon Kredit : "+Utils.defaultNF.format(policy.getPlafon_kredit())+"\n" +
									"Mulai Asuransi : "+Utils.defaultDF.format(policy.getBeg_date())+"\n" +
									"Masa Asuransi : "+policy.getIns_period()+" tahun\n\n" +
									"Detail data dapat dilihat di website www.maibro.com dengan login yang telah diberikan.\n\n" +
									"Mohon dapat segera diproses ke proses selanjutnya. Atas bantuan dan kerjasamanya kami ucapkan terima kasih.\n\n" +
									"Hormat kami.\n\n" +
									"PT. Multi Artha Insurance Brokers";
					email.send(
							props.getProperty("admin.email.from"), mbank.email.split( ";" ), 
							"Notifikasi Pendaftaran Asuransi KPR Life SPAJ No. "+policy.getSpaj_no(), message);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			pesan="Polis gagal di transfer";

			email.send(
					props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
					"ERROR pada maibro (Transfer Mikro)", e.toString());
			
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/micro/"+pagename;
	}
	
}
