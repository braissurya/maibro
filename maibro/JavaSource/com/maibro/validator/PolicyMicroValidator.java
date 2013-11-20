package com.maibro.validator;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.maibro.model.Customer;
import com.maibro.model.MstCabBank;
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
public class PolicyMicroValidator implements Validator {
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
		
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.mst_product_id", "NotEmpty", new String[]{"Nama Produk"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "dealref", "NotEmpty", new String[]{"Deal Reff"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "username", "NotEmpty", new String[]{"Username"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "plafon_kredit", "NotEmpty", new String[]{"Plafon Kredit"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "gol_debitur", "NotEmpty", new String[]{"Gol Debitur"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "kode_cabang", "NotEmpty", new String[]{"Kode Cabang"});
		
		//customer
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.nama", "NotEmpty", new String[]{"Nama"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tempat_lahir", "NotEmpty", new String[]{"Tempat Lahir"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.tgl_lahir", "NotEmpty", new String[]{"Tanggal Lahir"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.pekerjaan", "NotEmpty", new String[]{"Pekerjaan"});
		
		


			
		//alamat rumah
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat1", "NotEmpty", new String[]{"Alamat 1"});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat2", "NotEmpty", new String[]{"Alamat 2"});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat3", "NotEmpty", new String[]{"Alamat 3"});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat4", "NotEmpty", new String[]{"Alamat 4"});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat5", "NotEmpty", new String[]{"Alamat 5"});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kodepos_rumah", "NotEmpty", new String[]{"Kode Pos"});
		
			
		//asuransi
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "ins_period", "NotEmpty", new String[]{"Lama Bulan"})	;
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "beg_date", "NotEmpty", new String[]{"Tanggal Realisasi"});
				
		//product
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.rate", "NotEmpty", new String[]{"Rate Asuransi"});
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "product.premi", "NotEmpty", new String[]{"Premi Asuransi"});
		
		//2. VALIDASI KHUSUS
		Policy polis = (Policy) obj;
		Customer cust = polis.getCustomer();
		
	
		
		//step berikut hanya dijalankan bila tidak ada error
		if(!e.hasErrors()){
			
			if(polis.getIns_period()>5){
				e.rejectValue("ins_period", "", "Masa Asuransi maksimal 5 tahun");
			}
			
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
				e.rejectValue("polis.plafon_kredit", "", "Plafon Kredit untuk polis mikro Maks Rp. "+Utils.defaultNF.format(new Double("500000000")));
			}
			
			List<MstCabBank> lsCabBanks=dbService.selectListMstCabBank(null,null, polis.getKode_cabang());
			if(lsCabBanks.isEmpty()){
				ValidationUtils.rejectIfEmptyOrWhitespace(e, "kode_cabang",null, "");
				e.rejectValue("kode_cabang", "", "Kode Cabang tidak ditemukan. Harap hubungi Admin untuk menambah cabang Bank.");
			}else{
				polis.getBank().mst_cab_bank_id=lsCabBanks.get(0).getId();
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
