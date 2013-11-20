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
 * Input Group Polis (Polis Life dan Fire digabung)
 * 
 * @author Yusuf
 * @since Feb 4, 2013 (10:06:18 AM)
 *
 */
@Controller
@RequestMapping("/kpr")
public class InputKPRController extends ParentController {

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
			map.put("listProductLife", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=1 and mst_bank_id = " + currentUser.getBank_id(), "nama"));
			map.put("listProductFire", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=2 and mst_bank_id = " + currentUser.getBank_id(), "nama"));
			map.put("listBank", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=1 and id = " + currentUser.getBank_id(), "nama"));
		}else{

			map.put("listProductLife", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=1", "nama"));
			map.put("listProductFire", dbService.selectDropDown("id", "nama", "mst_product", "active=1 and jenis=2", "nama"));
			map.put("listBank", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=1", "nama"));
		}
		
		map.put("listRelasi", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=2 and active=1", "keterangan"));
		map.put("listManfaat", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=7 and active=1", "keterangan"));
		map.put("listJenisBangunan", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=5 and active=1", "keterangan"));
		map.put("listAsuransiLife", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=2", "nama"));
		map.put("listAsuransiFire", dbService.selectDropDown("id", "nama", "mst_bank", "active=1 and jenis=4", "nama"));
		map.put("listStatusAksep", dbService.selectDropDown("jenis", "keterangan", "mst_master", "id=14 and active=1", "keterangan"));
		
		return map;
	}
	
	//Show Form
	@RequestMapping(value="/{pagename}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable String pagename) {
		logger.debug("Halaman: KPR Page, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null;
		String search=null, sort="id",sort_type=null,begdate=null,enddate=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5, 10, 15, 20, 25, 30, 40, 50};
		
		Integer posisi=null;
		String namaMenu="";
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		for(Menu mn:currentUser.listMenu){
			if(mn.link.contains("kpr/"+pagename)){
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
			totalData=dbService.selectListPolisPagingCount(1, null, search, posisi, begdate, enddate, null, null, null, cab_bank,asuransi_id);
			totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
			page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
			
			if(page<1) page = 1;
			if(page>totalPage) page = totalPage;
			int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
			
			if(offset<0)offset=0;
			
			List<Policy> listPolicy = dbService.selectListPolisPaging(1, null, search, offset, rowcount, sort, sort_type, posisi, begdate, enddate, null, null, null, cab_bank,asuransi_id);
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
		
		if(request.getParameter("download_akseptasi_life")!=null){
			String download="redirect:/report/print/report_akseptasi?jenis=1";
			if(!Utils.isEmpty(begdate)&!Utils.isEmpty(enddate)){
				download+="&begdate="+begdate+"&enddate="+enddate;
			}
			return download;
		}
		
		if(request.getParameter("download_akseptasi_fire")!=null){
			String download="redirect:/report/print/report_akseptasi?jenis=2";
			if(!Utils.isEmpty(begdate)&!Utils.isEmpty(enddate)){
				download+="&begdate="+begdate+"&enddate="+enddate;
			}
			return download;
		}

		return "input_kpr_polis_list";
	}	

	/*//Show Form
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {
		logger.debug("Halaman: INPUT, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null;
		String search=null, sort="id",sort_type=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5, 10, 15, 20, 25, 30, 40, 50};
		
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		totalData=dbService.selectListGroupPolicyPagingCount(1, search);
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		List<Policy> listGroupPolicy = dbService.selectListGroupPolicyPaging(1, search, offset, rowcount, sort, sort_type);
		
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
		model.addAttribute("listGroupPolicy", listGroupPolicy);
		
		return "input_kpr_list";
	}	*/	

	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy,HttpServletRequest request) {
		logger.debug("Halaman: INPUT, method: NEW");
		groupPolicy.setMode("NEW");
		
		Date sysdate = dbService.selectSysdate();
		
		//set beberapa nilai default
		groupPolicy.setLifeCheck(false);
		groupPolicy.setFireCheck(false);
		groupPolicy.getPolisAJK().getCustomer().setQ1(true);
		groupPolicy.getPolisAJK().getCustomer().setQ2(false);
		groupPolicy.getPolisAJK().getCustomer().setQ3(false);
		groupPolicy.getPolisAJK().getCustomer().setQ4(false);
		groupPolicy.getPolisAJK().getCustomer().setQ5(false);
		groupPolicy.getPolisAJK().setBeg_date(sysdate);
		
		groupPolicy.getPolisFire().getCustomer().getAddressInsured().setHarga_stock(0.);
		groupPolicy.getPolisFire().getCustomer().getAddressInsured().setHarga_perabot(0.);
		groupPolicy.getPolisFire().getCustomer().getAddressInsured().setHarga_lainnya(0.);
		
		groupPolicy.getPolisFire().setBeg_date(sysdate);

		groupPolicy.getPolisAJK().setSpaj_date(sysdate);
		groupPolicy.getPolisFire().setSpaj_date(sysdate);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");

		//cab bank otomatis dari user
		groupPolicy.setMst_bank_id(currentUser.getBank_id());
		groupPolicy.setMst_cab_bank_id(currentUser.getCab_bank_id());

		List<MstProduct> lsProduk=dbService.selectListMstProduct(null,1,groupPolicy.getMst_bank_id(),null);
		if(!lsProduk.isEmpty())
		groupPolicy.getPolisAJK().getProduct().setMst_product_id(lsProduk.get(0).id);
		
		lsProduk=dbService.selectListMstProduct(null,2,groupPolicy.getMst_bank_id(),null);
		if(!lsProduk.isEmpty()) 
		groupPolicy.getPolisFire().getProduct().setMst_product_id(lsProduk.get(0).id);
		

		groupPolicy.getPolisAJK().getBank().setMst_bank_id(currentUser.getBank_id());
		groupPolicy.getPolisAJK().getBank().setNamaBank(dbService.selectListMstBank(currentUser.getBank_id()).get(0).nama);
		groupPolicy.getPolisAJK().getBank().setMst_cab_bank_id(currentUser.getCab_bank_id());
		
		//populate bank dan nama cabang dan bank clause
		List<MstCabBank> lsCabbank=dbService.selectListMstCabBank(currentUser.getCab_bank_id(), currentUser.getBank_id(),null);
		if(!lsCabbank.isEmpty()){
			groupPolicy.getPolisAJK().getBank().setNamaCabang(lsCabbank.get(0).nama);
			groupPolicy.getPolisFire().getBank().setNamaCabang(lsCabbank.get(0).nama);
			groupPolicy.getPolisFire().getBank().setClause("Bank bjb Jawa Barat dan Banten " + lsCabbank.get(0).nama);
		}

		groupPolicy.getPolisFire().getBank().setMst_bank_id(currentUser.getBank_id());
		groupPolicy.getPolisFire().getBank().setNamaBank(dbService.selectListMstBank(currentUser.getBank_id()).get(0).nama);
		groupPolicy.getPolisFire().getBank().setMst_cab_bank_id(currentUser.getCab_bank_id());
				
	
		
		return "input_kpr_edit";
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
	
		if(policy.getJenis()==1){
			groupPolicy.setLifeCheck(true);
			groupPolicy.setPolisAJK(policy);
			groupPolicy.getPolisAJK().setBank(dbService.selectBank(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getBank() != null){
				groupPolicy.setMst_bank_id(groupPolicy.getPolisAJK().getBank().getMst_bank_id());
				groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisAJK().getBank().getMst_cab_bank_id());
			}
			
			groupPolicy.getPolisAJK().setAgent(dbService.selectAgent(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getAgent() != null){
				groupPolicy.setNama_agent(groupPolicy.getPolisAJK().getAgent().getNama());
				groupPolicy.setTelp_agent(groupPolicy.getPolisAJK().getAgent().getTelp_hp());
				groupPolicy.setJabatan_agent(groupPolicy.getPolisAJK().getAgent().getJabatan());
			}
			
			groupPolicy.getPolisAJK().setProduct(dbService.selectProduct(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setCustomer(dbService.selectCustomer(groupPolicy.getPolisAJK().getCustomer_id()));
			groupPolicy.getPolisAJK().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisAJK().getCustomer().getAddress_id()));
		
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
		
		
		return "input_kpr_edit";
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
		
		if(policy.getJenis()==1){
			groupPolicy.setLifeCheck(true);
			groupPolicy.setPolisAJK(policy);
			groupPolicy.getPolisAJK().setBank(dbService.selectBank(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getBank() != null){
				groupPolicy.setMst_bank_id(groupPolicy.getPolisAJK().getBank().getMst_bank_id());
				groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisAJK().getBank().getMst_cab_bank_id());
			}
			
			groupPolicy.getPolisAJK().setAgent(dbService.selectAgent(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getAgent() != null){
				groupPolicy.setNama_agent(groupPolicy.getPolisAJK().getAgent().getNama());
				groupPolicy.setTelp_agent(groupPolicy.getPolisAJK().getAgent().getTelp_hp());
				groupPolicy.setJabatan_agent(groupPolicy.getPolisAJK().getAgent().getJabatan());
			}
			
			groupPolicy.getPolisAJK().setProduct(dbService.selectProduct(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setCustomer(dbService.selectCustomer(groupPolicy.getPolisAJK().getCustomer_id()));
			groupPolicy.getPolisAJK().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisAJK().getCustomer().getAddress_id()));
		
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
			groupPolicy.getPolisFire().setDeductible("NIL");
		}
		
		return "input_kpr_edit";
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
		if(policy.getJenis()==1){
			groupPolicy.setLifeCheck(true);
			groupPolicy.setPolisAJK(policy);
			groupPolicy.getPolisAJK().setBank(dbService.selectBank(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getBank() != null){
				groupPolicy.setMst_bank_id(groupPolicy.getPolisAJK().getBank().getMst_bank_id());
				groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisAJK().getBank().getMst_cab_bank_id());
			}
			
			groupPolicy.getPolisAJK().setAgent(dbService.selectAgent(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getAgent() != null){
				groupPolicy.setNama_agent(groupPolicy.getPolisAJK().getAgent().getNama());
				groupPolicy.setTelp_agent(groupPolicy.getPolisAJK().getAgent().getTelp_hp());
				groupPolicy.setJabatan_agent(groupPolicy.getPolisAJK().getAgent().getJabatan());
			}
			
			groupPolicy.getPolisAJK().setProduct(dbService.selectProduct(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setCustomer(dbService.selectCustomer(groupPolicy.getPolisAJK().getCustomer_id()));
			groupPolicy.getPolisAJK().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisAJK().getCustomer().getAddress_id()));
		
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
		return "input_kpr_edit";
	}

	/*//edit data
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String update(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy, @PathVariable Integer id,HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		logger.debug("Halaman: INPUT, method: EDIT");
		
		GroupPolicy tmp = dbService.selectGroupPolicy(id);
		BeanUtils.copyProperties(tmp, groupPolicy);
		groupPolicy.setMode("EDIT");
		
		groupPolicy.setPolisAJK(dbService.selectPolicy(null,groupPolicy.id,1));
		if(groupPolicy.getPolisAJK() != null){
			groupPolicy.setLifeCheck(true);
			
			groupPolicy.getPolisAJK().setBank(dbService.selectBank(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getBank() != null){
				groupPolicy.setMst_bank_id(groupPolicy.getPolisAJK().getBank().getMst_bank_id());
				groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisAJK().getBank().getMst_cab_bank_id());
			}
			
			groupPolicy.getPolisAJK().setAgent(dbService.selectAgent(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getAgent() != null){
				groupPolicy.setNama_agent(groupPolicy.getPolisAJK().getAgent().getNama());
				groupPolicy.setTelp_agent(groupPolicy.getPolisAJK().getAgent().getTelp_hp());
			}
			
			groupPolicy.getPolisAJK().setProduct(dbService.selectProduct(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setCustomer(dbService.selectCustomer(groupPolicy.getPolisAJK().getCustomer_id()));
			groupPolicy.getPolisAJK().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisAJK().getCustomer().getAddress_id()));
		}
		
		groupPolicy.setPolisFire(dbService.selectPolicy(null,groupPolicy.id,2));
		if(groupPolicy.getPolisFire() != null){
			groupPolicy.setFireCheck(true);

			groupPolicy.getPolisFire().setBank(dbService.selectBank(groupPolicy.getPolisFire().getId()));
			if(groupPolicy.getPolisFire().getBank() != null){
				groupPolicy.setMst_bank_id(groupPolicy.getPolisFire().getBank().getMst_bank_id());
				groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisFire().getBank().getMst_cab_bank_id());
			}

			groupPolicy.getPolisFire().setAgent(dbService.selectAgent(groupPolicy.getPolisFire().getId()));
			if(groupPolicy.getPolisFire().getAgent() != null){
				groupPolicy.setNama_agent(groupPolicy.getPolisFire().getAgent().getNama());
				groupPolicy.setTelp_agent(groupPolicy.getPolisFire().getAgent().getTelp_hp());
			}

			groupPolicy.getPolisFire().setProduct(dbService.selectProduct(groupPolicy.getPolisFire().getId()));
			groupPolicy.getPolisFire().setCustomer(dbService.selectCustomer(groupPolicy.getPolisFire().getCustomer_id()));
			groupPolicy.getPolisFire().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisFire().getCustomer().getAddress_id()));
			groupPolicy.getPolisFire().getCustomer().setAddressInsured(dbService.selectAddress(groupPolicy.getPolisFire().getCustomer().getObjek_pertanggungan()));
		}
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//cab bank otomatis dari user
		groupPolicy.setMst_bank_id(currentUser.getBank_id());
		groupPolicy.setMst_cab_bank_id(currentUser.getCab_bank_id());
		
		return "input_kpr_edit";
	}
	
	//view data
	@RequestMapping(value="/view/{id}", method=RequestMethod.GET)
	public String view(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy, @PathVariable Integer id,HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		logger.debug("Halaman: INPUT, method: View");
		
		GroupPolicy tmp = dbService.selectGroupPolicy(id);
		BeanUtils.copyProperties(tmp, groupPolicy);
		groupPolicy.setMode("VIEW");
		
		groupPolicy.setPolisAJK(dbService.selectPolicy(null,groupPolicy.id,1));
		if(groupPolicy.getPolisAJK() != null){
			groupPolicy.setLifeCheck(true);
			
			groupPolicy.getPolisAJK().setBank(dbService.selectBank(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getBank() != null){
				groupPolicy.setMst_bank_id(groupPolicy.getPolisAJK().getBank().getMst_bank_id());
				groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisAJK().getBank().getMst_cab_bank_id());
			}
			
			groupPolicy.getPolisAJK().setAgent(dbService.selectAgent(groupPolicy.getPolisAJK().getId()));
			if(groupPolicy.getPolisAJK().getAgent() != null){
				groupPolicy.setNama_agent(groupPolicy.getPolisAJK().getAgent().getNama());
				groupPolicy.setTelp_agent(groupPolicy.getPolisAJK().getAgent().getTelp_hp());
			}
			
			groupPolicy.getPolisAJK().setProduct(dbService.selectProduct(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setBeneficiary(dbService.selectBeneficiary(groupPolicy.getPolisAJK().getId()));
			groupPolicy.getPolisAJK().setCustomer(dbService.selectCustomer(groupPolicy.getPolisAJK().getCustomer_id()));
			groupPolicy.getPolisAJK().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisAJK().getCustomer().getAddress_id()));
		}
		
		groupPolicy.setPolisFire(dbService.selectPolicy(null,groupPolicy.id,1));
		if(groupPolicy.getPolisFire() != null){
			groupPolicy.setFireCheck(true);

			groupPolicy.getPolisFire().setBank(dbService.selectBank(groupPolicy.getPolisFire().getId()));
			if(groupPolicy.getPolisFire().getBank() != null){
				groupPolicy.setMst_bank_id(groupPolicy.getPolisFire().getBank().getMst_bank_id());
				groupPolicy.setMst_cab_bank_id(groupPolicy.getPolisFire().getBank().getMst_cab_bank_id());
			}

			groupPolicy.getPolisFire().setAgent(dbService.selectAgent(groupPolicy.getPolisFire().getId()));
			if(groupPolicy.getPolisFire().getAgent() != null){
				groupPolicy.setNama_agent(groupPolicy.getPolisFire().getAgent().getNama());
				groupPolicy.setTelp_agent(groupPolicy.getPolisFire().getAgent().getTelp_hp());
			}

			groupPolicy.getPolisFire().setProduct(dbService.selectProduct(groupPolicy.getPolisFire().getId()));
			groupPolicy.getPolisFire().setCustomer(dbService.selectCustomer(groupPolicy.getPolisFire().getCustomer_id()));
			groupPolicy.getPolisFire().getCustomer().setAddress(dbService.selectAddress(groupPolicy.getPolisFire().getCustomer().getAddress_id()));
			groupPolicy.getPolisFire().getCustomer().setAddressInsured(dbService.selectAddress(groupPolicy.getPolisFire().getCustomer().getObjek_pertanggungan()));
		}
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		//cab bank otomatis dari user
		groupPolicy.setMst_bank_id(currentUser.getBank_id());
		groupPolicy.setMst_cab_bank_id(currentUser.getCab_bank_id());
		return "input_kpr_edit";
	}*/
	
	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("groupPolicy") GroupPolicy groupPolicy, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) {
		logger.debug("Halaman: INPUT, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			
			if(groupPolicy.mode.equals("VALIDASI")){
				if(groupPolicy.lifeCheck)
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "polisAJK.asuransi_id", "NotEmpty", new String[]{""});
				else if(groupPolicy.fireCheck){
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "polisFire.asuransi_id", "NotEmpty", new String[]{""});
					if(groupPolicy.polisFire.getCustomer().getAddressInsured().jenis_bangunan==3){
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "polisFire.premi", "NotEmpty", new String[]{""});
						if(groupPolicy.polisFire.getPremi()==null)ValidationUtils.rejectIfEmptyOrWhitespace(errors, "polisFire.rate", "NotEmpty", new String[]{""});
					}
					
					
					
				}
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
			return "input_kpr_edit";
		}

		//bila tidak ada error simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan = dbService.saveGroupPolicy(1, groupPolicy, currentUser); //1 = jenis group polis KPR
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/kpr/"+(Utils.isEmpty(groupPolicy.getPagename())?"input":groupPolicy.getPagename());//balikin ke layar list input
		
	}

	/*
	//user menekan tombol delete (delete dalam hal ini me-nonaktifkan data saja)
	@RequestMapping(value="/delete/{uname}")
	public String delete(@PathVariable String uname, RedirectAttributes ra) {
		logger.debug("Halaman: INPUT, method: DELETE");

		String pesan = oracleService.deleteUser(uname);
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/input";
	}
	*/
	
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
			
			if(policy.getJenis()==1){
				pesan=dbService.savePolisAJK(policy, currentUser);
				if(pesan.toLowerCase().contains("berhasil")){
					if(policy.getPosisi()==4){
						MstBank mbank=dbService.selectListMstBank(policy.getAsuransi_id()).get(0);
						Customer cs=dbService.selectCustomer(policy.getCustomer_id());
						String message=	"Kepada Yth :\n" +
										""+mbank.nama+"\n\n" +
										"Dengan hormat,\n" +
										"Berikut notifikasi pendaftaran Asuransi KPR Life dari Maibro.com dengan data sebagai berikut :\n\n" +
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
			}else if(policy.getJenis()==2){
				pesan=dbService.savePolicyFire(policy, currentUser);
				if(pesan.toLowerCase().contains("berhasil")){
					if(policy.getPosisi()==5){
						MstBank mbank=dbService.selectListMstBank(policy.getAsuransi_id()).get(0);
						Customer cs=dbService.selectCustomer(policy.getCustomer_id());
						String message=	"Kepada Yth :\n" +
										""+mbank.nama+"\n\n" +
										"Dengan hormat,\n" +
										"Berikut notifikasi pendaftaran Asuransi KPR Fire dari Maibro.com dengan data sebagai berikut :\n\n" +
										"No. SPAK : "+policy.getSpaj_no()+"\n" +
										"Kreditur : "+policy.getNamabank()+" "+policy.getNamacabbank()+"\n" +
										"Debitur : "+policy.getDebitur()+"\n" +
										"Plafon Kredit : "+Utils.defaultNF.format(policy.getPlafon_kredit())+"\n" +
										"Mulai Asuransi : "+Utils.defaultDF.format(policy.getBeg_date())+"\n" +
										"Masa Asuransi : "+policy.getIns_period()+" tahun\n\n" +
										"Detail data dapat dilihat di website www.maibro.com dengan login yang telah diberikan.\n\n" +
										"Mohon dapat segera diproses ke proses selanjutnya. Atas bantuan dan kerjasamanya kami ucapkan terima kasih.\n\n" +
										"Hormat kami.\n\n" +
										"PT. Multi Artha Insurance Brokers";
						email.send(
								props.getProperty("admin.email.from"), mbank.email.split( ";" ), 
								"Notifikasi Pendaftaran Asuransi KPR Fire SPAJ No. "+policy.getSpaj_no(), message);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			pesan="Polis gagal di transfer";

			email.send(
					props.getProperty("admin.email.from"), props.getProperty("admin.email.to").split( ";" ), 
					"ERROR pada maibro (Gagal Transfer KPR)", e.toString());
			
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/kpr/"+pagename;
	}
	
}
