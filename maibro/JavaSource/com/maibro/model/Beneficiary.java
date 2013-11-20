package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table BENEFICIARY
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Beneficiary implements Serializable {

	private static final long serialVersionUID = -7388311992702688817L;

	//kolom2 tabel
	public Integer policy_id;
	public String nama;
	public Integer relasi;
	public String relasi_lain;
	
	public Integer createby;
	public Date createdate;
	
	public Integer modifyby;
	public Date modifydate;
	
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
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getRelasi() {
		return relasi;
	}
	public void setRelasi(Integer relasi) {
		this.relasi = relasi;
	}
	public String getRelasi_lain() {
		return relasi_lain;
	}
	public void setRelasi_lain(String relasi_lain) {
		this.relasi_lain = relasi_lain;
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

}