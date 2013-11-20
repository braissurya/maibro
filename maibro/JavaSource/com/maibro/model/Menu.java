package com.maibro.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Domain object untuk table MENU
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Menu implements Serializable {

	private static final long serialVersionUID = 1461870992296826635L;
	
	public Integer id;
	public Integer parent;
	public String nama;
	public String link;
	public Integer urut;
	public Integer active;
	public Integer createby;
	public Date createdate;
	public Integer modifyby;
	public Date modifydate;
	
	
	//tambahan
	public String mode;
	
	public List<Menu> listChild;
	public String parent_nama;
	public String parent_link;
	public Boolean akses;
	
	//constructors
	public Menu() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getUrut() {
		return urut;
	}

	public void setUrut(Integer urut) {
		this.urut = urut;
	}

	public String getParent_nama() {
		return parent_nama;
	}

	public void setParent_nama(String parent_nama) {
		this.parent_nama = parent_nama;
	}

	public String getParent_link() {
		return parent_link;
	}

	public void setParent_link(String parent_link) {
		this.parent_link = parent_link;
	}

	public List<Menu> getListChild() {
		return listChild;
	}

	public void setListChild(List<Menu> listChild) {
		this.listChild = listChild;
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

	public Boolean getAkses() {
		return akses;
	}

	public void setAkses(Boolean akses) {
		this.akses = akses;
	}
	
	
}