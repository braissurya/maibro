package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

import com.maibro.utils.Utils;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 5:17:07 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class MstRate implements Serializable{
	
	private static final long serialVersionUID = 2644187412410230533L;
	public Integer id;
	public Integer mst_product_id;
	public Integer tenor;
	public Integer umur;
	public Integer jenis_bangunan;
	public Integer jenis_rate;
	public Double rate;
	public Integer createby;
	public Date createdate;
	public Integer active;
	public Integer modifyby;
	public Date modifydate;
	
	//tambahan
	public String mode;
	public String namaproduct;
	public String createuser;
	public String jenis_bangunanNama;
	public String fileUpload;
	public String jenis_rateNama;
	
	public MstRate(){
		
	}
	
	public MstRate(Integer tenor,Integer umur,Integer jenis_bangunan,Double rate,Integer mst_product_id){
		this.tenor=tenor;
		this.umur=umur;
		this.jenis_bangunan=jenis_bangunan;
		this.mst_product_id=mst_product_id;
		this.rate=rate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMst_product_id() {
		return mst_product_id;
	}

	public void setMst_product_id(Integer mst_product_id) {
		this.mst_product_id = mst_product_id;
	}

	public Integer getTenor() {
		return tenor;
	}

	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}

	public Integer getUmur() {
		return umur;
	}

	public void setUmur(Integer umur) {
		this.umur = umur;
	}

	public Integer getJenis_bangunan() {
		return jenis_bangunan;
	}

	public void setJenis_bangunan(Integer jenis_bangunan) {
		this.jenis_bangunan = jenis_bangunan;
		if(jenis_bangunan!=null){
			for(DropDown dd:Utils.lstjenisBangunan()){
				if(dd.getKey().equals(""+jenis_bangunan)){
					jenis_bangunanNama = dd.getValue();
				}
			}
		}
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
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

	public String getNamaproduct() {
		return namaproduct;
	}

	public void setNamaproduct(String namaproduct) {
		this.namaproduct = namaproduct;
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

	public String getJenis_bangunanNama() {
		return jenis_bangunanNama;
	}

	public void setJenis_bangunanNama(String jenis_bangunanNama) {
		this.jenis_bangunanNama = jenis_bangunanNama;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public Integer getJenis_rate() {
		return jenis_rate;
	}

	public void setJenis_rate(Integer jenis_rate) {
		this.jenis_rate = jenis_rate;
	}

	public String getJenis_rateNama() {
		return jenis_rateNama;
	}

	public void setJenis_rateNama(String jenis_rateNama) {
		this.jenis_rateNama = jenis_rateNama;
	}
	
	
	
}
