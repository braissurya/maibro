package com.maibro.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jolbox.bonecp.BoneCPDataSource;
import com.maibro.model.DropDown;
import com.maibro.model.History;
import com.maibro.model.MstCabBank;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.utils.JasperUtils;
import com.maibro.utils.Utils;

/**
 * Report Controller
 * 
 * @author Yusuf
 * @since Feb 5, 2013 (9:41:40 AM)
 *
 */
@Controller
@RequestMapping("/report")
public class ReportController extends ParentController{
	
	@Autowired
	private BoneCPDataSource dbDataSource;
	
	private Connection connection;
	
	private Connection getConnection() {
		//bila koneksi belum ada, retrieve dari db pool
		if(this.connection==null){
			try { this.connection = dbDataSource.getConnection(); } 
			catch (SQLException e) { e.printStackTrace(); }
			
		}else{
			//bila koneksi sudah ada, tapi tidak valid atau sudah tertutup, maka lakukan looping untuk mengambil koneksi yang valid
			try {
				while(!this.connection.isValid(5)){
					this.connection = dbDataSource.getConnection();
				}
			}catch (SQLException e) { e.printStackTrace(); }
		}
		return this.connection;
	}	
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(HttpServletRequest request){
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		String filter_bank=currentUser.getBank_id()==1?"":"and mst_bank_id ="+currentUser.getBank_id();
		map.put("AllBank", dbService.selectDropDown("id", "nama", "mst_bank", "active = 1 and jenis = 1", "nama"));
		map.put("AllCabBank", dbService.selectDropDown("id", "nama", "mst_cab_bank", "active = 1", "nama"));
		map.put("AllProduk", dbService.selectDropDown("id", "nama", "mst_product", "active = 1 "+filter_bank, "nama"));
		map.put("AllProdukGroup", dbService.selectDropDown("p.id", "p.nama", "mst_product p, mst_master m", "p.group_product = m.jenis and p.active = 1 and m.id = 3 and p.group_product = " + currentUser.getMst_product_id()+" "+filter_bank, "p.nama"));
		map.put("AllAsuransi", dbService.selectDropDown("id", "nama", "mst_bank", "active = 1 and jenis in (2, 4)", "nama"));
		return map;
	}
	
	/**
	 * Fungsi untuk generate report, dipanggil oleh seluruh report
	 * 
	 * @author Yusuf
	 * @since Feb 5, 2013 (3:23:00 PM)
	 *
	 * @param jenis
	 * @param params
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	private String generateReport(String jenis, Map params, HttpSession session, HttpServletRequest request, HttpServletResponse response,List dataCollection) throws JRException, IOException{
		ServletContext context = session.getServletContext();
		String format = (String) params.get("format");
		
		//Generate report
		/*JasperPrint jasperPrint = JasperFillManager.fillReport(
			context.getRealPath("/WEB-INF/classes/" + props.getProperty("dir.report") + "/" +  
			props.getProperty("report." + jenis) + ".jasper"), //report path 
			params, //report parameters
			getConnection() //connection object
			);*/
		
		// Generate report
		JasperPrint jasperPrint=null;
		if(dataCollection==null)
		 jasperPrint = JasperFillManager.fillReport(context.getRealPath("/WEB-INF/classes/" + props.getProperty("dir.report") + "/" + props.getProperty("report." + jenis) + ".jasper"),
			params, getConnection());
		else {
		    JRDataSource beanDatasource= new JRBeanCollectionDataSource(dataCollection,true);
		    jasperPrint = JasperFillManager.fillReport(context.getRealPath("/WEB-INF/classes/" + props.getProperty("dir.report") + "/" + props.getProperty("report." + jenis) + ".jasper"), params, beanDatasource);
		}

		//Put generated report into session
		session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		
		//Text File
		if(format.equalsIgnoreCase("txt")){
			JRCsvExporter exporter = new JRCsvExporter();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
			//tambahan header khusus file CSV
			response.setHeader("Content-Disposition","attachment; filename=\"report.txt\";");
			
			exporter.exportReport();
			return null;
	
		//csv File
		}else if(format.equalsIgnoreCase("csv")){
				JRCsvExporter exporter = new JRCsvExporter();
				exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER,",");
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
				//tambahan header khusus file CSV
				response.setHeader("Content-Disposition","attachment; filename=\"report.csv\";");
				
				exporter.exportReport();
				return null;
		
			//HTML File
		}else if(format.equalsIgnoreCase("html")){
			JRHtmlExporter exporter = new JRHtmlExporter();
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
			//HTML Specific parameters
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + "/jasper/image?image=");
			exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, true); //biar gak terlalu banyak white space
			exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true); //biar gak terlalu banyak white space
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, ""); //biar tidak ada paging (khusus html)
			
			exporter.exportReport();
			return null;
			
		//format selain HTML dan TXT
		}else{
			response.setHeader("Content-Disposition","attachment; filename=\"report."+format+"\";");
			return "redirect:/jasper/" + format; //redirect ke JasperReports Servlet sesuai format
		}		
	}
	
	/**
		Report Rekapitulasi Peserta Asuransi per Bank (rekap_peserta_asuransi)
		parameter: 
		- sudah terbit polis vs belum terbit polis
		- jenis report (summary vs detail)
		- jenis produk (mikro vs life vs fire)
		- nama produk dan dari perusahaan asuransi mana (bisa all)
		- nama bank dan cabang bank mana (bisa all)
		- periode (begdate)
	 * 
	 * @author Yusuf
	 * @since Feb 5, 2013 (3:01:15 PM)
	 *
	 * @param jenis
	 * @return
	 * @throws IOException 
	 * @throws JRException 
	 * @throws ServletRequestBindingException 
	 */
	@RequestMapping("/rekap_peserta_asuransi")
	public String rekap_peserta_asuransi(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
			throws JRException, IOException, ServletRequestBindingException {		
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String jenisReport = "rekap_peserta_asuransi";
		String param = "", param2 = "", param3 = "";
		String jenis = "", bank = "", cab_bank = "", asuransi = "", jenis_terbit = "", jenis_produk = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		
		logger.debug("Halaman: REPORT " + jenisReport);
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			String begdate=ServletRequestUtils.getStringParameter(request, "beg_date","");
			String enddate= ServletRequestUtils.getStringParameter(request, "end_date","");
			params.put("beg_date", begdate);
			params.put("end_date",enddate);
			String begdate1=ServletRequestUtils.getStringParameter(request, "beg_date1","");
			String enddate1= ServletRequestUtils.getStringParameter(request, "end_date1","");
			params.put("beg_date1", begdate1);
			params.put("end_date1",enddate1);
			jenis = ServletRequestUtils.getRequiredStringParameter(request, "jenis_report");
			bank = ServletRequestUtils.getRequiredStringParameter(request, "bank");
			cab_bank = ServletRequestUtils.getStringParameter(request, "cab_bank");
			asuransi = ServletRequestUtils.getStringParameter(request, "asuransi");
			jenis_terbit = ServletRequestUtils.getRequiredStringParameter(request, "jenis_terbit");
			jenis_produk = ServletRequestUtils.getRequiredStringParameter(request, "jenis_produk");
			
//			if(currentUser.bank_jenis == 1){
//				jenisReport = "rekap_produksi_premi_bank";	//BANK & CABANG
//			}else if(currentUser.bank_jenis == 2){
//				jenisReport = "rekap_produksi_premi_asuransi";	//ASURANSI JIWA
//			}else if(currentUser.bank_jenis == 3){
//				jenisReport = "rekap_produksi_premi";	//BROKER
//			}else if(currentUser.bank_jenis == 4){
//				jenisReport = "rekap_produksi_premi_asuransi";	//ASURANSI KERUGIAN
//			}
			
			//filter jenis report
			if(jenis.equals("1")){//summary
				if(currentUser.bank_jenis==1){//bank
					jenisReport = "rekap_peserta_asuransi";	
				}else {
					jenisReport = "rekap_peserta_asuransi_complete";
				}
				//tampilan data, ini digunakan pada saat RIGHT JOIN supaya data yg ditampilkan gk ALL (khusus yg login bukan Broker)
				if(currentUser.bank_jenis == 1){
					param3 = " AND a.id = " + bank;	//BANK
					if(currentUser.cab_bank_jenis != 0){
						param3 = param3 + " AND b.id = " + cab_bank;	//CABANG 
					}
				}else if(currentUser.bank_jenis == 2 || currentUser.bank_jenis == 4){
					param2 = " AND ass.id = " + asuransi;	//ASURANSI JIWA & ASURANSI KERUGIAN
					param3 = " AND 1 = 2";	//diakalin supaya gk muncul
				}
			}else if(jenis.equals("2")){
				if(currentUser.bank_jenis==1){
					jenisReport = "rekap_peserta_asuransi_det";	//detail
				}else {
					jenisReport = "rekap_peserta_asuransi_det_complete";
				}
			}
			
			param = "1 = 1";
			//filter periode
			if(!Utils.isEmpty(begdate)&&!Utils.isEmpty(enddate)){
				param += " and DATE(p.beg_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
			}
			
			if(!Utils.isEmpty(begdate1)&&!Utils.isEmpty(enddate1)){
				param += " and DATE(p.tgl_aksep) BETWEEN STR_TO_DATE($P{beg_date1}, '%d-%m-%Y') and STR_TO_DATE($P{end_date1}, '%d-%m-%Y')";
			}
			
			//filter terbit polis
			if(jenis_terbit.equals("2")){
				param = param + " AND p.policy_no is not null ";
			}else if(jenis_terbit.equals("3")){
				param = param + " AND p.policy_no is null ";
			}	
			
			//filter jenis produk
			if(jenis_produk != ""){
				param = param + " AND pro.id = " + jenis_produk;
				//Yusuf (22/4/13) - tambah param nama produk ke dlm report
				params.put("namaProduk", dbService.selectMstProductName(Integer.valueOf(jenis_produk)));
			}else{
				if(currentUser.mst_product_id != null){
					//kalau ada isinya maka harus baca ke mst_master id 3 (group_product)
					param = param + " AND pro.group_product = " + currentUser.mst_product_id;
					//Yusuf (22/4/13) - tambah param nama produk ke dlm report
					params.put("namaProduk", dbService.selectMstProductName(currentUser.mst_product_id));
				}
			}
			
			//filter bank
			if(bank != ""){
				param = param + " AND ba.id = " + bank;
			}	
			
			//filter cabang bank
			if(cab_bank != "" && cab_bank != null){
				param = param + " AND cb.id = " + cab_bank;
			}
			
			//filter asuransi
			if(asuransi != ""){
				param = param + " AND p.asuransi_id = " + asuransi;
			}
						
			params.put("param", param);
			params.put("param2", param2);
			params.put("param3", param3);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response,null);
		}else{
			
			model.addAttribute("jenis_user", currentUser.bank_jenis);
			model.addAttribute("jenis_user_cab", currentUser.cab_bank_jenis);
			model.addAttribute("bank_id", currentUser.bank_id);
			model.addAttribute("bank_nama", dbService.selectListMstBank(currentUser.getBank_id()).get(0).nama);
			List<MstCabBank> lsCabbank=dbService.selectListMstCabBank(currentUser.getCab_bank_id(), currentUser.getBank_id(),null);
			if(!lsCabbank.isEmpty()){
				model.addAttribute("cab_nama",lsCabbank.get(0).nama);
				model.addAttribute("cab_id",lsCabbank.get(0).id);
			}

			model.addAttribute("product_id", currentUser.mst_product_id);
			/*model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);	*/	
			
			return "report_" + jenisReport;
		}
	}

	/**
		Report Rekapitulasi Produksi Premi per Bank (produksi_premi)
		parameter:
		- jenis produk (mikro vs life vs fire)
		- nama bank dan cabang bank mana (bisa all)
		- periode (begdate)
	 * 
	 * @author Yusuf
	 * @since Feb 5, 2013 (4:05:31 PM)
	 *
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping("/rekap_produksi_premi")
	public String rekap_produksi_premi(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
			throws JRException, IOException, ServletRequestBindingException {
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String jenisReport = "rekap_produksi_premi";
		String param = "", param2 = "";
		String bank = "", cab_bank = "", asuransi = "", jenis_terbit = "", jenis_produk = "";
		/*String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");*/
		
		logger.debug("Halaman: REPORT " + jenisReport);
		
		if(request.getParameter("show") != null){			
			Map<String, String> params = new HashMap<String, String>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			/*params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));*/
			String begdate=ServletRequestUtils.getStringParameter(request, "beg_date","");
			String enddate= ServletRequestUtils.getStringParameter(request, "end_date","");
			params.put("beg_date", begdate);
			params.put("end_date",enddate);
			String begdate1=ServletRequestUtils.getStringParameter(request, "beg_date1","");
			String enddate1= ServletRequestUtils.getStringParameter(request, "end_date1","");
			params.put("beg_date1", begdate1);
			params.put("end_date1",enddate1);
			bank = ServletRequestUtils.getRequiredStringParameter(request, "bank");
			cab_bank = ServletRequestUtils.getStringParameter(request, "cab_bank");
			asuransi = ServletRequestUtils.getStringParameter(request, "asuransi");
			jenis_terbit = ServletRequestUtils.getRequiredStringParameter(request, "jenis_terbit");
			jenis_produk = ServletRequestUtils.getRequiredStringParameter(request, "jenis_produk");
			
			//filter jenis report
			if(currentUser.bank_jenis == 1){
				jenisReport = "rekap_produksi_premi_bank";	//BANK & CABANG
			}else if(currentUser.bank_jenis == 2){
				jenisReport = "rekap_produksi_premi_asuransi";	//ASURANSI JIWA
			}else if(currentUser.bank_jenis == 3){
				jenisReport = "rekap_produksi_premi";	//BROKER
			}else if(currentUser.bank_jenis == 4){
				jenisReport = "rekap_produksi_premi_asuransi";	//ASURANSI KERUGIAN
			}
			
			param="1 =1";
			
			//filter terbit polis
			if(jenis_terbit.equals("2")){
				param = param + " and  p.policy_no is not null ";
			}else if(jenis_terbit.equals("3")){
				param = param + " and  p.policy_no is null ";
			}
			
			//filter periode
			if(!Utils.isEmpty(begdate)&&!Utils.isEmpty(enddate)){
				param += " and DATE(p.beg_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
			}
			
			if(!Utils.isEmpty(begdate1)&&!Utils.isEmpty(enddate1)){
				param += " and DATE(p.tgl_aksep) BETWEEN STR_TO_DATE($P{beg_date1}, '%d-%m-%Y') and STR_TO_DATE($P{end_date1}, '%d-%m-%Y')";
			}
			
			//filter jenis produk
			if(jenis_produk != ""){
				param = param + " AND pro.id = " + jenis_produk;
			}else{
				if(currentUser.mst_product_id != null){
					//kalau ada isinya maka harus baca ke mst_master id 3 (group_product)
					param = param + " AND pro.group_product = " + currentUser.mst_product_id;
				}
			}
			
			//filter bank
			if(bank != ""){
				param = param + " AND ba.id = " + bank;
			}	
			
			//filter cabang bank
			if(cab_bank != "" && cab_bank != null){
				param = param + " AND cb.id = " + cab_bank;
			}
			
			//filter asuransi
			if(asuransi != ""){
				param = param + " AND p.asuransi_id = " + asuransi;
			}
			
			//tampilan data, ini digunakan pada saat RIGHT JOIN supaya data yg ditampilkan gk ALL (khusus yg login bukan Broker)
			if(currentUser.bank_jenis == 2 || currentUser.bank_jenis == 4){
				param2 = " AND b.id = " + asuransi;	//ASURANSI JIWA & ASURANSI KERUGIAN
			}
						
			params.put("param", param);
			params.put("param2", param2);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response,null);
		}else{
			model.addAttribute("jenis_user", currentUser.bank_jenis);
			model.addAttribute("jenis_user_cab", currentUser.cab_bank_jenis);
			model.addAttribute("bank_id", currentUser.bank_id);
			model.addAttribute("bank_nama", dbService.selectListMstBank(currentUser.getBank_id()).get(0).nama);
			List<MstCabBank> lsCabbank=dbService.selectListMstCabBank(currentUser.getCab_bank_id(), currentUser.getBank_id(),null);
			if(!lsCabbank.isEmpty()){
				model.addAttribute("cab_nama",lsCabbank.get(0).nama);
				model.addAttribute("cab_id",lsCabbank.get(0).id);
			}
			
			model.addAttribute("product_id", currentUser.mst_product_id);
			model.addAttribute("product_name", dbService.selectListMstProduct(currentUser.getMst_product_id(), null, null,null).get(0).nama);
			/*model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);*/
			return "report_" + jenisReport;
		}
	}

	/**
		Report Billing
		parameter:
		- periode (begdate)
		- status (paid / unpaid)
	 * 
	 * @author Rudy
	 * @since Feb 11, 2013 (4:05:31 PM)
	 *
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping("/rekap_billing")
	public String rekap_billing(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
			throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String jenisReport = "rekap_billing";
		String status = "";
		String param = "";
		String bank = "", cab_bank = "", asuransi = "", jenis_produk = "";
		/*String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");*/
		
		logger.debug("Halaman: REPORT " + jenisReport);		
		
		if(request.getParameter("show") != null){
			Map<String, String> params = new HashMap<String, String>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			String begdate=ServletRequestUtils.getStringParameter(request, "beg_date","");
			String enddate= ServletRequestUtils.getStringParameter(request, "end_date","");
			params.put("beg_date", begdate);
			params.put("end_date",enddate);
			String begdate1=ServletRequestUtils.getStringParameter(request, "beg_date1","");
			String enddate1= ServletRequestUtils.getStringParameter(request, "end_date1","");
			params.put("beg_date1", begdate1);
			params.put("end_date1",enddate1);
			bank = ServletRequestUtils.getRequiredStringParameter(request, "bank");
			cab_bank = ServletRequestUtils.getStringParameter(request, "cab_bank");
			asuransi = ServletRequestUtils.getStringParameter(request, "asuransi");
			jenis_produk = ServletRequestUtils.getRequiredStringParameter(request, "jenis_produk");
			status = ServletRequestUtils.getRequiredStringParameter(request, "status");
			
			param = "1 = 1";
			//filter periode
			if(!Utils.isEmpty(begdate)&&!Utils.isEmpty(enddate)){
				param += " and DATE(p.beg_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
			}
			
			if(!Utils.isEmpty(begdate1)&&!Utils.isEmpty(enddate1)){
				param += " and DATE(p.tgl_aksep) BETWEEN STR_TO_DATE($P{beg_date1}, '%d-%m-%Y') and STR_TO_DATE($P{end_date1}, '%d-%m-%Y')";
			}
			
			//filter status paid
			if(status != ""){
				param = param + " AND IFNULL(pr.flag_paid, 0) = " + status;
				if(status.equals("1")){
					status = "(PAID)";
				}else if(status.equals("2")){
					status = "(LEBIH BAYAR)";
				}else if(status.equals("3")){
					status = "(KURANG BAYAR)";
				}else if(status.equals("0")){
					status = "(OUTSTANDING)";
				}
			}	
			
			//filter jenis produk
			if(jenis_produk != ""){
				param = param + " AND pro.id = " + jenis_produk;
			}else{
				if(currentUser.mst_product_id != null){
					//kalau ada isinya maka harus baca ke mst_master id 3 (group_product)
					param = param + " AND pro.group_product = " + currentUser.mst_product_id;
				}
			}
			
			//filter bank
			if(bank != ""){
				param = param + " AND ba.id = " + bank;
			}	
			
			//filter cabang bank
			if(cab_bank != "" && cab_bank != null){
				param = param + " AND cb.id = " + cab_bank;
			}
			
			//filter asuransi
			if(asuransi != ""){
				param = param + " AND p.asuransi_id = " + asuransi;
			}
						
			params.put("param", param);		
			params.put("status", status);					
			params.put("username", currentUser.username);
			
			return generateReport(jenisReport, params, session, request, response,null);
		}else{
			
			model.addAttribute("jenis_user", currentUser.bank_jenis);
			model.addAttribute("bank_id", currentUser.bank_id);
			model.addAttribute("bank_nama", dbService.selectListMstBank(currentUser.getBank_id()).get(0).nama);
			List<MstCabBank> lsCabbank=dbService.selectListMstCabBank(currentUser.getCab_bank_id(), currentUser.getBank_id(),null);
			if(!lsCabbank.isEmpty()){
				model.addAttribute("cab_nama",lsCabbank.get(0).nama);
				model.addAttribute("cab_id",lsCabbank.get(0).id);
			}

			model.addAttribute("product_id", currentUser.mst_product_id);
			model.addAttribute("product_name", dbService.selectListMstProduct(currentUser.getMst_product_id(), null, null,null).get(0).nama);
		/*	model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);*/
			return "report_" + jenisReport;
		}
	}
	
	@RequestMapping("/print/{id}/polis_fire")
	public String print_polis_fire(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable Integer id) 
			throws Exception {
		
		String jenisReport = "polis_fire";
		
		
		logger.debug("Halaman: REPORT " + jenisReport);		
		
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("id",id);
		params.put("bank_id",""+currentUser.getBank_jenis());
		
		Policy policy=dbService.selectPolicy(id, null, null, null);
		policy.setBank(dbService.selectBank(id));
		
		List<String> listFile=new ArrayList<String>();
		
		String location = props.getProperty("upload.polis")+"\\"+policy.getBank().mst_bank_id+"\\"+policy.getBank().mst_cab_bank_id+"\\"+policy.getSpaj_no();
		String filename="POLIS_"+policy.getSpaj_no()+".pdf";
		Utils.deleteFile(location,filename, response);
		JasperUtils.exportReportToPdf(
				props.getProperty("dir.report")+ "/"  +props.getProperty("report." + jenisReport) + ".jasper", location, filename, 
				params, getConnection(), 
				null, null, null);
		
		listFile.add(location+"\\"+filename);
		

		location = props.getProperty("upload.polis")+"\\"+policy.getBank().mst_bank_id+"\\"+policy.getBank().mst_cab_bank_id+"\\"+policy.getSpaj_no();
		filename="KWT_"+policy.getSpaj_no()+".pdf";
		jenisReport="kwitansi_fire";
		Utils.deleteFile(location,filename, response);
		JasperUtils.exportReportToPdf(
				props.getProperty("dir.report")+ "/"  +props.getProperty("report." + jenisReport) + ".jasper", location, filename, 
				params, getConnection(), 
				null, null, null);
		
		listFile.add(location+"\\"+filename);
		
		
		filename="POLIS_ALL_"+policy.getSpaj_no()+".pdf";
		Utils.deleteFile(location,filename , response);
		JasperUtils.concatPdf(listFile, new FileOutputStream(location+"\\"+filename), false);
		
		Utils.downloadFile(location+"\\"+filename, "", response, "inline");
		
		Policy tmp=new Policy();
		tmp.setId(policy.getId());
		tmp.setTgl_print(dbService.selectSysdate());
		dbService.updatePolicy(tmp);
		dbService.insertHistory(new History(1, "Print Polis", currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
		
		
		
		return null;
		
	}
	
	@RequestMapping("/print/{id}/polis_life")
	public String print_polis_life(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable Integer id) 
			throws Exception {
		
		String jenisReport = "polis_fire";
		
		
		logger.debug("Halaman: REPORT " + jenisReport);		
		
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		Policy policy=dbService.selectPolicy(id, null, null, null);
		policy.setBank(dbService.selectBank(id));
		

		Policy tmp=new Policy();
		tmp.setId(policy.getId());
		tmp.setTgl_print(dbService.selectSysdate());
		dbService.updatePolicy(tmp);
		
		dbService.insertHistory(new History(1, "Print KPS", currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
		
		String filename="KPS_"+policy.getSpaj_no()+".pdf";

		//buat directory di server bila belum ada
		String location = props.getProperty("upload.polis")+"\\"+policy.getBank().mst_bank_id+"\\"+policy.getBank().mst_cab_bank_id+"\\"+policy.getSpaj_no()+"\\";
		
		Utils.downloadFile(location, filename, response, "attached");
		return null;
		
	}
	
	@RequestMapping("/print/{id}/sppa_upload")
	public String print_sppa_upload(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable Integer id) 
			throws Exception {
		
		String jenisReport = "sppa_upload";
		
		
		logger.debug("Halaman: REPORT " + jenisReport);		
		
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		Policy policy=dbService.selectPolicy(id, null, null, null);
		policy.setBank(dbService.selectBank(id));
		

		Policy tmp=new Policy();
		//tmp.setId(policy.getId());
		//tmp.setTgl_print(dbService.selectSysdate());
		//dbService.updatePolicy(tmp);
		
		dbService.insertHistory(new History(1, "Print SPPA Upload", currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
		String filetype="pdf";
		if(!Utils.isEmpty(policy.getFiletype_spaj()))filetype=policy.getFiletype_spaj();
		String filename="SPAJ_"+policy.getSpaj_no()+"."+filetype;
		
		if(policy.getJenis()==2)filename="SPAK_"+policy.getSpaj_no()+"."+filetype;

		//buat directory di server bila belum ada
		String location = props.getProperty("upload.polis")+"\\"+policy.getBank().mst_bank_id+"\\"+policy.getBank().mst_cab_bank_id+"\\"+policy.getSpaj_no()+"\\";
		
		Utils.downloadFile(location, filename, response, "attached");
		return null;
		
	}
	
	@RequestMapping("/print/{id}/{jenisReport}")
	public String print_kuesioner(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable Integer id,@PathVariable String jenisReport) 
			throws Exception {
		
		logger.debug("Halaman: Print " + jenisReport);		
		
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		Policy policy=dbService.selectPolicy(id, null, null, null);
		policy.setBank(dbService.selectBank(id));
		
		dbService.insertHistory(new History(1, "Print "+jenisReport+" Upload", currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
		
		String filename=jenisReport.toUpperCase()+"_"+policy.getSpaj_no()+".pdf";

		//buat directory di server bila belum ada
		String location = props.getProperty("upload.polis")+"\\"+policy.getBank().mst_bank_id+"\\"+policy.getBank().mst_cab_bank_id+"\\"+policy.getSpaj_no()+"\\";
		
		Utils.downloadFile(location, filename, response, "attached");
		return null;
		
	}
	
	
	@RequestMapping("/print_spaj/{id}/{pagename}")
	public String print_spaj_fire(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable Integer id,@PathVariable String pagename) 
			throws Exception {
		
		String jenisReport = pagename;
		
		
		logger.debug("Halaman: REPORT " + jenisReport);		
		
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("id",id);
		
		Policy policy=dbService.selectPolicy(id, null, null, null);
		policy.setBank(dbService.selectBank(id));
		
		
		Policy tmp=new Policy();
		tmp.setId(policy.getId());
		tmp.setTgl_print_spaj(dbService.selectSysdate());
		dbService.updatePolicy(tmp);
		dbService.insertHistory(new History(1, "Print SPAJ", currentUser.getId(), dbService.selectSysdate(), policy.getId(), policy.getPosisi()));
		
		
		
		return generateReport(jenisReport, params, session, request, response,null);
		
	}
	
	
	
	@RequestMapping("/print/report_akseptasi")
	public String report_akseptasi(HttpSession session, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		String jenisReport = "report_akseptasi";
		String param = "";
		
		logger.debug("Halaman: REPORT " + jenisReport);		
		
		
		Integer jenis=ServletRequestUtils.getRequiredIntParameter(request, "jenis");
		String begdate=ServletRequestUtils.getStringParameter(request, "begdate",null);
		String enddate=ServletRequestUtils.getStringParameter(request, "enddate",null);
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		if(jenis==1){//KPR Life
			jenisReport+="_life";
		}else if(jenis==2){//KPR Fire
			jenisReport+="_fire";
		}else if(jenis==3){//Micro Life
			jenisReport+="_life";
		}
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","xls"); //format report
		params.put("asuransi_id",currentUser.getBank_id()); //format report
		params.put("begdate", begdate);
		params.put("enddate", enddate);
		params.put("jenis", jenis);
		
		if(dbService.selectListMstBank(currentUser.bank_id).get(0).getJenis()!=3){//klo bukan broker pake id 
			param+=" AND p.asuransi_id = $P{asuransi_id}";
		}
		
		if(!Utils.isEmpty(begdate)&&!Utils.isEmpty(enddate)){
			param+= " AND (DATE(p.beg_date) BETWEEN STR_TO_DATE($P{begdate}, '%d-%m-%Y') and STR_TO_DATE($P{enddate}, '%d-%m-%Y'))";
		}
		params.put("param", param);
		
		return generateReport(jenisReport, params, session, request, response,null);
		
	}
	
	@RequestMapping(value="/viewer/print",method={RequestMethod.GET, RequestMethod.POST})
	public String reportViewer(HttpSession session, HttpServletResponse response,HttpServletRequest request) throws ServletRequestBindingException, JRException, IOException {
		logger.debug("Halaman: Viewer Page, method: Report");
		String jenisReport = "viewer";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
		
		String search=null,begdatepaid=null,enddatepaid=null,tgl_aksep=null,tgl_aksep_end=null;
		
		
		Integer jenis_produk=ServletRequestUtils.getIntParameter(request, "jenis_produk",0);
		Integer paid=ServletRequestUtils.getIntParameter(request, "paid",-1);
		String sort=ServletRequestUtils.getStringParameter(request, "sort","id");
		String sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		
		Integer grouppolis=null;
		Integer jenispolis=null;
		
		if(jenis_produk!=0){
			jenispolis=jenis_produk;
		}else{
			grouppolis=null;
			jenispolis=null;
		}
		
		

		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		String defaultbegdate=null;
		String defaultdate=null;
		String begdate=ServletRequestUtils.getStringParameter(request, "begdate", defaultbegdate);
		String enddate=ServletRequestUtils.getStringParameter(request, "enddate", defaultdate);
		begdatepaid=ServletRequestUtils.getStringParameter(request, "begdatepaid", defaultbegdate);
		enddatepaid=ServletRequestUtils.getStringParameter(request, "enddatepaid", defaultdate);
		tgl_aksep=ServletRequestUtils.getStringParameter(request, "tgl_aksep", defaultbegdate);
		tgl_aksep_end=ServletRequestUtils.getStringParameter(request, "tgl_aksep_end", defaultbegdate);		
		
		if(Utils.isEmpty(begdate))begdate=null;
		if(Utils.isEmpty(begdatepaid))begdatepaid=null;
		if(Utils.isEmpty(tgl_aksep))tgl_aksep=null;
			
		//perhitungan paging
		
		//bila user bank (jenis = 1) dan user KC / KCP (jenis = 1 atau 2), maka user hanya bisa melihat yg ada di cabangnya saja
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Integer cab_bank = null;
		Integer asuransi_id = null;
		if(currentUser.getBank_jenis().intValue() == 1 && (currentUser.getCab_bank_jenis().intValue() == 1 || currentUser.getCab_bank_jenis().intValue() == 2)){
			cab_bank = currentUser.getCab_bank_id();
		}else if(currentUser.getBank_jenis().intValue() == 2||currentUser.getBank_jenis().intValue() == 4){
			asuransi_id=currentUser.getBank_id();
		}
		
		String param1="",param2="";
		
		grouppolis=currentUser.getMst_product_id();
		
		if(cab_bank!=null){
			param1+=" AND mcb.id = "+cab_bank;
		}
		
		if(asuransi_id!=null){
			param1+=" AND p.asuransi_id = "+asuransi_id;
		}
		
		if(paid!=null){
			if(paid==1){
				param1+=" AND pr.tgl_paid is not null "
						+ " AND pr.flag_paid =1 ";
				if(Utils.isEmpty(begdatepaid)){
					param1+=" AND DATE(pr.tgl_paid) BETWEEN STR_TO_DATE('"+begdatepaid+"', '%d-%m-%Y') and STR_TO_DATE('"+enddatepaid+"', '%d-%m-%Y') ";
				}
			}else if(paid==2){
				param1+=" AND pr.tgl_paid is not null "
						+ " AND pr.flag_paid =2 ";
				if(Utils.isEmpty(begdatepaid)){
					param1+=" AND DATE(pr.tgl_paid) BETWEEN STR_TO_DATE('"+begdatepaid+"', '%d-%m-%Y') and STR_TO_DATE('"+enddatepaid+"', '%d-%m-%Y') ";
				}
			}else if(paid==3){
				param1+=" AND pr.tgl_paid is not null "
						+ " AND pr.flag_paid =3 ";
				if(Utils.isEmpty(begdatepaid)){
					param1+=" AND DATE(pr.tgl_paid) BETWEEN STR_TO_DATE('"+begdatepaid+"', '%d-%m-%Y') and STR_TO_DATE('"+enddatepaid+"', '%d-%m-%Y') ";
				}
			}else if(paid==0){
				param1+=" AND pr.tgl_paid is  null ";
			}
		}
		
		if(grouppolis!=null){
			param1+=" and gp.jenis = "+grouppolis;
		}
		
		if(jenispolis!=null){
			param1+=" and mp.jenis = "+jenispolis;
		}
		
		if(!Utils.isEmpty(begdate)){
			param1+=" AND DATE(p.beg_date) BETWEEN STR_TO_DATE('"+begdate+"', '%d-%m-%Y') and STR_TO_DATE('"+enddate+"', '%d-%m-%Y')";
		}
		
		if(!Utils.isEmpty(tgl_aksep)&& !Utils.isEmpty(tgl_aksep_end)){
			param1+=" AND DATE(p.tgl_aksep) BETWEEN STR_TO_DATE('"+tgl_aksep+"', '%d-%m-%Y') and STR_TO_DATE('"+tgl_aksep_end+"', '%d-%m-%Y')";
		}
		Integer totalData=dbService.selectListPolisPagingCount(grouppolis,jenispolis, search,null,begdate,enddate,begdatepaid,enddatepaid,tgl_aksep,tgl_aksep_end,paid, cab_bank,asuransi_id);
		List<Policy> listPolicy = dbService.selectListPolisPaging(grouppolis,jenispolis, search, 0, null, "id", null,null,begdate,enddate,begdatepaid,enddatepaid,tgl_aksep,tgl_aksep_end,paid, cab_bank,asuransi_id);
		HashMap total=Utils.hitTotalPolicy(listPolicy);
		
		if(!Utils.isEmpty(search)){
			param2=" where (id LIKE CONCAT('%', '"+search+"', '%') OR spaj_no LIKE CONCAT('%', '"+search+"', '%') OR policy_no LIKE CONCAT('%', '"+search+"', '%') OR"+
				 " debitur LIKE CONCAT('%', '"+search+"', '%') OR  produk LIKE CONCAT('%', '"+search+"', '%') OR  createuser LIKE CONCAT('%', '"+search+"', '%')  )";
		}
		
		param2=" order by "+sort+" "+sort_type;
		
		params.put("namaProduk",dbService.selectMstProductName(jenis_produk) );
		params.put("username",currentUser.getUsername() );
		params.put("totalData",totalData );
		params.put("totalAktif",total.get("aktif") );
		params.put("totalPremi",total.get("premi") );
		params.put("totalNetPremi",total.get("premi_net") );
		params.put("totalBayar",total.get("bayar") );
		params.put("beg_date",begdate );
		params.put("end_date",enddate );
		params.put("jenis_produk",jenis_produk);
		params.put("beg_date_bayar",begdatepaid );
		params.put("end_date_bayar",enddatepaid );
		params.put("beg_date1",tgl_aksep );
		params.put("end_date1",tgl_aksep_end );
		params.put("sysdate",Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy") );
		params.put("paid",paid );
		params.put("product_id", currentUser.mst_product_id);
		params.put("bank_jenis", currentUser.bank_jenis);
		params.put("number_format", "#,##0.00;(#,##0.00)");
		params.put("param",param1 );
		params.put("param2",param2 );
		
		return generateReport(jenisReport, params, session, request, response,null);
	}
	
	/**
	 * Misal data masuk bln January 2016 ada 100 pst, sedangkan yang terbit dibulan January itu Hanya 50 pst yang sudah terbit polis,
                      maka yang keluar Nota Tagihan hanya yang 50pst yang sudah terbit polis saja, misal 50pst lg terbit di bln February 2016.
                      lalu pada bulan berikutnya February 2016 ada produksi baru sejumlah 120pst dan yang sudah terbit di bln Feb 2016 hanya 50pst
                      maka yang diterbitkan Nota Tagihan hanya 50pst utk bln Feb 2016
                      Kesimpulan utk bln January Keluar Nota Tagih     = 50pst dr 100pst (prod Jan)
                                            utk bln February ada 2 Nota Tagih    = 50pst (prod sisa Jan) dan 50pst(prod Feb)
	 * @param model
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 * @throws ServletRequestBindingException
	 * @date 29042016
	 * @author Bertho
	 */
	@RequestMapping("/nota_tagihan")
	public String nota_tagihan(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
			throws JRException, IOException, ServletRequestBindingException {
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String jenisReport = "nota_tagihan";
		String param = "", param2 = "";
		String bank = "", cab_bank = "", asuransi = "", jenis_terbit = "", jenis_produk = "";
		/*String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");*/
		
		logger.debug("Halaman: " + jenisReport);
		
		if(request.getParameter("show") != null){			
			Map<String, String> params = new HashMap<String, String>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			String begdate1=ServletRequestUtils.getRequiredStringParameter(request, "beg_date1");
			params.put("beg_date1", begdate1);
			bank = ServletRequestUtils.getRequiredStringParameter(request, "bank");
			cab_bank = ServletRequestUtils.getStringParameter(request, "cab_bank");
			asuransi = ServletRequestUtils.getStringParameter(request, "asuransi");
			String username=ServletRequestUtils.getStringParameter(request, "pejabat","....................");
			param="";
			
			
			if(!Utils.isEmpty(begdate1)){
				param += " and DATE_FORMAT(tgl_aksep, '%M %Y')='"+begdate1+"'";
			}
			
			//filter jenis produk
			if(jenis_produk != ""){
				param = param + " AND pro.id = " + jenis_produk;
			}else{
				if(currentUser.mst_product_id != null){
					//kalau ada isinya maka harus baca ke mst_master id 3 (group_product)
					param = param + " AND pro.group_product = " + currentUser.mst_product_id;
				}
			}
			
			//filter bank
			if(bank != ""){
				param = param + " AND ba.id = " + bank;
			}	
			
			//filter cabang bank
			if(cab_bank != "" && cab_bank != null){
				param = param + " AND cb.id = " + cab_bank;
			}
			
			//tampilan data, ini digunakan pada saat RIGHT JOIN supaya data yg ditampilkan gk ALL (khusus yg login bukan Broker)
			if(currentUser.bank_jenis == 2 || currentUser.bank_jenis == 4){
				param2 = " AND b.id = " + asuransi;	//ASURANSI JIWA & ASURANSI KERUGIAN
			}
						
			params.put("param", param);
			params.put("param2", param2);
			params.put("username", Utils.isEmpty(username)?"................................":username);	
			
			return generateReport(jenisReport, params, session, request, response,null);
		}else{
			model.addAttribute("jenis_user", currentUser.bank_jenis);
			model.addAttribute("jenis_user_cab", currentUser.cab_bank_jenis);
			model.addAttribute("bank_id", currentUser.bank_id);
			model.addAttribute("bank_nama", dbService.selectListMstBank(currentUser.getBank_id()).get(0).nama);
			List<MstCabBank> lsCabbank=dbService.selectListMstCabBank(currentUser.getCab_bank_id(), currentUser.getBank_id(),null);
			if(!lsCabbank.isEmpty()){
				model.addAttribute("cab_nama",lsCabbank.get(0).nama);
				model.addAttribute("cab_id",lsCabbank.get(0).id);
			}
			
			/*List<DropDown> monthList=new ArrayList<DropDown>();
			monthList.add(new DropDown("Januari","Januari"));
			monthList.add(new DropDown("Februari","Februari"));
			monthList.add(new DropDown("Maret","Maret"));
			monthList.add(new DropDown("April","April"));
			monthList.add(new DropDown("Mei","Juni"));
			monthList.add(new DropDown("Juli","Januari"));
			monthList.add(new DropDown("Januari","Januari"));
			monthList.add(new DropDown("Januari","Januari"));
			monthList.add(new DropDown("Januari","Januari"));
			monthList.add(new DropDown("Januari","Januari"));
			monthList.add(new DropDown("Januari","Januari"));
			monthList.add(new DropDown("Januari","Januari"));*/
			
			
			model.addAttribute("product_id", currentUser.mst_product_id);
			model.addAttribute("product_name", dbService.selectListMstProduct(currentUser.getMst_product_id(), null, null,null).get(0).nama);
			model.addAttribute("tgl_awal", Utils.convertDateToString(new java.util.Date(), "MMMMM yyyy"));
			/*model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);*/
			return jenisReport;
		}
	}


}