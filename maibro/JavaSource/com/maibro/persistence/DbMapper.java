package com.maibro.persistence;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

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

/**
 * MyBatis Mapper, sebagai pengganti DAO. Sudah tidak perlu dibuat class implement lagi (cukup interfacenya saja)
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:27:34 AM)
 *
 */
public interface DbMapper {

	public Date selectSysdate() throws DataAccessException;
	public List<DropDown> selectDropDown(HashMap<String, Object> map) throws DataAccessException;
	public Integer selectCountTable(HashMap<String, Object> map) throws DataAccessException;
	
	@SuppressWarnings("rawtypes")
	public HashMap selectMaxCounter(Integer id) throws DataAccessException;
	public void updateMaxCounter(@Param("id") Integer id,@Param("value") Integer value,
								 @Param("max") String max, @Param("modifyby") Integer modifyby,
								 @Param("modifydate") Date modifydate) throws DataAccessException;
	
	public User selectUser(User user) throws DataAccessException;
	
	public Integer insertMenu(Menu menu) throws DataAccessException;
	public void updateMenu(Menu menu) throws DataAccessException;
	public List<Menu> selectListMenu(Integer id) throws DataAccessException;
	public List<Menu> selectListMenuPaging(
			@Param("search") String search,  
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListMenuPagingCount(String search) throws DataAccessException;
	
	public void deleteGroupMenu(Integer id)throws DataAccessException;
	public void insertGroupMenu(GroupMenu groupMenu) throws DataAccessException;
	public void updateGroupMenu(GroupMenu groupMenu) throws DataAccessException;
	public List<GroupMenu> selectListGroupMenu(@Param("id") Integer id,
			@Param("menu_id") Integer menu_id,@Param("grouping") Integer grouping,@Param("aktif")Integer aktif) throws DataAccessException;
	public List<GroupMenu> selectListGroupMenuPaging(
			@Param("search") String search,  
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListGroupMenuPagingCount(String search) throws DataAccessException;
	
	public Integer insertUser(User user) throws DataAccessException;
	public void updateUser(User user) throws DataAccessException;
	public List<User> selectAllUser(@Param("group_menu_id") Integer group_menu_id,@Param("id") Integer id) throws DataAccessException;
	public List<User> selectListUserPaging(
			@Param("search") String search,  
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type, 
			@Param("group_menu_id") Integer group_menu_id) throws DataAccessException;	
	public Integer selectListUserPagingCount(
			@Param("search") String search,
			@Param("group_menu_id") Integer group_menu_id) throws DataAccessException;
	
	public List<Menu> selectMenuAccess(@Param("group_menu_id")Integer group_menu_id,@Param("root")Integer root) throws DataAccessException;
	public Integer selectTestDB() throws DataAccessException;
	public void updateTestDB(Integer toggle) throws DataAccessException;
	
	public List<Policy> selectListPolisPaging(
			@Param("groupjenis") Integer groupjenis, 
			@Param("jenis") Integer jenis, @Param("search") String search,  
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,
			@Param("posisi") Integer posisi,
			@Param("begdate") String begdate,@Param("enddate") String enddate,
			@Param("begdatepaid") String begdatepaid,@Param("enddatepaid") String enddatepaid,
			@Param("tgl_aksep") String tgl_aksep,@Param("tgl_aksep_end") String tgl_aksep_end,
			@Param("paid") Integer paid, @Param("cab_bank") Integer cab_bank, @Param("asuransi_id") Integer asuransi_id
			) throws DataAccessException;
	public Integer selectListPolisPagingCount(@Param("groupjenis") Integer groupjenis,
			@Param("jenis") Integer jenis, @Param("search") String search,
			@Param("posisi") Integer posisi,
			@Param("begdate") String begdate,@Param("enddate") String enddate,
			@Param("begdatepaid") String begdatepaid,@Param("enddatepaid") String enddatepaid,
			@Param("tgl_aksep") String tgl_aksep,@Param("tgl_aksep_end") String tgl_aksep_end,
			@Param("paid") Integer paid, @Param("cab_bank") Integer cab_bank, @Param("asuransi_id") Integer asuransi_id) throws DataAccessException;
	
	public Integer insertMstBank(MstBank mstBank) throws DataAccessException;
	public Integer insertMstCabBank(MstCabBank mstCabBank) throws DataAccessException;
	public Integer insertMstProduct(MstProduct mstProduct) throws DataAccessException;
	public Integer insertMstRate(MstRate mstRate) throws DataAccessException;
	public Integer insertMstMaster(MstMaster mstmaster) throws DataAccessException;
	
	public void updateMstBank(MstBank mstBank)throws DataAccessException;
	public void updateMstCabBank(MstCabBank mstCabBank)throws DataAccessException;
	public void updateMstProduct(MstProduct mstProduct)throws DataAccessException;
	public void updateMstRate(MstRate mstRate)throws DataAccessException;
	public void updateMstMaster(MstMaster mstMaster)throws DataAccessException;
	
	public void deleteMstRate(MstRate mstRate)throws DataAccessException;
	
	public List<MstProduct> selectListMstProduct(@Param("id") Integer id,@Param("jenis") Integer jenis,@Param("mst_bank_id")Integer mst_bank_id,@Param("group_product")Integer group_product) throws DataAccessException;
	public List<MstProduct> selectListMstProductPaging(HashMap<String, Object> map) throws DataAccessException;
	public Integer selectListMstProductPagingCount(String search) throws DataAccessException;
	
	public List<MstRate> selectListMstRate(HashMap<String, Object> map) throws DataAccessException;
	public List<MstRate> selectListMstRatePaging(HashMap<String, Object> map) throws DataAccessException;
	public Integer selectListMstRatePagingCount(HashMap<String, Object> map) throws DataAccessException;
	
	public Policy selectPolicy(@Param("id") Integer id,@Param("group_policy_id") Integer group_policy_id,@Param("jenis") Integer jenis,@Param("spaj_no") String spaj_no) throws DataAccessException;
	public void insertPolicy(Policy policy) throws DataAccessException;
	public int updatePolicy(Policy policy) throws DataAccessException;
	
	public Beneficiary selectBeneficiary(Integer id) throws DataAccessException;
	public void insertBeneficiary(Beneficiary beneficiary) throws DataAccessException;
	public int updateBeneficiary(Beneficiary beneficiary) throws DataAccessException;

	public Bank selectBank(Integer id) throws DataAccessException;
	public void insertBank(Bank bank) throws DataAccessException;
	public int updateBank(Bank bank) throws DataAccessException;
	
	public Agent selectAgent(Integer id) throws DataAccessException;
	public void insertAgent(Agent agent) throws DataAccessException;
	public int updateAgent(Agent agent) throws DataAccessException;

	public Product selectProduct(Integer id) throws DataAccessException;
	public void insertProduct(Product product) throws DataAccessException;
	public int updateProduct(Product product) throws DataAccessException;
	public int updateProductResetPayment(Product product) throws DataAccessException;

	
	public void insertHistory(History history) throws DataAccessException;
	public int updateHistory(History history) throws DataAccessException;
	
	public Customer selectCustomer(Integer id) throws DataAccessException;
	public Integer insertCustomer(Customer customer) throws DataAccessException;
	public void updateCustomer(Customer customer)throws DataAccessException;
	
	public Address selectAddress(Integer id) throws DataAccessException;
	public Integer insertAddress(Address address) throws DataAccessException;
	public void updateAddress(Address address)throws DataAccessException;
	
	public List<MstBank> selectListMstBank(Integer id) throws DataAccessException;
	public List<MstBank> selectListMstBankPaging(HashMap<String, Object> map) throws DataAccessException;
	public Integer selectListMstBankPagingCount(String search) throws DataAccessException;
	
	public List<MstCabBank> selectListMstCabBank(HashMap<String, Object> map) throws DataAccessException;
	public List<MstCabBank> selectListMstCabBankPaging(HashMap<String, Object> map) throws DataAccessException;
	public Integer selectListMstCabBankPagingCount(HashMap<String, Object> map) throws DataAccessException;	
	
	public List<MstMaster> selectListMstMaster(HashMap<String, Object> map) throws DataAccessException;
	public List<MstMaster> selectListMstMasterPaging(HashMap<String, Object> map) throws DataAccessException;
	public Integer selectListMstMasterPagingCount(String search) throws DataAccessException;

	public List<Policy> selectListGroupPolicyPaging(
			@Param("jenis") Integer jenis, @Param("search") String search,  
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListGroupPolicyPagingCount(@Param("jenis") Integer jenis, @Param("search") String search) throws DataAccessException;
		
	public GroupPolicy selectGroupPolicy(Integer id) throws DataAccessException;
	public void insertGroupPolicy(GroupPolicy groupPolicy) throws DataAccessException;
	public int updateGroupPolicy(GroupPolicy groupPolicy) throws DataAccessException;
	
	public Double selectRate(
			@Param("jenis_rate") Integer jenisRate,
			@Param("mst_product_id") Integer id, @Param("tenor") Integer tenor, 
			@Param("jenis_bangunan") Integer jenisBangunan, @Param("umur") Integer umur) throws DataAccessException;
	
	public Claim selectClaim(Integer id) throws DataAccessException;
	public void insertClaim(Claim claim) throws DataAccessException;
	public int updateClaim(Claim claim) throws DataAccessException;
	
	public List<Claim> selectListClaimPaging(
			@Param("search") String search,  
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,
			@Param("posisi") Integer posisi ,
			@Param("begdate") String begdate,@Param("enddate") String enddate, 
			@Param("cab_bank") Integer cab_bank, @Param("asuransi_id") Integer asuransi_id,
			@Param("group_product") Integer group_product
			) throws DataAccessException;
	public Integer selectListClaimPagingCount( @Param("search") String search,
			@Param("posisi") Integer posisi,
			@Param("begdate") String begdate,@Param("enddate") String enddate, 
			@Param("cab_bank") Integer cab_bank, @Param("asuransi_id") Integer asuransi_id,
			 @Param("group_product") Integer group_product) throws DataAccessException;

	public String selectKeteranganMaster(@Param("id") Integer id, @Param("jenis") Integer jenis) throws DataAccessException;

	public List<History> selectHistoryPolis(Integer policy_id) throws DataAccessException;
	
	public String selectMstProductName(Integer mst_product_id) throws DataAccessException;
	
	public Policy selectPolicyById(Integer id) throws DataAccessException;

}