package com.maibro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 5:12:54 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class MstProduct implements Serializable{

	private static final long serialVersionUID = 2264471547121875096L;
	public Integer id;
	public String nama;
	public Integer mst_bank_id;
	public Integer jenis;
	public String rek_no;
	public String rek_nama;
	public Integer group_product;
	public Integer createby;
	public Date createdate;
	public Integer active;
	public Integer modifyby;
	public Date modifydate;
	
	public String  kode;
	
	//tambahan
	public String mode;
	public String namabank;
	public String createuser;
	public String jenisName;
	public String group_product_name;
	
	public List<MstRate> mstRates=new ArrayList<MstRate>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getMst_bank_id() {
		return mst_bank_id;
	}

	public void setMst_bank_id(Integer mst_bank_id) {
		this.mst_bank_id = mst_bank_id;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<MstRate> getMstRates() {
		return mstRates;
	}

	public void setMstRates(List<MstRate> mstRates) {
		this.mstRates = mstRates;
	}

	public String getNamabank() {
		return namabank;
	}

	public void setNamabank(String namabank) {
		this.namabank = namabank;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
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

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;		
	}

	public String getJenisName() {
		return jenisName;
	}

	public void setJenisName(String jenisName) {
		this.jenisName = jenisName;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getRek_no() {
		return rek_no;
	}

	public void setRek_no(String rek_no) {
		this.rek_no = rek_no;
	}

	public String getRek_nama() {
		return rek_nama;
	}

	public void setRek_nama(String rek_nama) {
		this.rek_nama = rek_nama;
	}

	public Integer getGroup_product() {
		return group_product;
	}

	public void setGroup_product(Integer group_product) {
		this.group_product = group_product;
	}

	public String getGroup_product_name() {
		return group_product_name;
	}

	public void setGroup_product_name(String group_product_name) {
		this.group_product_name = group_product_name;
	}
	
}
