package com.maibro.model;

import java.io.Serializable;
import java.util.Date;

import com.maibro.utils.Utils;

/**
 * @author 		: Rudy Hermawan
 * @since		: Feb 04, 2013 3:58:00 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class MstMaster implements Serializable{
	
	private static final long serialVersionUID = 5860055857112401074L;
	
	public Integer id;
	public Integer jenis;
	public String keterangan;
	public Integer createby;
	public Date createdate;
	public Integer active;
	public Integer modifyby;
	public Date modifydate;
	
	//tambahan
	public String mode;
	public String createuser;
	public String idName;
	public Integer idUpdate;
	public Integer jenisUpdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		if(id!=null){
			for(DropDown dd:Utils.lstMaster()){
				if(dd.getKey().equals(""+id)){
					idName = dd.getValue();
				}
			}
		}
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
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

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public Integer getIdUpdate() {
		return idUpdate;
	}

	public void setIdUpdate(Integer idUpdate) {
		this.idUpdate = idUpdate;
	}

	public Integer getJenisUpdate() {
		return jenisUpdate;
	}

	public void setJenisUpdate(Integer jenisUpdate) {
		this.jenisUpdate = jenisUpdate;
	}
	
	
}
