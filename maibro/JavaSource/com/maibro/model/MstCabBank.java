package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

import com.maibro.utils.Utils;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 5:10:57 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class MstCabBank implements Serializable{

	private static final long serialVersionUID = 8290537442029353527L;
	public Integer id;
	public Integer mst_bank_id;
	public Integer cab_induk_id;
	public Integer jenis;
	public String kode_bank;
	public String nama;
	public Integer createby;
	public Date createdate;
	public Integer active;
	public Integer modifyby;
	public Date modifydate;
	
	//tambahan
	public String mode;
	public String createuser;
	public String namabank;
	public String jenisName;
	public String namacabinduk;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMst_bank_id() {
		return mst_bank_id;
	}

	public void setMst_bank_id(Integer mst_bank_id) {
		this.mst_bank_id = mst_bank_id;
	}

	public Integer getCab_induk_id() {
		return cab_induk_id;
	}

	public void setCab_induk_id(Integer cab_induk_id) {
		this.cab_induk_id = cab_induk_id;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
		if(jenis!=null){
			for(DropDown dd:Utils.lstJenisCabBank()){
				if(dd.getKey().equals(""+jenis)){
					jenisName = dd.getValue();
				}
			}
		}
	}

	public String getKode_bank() {
		return kode_bank;
	}

	public void setKode_bank(String kode_bank) {
		this.kode_bank = kode_bank;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
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

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getNamabank() {
		return namabank;
	}

	public void setNamabank(String namabank) {
		this.namabank = namabank;
	}

	public String getJenisName() {
		return jenisName;
	}

	public void setJenisName(String jenisName) {
		this.jenisName = jenisName;
	}

	public String getNamacabinduk() {
		return namacabinduk;
	}

	public void setNamacabinduk(String namacabinduk) {
		this.namacabinduk = namacabinduk;
	}
	
	
}
