package com.maibro.validator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.maibro.model.Customer;
import com.maibro.model.Policy;
import com.maibro.model.Product;
import com.maibro.service.DbService;
import com.maibro.utils.Utils;

/**
 * Validator Untuk Input Polis
 * 
 * @author Yusuf
 * @since Jan 30, 2013 (8:18:14 AM)
 *
 */
@Component
public class PolicyAJKValidator implements Validator {

	@Autowired
	protected DbService dbService;
	
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}

	public boolean supports(Class<?> clazz) {
		return Policy.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//1. VALIDASI STANDAR AWAL (tidak boleh kosong, dll)
		
		//product
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.mst_product_id", "NotEmpty", new String[]{""});
		
		//customer
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.nama", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.gender", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tempat_lahir", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tgl_lahir", "NotEmpty", new String[]{""});
		//as of (7 Mar 2013), request by Bambang, kondisi pengecekan BB vs TB semua di waive
		//as of (11 Mei 2013), req by Aris, kondisi pengecekan BB dan TB kembali diwajibkan
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.berat_badan", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tinggi_badan", "NotEmpty", new String[]{""});
		
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.pekerjaan", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.bagian", "NotEmpty", new String[]{""});
		
		//alamat kantor (tidak wajib)
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat_kantor", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kota_kantor", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kodepos_kantor", "NotEmpty", new String[]{""});
		
		//alamat rumah
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat_rumah", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kota_rumah", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kodepos_rumah", "NotEmpty", new String[]{""});
		
		//telp (yg wajib hp saja)
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_kantor", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_hp", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_rumah", "NotEmpty", new String[]{""});
		
		//asuransi
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "plafon_kredit", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "ins_period", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "beg_date", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "end_date", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "jenis_manfaat", "NotEmpty", new String[]{""});
		
		//bank
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "bank.mst_bank_id", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "bank.mst_cab_bank_id", "NotEmpty", new String[]{""});
		
		//beneficiary
		//Yusuf (15/3/13) Req Aris MaiBro, ahli waris tidak wajib
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.nama", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.relasi", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.relasi_lain", "NotEmpty", new String[]{""});
		
		//keterangan kesehatan
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q1_desc", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q2_desc", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q3_desc", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q4_desc", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q5_desc", "NotEmpty", new String[]{""});
		
		//agent
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "agent.nama", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "agent.telp_hp", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "agent.jabatan", "NotEmpty", new String[]{""});
		
		//2. VALIDASI KHUSUS
		Policy polis = (Policy) obj;
		Customer cust = polis.getCustomer();
		
		//bila relasi lainnya, harus diisi keterangannya
		if(!Utils.nvl(polis.getBeneficiary().getNama()).trim().equals("")){
			if(polis.getBeneficiary().getRelasi().intValue() == 0){
				ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.relasi_lain", "NotEmpty", new String[]{""});
			}
		}
		
		//bila kombinasi pertanyaannya bukan Y/T/T/T/T untuk 5 pertanyaan kesehatan, maka harus diinput keterangannya.
		if(!cust.getQ1()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q1_desc", "NotEmpty", new String[]{""});
		if(cust.getQ2()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q2_desc", "NotEmpty", new String[]{""});
		//if(cust.getQ3()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q3_desc", "NotEmpty", new String[]{""});
		if(cust.getQ4()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q4_desc", "NotEmpty", new String[]{""});
		if(cust.getQ5()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q5_desc", "NotEmpty", new String[]{""});
		
		//step berikut tidak perlu dijalankan bila error
		if(e.hasErrors()) return;
		
		if(polis.getPlafon_kredit().compareTo(0.0)==0)e.rejectValue("plafon_kredit", "", "Jumlah Plafon Kredit tidak boleh kosong atau 0");
		
		//hitung dan validasi umur
		cust.setUmur(Utils.hitUmur(polis.getCustomer().getTgl_lahir(), polis.getBeg_date()));
		int umur = cust.getUmur().intValue();
		if(umur < 20 || umur > 64){
			e.rejectValue("customer.umur", "", "Usia masuk yang diperkenankan adalah antara 20 s/d 64 tahun.");
		}
		
		//validasi umur ditambah ins period harus <= 65 (x+n <= 65)
		int ins = polis.getIns_period().intValue();
		if((umur + ins) > 65) {
			e.rejectValue("customer.umur", "", "Jumlah dari Usia Masuk (x) dan Masa Asuransi (n) maksimal 65 tahun (x+n<=65).");
		}
		
		//step berikut tidak perlu dijalankan bila error
		if(e.hasErrors()) return;
		
		Product prod = polis.getProduct();
		prod.setUp(polis.getPlafon_kredit());
		double up = prod.getUp().doubleValue();
		
		boolean standar = true; //kondisi STANDAR / NON STANDAR
		
		//perhitungan jenis rate, jenis medis, kondisi standar/nonstandar, rate, premi
		Map<String, Object> hasil = Utils.hitungPremiKPRLife(dbService, umur, up, prod.getMst_product_id(), ins);
		prod.setJenis_rate((Integer) hasil.get("jenisRate"));
		cust.setJenis_medis((Integer) hasil.get("jenisMedis"));
		standar = (Boolean) hasil.get("standar");
		cust.setStandar(standar);
		prod.setRate((Double) hasil.get("rate"));
		prod.setPremi((Double) hasil.get("premi"));

		//bila kondisi pertanyaan ada yang diluar standar, set jadi non standar juga
		//if(!cust.getQ1() || cust.getQ2() || cust.getQ3() || cust.getQ4() || cust.getQ5()) standar = false;
		if(!cust.getQ1() || cust.getQ2() || cust.getQ4() || cust.getQ5()) standar = false;

		//validasi akhir bila rate ternyata tidak ketemu, tapi kondisi standar, berarti ada rate kurang
		if(standar && prod.getRate() == null) {
			e.rejectValue("product.premi", "", "Rate tidak ditemukan");
		}

		/*
		as of (7 Mar 2013), request by Bambang, kondisi pengecekan BB vs TB semua di waive, jadi akan selalu STANDAR
		
		//pengecekan apakah Tinggi Badan VS Berat Badan dalam kondisi STANDAR / NON STANDAR (overweight/underweight)
		double tb = cust.getTinggi_badan();
		double bb = cust.getBerat_badan();
		
		//kondisi standar dan borderline standar (+8kg)
		if(
				((tb >= 133 && tb <= 137) && (bb >= 30 && bb <= 48+8)) || 
				((tb >= 138 && tb <= 142) && (bb >= 32 && bb <= 52+8)) || 
				((tb >= 143 && tb <= 147) && (bb >= 34 && bb <= 56+8)) || 
				((tb >= 148 && tb <= 152) && (bb >= 37 && bb <= 60+8)) || 
				((tb >= 153 && tb <= 157) && (bb >= 40 && bb <= 64+8)) || 
				((tb >= 158 && tb <= 162) && (bb >= 43 && bb <= 73+8)) || 
				((tb >= 163 && tb <= 167) && (bb >= 46 && bb <= 77+8)) || 
				((tb >= 168 && tb <= 172) && (bb >= 49 && bb <= 81+8)) || 
				((tb >= 173 && tb <= 177) && (bb >= 52 && bb <= 86+8)) || 
				((tb >= 178 && tb <= 182) && (bb >= 55 && bb <= 90+8)) || 
				((tb >= 183 && tb <= 187) && (bb >= 58 && bb <= 97+8)) || 
				((tb >= 188 && tb <= 192) && (bb >= 61 && bb <= 101+8)) || 
				((tb >= 193 && tb <= 197) && (bb >= 64 && bb <= 105+8)) || 
				((tb >= 198 && tb <= 202) && (bb >= 67 && bb <= 109+8)) || 
				((tb >= 203 && tb <= 207) && (bb >= 71 && bb <= 113+8)) || 
				((tb >= 208 && tb <= 212) && (bb >= 75 && bb <= 117+8))){
			standar = true;
			
		//kondisi non standar (under/overweight)
		}else{
			standar = false;
		}
		*/
		
		//notes tambahan, bila kondisi NONSTANDAR (ada medis, atau over/underweight)
		if(!standar) polis.setCatatan("\\n- Catatan Asuransi Jiwa: Kondisi NON-STANDAR, harap mengisi kuesioner kesehatan terkait dan melakukan konfirmasi nilai Premi dan Extra Premi (bila ada) dengan pihak MaiBro.");
		
		//terakhir, hitung end date (bulanan vs tahunan)
		if(polis.getIns_period_bln() != null){
			polis.setEnd_date(Utils.hitungEndDate(polis.getBeg_date(), polis.getIns_period_bln(), 1));
		}else{
			polis.setEnd_date(Utils.hitungEndDate(polis.getBeg_date(), polis.getIns_period(), 0));
		}
			
	}

}
