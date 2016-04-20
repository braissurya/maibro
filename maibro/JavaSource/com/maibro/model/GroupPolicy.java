package com.maibro.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Domain object untuk table GROUP_POLICY
 * 
 * @author Yusuf
 * @since Feb 4, 2013 (9:53:54 AM)
 *
 */
public class GroupPolicy implements Serializable {

	private static final long serialVersionUID = -3375446920481466215L;

	public Integer id;
	
	//FIXME buang field life_id dan fire_id, obsolete
	public Integer life_id;
	public Integer fire_id;
	
	public Integer jenis;
	
	
	public Integer createby;
	public Date createdate;
	public Integer modifyby;
	public Date modifydate;

	public String mode;
	public String pagename;
	public Policy polisAJK;
	public boolean lifeCheck;
	public Policy polisFire;
	public boolean fireCheck;
	public Policy polisMicro=new Policy();
	public boolean microCheck;
	
	public String life_policy_no, life_debitur, life_produk, life_no_pk; 
	public String fire_policy_no, fire_debitur, fire_produk;
	
	public String nama_agent;
	public String telp_agent;
	public String jabatan_agent;
	public Integer mst_bank_id;
	public Integer mst_cab_bank_id;
	private String fileUpload;
	
	public String createuser;
	
	private List<History> listHistory;
	
	public List<History> getListHistory() {
		return listHistory;
	}

	public void setListHistory(List<History> listHistory) {
		this.listHistory = listHistory;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Integer getMst_bank_id() {
		return mst_bank_id;
	}

	public void setMst_bank_id(Integer mst_bank_id) {
		this.mst_bank_id = mst_bank_id;
	}

	public Integer getMst_cab_bank_id() {
		return mst_cab_bank_id;
	}

	public void setMst_cab_bank_id(Integer mst_cab_bank_id) {
		this.mst_cab_bank_id = mst_cab_bank_id;
	}

	public String getNama_agent() {
		return nama_agent;
	}

	public void setNama_agent(String nama_agent) {
		this.nama_agent = nama_agent;
	}

	public String getTelp_agent() {
		return telp_agent;
	}

	public void setTelp_agent(String telp_agent) {
		this.telp_agent = telp_agent;
	}

	public GroupPolicy() {
		this.polisAJK = new Policy();
		this.polisFire = new Policy();
	}
	
	public String getLife_policy_no() {
		return life_policy_no;
	}
	public void setLife_policy_no(String life_policy_no) {
		this.life_policy_no = life_policy_no;
	}
	public String getLife_debitur() {
		return life_debitur;
	}
	public void setLife_debitur(String life_debitur) {
		this.life_debitur = life_debitur;
	}
	public String getLife_produk() {
		return life_produk;
	}
	public void setLife_produk(String life_produk) {
		this.life_produk = life_produk;
	}
	public String getFire_policy_no() {
		return fire_policy_no;
	}
	public void setFire_policy_no(String fire_policy_no) {
		this.fire_policy_no = fire_policy_no;
	}
	public String getFire_debitur() {
		return fire_debitur;
	}
	public void setFire_debitur(String fire_debitur) {
		this.fire_debitur = fire_debitur;
	}
	public String getFire_produk() {
		return fire_produk;
	}
	public void setFire_produk(String fire_produk) {
		this.fire_produk = fire_produk;
	}
	public boolean isLifeCheck() {
		return lifeCheck;
	}
	public void setLifeCheck(boolean lifeCheck) {
		this.lifeCheck = lifeCheck;
	}
	public boolean isFireCheck() {
		return fireCheck;
	}
	public void setFireCheck(boolean fireCheck) {
		this.fireCheck = fireCheck;
	}
	public Policy getPolisAJK() {
		return polisAJK;
	}
	public void setPolisAJK(Policy polisAJK) {
		this.polisAJK = polisAJK;
	}
	public Policy getPolisFire() {
		return polisFire;
	}
	public void setPolisFire(Policy polisFire) {
		this.polisFire = polisFire;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLife_id() {
		return life_id;
	}
	public void setLife_id(Integer life_id) {
		this.life_id = life_id;
	}
	public Integer getFire_id() {
		return fire_id;
	}
	public void setFire_id(Integer fire_id) {
		this.fire_id = fire_id;
	}
	public Integer getCreateby() {
		return createby;
	}
	public void setCreateby(Integer createby) {
		this.createby = createby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Integer getModifyby() {
		return modifyby;
	}
	public void setModifyby(Integer modifyby) {
		this.modifyby = modifyby;
	}
	public Date getModifydate() {
		return modifydate;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public Policy getPolisMicro() {
		return polisMicro;
	}

	public void setPolisMicro(Policy polisMicro) {
		this.polisMicro = polisMicro;
	}

	public boolean isMicroCheck() {
		return microCheck;
	}

	public void setMicroCheck(boolean microCheck) {
		this.microCheck = microCheck;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getJabatan_agent() {
		return jabatan_agent;
	}

	public void setJabatan_agent(String jabatan_agent) {
		this.jabatan_agent = jabatan_agent;
	}

	public String getLife_no_pk() {
		return life_no_pk;
	}

	public void setLife_no_pk(String life_no_pk) {
		this.life_no_pk = life_no_pk;
	}

	
	
}