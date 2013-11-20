package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table AGENT
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Agent implements Serializable {

	private static final long serialVersionUID = -7313219846186386373L;
	
	//kolom2 tabel
	public Integer policy_id;
	public String nama;
	public String telp_hp;
	public String jabatan;
	
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
	public String getTelp_hp() {
		return telp_hp;
	}
	public void setTelp_hp(String telp_hp) {
		this.telp_hp = telp_hp;
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
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	
	
}