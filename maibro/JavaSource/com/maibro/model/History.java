package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table HISTORY
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class History implements Serializable {

	private static final long serialVersionUID = -1005366859256929352L;

	//kolom2 tabel
	public Integer id;
	public Integer policy_id;
	public Integer jenis;
	public Integer posisi;
	public String keterangan;
	
	public Integer createby;
	public Date createdate;
	
	public String username;
	public String posisiKet;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPosisiKet() {
		return posisiKet;
	}

	public void setPosisiKet(String posisiKet) {
		this.posisiKet = posisiKet;
	}

	public History(){
		
	}
	
	public History(int jenis,String keterangan,int createby,Date createdate,Integer policy_id,Integer posisi){
		this.jenis=jenis;
		this.keterangan=keterangan;
		this.createby=createby;
		this.createdate=createdate;
		this.policy_id=policy_id;
		this.posisi=posisi;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPolicy_id() {
		return policy_id;
	}
	public void setPolicy_id(Integer policy_id) {
		this.policy_id = policy_id;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	
	
	
	
}