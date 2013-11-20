package com.maibro.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GroupMenu implements Serializable {
	private static final long serialVersionUID = 6937296910488083803L;
	
	public Integer id;
	public Integer menu_id;
	public String nama;
	public Integer createby;
	public Date createdate;
	public Integer modifyby;
	public Date modifydate;
	public Integer active;
	
	//tambahan
	public String mode;
	public String namamenu;
	
	public List<Menu> menu;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getNamamenu() {
		return namamenu;
	}
	public void setNamamenu(String namamenu) {
		this.namamenu = namamenu;
	}
	public List<Menu> getMenu() {
		return menu;
	}
	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	
	
	
}
