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
public class PolicyMicroAJKValidator implements Validator {

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
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.gender", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tempat_lahir", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tgl_lahir", "NotEmpty", new String[]{""});
		
		//as of (7 Mar 2013), request by Bambang, kondisi pengecekan BB vs TB semua di waive
		//as of (11 Mei 2013), req by Aris, kondisi pengecekan BB dan TB kembali diwajibkan		
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.berat_badan", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tinggi_badan", "NotEmpty", new String[]{""});

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.pekerjaan", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.bagian", "NotEmpty", new String[]{""});
		
		//alamat kantor (tidak wajib)
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat_kantor", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kota_kantor", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kodepos_kantor", "NotEmpty", new String[]{""});
		
		//alamat rumah
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat_rumah", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kota_rumah", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kodepos_rumah", "NotEmpty", new String[]{""});
		
		//telp (yg wajib hp saja)
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_kantor", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_hp", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_rumah", "NotEmpty", new String[]{""});
		
		//asuransi
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "plafon_kredit", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "ins_period", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "beg_date", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "end_date", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "jenis_manfaat", "NotEmpty", new String[]{""});
		
		//bank
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "bank.mst_bank_id", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "bank.mst_cab_bank_id", "NotEmpty", new String[]{""});
		
		//beneficiary
		//Yusuf (15/3/13) Req Aris MaiBro, ahli waris tidak wajib
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.nama", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.relasi", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.relasi_lain", "NotEmpty", new String[]{""});
		
		//keterangan kesehatan
		/*ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q1_desc", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q2_desc", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q3_desc", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q4_desc", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q5_desc", "NotEmpty", new String[]{""});*/
		
		//agent
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "agent.nama", "NotEmpty", new String[]{""});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "agent.telp_hp", "NotEmpty", new String[]{""});
		
		//2. VALIDASI KHUSUS FIXME belum ditest satu per satu
		Policy polis = (Policy) obj;
		Customer cust = polis.getCustomer();
		
		//bila relasi lainnya, harus diisi keterangannya
		if(polis.getBeneficiary().getRelasi()!=null)
		if(polis.getBeneficiary().getRelasi().intValue() == 0){
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "beneficiary.relasi_lain", "NotEmpty", new String[]{""});
		}
		
		//bila kombinasi pertanyaannya bukan Y/T/T/T/T untuk 5 pertanyaan kesehatan, maka harus diinput keterangannya.
	/*	if(!cust.isQ1()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q1_desc", "NotEmpty", new String[]{""});
		if(cust.isQ2()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q2_desc", "NotEmpty", new String[]{""});
		if(cust.isQ3()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q3_desc", "NotEmpty", new String[]{""});
		if(cust.isQ4()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q4_desc", "NotEmpty", new String[]{""});
		if(cust.isQ5()) ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.q5_desc", "NotEmpty", new String[]{""});*/
		
	
		
		//step berikut hanya dijalankan bila tidak ada error
		if(!e.hasErrors()){
			
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
			
			if(polis.getPlafon_kredit().compareTo(new Double("500000000"))>0){
				e.rejectValue("plafon_kredit", "", "Plafon Kredit untuk polis mikro Maks Rp. "+Utils.defaultNF.format(new Double("500000000")));
			}
			
			//3. PERHITUNGAN PREMI
			polis.getProduct().setUp(polis.getPlafon_kredit());
			Product prod = polis.getProduct();
			polis.setPlafon_kredit(prod.getUp());
			prod.setJenis_rate(2);
			
			//perhitungan rate, premi
			Map<String, Object> hasil = Utils.hitungPremiMicroLife(dbService, prod.getUp(), prod.getMst_product_id(), polis.getIns_period()); 
			prod.setRate((Double) hasil.get("rate"));
			prod.setPremi((Double) hasil.get("premi"));

			//bila rate tidak ada, error
			if(prod.getRate() == null) {
				e.rejectValue("product.premi", "", "Rate tidak ditemukan");
			}
			
			//terakhir, hitung end date (bulanan vs tahunan)
			if(polis.getIns_period_bln() != null){
				polis.setEnd_date(Utils.hitungEndDate(polis.getBeg_date(), polis.getIns_period_bln(), 1));
			}else{
				polis.setEnd_date(Utils.hitungEndDate(polis.getBeg_date(), polis.getIns_period(), 0));
			}
			
		}
		
	}

}
