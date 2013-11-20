package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

import com.maibro.utils.Utils;

/**
 * Domain object untuk table CUSTOMER
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Customer implements Serializable {

	private static final long serialVersionUID = -8140248762184465774L;

	//kolom2 tabel
	private Integer id;
	private Integer address_id;
	private Integer objek_pertanggungan;
	private Integer jenis_medis;
	private Boolean standar;
	private String nama;
	private String gender;
	private String tempat_lahir;
	private Date tgl_lahir;
	private Integer umur;
	private Double berat_badan;
	private Double tinggi_badan;
	private String no_identitas;
	private String pekerjaan;
	private String bagian;
	
	private Boolean q1, q2, q3, q4, q5;
	private String q1_desc, q2_desc, q3_desc, q4_desc, q5_desc;

	private Integer createby;
	private Date createdate;
	
	//kolom2 tambahan
	private Address address=new Address();
	private Address addressInsured=new Address();
	
	private String tgl_lahirStr;
	
	private Integer modifyby;
	private Date modifydate;
	
	public Boolean getStandar() {
		return standar;
	}
	public void setStandar(Boolean standar) {
		this.standar = standar;
	}
	public Integer getJenis_medis() {
		return jenis_medis;
	}
	public void setJenis_medis(Integer jenis_medis) {
		this.jenis_medis = jenis_medis;
	}
	public Integer getUmur() {
		return umur;
	}
	public void setUmur(Integer umur) {
		this.umur = umur;
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

	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAddress_id() {
		return address_id;
	}
	public void setAddress_id(Integer address_id) {
		this.address_id = address_id;
	}
	public Integer getObjek_pertanggungan() {
		return objek_pertanggungan;
	}
	public void setObjek_pertanggungan(Integer objek_pertanggungan) {
		this.objek_pertanggungan = objek_pertanggungan;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTempat_lahir() {
		return tempat_lahir;
	}
	public void setTempat_lahir(String tempat_lahir) {
		this.tempat_lahir = tempat_lahir;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
		if(tgl_lahir!=null){
			tgl_lahirStr=Utils.convertDateToString(tgl_lahir, "dd/MM/yyyy");
		}
	}
	public Double getBerat_badan() {
		return berat_badan;
	}
	public void setBerat_badan(Double berat_badan) {
		this.berat_badan = berat_badan;
	}
	public Double getTinggi_badan() {
		return tinggi_badan;
	}
	public void setTinggi_badan(Double tinggi_badan) {
		this.tinggi_badan = tinggi_badan;
	}
	public String getNo_identitas() {
		return no_identitas;
	}
	public void setNo_identitas(String no_identitas) {
		this.no_identitas = no_identitas;
	}
	public String getPekerjaan() {
		return pekerjaan;
	}
	public void setPekerjaan(String pekerjaan) {
		this.pekerjaan = pekerjaan;
	}
	public String getBagian() {
		return bagian;
	}
	public void setBagian(String bagian) {
		this.bagian = bagian;
	}
	
	public Boolean getQ1() {
		return q1;
	}
	public void setQ1(Boolean q1) {
		this.q1 = q1;
	}
	public Boolean getQ2() {
		return q2;
	}
	public void setQ2(Boolean q2) {
		this.q2 = q2;
	}
	public Boolean getQ3() {
		return q3;
	}
	public void setQ3(Boolean q3) {
		this.q3 = q3;
	}
	public Boolean getQ4() {
		return q4;
	}
	public void setQ4(Boolean q4) {
		this.q4 = q4;
	}
	public Boolean getQ5() {
		return q5;
	}
	public void setQ5(Boolean q5) {
		this.q5 = q5;
	}
	public String getQ1_desc() {
		return q1_desc;
	}
	public void setQ1_desc(String q1_desc) {
		this.q1_desc = q1_desc;
	}
	public String getQ2_desc() {
		return q2_desc;
	}
	public void setQ2_desc(String q2_desc) {
		this.q2_desc = q2_desc;
	}
	public String getQ3_desc() {
		return q3_desc;
	}
	public void setQ3_desc(String q3_desc) {
		this.q3_desc = q3_desc;
	}
	public String getQ4_desc() {
		return q4_desc;
	}
	public void setQ4_desc(String q4_desc) {
		this.q4_desc = q4_desc;
	}
	public String getQ5_desc() {
		return q5_desc;
	}
	public void setQ5_desc(String q5_desc) {
		this.q5_desc = q5_desc;
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
	public Address getAddressInsured() {
		return addressInsured;
	}
	public void setAddressInsured(Address addressInsured) {
		this.addressInsured = addressInsured;
	}
	public String getTgl_lahirStr() {
		return tgl_lahirStr;
	}
	public void setTgl_lahirStr(String tgl_lahirStr) {
		this.tgl_lahirStr = tgl_lahirStr;
		if(!Utils.isEmpty(tgl_lahirStr)){
			tgl_lahir=Utils.convertStringToDate(tgl_lahirStr, "dd/MM/yyyy");
		}
	}
	
	
}