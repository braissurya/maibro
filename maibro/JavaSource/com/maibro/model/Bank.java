package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table BANK
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Bank implements Serializable {

	private static final long serialVersionUID = -7388311992702688817L;

	//kolom2 tabel
	public Integer policy_id;
	public Integer mst_cab_bank_id;
	public String clause;
	
	public Integer createby;
	public Date createdate;
	public Integer modifyby;
	public Date modifydate;
	
	//kolom tambahan
	public Integer mst_bank_id;
	public String namaCabang;
	public String namaBank;
	
	public Integer getMst_bank_id() {
		return mst_bank_id;
	}
	public void setMst_bank_id(Integer mst_bank_id) {
		this.mst_bank_id = mst_bank_id;
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
	public Integer getPolicy_id() {
		return policy_id;
	}
	public void setPolicy_id(Integer policy_id) {
		this.policy_id = policy_id;
	}
	public Integer getMst_cab_bank_id() {
		return mst_cab_bank_id;
	}
	public void setMst_cab_bank_id(Integer mst_cab_bank_id) {
		this.mst_cab_bank_id = mst_cab_bank_id;
	}
	public String getClause() {
		return clause;
	}
	public void setClause(String clause) {
		this.clause = clause;
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
	public String getNamaCabang() {
		return namaCabang;
	}
	public void setNamaCabang(String namaCabang) {
		this.namaCabang = namaCabang;
	}
	public String getNamaBank() {
		return namaBank;
	}
	public void setNamaBank(String namaBank) {
		this.namaBank = namaBank;
	}
	
	

}