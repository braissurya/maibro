package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table ADDRESS
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Address implements Serializable {

	private static final long serialVersionUID = 5221903362458981055L;

	//kolom2 tabel
	public Integer id;
	public Integer tipe;
	public String alamat_kantor, kota_kantor, kodepos_kantor;
	public String alamat_rumah, kota_rumah, kodepos_rumah;
	public String telp_kantor, telp_hp, telp_rumah;
	public Integer jenis_bangunan;
	public String no_sertifikat,type_sertifikat;
	public String penggunaan_bangunan;
	public String kondisi_kiri, kondisi_kanan, kondisi_depan, kondisi_belakang;
	public Double luas_bangunan, luas_tanah;
	public Double harga_bangunan, harga_stock, harga_perabot, harga_lainnya;
	
	public String alamat1;
	public String alamat2;
	public String alamat3;
	public String alamat4;
	public String alamat5;
	
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTipe() {
		return tipe;
	}
	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}
	public String getAlamat_kantor() {
		return alamat_kantor;
	}
	public void setAlamat_kantor(String alamat_kantor) {
		this.alamat_kantor = alamat_kantor;
	}
	public String getKota_kantor() {
		return kota_kantor;
	}
	public void setKota_kantor(String kota_kantor) {
		this.kota_kantor = kota_kantor;
	}
	public String getKodepos_kantor() {
		return kodepos_kantor;
	}
	public void setKodepos_kantor(String kodepos_kantor) {
		this.kodepos_kantor = kodepos_kantor;
	}
	public String getAlamat_rumah() {
		return alamat_rumah;
	}
	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}
	public String getKota_rumah() {
		return kota_rumah;
	}
	public void setKota_rumah(String kota_rumah) {
		this.kota_rumah = kota_rumah;
	}
	public String getKodepos_rumah() {
		return kodepos_rumah;
	}
	public void setKodepos_rumah(String kodepos_rumah) {
		this.kodepos_rumah = kodepos_rumah;
	}
	public String getTelp_kantor() {
		return telp_kantor;
	}
	public void setTelp_kantor(String telp_kantor) {
		this.telp_kantor = telp_kantor;
	}
	public String getTelp_hp() {
		return telp_hp;
	}
	public void setTelp_hp(String telp_hp) {
		this.telp_hp = telp_hp;
	}
	public String getTelp_rumah() {
		return telp_rumah;
	}
	public void setTelp_rumah(String telp_rumah) {
		this.telp_rumah = telp_rumah;
	}
	public Integer getJenis_bangunan() {
		return jenis_bangunan;
	}
	public void setJenis_bangunan(Integer jenis_bangunan) {
		this.jenis_bangunan = jenis_bangunan;
	}
	public String getNo_sertifikat() {
		return no_sertifikat;
	}
	public void setNo_sertifikat(String no_sertifikat) {
		this.no_sertifikat = no_sertifikat;
	}
	public String getPenggunaan_bangunan() {
		return penggunaan_bangunan;
	}
	public void setPenggunaan_bangunan(String penggunaan_bangunan) {
		this.penggunaan_bangunan = penggunaan_bangunan;
	}
	public String getKondisi_kiri() {
		return kondisi_kiri;
	}
	public void setKondisi_kiri(String kondisi_kiri) {
		this.kondisi_kiri = kondisi_kiri;
	}
	public String getKondisi_kanan() {
		return kondisi_kanan;
	}
	public void setKondisi_kanan(String kondisi_kanan) {
		this.kondisi_kanan = kondisi_kanan;
	}
	public String getKondisi_depan() {
		return kondisi_depan;
	}
	public void setKondisi_depan(String kondisi_depan) {
		this.kondisi_depan = kondisi_depan;
	}
	public String getKondisi_belakang() {
		return kondisi_belakang;
	}
	public void setKondisi_belakang(String kondisi_belakang) {
		this.kondisi_belakang = kondisi_belakang;
	}
	public Double getLuas_bangunan() {
		return luas_bangunan;
	}
	public void setLuas_bangunan(Double luas_bangunan) {
		this.luas_bangunan = luas_bangunan;
	}
	public Double getLuas_tanah() {
		return luas_tanah;
	}
	public void setLuas_tanah(Double luas_tanah) {
		this.luas_tanah = luas_tanah;
	}
	public Double getHarga_bangunan() {
		return harga_bangunan;
	}
	public void setHarga_bangunan(Double harga_bangunan) {
		this.harga_bangunan = harga_bangunan;
	}
	public Double getHarga_stock() {
		return harga_stock;
	}
	public void setHarga_stock(Double harga_stock) {
		this.harga_stock = harga_stock;
	}
	public Double getHarga_perabot() {
		return harga_perabot;
	}
	public void setHarga_perabot(Double harga_perabot) {
		this.harga_perabot = harga_perabot;
	}
	public Double getHarga_lainnya() {
		return harga_lainnya;
	}
	public void setHarga_lainnya(Double harga_lainnya) {
		this.harga_lainnya = harga_lainnya;
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
	public String getAlamat1() {
		return alamat1;
	}
	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}
	public String getAlamat2() {
		return alamat2;
	}
	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}
	public String getAlamat3() {
		return alamat3;
	}
	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}
	public String getAlamat4() {
		return alamat4;
	}
	public void setAlamat4(String alamat4) {
		this.alamat4 = alamat4;
	}
	public String getAlamat5() {
		return alamat5;
	}
	public void setAlamat5(String alamat5) {
		this.alamat5 = alamat5;
	}
	public String getType_sertifikat() {
		return type_sertifikat;
	}
	public void setType_sertifikat(String type_sertifikat) {
		this.type_sertifikat = type_sertifikat;
	}
	
	
	
}