package com.maibro.service;

import static com.maibro.utils.Utils.nvl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.maibro.model.Address;
import com.maibro.model.Agent;
import com.maibro.model.Bank;
import com.maibro.model.Beneficiary;
import com.maibro.model.Claim;
import com.maibro.model.Customer;
import com.maibro.model.DropDown;
import com.maibro.model.GroupMenu;
import com.maibro.model.GroupPolicy;
import com.maibro.model.History;
import com.maibro.model.Menu;
import com.maibro.model.MstBank;
import com.maibro.model.MstCabBank;
import com.maibro.model.MstMaster;
import com.maibro.model.MstProduct;
import com.maibro.model.MstRate;
import com.maibro.model.Policy;
import com.maibro.model.Product;
import com.maibro.model.User;
import com.maibro.persistence.DbMapper;
import com.maibro.utils.Emailer;
import com.maibro.utils.SessionRegistry;
import com.maibro.utils.Utils;

/**
 * Main service object. semua business logic diletakkan disini
 * Fitur baru MyBatis, tidak perlu buat class DAO implementation lagi, cukup buat mapper interface + sql nya saja langsung bisa pakai
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:25:35 AM)
 *
 */
@Transactional
public class DbService {

	private static Logger logger = Logger.getLogger(DbService.class);
	
	@Autowired
	private DbMapper dbMapper;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	protected Emailer email;

	@Autowired
	protected Properties props;
		
	public Date selectSysdate() {
		logger.debug("selectSysdate");
		return dbMapper.selectSysdate();
	}
	
	public List<DropDown> selectDropDown(String key, String value, String table, String where, String orderby) {
		/*
		1 - Posisi Dokumen Polis (Input, Validasi Dll)
		2 - Jenis Relasi (Diri sendiri dll)
		3 - Jenis Grup Polis (KPR/MRI, Mikro)
		4 - Jenis Produk (Life, Fire)
		5 - Jenis Bangunan (Rumah/Apt, Ruko, Lainnya)
		6 - Jenis Address (Alamat, Objek Pertanggungan)
		7 - Jenis Manfaat Life (Jiwa, Jiwa + Cacat Tetap Total + Tunggakan + Denda)
		8 - Jenis Bank (Bank, Asuransi Jiwa, Broker, Asuransi Kerugian)
		*/		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("keycol", key);
		map.put("valcol", value);
		map.put("tablename", table);
		map.put("where", where);
		map.put("ordercol", orderby);
		return dbMapper.selectDropDown(map);
	}
	
	/**
	 * 
	 * @Method_name	: generateCounter
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Feb 16, 2013 11:33:23 AM
	 * @return_type : String
	 * @Description : generate no spaj and no polis
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public String generateCounter(Integer id, Integer lcounter,Integer product_id,User currentUser,String bank_id) throws DataAccessException{
		HashMap counterMap=dbMapper.selectMaxCounter(id);
		Integer counter = (Integer) counterMap.get("VALUE");
		String max= (String) counterMap.get("MAX");
		String year=Utils.convertDateToString(dbMapper.selectSysdate(), "yyyy");
		
		if(!year.equals(max)){//reset tiap tahun
			counter=1;
			max=year;
		}else{
			counter=counter+1;
		}
		
		String result =null;
		String product_kode=dbMapper.selectListMstProduct(product_id,null,null,null).get(0).kode;
		
		
		if(id==1){//spaj
			result =product_kode+year+Utils.rpad("0", "" + counter, lcounter); //FIXME:format nya belum ada
		}else if(id==2){//polis
			result =Utils.rpad("0",  bank_id, 2)+product_kode+year+Utils.rpad("0", "" + counter, lcounter);
		}
		 
		dbMapper.updateMaxCounter(id, counter, max, currentUser.id, dbMapper.selectSysdate());
		return result;
	}
	
	
	public Integer selectCountTable(String table, String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tablename", table);
		map.put("where", where);		
		return dbMapper.selectCountTable(map);
	}
	
	public User selectUser(User user) {
		logger.debug("selectUser(User user) {");
		return dbMapper.selectUser(user);
	}
	
	public void login(User currentUser, SessionRegistry sessionRegistry, HttpServletRequest request) {
		logger.debug("login(User currentUser, SessionRegistry sessionRegistry, HttpServletRequest request)");
		//bila sudah pernah login, kick session lama
		if(sessionRegistry.contains(currentUser)) sessionRegistry.kick(currentUser, request.getSession(false));
		//create session baru
		HttpSession session = request.getSession(true);
		//set login time
		currentUser.setLoginTime(dbMapper.selectSysdate());
		//set menu sesuai hak akses
		currentUser.setListMenu(dbMapper.selectMenuAccess(currentUser.getGroup_menu_id(),1));
		
		//generate menu
		currentUser.setMenuUser(Utils.generateMenu(dbMapper.selectMenuAccess(currentUser.getGroup_menu_id(),null),  request.getContextPath(), currentUser));
		
		//letakkan currentUser di session
		session.setAttribute("currentUser", currentUser);
		//letakkan currentUser di daftar user
		sessionRegistry.put(currentUser);
	}
	
	public String changePassword(User user){
		user.modifyby = user.id;
		user.modifydate = dbMapper.selectSysdate();
		user.passdecrypt = user.confirmPassword;
		dbMapper.updateUser(user);
		return "Password berhasil dirubah";
	}
	
	public List<Menu> selectMenuAccess(Integer group_menu_id) {
		logger.debug("selectMenuAccess(Integer group_menu_id)");
		return dbMapper.selectMenuAccess(group_menu_id,1);
	}
	
	public List<Policy> selectListGroupPolicyPaging(Integer jenis, String search, Integer offset, Integer rowcount, String sort, String sort_type)  {
		return dbMapper.selectListGroupPolicyPaging(jenis, search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListGroupPolicyPagingCount(Integer jenis, String search)  {
		return dbMapper.selectListGroupPolicyPagingCount(jenis, search);
	}
	
	public List<Policy> selectListPolisPaging(Integer groupjenis,Integer jenis, String search, 
			Integer offset, Integer rowcount, String sort, String sort_type,Integer posisi,
			String begdate, String enddate,String begdatepaid,String enddatepaid, String tgl_aksep,String tgl_aksep_end,Integer paid, Integer cab_bank, Integer asuransi_id)  {
		return dbMapper.selectListPolisPaging(groupjenis,jenis, search, offset, rowcount, sort, sort_type,posisi,begdate,enddate,begdatepaid,enddatepaid,tgl_aksep,tgl_aksep_end, paid, cab_bank,asuransi_id);
	}

	public Integer selectListPolisPagingCount(Integer groupjenis,Integer jenis, String search,Integer posisi,String begdate, String enddate,String begdatepaid,String enddatepaid,String tgl_aksep,String tgl_aksep_end,Integer paid, Integer cab_bank, Integer asuransi_id)  {
		return dbMapper.selectListPolisPagingCount(groupjenis,jenis, search,posisi,begdate,enddate,begdatepaid,enddatepaid,tgl_aksep,tgl_aksep_end,  paid, cab_bank,asuransi_id);
	}
	
	public List<Policy> selectListPolisPaging(Integer groupjenis,Integer jenis, String search, 
			Integer offset, Integer rowcount, String sort, String sort_type,Integer posisi,
			String begdate, String enddate,String begdatepaid,String enddatepaid,Integer paid, Integer cab_bank, Integer asuransi_id)  {
		return dbMapper.selectListPolisPaging(groupjenis,jenis, search, offset, rowcount, sort, sort_type,posisi,begdate,enddate,begdatepaid,enddatepaid,null,null, paid, cab_bank,asuransi_id);
	}

	public Integer selectListPolisPagingCount(Integer groupjenis,Integer jenis, String search,Integer posisi,String begdate, String enddate,String begdatepaid,String enddatepaid,Integer paid, Integer cab_bank, Integer asuransi_id)  {
		return dbMapper.selectListPolisPagingCount(groupjenis,jenis, search,posisi,begdate,enddate,begdatepaid,enddatepaid,null,null,  paid, cab_bank,asuransi_id);
	}
	
	public Policy selectPolicy(Integer id,Integer group_policy_id,Integer jenis,String spaj_no){
		logger.debug("selectPolicy(Integer id)");
		return dbMapper.selectPolicy(id, group_policy_id, jenis,spaj_no);
	}

	public String savePolisAJK(Policy policy, User currentUser){
		logger.debug("savePolicy(Policy policy, User currentUser)");
		
		String pesan=null;
		
		//INSERT
		if("NEW".equals(policy.getMode())){
			//set createby dan createdate
			policy.setCreateby(currentUser.getId());
			policy.setCreatedate(dbMapper.selectSysdate());
			policy.getCustomer().getAddress().setCreateby(policy.getCreateby());
			policy.getCustomer().getAddress().setCreatedate(policy.getCreatedate());
			policy.getCustomer().setCreateby(policy.getCreateby());
			policy.getCustomer().setCreatedate(policy.getCreatedate());
			policy.getBeneficiary().setCreateby(policy.getCreateby());
			policy.getBeneficiary().setCreatedate(policy.getCreatedate());
			policy.getBank().setCreateby(policy.getCreateby());
			policy.getBank().setCreatedate(policy.getCreatedate());
			policy.getAgent().setCreateby(policy.getCreateby());
			policy.getAgent().setCreatedate(policy.getCreatedate());
			policy.getProduct().setCreateby(policy.getCreateby());
			policy.getProduct().setCreatedate(policy.getCreatedate());
			policy.getHistory().setCreateby(policy.getCreateby());
			policy.getHistory().setCreatedate(policy.getCreatedate());
			
			policy.setSpaj_no(generateCounter(1, 5, policy.getProduct().getMst_product_id(), currentUser,null));
			policy.setPosisi(1); //posisi 1=input
			policy.setPay_mode(1); //paymode 1=sekaligus
			policy.getHistory().setPosisi(1); //posisi 1=input
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			policy.getHistory().setKeterangan("INPUT POLIS");
			
			dbMapper.insertAddress(policy.getCustomer().getAddress());
			policy.getCustomer().setAddress_id(policy.getCustomer().getAddress().getId());

			dbMapper.insertCustomer(policy.getCustomer());
			policy.setCustomer_id(policy.getCustomer().getId());

			dbMapper.insertPolicy(policy);

			//set policy_id di semua tabel2 anak
			policy.getBeneficiary().setPolicy_id(policy.getId());
			policy.getBank().setPolicy_id(policy.getId());
			policy.getAgent().setPolicy_id(policy.getId());
			policy.getProduct().setPolicy_id(policy.getId());
			policy.getHistory().setPolicy_id(policy.getId());
			
			//insert2
			dbMapper.insertBeneficiary(policy.getBeneficiary());
			dbMapper.insertBank(policy.getBank());
			dbMapper.insertAgent(policy.getAgent());
			dbMapper.insertProduct(policy.getProduct());
			dbMapper.insertHistory(policy.getHistory());
			
			pesan = "Polis berhasil ditambahkan";
			
		//transfer
		}else if("TRANSFER".equals(policy.getMode())){
			String keterangan=selectListMstMaster(1,policy.getPosisi()).get(0).keterangan;
			policy.setPosisi(Utils.tranferPosisi(policy.getPosisi()));
			//klo micro ga ada upload kps & print kps
			if(policy.getJenis()==3){
				if(policy.getPosisi()==4)policy.setPosisi(5);
				else if(policy.getPosisi()==6)policy.setPosisi(99);
			}else{
				if(policy.getPosisi()==3)policy.setPosisi(4);
			}
			keterangan+=" ke "+selectListMstMaster(1,policy.getPosisi()).get(0).keterangan;
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			dbMapper.updatePolicy(policy);
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setKeterangan("Transfer Polis Life (KPR) dari"+keterangan);
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			dbMapper.insertHistory(policy.getHistory());
			pesan = "Polis berhasil ditransfer";
		
		//VALIDASI
		}else if("VALIDASI".equals(policy.getMode())){
			
			//policy.setPosisi(Utils.tranferPosisi(selectPolicy(policy.getId(),null,null,null).getPosisi()));
			
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			policy.setTgl_aksep(selectSysdate());
			dbMapper.updatePolicy(policy);
			
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			
			int flag_aksep_mb = policy.getFlag_akseptasi_mb().intValue();
			if(flag_aksep_mb == 1){ //aksep
				policy.getHistory().setKeterangan("Validasi Polis Life (Status = AKSEP)");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = AKSEP";
			}else if(flag_aksep_mb == 2){ //pending
				policy.getHistory().setKeterangan("Validasi Polis Life (Status = PENDING)");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = PENDING";
			}else if(flag_aksep_mb == 3){ //tolak
				policy.getHistory().setPosisi(95);
				policy.getHistory().setKeterangan("Validasi Polis Life (Status = TOLAK, Alasan = " + policy.getAsuransi_desc() + ")");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = TOLAK";
				policy.setPosisi(95);
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);				
			}else{
				throw new RuntimeException("Ada error");
			}
		
		//AKSEPTASI
		}else if("AKSEPTASI".equals(policy.getMode())){
			
			if(policy.getFlag_akseptasi()==1){
				
				policy.setPosisi(Utils.tranferPosisi(selectPolicy(policy.getId(),null,null,null).getPosisi()));//dari 3 ke 4 upload kps
				//klo micro ga ada upload kps & print kps
				if(policy.getJenis()==3){
					if(policy.getPosisi()==4)policy.setPosisi(5);
					else if(policy.getPosisi()==6)policy.setPosisi(99);
				}
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);
			
				policy.getProduct().setModifyby(policy.getModifyby());
				policy.getProduct().setModifydate(policy.getModifydate());
				policy.getProduct().setPolicy_id(policy.getId());
				dbMapper.updateProduct(policy.getProduct());
				
				policy.getHistory().setCreateby(currentUser.getId());
				policy.getHistory().setCreatedate(dbMapper.selectSysdate());
				policy.getHistory().setPolicy_id(policy.getId());
				policy.getHistory().setPosisi(policy.getPosisi());
				policy.getHistory().setKeterangan("Akseptasi Polis Life");
				policy.getHistory().setJenis(1);//jenis 1 = history policy
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil di akseptasi";
			}else if(policy.getFlag_akseptasi()==2){
				
				policy.setPosisi(95);//dibatalkan
				
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);
				
				policy.getHistory().setCreateby(currentUser.getId());
				policy.getHistory().setCreatedate(dbMapper.selectSysdate());
				policy.getHistory().setPolicy_id(policy.getId());
				policy.getHistory().setPosisi(policy.getPosisi());
				policy.getHistory().setKeterangan("Polis Life Di TOLAK ASURANSI");
				policy.getHistory().setJenis(1);//jenis 1 = history policy
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil di akseptasi";
			}
		//UPDATE
		}else{
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			policy.getCustomer().getAddress().setModifyby(policy.getModifyby());
			policy.getCustomer().getAddress().setModifydate(policy.getModifydate());
			policy.getCustomer().setModifyby(policy.getModifyby());
			policy.getCustomer().setModifydate(policy.getModifydate());
			policy.getProduct().setModifyby(policy.getModifyby());
			policy.getProduct().setModifydate(policy.getModifydate());
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getBank().setModifyby(policy.getModifyby());
			policy.getBank().setModifydate(policy.getModifydate());
			
			policy.getBank().setPolicy_id(policy.getId());
			policy.getProduct().setPolicy_id(policy.getId());
			policy.getHistory().setPolicy_id(policy.getId());
			
			dbMapper.updateAddress(policy.getCustomer().getAddress());
			dbMapper.updateCustomer(policy.getCustomer());
			dbMapper.updatePolicy(policy);
			try{
				dbMapper.updateBeneficiary(policy.getBeneficiary());
			}catch(Exception e){
				
			}
			dbMapper.updateBank(policy.getBank());
			dbMapper.updateAgent(policy.getAgent());
			dbMapper.updateProduct(policy.getProduct());
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setKeterangan("Update Polis Life");
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			dbMapper.insertHistory(policy.getHistory());
			pesan = "Polis berhasil diupdate";
		}
		
		return pesan;
	}
	
	
	
	public String savePolicyMicro(Policy policy, User currentUser){
		logger.debug("savePolicy(Policy policy, User currentUser)");
		
		String pesan=null;
		
		//INSERT
		if("NEW".equals(policy.getMode())){
			policy.setSpaj_no(generateCounter(1, 5, policy.getProduct().getMst_product_id(), currentUser,null));
			//set createby dan createdate
			policy.setCreateby(currentUser.getId());
			policy.setCreatedate(dbMapper.selectSysdate());
			policy.getCustomer().getAddress().setCreateby(policy.getCreateby());
			policy.getCustomer().getAddress().setCreatedate(policy.getCreatedate());
			policy.getCustomer().setCreateby(policy.getCreateby());
			policy.getCustomer().setCreatedate(policy.getCreatedate());
			policy.getProduct().setCreateby(policy.getCreateby());
			policy.getProduct().setCreatedate(policy.getCreatedate());
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getBank().setCreateby(policy.getCreateby());
			policy.getBank().setCreatedate(policy.getCreatedate());
			
			dbMapper.insertAddress(policy.getCustomer().getAddress());
			
			policy.getCustomer().setAddress_id(policy.getCustomer().getAddress().getId());
			policy.getCustomer().setObjek_pertanggungan(policy.getCustomer().getAddress().getId());
			
			dbMapper.insertCustomer(policy.getCustomer());
			
			policy.setCustomer_id(policy.getCustomer().getId());
			
			policy.setPosisi(2);//klo upload micro langsung ke posisi validasi
			
			dbMapper.insertPolicy(policy);

			//set policy_id di semua tabel2 anak
		
			policy.getBank().setPolicy_id(policy.getId());
			policy.getProduct().setPolicy_id(policy.getId());
			policy.getHistory().setPolicy_id(policy.getId());
			
			
			//insert2
			
			dbMapper.insertProduct(policy.getProduct());
			dbMapper.insertBank(policy.getBank());
			
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setKeterangan("Input Polis Life (Mikro)");
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			dbMapper.insertHistory(policy.getHistory());
			
			
			pesan = "Polis berhasil ditambahkan";
			
		//transfer
		}else if("TRANSFER".equals(policy.getMode())){
			String keterangan=selectListMstMaster(1,policy.getPosisi()).get(0).keterangan;
			policy.setPosisi(Utils.tranferPosisi(policy.getPosisi()));
			//klo micro ga ada upload kps & print kps
			if(policy.getJenis()==3){
				if(policy.getPosisi()==3)policy.setPosisi(4);
				else if(policy.getPosisi()==6)policy.setPosisi(99);
			}
			 keterangan+=" ke "+selectListMstMaster(1,policy.getPosisi()).get(0).keterangan;
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			dbMapper.updatePolicy(policy);
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setKeterangan("Transfer Polis Life (Mikro) dari"+keterangan);
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			dbMapper.insertHistory(policy.getHistory());
			pesan = "Polis berhasil ditransfer";
		
			//VALIDASI
		}else if("VALIDASI".equals(policy.getMode())){
			
//			policy.setPosisi(Utils.tranferPosisi(selectPolicy(policy.getId(),null,null,null).getPosisi()));
			
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			policy.setTgl_aksep(selectSysdate());
			dbMapper.updatePolicy(policy);
			
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			
			int flag_aksep_mb = policy.getFlag_akseptasi_mb().intValue();
			if(flag_aksep_mb == 1){ //aksep
				policy.getHistory().setKeterangan("Validasi Polis Micro (Status = AKSEP)");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = AKSEP";
			}else if(flag_aksep_mb == 2){ //pending
				policy.getHistory().setKeterangan("Validasi Polis Micro (Status = PENDING)");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = PENDING";
			}else if(flag_aksep_mb == 3){ //tolak
				policy.getHistory().setPosisi(95);
				policy.getHistory().setKeterangan("Validasi Polis Micro (Status = TOLAK, Alasan = " + policy.getAsuransi_desc() + ")");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = TOLAK";
				policy.setPosisi(95);
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);				
			}else{
				throw new RuntimeException("Ada error");
			}

		//AKSEPTASI
		}else if("AKSEPTASI".equals(policy.getMode())){
			
			if(policy.getFlag_akseptasi()==1){
				
				policy.setPosisi(Utils.tranferPosisi(selectPolicy(policy.getId(),null,null,null).getPosisi()));//dari 3 ke 4 upload kps
				//klo micro ga ada upload kps & print kps
				if(policy.getJenis()==3){
					if(policy.getPosisi()==4)policy.setPosisi(5);
					else if(policy.getPosisi()==6)policy.setPosisi(99);
				}
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);
			
				policy.getProduct().setModifyby(policy.getModifyby());
				policy.getProduct().setModifydate(policy.getModifydate());
				policy.getProduct().setPolicy_id(policy.getId());
				dbMapper.updateProduct(policy.getProduct());
				
				policy.getHistory().setCreateby(currentUser.getId());
				policy.getHistory().setCreatedate(dbMapper.selectSysdate());
				policy.getHistory().setPolicy_id(policy.getId());
				policy.getHistory().setPosisi(policy.getPosisi());
				policy.getHistory().setKeterangan("Akseptasi Polis Life (Mikro)");
				policy.getHistory().setJenis(1);//jenis 1 = history policy
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil di akseptasi";
			}else if(policy.getFlag_akseptasi()==2){
				
				policy.setPosisi(95);//dibatalkan
				
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);
				
				policy.getHistory().setCreateby(currentUser.getId());
				policy.getHistory().setCreatedate(dbMapper.selectSysdate());
				policy.getHistory().setPolicy_id(policy.getId());
				policy.getHistory().setPosisi(policy.getPosisi());
				policy.getHistory().setKeterangan("Polis Life (Mikro) Di TOLAK ASURANSI");
				policy.getHistory().setJenis(1);//jenis 1 = history policy
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil di akseptasi";
			}

		//UPDATE
		}else{
			//set createby dan createdate
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			policy.getCustomer().getAddress().setModifyby(policy.getModifyby());
			policy.getCustomer().getAddress().setModifydate(policy.getModifydate());
			policy.getCustomer().setModifyby(policy.getModifyby());
			policy.getCustomer().setModifydate(policy.getModifydate());
			policy.getProduct().setModifyby(policy.getModifyby());
			policy.getProduct().setModifydate(policy.getModifydate());
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getBank().setModifyby(policy.getModifyby());
			policy.getBank().setModifydate(policy.getModifydate());
			
			policy.getBank().setPolicy_id(policy.getId());
			policy.getProduct().setPolicy_id(policy.getId());
			policy.getHistory().setPolicy_id(policy.getId());
			
			dbMapper.updateAddress(policy.getCustomer().getAddress());
			dbMapper.updateCustomer(policy.getCustomer());
			dbMapper.updatePolicy(policy);
			policy.getHistory().setPolicy_id(policy.getId());
			dbMapper.updateProduct(policy.getProduct());
			policy.getHistory().setPosisi(policy.getPosisi());
			
		
			policy.getHistory().setKeterangan("Update Polis Life (Mikro)");
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			
			dbMapper.insertHistory(policy.getHistory());
			
			pesan = "Polis berhasil diupdate";
		}
		
		return pesan;
	}
	
	public int updatePolicy(Policy policy) {
		return dbMapper.updatePolicy(policy);
	}
	
	public Integer insertMstBank(MstBank mstBank) {
		return dbMapper.insertMstBank(mstBank);
	}
	public Integer selectTestDB() {
		return dbMapper.selectTestDB();
	}
	public void updateTestDB(Integer toggle) {
		dbMapper.updateTestDB(toggle);
	}
	public Integer insertMstCabBank(MstCabBank mstCabBank) {
		return dbMapper.insertMstCabBank(mstCabBank);
	}
	public Integer insertMstProduct(MstProduct mstProduct) {
		return dbMapper.insertMstProduct(mstProduct);
	}
	public Integer insertMstRate(MstRate mstRate) {
		return dbMapper.insertMstRate(mstRate);
	}
	
	public void updateMstBank(MstBank mstBank) {
		dbMapper.updateMstBank(mstBank);
	}
	public void updateMstCabBank(MstCabBank mstCabBank) {
		 dbMapper.updateMstCabBank(mstCabBank);
	}
	public void updateMstProduct(MstProduct mstProduct) {
		 dbMapper.updateMstProduct(mstProduct);
	}
	public void updateMstRate(MstRate mstRate) {
		 dbMapper.updateMstRate(mstRate);
	}
	
	public List<MstProduct> selectListMstProduct(Integer id,Integer jenis,Integer mst_bank_id,Integer group_product) {
		logger.debug("selectListMstProduct(Integer id)");
		return dbMapper.selectListMstProduct(id,jenis,mst_bank_id,group_product);
	}
		

	public List<MstProduct> selectListMstProductPaging(String search,  Integer offset, Integer rowcount,String sort,String sort_type)  {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("rowcount", rowcount);
		map.put("sort", sort);
		map.put("sort_type", sort_type);
		map.put("search", search);
		return dbMapper.selectListMstProductPaging(map);
	}
	

	public Integer selectListMstProductPagingCount(String search)  {
		return dbMapper.selectListMstProductPagingCount(search);
	}
	
	public String saveMstProduct(MstProduct mstproduct, User currentUser){
		logger.debug("saveUser(User user, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(mstproduct.mode)){
			mstproduct.active=1;
			mstproduct.createby = currentUser.id;
			mstproduct.createdate = dbMapper.selectSysdate();
			mstproduct.group_product=mstproduct.jenis==3?2:1;
			mstproduct.id=dbMapper.insertMstProduct(mstproduct);
			pesan = messageSource.getMessage("submitsuccess"+mstproduct.mode, new String[]{"Master Product",""+mstproduct.nama},null);
		}else if("DELETE".equals(mstproduct.mode)){
			mstproduct.active=0;
			mstproduct.modifyby = currentUser.id;
			mstproduct.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMstProduct(mstproduct);
			pesan = messageSource.getMessage("submitsuccess"+mstproduct.mode, new String[]{"Master Product",""+mstproduct.nama},null);
		}else {
			mstproduct.modifyby = currentUser.id;
			mstproduct.modifydate = dbMapper.selectSysdate();
			mstproduct.group_product=mstproduct.jenis==3?2:1;
			dbMapper.updateMstProduct(mstproduct);
			pesan = messageSource.getMessage("submitsuccess"+mstproduct.mode, new String[]{"Master Product",""+mstproduct.nama},null);
		}
		
		
		return pesan;
	}

	public List<MstRate> selectListMstRate(Integer id, Integer mst_product_id,Integer tenor,Integer umur,Integer jenis_bangunan,Integer jenis_rate) {
		logger.debug("selectListMstRate(Integer id, Integer mst_product_id)");
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("mst_product_id", mst_product_id);
		map.put("tenor", tenor);
		map.put("umur", umur);
		map.put("jenis_bangunan", jenis_bangunan);
		map.put("jenis_rate", jenis_rate);
		return dbMapper.selectListMstRate(map);
	}
		     

	public List<MstRate> selectListMstRatePaging(String search,  Integer offset, Integer rowcount,String sort,String sort_type, Integer mst_product_id)  {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("rowcount", rowcount);
		map.put("sort", sort);
		map.put("sort_type", sort_type);
		map.put("search", search);
		map.put("mst_product_id", mst_product_id);
		return dbMapper.selectListMstRatePaging(map);
	}
	

	public Integer selectListMstRatePagingCount(String search, Integer mst_product_id)  {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("search", search);
		map.put("mst_product_id", mst_product_id);
		return dbMapper.selectListMstRatePagingCount(map);
	}
	
	public Product selectProduct(Integer id) {
		return dbMapper.selectProduct(id);
	}
	
	public Beneficiary selectBeneficiary(Integer id) {
		return dbMapper.selectBeneficiary(id);
	}
	
	public Bank selectBank(Integer id) {
		return dbMapper.selectBank(id);
	}
	
	public Customer selectCustomer(Integer id) {
		return dbMapper.selectCustomer(id);
	}
	
	public Address selectAddress(Integer id) {
		return dbMapper.selectAddress(id);
	}
	
	public Agent selectAgent(Integer id) {
		return dbMapper.selectAgent(id);
	}
	
	
	public String saveMstRate(List<MstRate> lsmstrate, User currentUser){
		String pesan=null;
		for(MstRate mstrate:lsmstrate){
			List<MstRate>lmstRate=selectListMstRate(null, mstrate.mst_product_id,mstrate.tenor,mstrate.umur,mstrate.jenis_bangunan,mstrate.jenis_rate);
			
			if(lmstRate.isEmpty()){
				mstrate.mode="NEW";
			}else{
				mstrate.mode="EDIT";
				mstrate.id=lmstRate.get(0).getId();
			}
			
			pesan=saveMstRate(mstrate, currentUser);
		}
		return pesan;
	}
	
	public String saveMstRate(MstRate mstrate, User currentUser){
		logger.debug("saveMstRate(MstRate mstrate, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(mstrate.mode)){
			mstrate.active=1;
			mstrate.createby = currentUser.id;
			mstrate.createdate = dbMapper.selectSysdate();
			mstrate.id=dbMapper.insertMstRate(mstrate);
			
			pesan = messageSource.getMessage("submitsuccess"+mstrate.mode, new String[]{"Master Rate",""+mstrate.id},null);
		}else if("DELETE".equals(mstrate.mode)){			
			dbMapper.deleteMstRate(mstrate);
			pesan = messageSource.getMessage("submitsuccess"+mstrate.mode, new String[]{"Master Rate",""+mstrate.id},null);
		}else {
			mstrate.modifyby = currentUser.id;
			mstrate.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMstRate(mstrate);
			pesan = messageSource.getMessage("submitsuccess"+mstrate.mode, new String[]{"Master Rate",""+mstrate.id},null);
		}
		
		if(!Utils.isEmpty(mstrate.getFileUpload()))
			dbMapper.insertHistory(new History(4, mstrate.getFileUpload(), currentUser.getId(), selectSysdate(), null, null));
		
		return pesan;
	}
	
	public String savePolicyFire(Policy policy, User currentUser){

		logger.debug("savePolicy(Policy policy, User currentUser)");
		
		
		
		String pesan=null;
		
		//INSERT
		if("NEW".equals(policy.getMode())){
			//set createby dan createdate
			policy.setCreateby(currentUser.getId());
			policy.setCreatedate(dbMapper.selectSysdate());
			policy.getCustomer().getAddressInsured().setCreateby(policy.getCreateby());
			policy.getCustomer().getAddressInsured().setCreatedate(policy.getCreatedate());
			policy.getCustomer().getAddress().setCreateby(policy.getCreateby());
			policy.getCustomer().getAddress().setCreatedate(policy.getCreatedate());
			policy.getCustomer().setCreateby(policy.getCreateby());
			policy.getCustomer().setCreatedate(policy.getCreatedate());
			policy.getBank().setCreateby(policy.getCreateby());
			policy.getBank().setCreatedate(policy.getCreatedate());
			policy.getAgent().setCreateby(policy.getCreateby());
			policy.getAgent().setCreatedate(policy.getCreatedate());
			policy.getProduct().setCreateby(policy.getCreateby());
			policy.getProduct().setCreatedate(policy.getCreatedate());
			policy.setHistory(new History());
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			
			policy.setSpaj_no(generateCounter(1, 5, policy.getProduct().getMst_product_id(), currentUser,null));
			policy.setPosisi(1); //posisi 1=input
			policy.setPay_mode(1); //paymode 1=sekaligus
			policy.getHistory().setPosisi(1); //posisi 1=input
			policy.getHistory().setKeterangan("INPUT POLIS");
			policy.getHistory().setJenis(1);//jenis 1 = history policy

			dbMapper.insertAddress(policy.getCustomer().getAddress());
			dbMapper.insertAddress(policy.getCustomer().getAddressInsured());
			
			policy.getCustomer().setAddress_id(policy.getCustomer().getAddress().getId());
			policy.getCustomer().setObjek_pertanggungan(policy.getCustomer().getAddressInsured().getId());
			
			dbMapper.insertCustomer(policy.getCustomer());
			policy.setCustomer_id(policy.getCustomer().getId());
			
			dbMapper.insertPolicy(policy);

			//set policy_id di semua tabel2 anak
			policy.getBeneficiary().setPolicy_id(policy.getId());
			policy.getBank().setPolicy_id(policy.getId());
			policy.getAgent().setPolicy_id(policy.getId());
			policy.getProduct().setPolicy_id(policy.getId());
			policy.getHistory().setPolicy_id(policy.getId());
			
			//insert2
			dbMapper.insertBank(policy.getBank());
			dbMapper.insertAgent(policy.getAgent());
			dbMapper.insertProduct(policy.getProduct());
			dbMapper.insertHistory(policy.getHistory());
			pesan = "Polis berhasil ditambahkan";
			
			//transfer
		}else if("TRANSFER".equals(policy.getMode())){
			String keterangan=selectListMstMaster(1,policy.getPosisi()).get(0).keterangan;
			policy.setPosisi(Utils.tranferPosisi(policy.getPosisi()));
			if(policy.getPosisi()==3){
				if(policy.getTgl_upload_spaj()==null){
					policy.setPosisi(1);
				}else{
					policy.setPosisi(5);//tidak ada proses akseptasi
					
					policy.setPolicy_no(generateCounter(2, 5, selectProduct(policy.getId()).mst_product_id, currentUser,""+selectPolicy(policy.getId(),null,null,null).getAsuransi_id()));
					policy.setFlag_akseptasi(1);
					policy.setDesc_akseptasi("Tidak perlu akseptasi");
					
					policy.getProduct().setModifyby(policy.getModifyby());
					policy.getProduct().setModifydate(policy.getModifydate());
					policy.getProduct().setPolicy_id(policy.getId());
					dbMapper.updateProduct(policy.getProduct());
					
					policy.getHistory().setCreateby(currentUser.getId());
					policy.getHistory().setCreatedate(dbMapper.selectSysdate());
					policy.getHistory().setPolicy_id(policy.getId());
					policy.getHistory().setPosisi(policy.getPosisi());
					policy.getHistory().setKeterangan("Akseptasi Polis Fire");
					policy.getHistory().setJenis(1);//jenis 1 = history policy
					dbMapper.insertHistory(policy.getHistory());
				}
			}
			keterangan+=" ke "+selectListMstMaster(1,policy.getPosisi()).get(0).keterangan;
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			dbMapper.updatePolicy(policy);
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setKeterangan("Transfer Polis Fire (KPR) dari"+keterangan);
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			dbMapper.insertHistory(policy.getHistory());
			pesan = "Polis berhasil ditransfer";
		
			//VALIDASI
		}else if("VALIDASI".equals(policy.getMode())){
			
//			policy.setPosisi(Utils.tranferPosisi(selectPolicy(policy.getId(),null,null,null).getPosisi()));
			
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			policy.setTgl_aksep(selectSysdate());
			dbMapper.updatePolicy(policy);

			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			
			if(policy.getProduct().getPremi()==null){
				if(policy.getRate()!=null)policy.getProduct().rate=policy.getRate();
				policy.getProduct().premi=policy.getPremi();
				policy.getProduct().deductible=policy.getDeductible();
				dbMapper.updateProduct(policy.getProduct());
			}
			
			int flag_aksep_mb = policy.getFlag_akseptasi_mb().intValue();
			if(flag_aksep_mb == 1){ //aksep
				policy.getHistory().setKeterangan("Validasi Polis Fire (Status = AKSEP)");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = AKSEP";
			}else if(flag_aksep_mb == 2){ //pending
				policy.getHistory().setKeterangan("Validasi Polis Fire (Status = PENDING)");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = PENDING";
			}else if(flag_aksep_mb == 3){ //tolak
				policy.getHistory().setPosisi(95);
				policy.getHistory().setKeterangan("Validasi Polis Fire (Status = TOLAK, Alasan = " + policy.getAsuransi_desc() + ")");
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil divalidasi dengan status = TOLAK";
				policy.setPosisi(95);
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);				
			}else{
				throw new RuntimeException("Ada error");
			}
			
		//AKSEPTASI
		}else if("AKSEPTASI".equals(policy.getMode())){
			
			if(policy.getFlag_akseptasi()==1){
				
				policy.setPosisi(Utils.tranferPosisi(selectPolicy(policy.getId(),null,null,null).getPosisi()+1));//dari 3 ke 5 Payment -- sudah bisa print juga
				
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				policy.setPolicy_no(generateCounter(2, 5, selectProduct(policy.getId()).mst_product_id, currentUser,""+selectPolicy(policy.getId(),null,null,null).getAsuransi_id()));
				dbMapper.updatePolicy(policy);
			
				policy.getProduct().setModifyby(policy.getModifyby());
				policy.getProduct().setModifydate(policy.getModifydate());
				policy.getProduct().setPolicy_id(policy.getId());
				dbMapper.updateProduct(policy.getProduct());
				
				policy.getHistory().setCreateby(currentUser.getId());
				policy.getHistory().setCreatedate(dbMapper.selectSysdate());
				policy.getHistory().setPolicy_id(policy.getId());
				policy.getHistory().setPosisi(policy.getPosisi());
				policy.getHistory().setKeterangan("Akseptasi Polis Fire");
				policy.getHistory().setJenis(1);//jenis 1 = history policy
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil di akseptasi";
			}else if(policy.getFlag_akseptasi()==2){
				
				policy.setPosisi(95);//dibatalkan
				
				policy.setModifyby(currentUser.getId());
				policy.setModifydate(dbMapper.selectSysdate());
				dbMapper.updatePolicy(policy);
				
				policy.getHistory().setCreateby(currentUser.getId());
				policy.getHistory().setCreatedate(dbMapper.selectSysdate());
				policy.getHistory().setPolicy_id(policy.getId());
				policy.getHistory().setPosisi(policy.getPosisi());
				policy.getHistory().setKeterangan("Polis Fire Di TOLAK ASURANSI");
				policy.getHistory().setJenis(1);//jenis 1 = history policy
				dbMapper.insertHistory(policy.getHistory());
				pesan = "Polis berhasil di akseptasi";
			}
			//UPDATE
		}else{
			policy.setModifyby(currentUser.getId());
			policy.setModifydate(dbMapper.selectSysdate());
			policy.getCustomer().getAddress().setModifyby(policy.getModifyby());
			policy.getCustomer().getAddress().setModifydate(policy.getModifydate());
			policy.getCustomer().setModifyby(policy.getModifyby());
			policy.getCustomer().setModifydate(policy.getModifydate());
			policy.getProduct().setModifyby(policy.getModifyby());
			policy.getProduct().setModifydate(policy.getModifydate());
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getBank().setModifyby(policy.getModifyby());
			policy.getBank().setModifydate(policy.getModifydate());
			
			policy.getAgent().setPolicy_id(policy.getId());
			policy.getBank().setPolicy_id(policy.getId());
			policy.getProduct().setPolicy_id(policy.getId());
			policy.getHistory().setPolicy_id(policy.getId());
			dbMapper.updateAddress(policy.getCustomer().getAddress());
			dbMapper.updateAddress(policy.getCustomer().getAddressInsured());
			dbMapper.updateCustomer(policy.getCustomer());
			dbMapper.updatePolicy(policy);
			dbMapper.updateBank(policy.getBank());
			dbMapper.updateProduct(policy.getProduct());
			dbMapper.updateAgent(policy.getAgent());
			policy.getHistory().setCreateby(currentUser.getId());
			policy.getHistory().setCreatedate(dbMapper.selectSysdate());
			policy.getHistory().setPolicy_id(policy.getId());
			policy.getHistory().setPosisi(policy.getPosisi());
			policy.getHistory().setKeterangan("Update Polis Fire");
			policy.getHistory().setJenis(1);//jenis 1 = history policy
			dbMapper.insertHistory(policy.getHistory());
			pesan = "Polis berhasil diupdate";
		}
		
		return pesan;
	
	}
	
	
	public List<MstBank> selectListMstBank(Integer id) {
		logger.debug("selectListMstBank(Integer id)");
		return dbMapper.selectListMstBank(id);
	}
		
	public List<MstBank> selectListMstBankPaging(String search,  Integer offset, Integer rowcount,String sort,String sort_type)  {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("rowcount", rowcount);
		map.put("sort", sort);
		map.put("sort_type", sort_type);
		map.put("search", search);
		return dbMapper.selectListMstBankPaging(map);
	}
	
	public Integer selectListMstBankPagingCount(String search)  {
		return dbMapper.selectListMstBankPagingCount(search);
	}
	
	public String saveMstBank(MstBank mstbank, User currentUser){
		logger.debug("saveUser(User user, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(mstbank.mode)){
			mstbank.active=1;
			mstbank.createby = currentUser.id;
			mstbank.createdate = dbMapper.selectSysdate();
			mstbank.id=dbMapper.insertMstBank(mstbank);
			pesan = messageSource.getMessage("submitsuccess"+mstbank.mode, new String[]{"Master Perusahaan",""+mstbank.nama},null);
		}else if("DELETE".equals(mstbank.mode)){
			mstbank.active=0;
			mstbank.createby = currentUser.id;
			mstbank.createdate = dbMapper.selectSysdate();
			dbMapper.updateMstBank(mstbank);
			pesan = messageSource.getMessage("submitsuccess"+mstbank.mode, new String[]{"Master Perusahaan",""+mstbank.nama},null);
		}else {
			mstbank.modifyby = currentUser.id;
			mstbank.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMstBank(mstbank);
			pesan = messageSource.getMessage("submitsuccess"+mstbank.mode, new String[]{"Master Perusahaan",""+mstbank.nama},null);
		}

		return pesan;
	}
	
	public String saveClaim(Claim claim, User currentUser){
		logger.debug("saveUser(User user, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(claim.mode)){
			claim.posisi=1;
			claim.createby = currentUser.id;
			claim.createdate = dbMapper.selectSysdate();
			dbMapper.insertClaim(claim);
			pesan = "Klaim ID "+claim.id+" berhasil diinput";
			dbMapper.insertHistory(new History(8, "INPUT KLAIM", currentUser.id, claim.createdate, claim.policy_id, claim.posisi));
		}else if("TRANSFER".equals(claim.mode)){
			claim.posisi=Utils.tranferPosisiKlaim(1);
			claim.modifyby = currentUser.id;
			claim.modifydate = dbMapper.selectSysdate();
			dbMapper.updateClaim(claim);
			pesan = "Klaim ID "+claim.id+" berhasil ditransfer";
			dbMapper.insertHistory(new History(8, "Transfer KLAIM", currentUser.id, claim.modifydate, claim.policy_id, claim.posisi));
		}else if("VALIDASI".equals(claim.mode)){
			if(claim.statusClaim==1){
				claim.posisi=Utils.tranferPosisiKlaim(2);
			}else{
				claim.posisi=4;
			}
			claim.modifyby = currentUser.id;
			claim.modifydate = dbMapper.selectSysdate();
			dbMapper.updateClaim(claim);
			pesan = "Klaim ID "+claim.id+" berhasil divalidasi";
			dbMapper.insertHistory(new History(8, "Validasi KLAIM", currentUser.id, claim.modifydate, claim.policy_id, claim.posisi));
		}else if("AKSEPTASI".equals(claim.mode)){
			if(claim.statusClaim==1){
				claim.posisi=Utils.tranferPosisiKlaim(3);
			}else if(claim.statusClaim==2){
				claim.posisi=95;
			}else{
				claim.posisi=4;
			}
			claim.modifyby = currentUser.id;
			claim.modifydate = dbMapper.selectSysdate();
			dbMapper.updateClaim(claim);
			pesan = "Klaim ID "+claim.id+" berhasil di akseptasi";
			dbMapper.insertHistory(new History(8, "Validasi KLAIM", currentUser.id, claim.modifydate, claim.policy_id, claim.posisi));
		}else {
			claim.modifyby = currentUser.id;
			claim.modifydate = dbMapper.selectSysdate();
			dbMapper.updateClaim(claim);
			pesan = "Klaim ID "+claim.id+" berhasil diupdate";
			dbMapper.insertHistory(new History(8, "Update KLAIM", currentUser.id, claim.modifydate, claim.policy_id, claim.posisi));
		}

		return pesan;
	}
	
	public List<MstCabBank> selectListMstCabBank(Integer id, Integer mst_bank_id,String kode_bank) {
		logger.debug("selectListMstCabBank(Integer id, Integer mst_bank_id)");
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("mst_bank_id", mst_bank_id);
		map.put("kode_bank", kode_bank);
		return dbMapper.selectListMstCabBank(map);
	}
		     

	public List<MstCabBank> selectListMstCabBankPaging(String search,  Integer offset, Integer rowcount,String sort,String sort_type, Integer mst_bank_id)  {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("rowcount", rowcount);
		map.put("sort", sort);
		map.put("sort_type", sort_type);
		map.put("search", search);
		map.put("mst_bank_id", mst_bank_id);
		return dbMapper.selectListMstCabBankPaging(map);
	}
	

	public Integer selectListMstCabBankPagingCount(String search, Integer mst_bank_id)  {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("search", search);
		map.put("mst_bank_id", mst_bank_id);
		return dbMapper.selectListMstCabBankPagingCount(map);
	}
	
	public String saveMstCabBank(MstCabBank mstcabbank, User currentUser){
		logger.debug("saveUser(User user, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(mstcabbank.mode)){
			mstcabbank.active=1;
			mstcabbank.createby = currentUser.id;
			mstcabbank.createdate = dbMapper.selectSysdate();
			mstcabbank.id=dbMapper.insertMstCabBank(mstcabbank);
			pesan = messageSource.getMessage("submitsuccess"+mstcabbank.mode, new String[]{"Master Cabang Bank",""+mstcabbank.nama},null);
		}else if("DELETE".equals(mstcabbank.mode)){
			mstcabbank.active=0;
			mstcabbank.createby = currentUser.id;
			mstcabbank.createdate = dbMapper.selectSysdate();
			dbMapper.updateMstCabBank(mstcabbank);
			pesan = messageSource.getMessage("submitsuccess"+mstcabbank.mode, new String[]{"Master Cabang Bank",""+mstcabbank.nama},null);
		}else {
			mstcabbank.modifyby = currentUser.id;
			mstcabbank.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMstCabBank(mstcabbank);
			pesan = messageSource.getMessage("submitsuccess"+mstcabbank.mode, new String[]{"Master Cabang Bank",""+mstcabbank.nama},null);
		}

		return pesan;
	}
	
	public Integer insertMenu(Menu menu) {
		return dbMapper.insertMenu(menu);
	}
	public void updateMenu(Menu menu) {
		dbMapper.updateMenu(menu);
	}
	public List<Menu> selectListMenu(Integer id) {
		return dbMapper.selectListMenu(id);
	}
	public List<Menu> selectListMenuPaging(String search,Integer offset,Integer rowcount,String sort,String sort_type) {
		return dbMapper.selectListMenuPaging(search, offset, rowcount, sort, sort_type);
	}
	public Integer selectListMenuPagingCount(String search) {
		return dbMapper.selectListMenuPagingCount(search);
	}
	
	public String saveMenu(Menu menu, User currentUser){
		logger.debug("saveMenu(Menu menu, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(menu.mode)){
			menu.active=1;
			menu.createby = currentUser.id;
			menu.createdate = dbMapper.selectSysdate();
			menu.id=dbMapper.insertMenu(menu);
			pesan = messageSource.getMessage("submitsuccess"+menu.mode, new String[]{"Master Menu",""+menu.nama},null);
		}else if("DELETE".equals(menu.mode)){
			menu.active=0;
			menu.modifyby = currentUser.id;
			menu.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMenu(menu);
			pesan = messageSource.getMessage("submitsuccess"+menu.mode, new String[]{"Master Menu",""+menu.nama},null);
		}else {
			menu.modifyby = currentUser.id;
			menu.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMenu(menu);
			pesan = messageSource.getMessage("submitsuccess"+menu.mode, new String[]{"Master Menu",""+menu.nama},null);
		}
		
		
		return pesan;
	}
	
	public void deleteGroupMenu(Integer id){
		dbMapper.deleteGroupMenu(id);
	}
	public void insertGroupMenu(GroupMenu groupMenu) {
		 dbMapper.insertGroupMenu(groupMenu);
	}
	public void updateGroupMenu(GroupMenu groupMenu) {
		dbMapper.updateGroupMenu(groupMenu);
	}
	public List<GroupMenu> selectListGroupMenu(Integer id,Integer menu_id,Integer grouping,Integer aktif) {
		return dbMapper.selectListGroupMenu(id,menu_id,grouping,aktif);
	}
	public List<GroupMenu> selectListGroupMenuPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type) {
		return dbMapper.selectListGroupMenuPaging(search, offset, rowcount, sort, sort_type);
	}
	public Integer selectListGroupMenuPagingCount(String search) {
		return dbMapper.selectListGroupMenuPagingCount(search);
	}
	
	public String saveGroupMenu(GroupMenu groupMenu, User currentUser){
		logger.debug("saveGroupMenu(GroupMenu groupMenu, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(groupMenu.mode)){
			groupMenu.createby = currentUser.id;
			groupMenu.createdate = dbMapper.selectSysdate();
			groupMenu.id=selectCountTable("(select * from group_menu WHERE 1=1 group by nama )x",null)+1;
			for (Menu mn:groupMenu.getMenu()) {
				groupMenu.menu_id=mn.id;
				if(mn.akses){
					groupMenu.active=1;
					dbMapper.insertGroupMenu(groupMenu);
				}
			}			
			pesan = messageSource.getMessage("submitsuccess"+groupMenu.mode, new String[]{"Master GroupMenu",""+groupMenu.nama},null);
		}else if("DELETE".equals(groupMenu.mode)){
			groupMenu.modifyby = currentUser.id;
			groupMenu.modifydate = dbMapper.selectSysdate();
			groupMenu.menu_id=null;
			groupMenu.active=0;
			dbMapper.updateGroupMenu(groupMenu);
			pesan = messageSource.getMessage("submitsuccess"+groupMenu.mode, new String[]{"Master GroupMenu",""+groupMenu.nama},null);
		}else {
			groupMenu.modifyby = currentUser.id;
			groupMenu.modifydate = dbMapper.selectSysdate();
			for (Menu mn:groupMenu.getMenu()) {
				groupMenu.menu_id=mn.id;
				if(mn.akses){
					if(selectListGroupMenu(groupMenu.id, mn.id, null,null).isEmpty()){
						groupMenu.active=1;
						dbMapper.insertGroupMenu(groupMenu);
					}else{
						groupMenu.active=1;
						dbMapper.updateGroupMenu(groupMenu);
					}
				}else{
					if(!selectListGroupMenu(groupMenu.id, mn.id, null,null).isEmpty()){
						groupMenu.active=0;
						dbMapper.updateGroupMenu(groupMenu);
					}
				}
			}
			
			pesan = messageSource.getMessage("submitsuccess"+groupMenu.mode, new String[]{"Master GroupMenu",""+groupMenu.nama},null);
		}
		
		
		return pesan;
	}
	
	public Integer insertUser(User user) {
		return dbMapper.insertUser(user);
	}
	public void updateUser(User user) {
		dbMapper.updateUser(user);
	}
	public List<User> selectAllUser(Integer group_menu_id,Integer id) {
		return dbMapper.selectAllUser(group_menu_id, id);
	}
	public List<User> selectListUserPaging(String search,Integer offset, Integer rowcount,String sort,String sort_type, Integer group_menu_id) {
		return dbMapper.selectListUserPaging(search, offset, rowcount, sort, sort_type, group_menu_id);
	}	
	public Integer selectListUserPagingCount(String search,Integer group_menu_id) {
		return dbMapper.selectListUserPagingCount(search, group_menu_id);
	}
	
	public String saveUser(User user, User currentUser){
		logger.debug("saveUser(User user, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(user.mode)){
			user.active=1;
			user.createby = currentUser.id;
			user.createdate = dbMapper.selectSysdate();
			user.password="123BCD";
			user.id=dbMapper.insertUser(user);
			pesan = messageSource.getMessage("submitsuccess"+user.mode, new String[]{"Master User",""+user.username},null);
		}else if("DELETE".equals(user.mode)){
			user.active=0;
			user.modifyby = currentUser.id;
			user.modifydate = dbMapper.selectSysdate();
			dbMapper.updateUser(user);
			pesan = messageSource.getMessage("submitsuccess"+user.mode, new String[]{"Master User",""+user.username},null);
		}else if("RESET".equals(user.mode)){
			user.password="123BCD";
			user.passdecrypt=user.password;
			user.modifyby = currentUser.id;
			user.modifydate = dbMapper.selectSysdate();
			dbMapper.updateUser(user);
			pesan = "Master User "+user.username+" password berhasil di reset";
		}else {
			user.active=1;
			user.modifyby = currentUser.id;
			user.modifydate = dbMapper.selectSysdate();
			dbMapper.updateUser(user);
			pesan = messageSource.getMessage("submitsuccess"+user.mode, new String[]{"Master User",""+user.username},null);
		}
		
		return pesan;
	}
	
	public GroupPolicy selectGroupPolicy(Integer id){
		logger.debug("selectGroupPolicy(Integer id)");
		return dbMapper.selectGroupPolicy(id);
	}

	public String saveGroupPolicy(int jenis, GroupPolicy groupPolicy, User currentUser) {
		logger.debug("saveGroupPolicy(GroupPolicy groupPolicy, User currentUser)");
		
		String pesan;
		
		//set createby dan createdate
		groupPolicy.setJenis(jenis);
		groupPolicy.setCreateby(currentUser.getId());
		groupPolicy.setCreatedate(dbMapper.selectSysdate());
		groupPolicy.getPolisAJK().setMode(groupPolicy.getMode());
		groupPolicy.getPolisFire().setMode(groupPolicy.getMode());
		groupPolicy.getPolisMicro().setMode(groupPolicy.getMode());
		
		//INSERT
		if("NEW".equals(groupPolicy.getMode())){
			StringBuffer message = new StringBuffer();
			
			dbMapper.insertGroupPolicy(groupPolicy);
			if(groupPolicy.isLifeCheck()&groupPolicy.isMicroCheck()) {
				groupPolicy.getPolisMicro().setGroup_policy_id(groupPolicy.id);
				this.savePolisAJK(groupPolicy.getPolisMicro(), currentUser);
				groupPolicy.setLife_id(groupPolicy.getPolisMicro().getId());
				message.append(
						"\\n- Premi Asuransi Jiwa = Rp. " + Utils.defaultNF.format(groupPolicy.getPolisMicro().getProduct().getPremi()) +
						"\\n- Jenis Pemeriksaan Kesehatan = " + dbMapper.selectKeteranganMaster(10, groupPolicy.getPolisMicro().getCustomer().getJenis_medis()) +
						nvl(groupPolicy.getPolisMicro().getCatatan())
				);
			}else{
			if(groupPolicy.isLifeCheck()) {
				groupPolicy.getPolisAJK().setGroup_policy_id(groupPolicy.id);
				this.savePolisAJK(groupPolicy.getPolisAJK(), currentUser);
				groupPolicy.setLife_id(groupPolicy.getPolisAJK().getId());
				Policy tmp=dbMapper.selectPolicy(groupPolicy.getPolisAJK().getId(), null, null, null);
				String psn="";
				if(tmp.getNoclaim()>60)psn="\\n- Harap Upload Form Subject To No Claim.";
				message.append(
					"\\n- Premi Asuransi Jiwa = Rp. " + Utils.defaultNF.format(groupPolicy.getPolisAJK().getProduct().getPremi()) +
					"\\n- Jenis Pemeriksaan Kesehatan = " + dbMapper.selectKeteranganMaster(10, groupPolicy.getPolisAJK().getCustomer().getJenis_medis()) +
					""+psn+
					nvl(groupPolicy.getPolisAJK().getCatatan())
				);
			}
			if(groupPolicy.isFireCheck()){
				groupPolicy.getPolisFire().setGroup_policy_id(groupPolicy.id);
				this.savePolicyFire(groupPolicy.getPolisFire(), currentUser);
				groupPolicy.setFire_id(groupPolicy.getPolisFire().getId());
				message.append(
					"\\n- Premi Asuransi Kebakaran = Rp. " + (groupPolicy.getPolisFire().getProduct().getPremi()==null?"":Utils.defaultNF.format(groupPolicy.getPolisFire().getProduct().getPremi())) +
					nvl(groupPolicy.getPolisFire().getCatatan())
				);
			}
			if(groupPolicy.isMicroCheck()) {
				groupPolicy.getPolisMicro().setGroup_policy_id(groupPolicy.id);
				this.savePolicyMicro(groupPolicy.getPolisMicro(), currentUser);
				groupPolicy.setLife_id(groupPolicy.getPolisMicro().getId());
				message.append(
					"\\n- Premi Asuransi Micro = Rp. " + Utils.defaultNF.format(groupPolicy.getPolisMicro().getProduct().getPremi()) +
					nvl(groupPolicy.getPolisMicro().getCatatan())
				);
			}
			}
			
			pesan = "Polis berhasil ditambahkan. " + message.toString();
			
		//VALIDASI
		}else if("VALIDASI".equals(groupPolicy.getMode())){
			if(groupPolicy.isLifeCheck() && groupPolicy.isMicroCheck()) {				
				this.savePolisAJK(groupPolicy.getPolisMicro(), currentUser);
			}else{
			if(groupPolicy.isLifeCheck()) this.savePolisAJK(groupPolicy.getPolisAJK(), currentUser);
			if(groupPolicy.isFireCheck()) this.savePolicyFire(groupPolicy.getPolisFire(), currentUser);
			if(groupPolicy.isMicroCheck()) this.savePolicyMicro(groupPolicy.getPolisMicro(), currentUser);
			}
			pesan = "Polis berhasil di validasi";
			
		//TRANSFER
		}else if("TRANSFER".equals(groupPolicy.getMode())){
			if(groupPolicy.isLifeCheck() && groupPolicy.isMicroCheck()) {
				
				this.savePolisAJK(groupPolicy.getPolisMicro(), currentUser);
			}else{
			if(groupPolicy.isLifeCheck()) this.savePolisAJK(groupPolicy.getPolisAJK(), currentUser);
			if(groupPolicy.isFireCheck()) this.savePolicyFire(groupPolicy.getPolisFire(), currentUser);
			if(groupPolicy.isMicroCheck()) this.savePolicyMicro(groupPolicy.getPolisMicro(), currentUser);
			}
			pesan = "Polis berhasil di Transfer";
		
		//Akseptasi
		}else if("AKSEPTASI".equals(groupPolicy.getMode())){
			
			if(groupPolicy.isLifeCheck()) this.savePolisAJK(groupPolicy.getPolisMicro(), currentUser);
			if(groupPolicy.isFireCheck()) this.savePolicyFire(groupPolicy.getPolisMicro(), currentUser);
			if(groupPolicy.isMicroCheck()) this.savePolicyMicro(groupPolicy.getPolisMicro(), currentUser);
			
			pesan = "Polis berhasil di akseptasi";
			
		//UPDATE
		}else{
			StringBuffer message = new StringBuffer();
			if(groupPolicy.isLifeCheck()&groupPolicy.isMicroCheck()) {
				this.savePolisAJK(groupPolicy.getPolisMicro(), currentUser);
			}else{
			if(groupPolicy.isLifeCheck()) {
				this.savePolisAJK(groupPolicy.getPolisAJK(), currentUser);
				Policy tmp=dbMapper.selectPolicy(groupPolicy.getPolisAJK().getId(), null, null, null);
				String psn="";
				if(tmp.getNoclaim()>60)psn="\\n- Harap Upload Form Subject To No Claim.";
				message.append(
					"\\n- Premi Asuransi Jiwa = Rp. " + Utils.defaultNF.format(groupPolicy.getPolisAJK().getProduct().getPremi()) +
					"\\n- Jenis Pemeriksaan Kesehatan = " + dbMapper.selectKeteranganMaster(10, groupPolicy.getPolisAJK().getCustomer().getJenis_medis()) +
					""+psn+
					nvl(groupPolicy.getPolisAJK().getCatatan())
				);
			}
			if(groupPolicy.isFireCheck()) {
				this.savePolicyFire(groupPolicy.getPolisFire(), currentUser);
				message.append(
					"\\n- Premi Asuransi Kebakaran = Rp. " + (groupPolicy.getPolisFire().getProduct().getPremi()==null ?""
							:Utils.defaultNF.format(groupPolicy.getPolisFire().getProduct().getPremi())) +
					nvl(groupPolicy.getPolisFire().getCatatan())
				);
			}
			if(groupPolicy.isMicroCheck()) {
				this.savePolicyMicro(groupPolicy.getPolisMicro(), currentUser);
				message.append(
					"\\n- Premi Asuransi Micro = Rp. " + Utils.defaultNF.format(groupPolicy.getPolisMicro().getProduct().getPremi()) +
					nvl(groupPolicy.getPolisMicro().getCatatan())
				);
			}
			}
//			dbMapper.updateGroupPolicy(groupPolicy); // ga perlu ada update group polis
			pesan = "Polis berhasil diupdate " + message.toString();
		}
		
		return pesan;
	}
	
	public String saveGroupPolicy(int jenis,List<GroupPolicy> lsgroupPolicy, User currentUser,Integer jenisUpload) {
		logger.debug("saveGroupPolicy(GroupPolicy groupPolicy, User currentUser)");
		
		String pesan=null;
		int count=0;
		for(GroupPolicy groupPolicy:lsgroupPolicy){
			pesan=saveGroupPolicy(jenis, groupPolicy, currentUser);
			if(count==0)
				if(!Utils.isEmpty(groupPolicy.getFileUpload()))
					dbMapper.insertHistory(new History(jenisUpload, groupPolicy.getFileUpload(), currentUser.getId(), selectSysdate(), null, null));
			count++;
		}
		
			
		
		return pesan;
	}
	
	
	public List<MstMaster> selectListMstMaster(Integer id, Integer jenis) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("jenis", jenis);
		return dbMapper.selectListMstMaster(map);
	}
		
	public List<MstMaster> selectListMstMasterPaging(String search,  Integer offset, Integer rowcount,String sort,String sort_type)  {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("rowcount", rowcount);
		map.put("sort", sort);
		map.put("sort_type", sort_type);
		map.put("search", search);
		return dbMapper.selectListMstMasterPaging(map);
	}
	
	public Integer selectListMstMasterPagingCount(String search)  {
		return dbMapper.selectListMstMasterPagingCount(search);
	}
	
	public String saveMstMaster(MstMaster mstmaster, User currentUser){
		logger.debug("saveMstMaster(MstMaster mstmaster, User currentUser)");
		
		String pesan;
		
		if("NEW".equals(mstmaster.mode)){
			mstmaster.active=1;
			mstmaster.createby = currentUser.id;
			mstmaster.createdate = dbMapper.selectSysdate();
			mstmaster.id=dbMapper.insertMstMaster(mstmaster);
			pesan = messageSource.getMessage("submitsuccess"+mstmaster.mode, new String[]{"Master Konfigurasi",""+mstmaster.keterangan},null);
		}else if("DELETE".equals(mstmaster.mode)){
			mstmaster.active=0;
			mstmaster.createby = currentUser.id;
			mstmaster.createdate = dbMapper.selectSysdate();
			dbMapper.updateMstMaster(mstmaster);
			pesan = messageSource.getMessage("submitsuccess"+mstmaster.mode, new String[]{"Master Konfigurasi",""+mstmaster.keterangan},null);
		}else {
			mstmaster.modifyby = currentUser.id;
			mstmaster.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMstMaster(mstmaster);
			pesan = messageSource.getMessage("submitsuccess"+mstmaster.mode, new String[]{"Master Konfigurasi",""+mstmaster.keterangan},null);
		}

		return pesan;
	}
	
	public Double selectRate(Integer jenisRate, Integer id, Integer tenor, Integer jenisBangunan, Integer umur){
		return dbMapper.selectRate(jenisRate, id, tenor, jenisBangunan, umur);
	}
	
	public Claim selectClaim(Integer id){
		return dbMapper.selectClaim(id);
	}
	
	
	public List<Claim> selectListClaimPaging(String search,Integer offset,Integer rowcount,
			String sort, String sort_type,Integer posisi,String begdate,String enddate,Integer cab_bank, Integer asuransi_id,Integer group_product) {
		return  dbMapper.selectListClaimPaging(search, offset, rowcount, sort, sort_type, posisi, begdate, enddate, cab_bank, asuransi_id, group_product);
	}
	public Integer selectListClaimPagingCount( String search,Integer posisi,String begdate,String enddate,Integer cab_bank, Integer asuransi_id,Integer group_product) {
		return dbMapper.selectListClaimPagingCount(search, posisi, begdate, enddate, cab_bank, asuransi_id, group_product);
	}
	
	public String savePayment(List<Policy> lspolicy, User currentUser){
		logger.debug("savePayment(Product product, User currentUser)");
		
		String pesan;
		
		for(Policy policy:lspolicy){
			policy.setMode("NEW");
			savePayment(policy, currentUser);
		}
		pesan = "Input Payment sebanyak "+lspolicy.size()+" polis berhasil dilakukan";
		
		return pesan;
	}
	
	public String savePayment(Policy policy, User currentUser){
		logger.debug("savePayment(Product product, User currentUser)");
		
		String pesan=null;
		
		policy.getProduct().policy_id=policy.getId();
		policy.getProduct().modifyby=currentUser.getId();
		policy.getProduct().modifydate=selectSysdate();
		
		policy.getHistory().setCreateby(currentUser.getId());
		policy.getHistory().setCreatedate(dbMapper.selectSysdate());
		policy.getHistory().setPolicy_id(policy.getId());
		policy.getHistory().setPosisi(policy.getPosisi());

		policy.getHistory().setJenis(1);//jenis 1 = history policy
		
		if(policy.getMode().equals("NEW")){
		
			
			policy.getProduct().inputpayby=currentUser.getId();
				
			dbMapper.updateProduct(policy.getProduct());
			
			
			policy.getHistory().setKeterangan("Input Payment Polis sebesar "+Utils.defaultNF.format(policy.getProduct().nominal_paid));
			pesan = "Input Payment No SPAJ "+policy.getSpaj_no()+" berhasil";
		}else if(policy.getMode().equals("RESET")){
			policy.getProduct().policy_id=policy.getId();
			
			dbMapper.updateProductResetPayment(policy.getProduct());
			
			
			policy.getHistory().setKeterangan("Reset Payment Polis");
			
			pesan = "Reset Payment No SPAJ "+policy.getSpaj_no()+" berhasil";
		}
		dbMapper.insertHistory(policy.getHistory());
//		pesan = messageSource.getMessage("submitsuccess"+policy.getMode(), new String[]{"Save Payment",""+policy.getId()},null);
		
		return pesan;
	}
	
	public void insertHistory(History history){
		 dbMapper.insertHistory(history);
	}

	public List<History> selectHistoryPolis(Integer policy_id) {
		return dbMapper.selectHistoryPolis(policy_id);
	}
	
	public String selectKeteranganMaster(Integer id, Integer jenis) {
		return dbMapper.selectKeteranganMaster(id, jenis);
	}

	public String selectMstProductName(Integer mst_product_id){
		return dbMapper.selectMstProductName(mst_product_id);
	}
	
	public Policy selectPolicyById(Integer id){
		return dbMapper.selectPolicyById(id);
	}
	
	public String cancelPolis(Integer policy_id, User currentUser, String keterangan) throws MessagingException{
		Date sysdate = dbMapper.selectSysdate();
		Integer posisi = 95; //cancelled
		
		//1. Update polis menjadi cancelled
		Policy policy = new Policy();
		policy.setId(policy_id);
		policy.setPosisi(posisi);
		policy.setModifyby(currentUser.getId());
		policy.setModifydate(sysdate);
		dbMapper.updatePolicy(policy);
		
		//2. Insert history polis
		History history = new History(1, keterangan, currentUser.getId(), sysdate, policy_id, posisi);
		dbMapper.insertHistory(history);
		
		//3. Email notifikasi ke user yg melakukan cancel, dan ke pihak asuransi nya, bila sudah ada asuransinya
		Policy tmp = selectPolicyById(policy_id);
		if(tmp.getAsuransi_id() != null){
			MstBank mbank = selectListMstBank(tmp.getAsuransi_id()).get(0);
			Customer cs = selectCustomer(tmp.getCustomer_id());
			String message=	"Kepada Yth :\n" +
							""+mbank.nama+"\n\n" +
							"Dengan hormat,\n" +
							"Berikut notifikasi PEMBATALAN Asuransi dari MaiBro dengan data sebagai berikut :\n\n" +
							"No. SPAJ : " + Utils.nvl(tmp.getSpaj_no()) + "\n" +
							"No. Polis : " + Utils.nvl(tmp.getPolicy_no()) + "\n" +
							"Kreditur : " + Utils.nvl(tmp.getNamabank()) + " " + Utils.nvl(tmp.getNamacabbank()) + "\n" +
							"Debitur : " + Utils.nvl(tmp.getNamacustomer()) + "\n" +
							"Tanggal Lahir : " + (cs.getTgl_lahir()==null ? "" : Utils.defaultDF.format(cs.getTgl_lahir())) + "\n" +
							"Produk : " + Utils.nvl(tmp.getNamaproduk()) + "\n" +
							"Plafon Kredit : " + (tmp.getUp()==null ? "" : Utils.defaultNF.format(tmp.getUp())) + "\n" +
							"Mulai Asuransi : " + (tmp.getBeg_date()==null ? "" : Utils.defaultDF.format(tmp.getBeg_date())) + "\n" +
							"Masa Asuransi : " + (tmp.getIns_period()==null ? "" : tmp.getIns_period()) + " tahun\n\n" +
							"Detail data dapat dilihat di website www.maibro.com dengan login yang telah diberikan.\n\n" +
							"Mohon dapat segera diproses pembatalannya. Atas bantuan dan kerjasamanya kami ucapkan terima kasih.\n\n" +
							"Hormat kami.\n\n" +
							"PT. Multi Artha Insurance Brokers";
			
			if(mbank.email != null){
				email.send(
						props.getProperty("admin.email.from"), mbank.email.split( ";" ), props.getProperty("admin.email.cc").split(";"), 
						props.getProperty("admin.email.bcc").split(";"),
						"Notifikasi PEMBATALAN Asuransi SPAJ No. " + tmp.getSpaj_no(), message);
				return "Polis berhasil dibatalkan. Email notifikasi sudah dikirimkan ke pihak asuransi";
			}
		}
		return "Polis berhasil dibatalkan.";
	}
	
}