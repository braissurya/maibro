package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table PRODUCT
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Product implements Serializable {

	private static final long serialVersionUID = -5511576017168275461L;
	 
	//kolom2 tabel
	public Integer policy_id;
	public Double premi_pokok;
	public Double premi_extra;
	public Double biaya_admin; 
	public Double premi;
	public Double up;
	public Double rate;
	public Integer jenis_rate;
	public Integer mst_product_id;
	public String deductible;
	
	public Integer createby;
	public Date createdate;
	
	
	public Date tgl_paid;
	public Double nominal_paid;
	public Integer inputpayby;
	public Integer flag_paid;
	public Double nominal_remain;
	
	public Integer modifyby;
	public Date modifydate;
	
	//tambahan
	public String namaProduk;
	public String inputpayuser;
	
	public Integer getJenis_rate() {
		return jenis_rate;
	}
	public void setJenis_rate(Integer jenis_rate) {
		this.jenis_rate = jenis_rate;
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
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public Double getUp() {
		return up;
	}
	public void setUp(Double up) {
		this.up = up;
	}
	
	public Integer getMst_product_id() {
		return mst_product_id;
	}
	public void setMst_product_id(Integer mst_product_id) {
		this.mst_product_id = mst_product_id;
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
	public String getNamaProduk() {
		return namaProduk;
	}
	public void setNamaProduk(String namaProduk) {
		this.namaProduk = namaProduk;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Date getTgl_paid() {
		return tgl_paid;
	}
	public void setTgl_paid(Date tgl_paid) {
		this.tgl_paid = tgl_paid;
	}
	public Double getNominal_paid() {
		return nominal_paid;
	}
	public void setNominal_paid(Double nominal_paid) {
		this.nominal_paid = nominal_paid;
	}
	public Integer getInputpayby() {
		return inputpayby;
	}
	public void setInputpayby(Integer inputpayby) {
		this.inputpayby = inputpayby;
	}
	public String getInputpayuser() {
		return inputpayuser;
	}
	public void setInputpayuser(String inputpayuser) {
		this.inputpayuser = inputpayuser;
	}
	public Double getPremi_pokok() {
		return premi_pokok;
	}
	public void setPremi_pokok(Double premi_pokok) {
		this.premi_pokok = premi_pokok;
	}
	public Double getPremi_extra() {
		return premi_extra;
	}
	public void setPremi_extra(Double premi_extra) {
		this.premi_extra = premi_extra;
	}
	public Double getBiaya_admin() {
		return biaya_admin;
	}
	public void setBiaya_admin(Double biaya_admin) {
		this.biaya_admin = biaya_admin;
	}
	public Integer getFlag_paid() {
		return flag_paid;
	}
	public void setFlag_paid(Integer flag_paid) {
		this.flag_paid = flag_paid;
	}
	public Double getNominal_remain() {
		return nominal_remain;
	}
	public void setNominal_remain(Double nominal_remain) {
		this.nominal_remain = nominal_remain;
	}
	public String getDeductible() {
		return deductible;
	}
	public void setDeductible(String deductible) {
		this.deductible = deductible;
	}
	
	
	
}