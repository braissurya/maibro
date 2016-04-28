package com.maibro.web;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 * @author 		: Yusuf S
 * @since		: 27 Feb 2013
 * @Description : Viewer Controller
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/viewer")
public class ViewerController extends ParentController {

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
		map.put("AllProduct", dbService.selectDropDown("id", "nama", "mst_product", "active =1 "+filter_bank, "id"));
		map.put("AllProdukGroup", dbService.selectDropDown("p.id", "p.nama", "mst_product p, mst_master m", "p.group_product = m.jenis and p.active = 1 and m.id = 3 and p.group_product = " + currentUser.getMst_product_id()+" "+filter_bank, "p.nama"));
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
		String search=null, sort="id",sort_type=null,begdate=null,enddate=null,begdatepaid=null,enddatepaid=null,tgl_aksep=null,tgl_aksep_end=null;
		
		//reference data utk dropdown
		int[] listNumRows = new int[]{5, 10, 15, 20, 25, 30, 40, 50};
		
		Integer posisi = null; //sengaja di null, krn viewer bisa view all
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
		String defaultbegdate="01-"+Utils.convertDateToString(dbService.selectSysdate(), "MM-yyyy");
		String defaultdate=Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		begdate=ServletRequestUtils.getStringParameter(request, "begdate", defaultbegdate);
		enddate=ServletRequestUtils.getStringParameter(request, "enddate", defaultdate);
		begdatepaid=ServletRequestUtils.getStringParameter(request, "begdatepaid", null);
		enddatepaid=ServletRequestUtils.getStringParameter(request, "enddatepaid", null);
		tgl_aksep=ServletRequestUtils.getStringParameter(request, "tgl_aksep", null);
		tgl_aksep_end=ServletRequestUtils.getStringParameter(request, "tgl_aksep_end", null);		
		
		if(Utils.isEmpty(begdate))begdate=null;
		if(Utils.isEmpty(begdatepaid))begdatepaid=null;
		if(Utils.isEmpty(tgl_aksep))tgl_aksep=null;
			
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
		
	
		grouppolis=currentUser.getMst_product_id();
		

		totalData=dbService.selectListPolisPagingCount(grouppolis,jenispolis, search,posisi,begdate,enddate,begdatepaid,enddatepaid,tgl_aksep,tgl_aksep_end,paid, cab_bank,asuransi_id);
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		rowcount=null;
		List<Policy> listPolicy = dbService.selectListPolisPaging(grouppolis,jenispolis, search, offset, rowcount, sort, sort_type,posisi,begdate,enddate,begdatepaid,enddatepaid,tgl_aksep,tgl_aksep_end,paid, cab_bank,asuransi_id);
		model.addAttribute("listPolicy", listPolicy);
		model.addAttribute("total", Utils.hitTotalPolicy(listPolicy));
		
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
		model.addAttribute("tgl_aksep",tgl_aksep );
		model.addAttribute("tgl_aksep_end",tgl_aksep_end );
		model.addAttribute("sysdate",Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy") );
		model.addAttribute("paid",paid );
		model.addAttribute("product_id", currentUser.mst_product_id);
		
		return "input_viewer_list";
	}		

	//view data
	@RequestMapping(value="/view/{id}", method=RequestMethod.GET)
	public String view(@ModelAttribute("groupPolicy") GroupPolicy groupPolicy, @PathVariable Integer id) {
		logger.debug("Halaman: INPUT, method: View");
		
		Policy policy=dbService.selectPolicy(id,null,null,null);
		GroupPolicy tmp = dbService.selectGroupPolicy(policy.getGroup_policy_id());
		BeanUtils.copyProperties(tmp, groupPolicy);
		groupPolicy.setMode("VIEW");
		groupPolicy.setPagename("viewer");
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
	
	@RequestMapping(value="/saveAll", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String justViewed( HttpServletRequest request, Model model, RedirectAttributes ra) {
		logger.debug("Halaman: INPUT, method: SAVE");
		List<String> message=new ArrayList<String>();
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String[] lsview=ServletRequestUtils.getStringParameters(request, "flag_view");
		List<Policy> lsPolicy=new ArrayList<Policy>();
		
		if(lsview.length==0){
			message.add("Tidak ada polis yang di proses");
		}else{
			
			for(String viewid:lsview){
				Policy policy=dbService.selectPolicy(Integer.parseInt(viewid), null, null,null);
				
				policy.setFlag_view_asuransi(1);
				policy.setTgl_view_asuransi(dbService.selectSysdate());
				
				dbService.updatePolicy(policy);
			}
		}
		String pesan="";
		//bila ada error, kembalikan ke halaman edit
		if (!message.isEmpty()) {
			
			for(String m:message){
				pesan+=m+"<br/>";
			}
			
		}else{
			
			
		}

		//bila tidak ada error simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
//		String pesan = dbService.savePayment(policy, currentUser);
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/viewer"; //balikin ke layar list input
		
	}


}
