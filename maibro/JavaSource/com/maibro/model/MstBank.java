package com.maibro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.maibro.utils.Utils;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 4:58:39 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class MstBank implements Serializable{

	private static final long serialVersionUID = 7872871092524372672L;
	
	public Integer id;
	public Integer jenis;
	public String nama;
	public String polis_induk;
	public Double diskon_premi;
	public Double diskon_brokerage;
	public Double diskon_komisi;
	public Double ppn_premi;
	public Double pph_premi;
	public Double pph_bpr;
	public Double pph_agent;
	public String email;
	public Integer createby;
	public Date createdate;
	public Integer active;
	public Integer modifyby;
	public Date modifydate;
	
	//tambahan
	public String mode;
	public String createuser;
	public String jenisName;
	
	public MultipartFile uploadFile;
	public MultipartFile uploadFileTTD;
	public MultipartFile uploadFileCap;
	
	public List<MstCabBank> cabBanks=new ArrayList<MstCabBank>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
		if(jenis!=null){
			for(DropDown dd:Utils.lstJenisBank()){
				if(dd.getKey().equals(""+jenis)){
					jenisName = dd.getValue();
				}
			}
		}
			
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Double getDiskon_premi() {
		return diskon_premi;
	}

	public void setDiskon_premi(Double diskon_premi) {
		this.diskon_premi = diskon_premi;
	}

	public Double getDiskon_brokerage() {
		return diskon_brokerage;
	}

	public void setDiskon_brokerage(Double diskon_brokerage) {
		this.diskon_brokerage = diskon_brokerage;
	}

	public Double getDiskon_komisi() {
		return diskon_komisi;
	}

	public void setDiskon_komisi(Double diskon_komisi) {
		this.diskon_komisi = diskon_komisi;
	}	

	public Double getPpn_premi() {
		return ppn_premi;
	}

	public void setPpn_premi(Double ppn_premi) {
		this.ppn_premi = ppn_premi;
	}

	public Double getPph_premi() {
		return pph_premi;
	}

	public void setPph_premi(Double pph_premi) {
		this.pph_premi = pph_premi;
	}

	public Double getPph_bpr() {
		return pph_bpr;
	}

	public void setPph_bpr(Double pph_bpr) {
		this.pph_bpr = pph_bpr;
	}

	public Double getPph_agent() {
		return pph_agent;
	}

	public void setPph_agent(Double pph_agent) {
		this.pph_agent = pph_agent;
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

	public List<MstCabBank> getCabBanks() {
		return cabBanks;
	}

	public void setCabBanks(List<MstCabBank> cabBanks) {
		this.cabBanks = cabBanks;
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

	public String getJenisName() {
		return jenisName;
	}

	public void setJenisName(String jenisName) {
		this.jenisName = jenisName;
	}

	public String getPolis_induk() {
		return polis_induk;
	}

	public void setPolis_induk(String polis_induk) {
		this.polis_induk = polis_induk;
	}

	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public MultipartFile getUploadFileTTD() {
		return uploadFileTTD;
	}

	public void setUploadFileTTD(MultipartFile uploadFileTTD) {
		this.uploadFileTTD = uploadFileTTD;
	}

	public MultipartFile getUploadFileCap() {
		return uploadFileCap;
	}

	public void setUploadFileCap(MultipartFile uploadFileCap) {
		this.uploadFileCap = uploadFileCap;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
}
