package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class Claim implements Serializable{
	private static final long serialVersionUID = 578209923700346470L;
	public Integer id;
	public Integer policy_id;
	public Integer posisi;
	public String keterangan;
	public Integer createby;
	public Date createdate;
	public Integer modifyby;
	public Date  modifydate;
	public Double jumlah_klaim;
	public Double jumlah_bayar;
	
	
	
	//tambahan
	public String mode;
	public String pagename;
	public Integer statusClaim;
	
	public String posisiklaim;
	public Integer posisipolis;
	public String spaj_no;
	public String policy_no;
	public Integer asuransi_id;
	public Double plafon_kredit;
	public Date spaj_date; 
	public Integer ins_period;
	public Date beg_date;
	public Date end_date;
	public Integer jenis_manfaat;
	public Integer pay_mode;  
	public String gol_debitur;
	public String kode_cabang;
	public String username; 
	public String dealref;
	public String namacustomer; 
	public Double premi;
	public Double up;
	public Double rate;
	public Date tgl_paid;
	public Double nominal_paid;
	public String kode;
	public String namaproduk;
	public String klaimusername;
	public String namaasuransi;
	public String namacabang;
	public String namabank;
	
	public String fileCategory;	
	public MultipartFile uploadFile;
	
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getPosisipolis() {
		return posisipolis;
	}
	public void setPosisipolis(Integer posisipolis) {
		this.posisipolis = posisipolis;
	}
	public String getSpaj_no() {
		return spaj_no;
	}
	public void setSpaj_no(String spaj_no) {
		this.spaj_no = spaj_no;
	}
	public String getPolicy_no() {
		return policy_no;
	}
	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}
	public Integer getAsuransi_id() {
		return asuransi_id;
	}
	public void setAsuransi_id(Integer asuransi_id) {
		this.asuransi_id = asuransi_id;
	}
	public Double getPlafon_kredit() {
		return plafon_kredit;
	}
	public void setPlafon_kredit(Double plafon_kredit) {
		this.plafon_kredit = plafon_kredit;
	}
	public Date getSpaj_date() {
		return spaj_date;
	}
	public void setSpaj_date(Date spaj_date) {
		this.spaj_date = spaj_date;
	}
	public Integer getIns_period() {
		return ins_period;
	}
	public void setIns_period(Integer ins_period) {
		this.ins_period = ins_period;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Integer getJenis_manfaat() {
		return jenis_manfaat;
	}
	public void setJenis_manfaat(Integer jenis_manfaat) {
		this.jenis_manfaat = jenis_manfaat;
	}
	public Integer getPay_mode() {
		return pay_mode;
	}
	public void setPay_mode(Integer pay_mode) {
		this.pay_mode = pay_mode;
	}
	public String getGol_debitur() {
		return gol_debitur;
	}
	public void setGol_debitur(String gol_debitur) {
		this.gol_debitur = gol_debitur;
	}
	public String getKode_cabang() {
		return kode_cabang;
	}
	public void setKode_cabang(String kode_cabang) {
		this.kode_cabang = kode_cabang;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDealref() {
		return dealref;
	}
	public void setDealref(String dealref) {
		this.dealref = dealref;
	}
	public String getNamacustomer() {
		return namacustomer;
	}
	public void setNamacustomer(String namacustomer) {
		this.namacustomer = namacustomer;
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
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getNamaproduk() {
		return namaproduk;
	}
	public void setNamaproduk(String namaproduk) {
		this.namaproduk = namaproduk;
	}
	public String getKlaimusername() {
		return klaimusername;
	}
	public void setKlaimusername(String klaimusername) {
		this.klaimusername = klaimusername;
	}
	public String getNamaasuransi() {
		return namaasuransi;
	}
	public void setNamaasuransi(String namaasuransi) {
		this.namaasuransi = namaasuransi;
	}
	public String getNamacabang() {
		return namacabang;
	}
	public void setNamacabang(String namacabang) {
		this.namacabang = namacabang;
	}
	public String getNamabank() {
		return namabank;
	}
	public void setNamabank(String namabank) {
		this.namabank = namabank;
	}
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	public Double getJumlah_klaim() {
		return jumlah_klaim;
	}
	public void setJumlah_klaim(Double jumlah_klaim) {
		this.jumlah_klaim = jumlah_klaim;
	}
	public Double getJumlah_bayar() {
		return jumlah_bayar;
	}
	public void setJumlah_bayar(Double jumlah_bayar) {
		this.jumlah_bayar = jumlah_bayar;
	}
	public MultipartFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getFileCategory() {
		return fileCategory;
	}
	public void setFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
	}
	public Integer getStatusClaim() {
		return statusClaim;
	}
	public void setStatusClaim(Integer statusClaim) {
		this.statusClaim = statusClaim;
	}
	public String getPosisiklaim() {
		return posisiklaim;
	}
	public void setPosisiklaim(String posisiklaim) {
		this.posisiklaim = posisiklaim;
	}
	
	
	
}
