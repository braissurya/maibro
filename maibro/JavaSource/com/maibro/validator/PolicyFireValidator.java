package com.maibro.validator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.maibro.model.Address;
import com.maibro.model.Policy;
import com.maibro.model.Product;
import com.maibro.service.DbService;
import com.maibro.utils.Utils;

import static com.maibro.utils.Utils.nvl;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 31, 2013 9:05:55 PM
 * @Description : Validator  untuk Polis Fire
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Component
public class PolicyFireValidator implements Validator {

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
		
		//alamat rumah
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.alamat_rumah", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kota_rumah", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.kodepos_rumah", "NotEmpty", new String[]{""});
		
		//telp
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_kantor", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_hp", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.address.telp_rumah", "NotEmpty", new String[]{""});
		
		//asuransi
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "ins_period", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "beg_date", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "end_date", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "spaj_date", "NotEmpty", new String[]{""});
		
		//bank
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "bank.mst_cab_bank_id", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "bank.clause", "NotEmpty", new String[]{""});
		
		//alamat object Pertanggungan
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.alamat_rumah", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.kota_rumah", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.kodepos_rumah", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.no_sertifikat", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.type_sertifikat", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.jenis_bangunan", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.penggunaan_bangunan", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.harga_bangunan", "NotEmpty", new String[]{""});
		//Yusuf (7 Mar 13), req Chandra MB, tidak wajib
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.harga_stock", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.harga_perabot", "NotEmpty", new String[]{""});
		//ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.harga_lainnya", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.luas_bangunan", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.luas_tanah", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.kondisi_kiri", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.kondisi_kanan", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.kondisi_depan", "NotEmpty", new String[]{""});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.kondisi_belakang", "NotEmpty", new String[]{""});
		
		//step berikut tidak perlu dijalankan bila error
		if(e.hasErrors()) return;

		//2. VALIDASI KHUSUS
		Policy polis = (Policy) obj;
		
		//bila jenis bangunan lainnya, harus diisi keterangannya
		if(polis.getCustomer().getAddressInsured().getJenis_bangunan().intValue() == 3){
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "customer.addressInsured.penggunaan_bangunan", "NotEmpty", new String[]{""});
		}
		
		//hitung umur saja, tidak perlu validasi
		polis.getCustomer().setUmur(Utils.hitUmur(polis.getCustomer().getTgl_lahir(), polis.getBeg_date()));
		
		//step berikut tidak perlu dijalankan bila error
		if(e.hasErrors()) return;
		
		//3. PERHITUNGAN PREMI
		Product prod = polis.getProduct();
		Address insured = polis.getCustomer().getAddressInsured();
		prod.setUp(nvl(insured.getHarga_bangunan()) + nvl(insured.getHarga_stock()) + nvl(insured.getHarga_perabot()) + nvl(insured.getHarga_lainnya()));
		polis.setPlafon_kredit(prod.getUp());
		
		//perhitungan rate, premi
		Map<String, Object> hasil = Utils.hitungPremiKPRFire(dbService, prod.getUp(), prod.getMst_product_id(), polis.getIns_period(), insured.getJenis_bangunan()); 
		prod.setRate((Double) hasil.get("rate"));
		prod.setPremi((Double) hasil.get("premi"));
		polis.setCatatan((String) hasil.get("catatan"));
		
		//bila jenis bangunan bukan LAINNYA, tapi preminya gak ada, maka error
		if(prod.getRate() == null && insured.getJenis_bangunan().intValue() != 3) { 
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
