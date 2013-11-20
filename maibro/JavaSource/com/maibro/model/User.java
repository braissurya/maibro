package com.maibro.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Domain object untuk table USER
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = 5367856769525827869L;

	//variable utama, validasi2 standar langsung di-define disini dan 
	//di-enable di Spring menggunakan annotation @Valid, dan menggunakan library Hibernate Validator (spesifikasi JSR-303)
	//error message bisa di-define satu persatu disini, atau diletakkan di messages.properties di WEB-INF
	//untuk definisi lengkapnya, bisa lihat dokumentasi hibernate validator 4.2.0 bagian 2.4.1
	
	public Integer id;
	
	@NotEmpty
	@Size(max=20, min=3)
	@Pattern(regexp="^[a-zA-Z0-9_-]+$")
	public String username;

	@NotEmpty
	@Size(max=20, min=6)
	public String password;
	
	public String newPassword;
	public String confirmPassword;
	
	public Integer createby;
	public Date createdate;
	public Integer active;
	public Integer modifyby;
	public Date modifydate;
	public Integer bank_id;
	public Integer cab_bank_id;
	public Integer mst_product_id;
	
	@Email(message="Harap masukkan format email yang benar") //contoh bila error message langsung diletakkan disini, tapi ini akan dioverride oleh message.properties bila ada
	public String email;
	
	//variable tambahan
	public Date loginTime;
	public List<Menu> listMenu;
	public Integer group_menu_id;
	
	public String menuUser;
	
	public String mode;
	public String passdecrypt;
	public String namagroup;
	public String namabank;
	public String namacabang;
	public String namaproduct;
	
	public Integer bank_jenis;
	public Integer cab_bank_jenis;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	//constructors
	public User() {
	}
	
	public Integer getBank_jenis() {
		return bank_jenis;
	}

	public void setBank_jenis(Integer bank_jenis) {
		this.bank_jenis = bank_jenis;
	}

	public Integer getCab_bank_jenis() {
		return cab_bank_jenis;
	}

	public void setCab_bank_jenis(Integer cab_bank_jenis) {
		this.cab_bank_jenis = cab_bank_jenis;
	}

	public Integer getGroup_menu_id() {
		return group_menu_id;
	}

	public void setGroup_menu_id(Integer group_menu_id) {
		this.group_menu_id = group_menu_id;
	}

	public List<Menu> getListMenu() {
		return listMenu;
	}

	public void setListMenu(List<Menu> listMenu) {
		this.listMenu = listMenu;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getMenuUser() {
		return menuUser;
	}

	public void setMenuUser(String menuUser) {
		this.menuUser = menuUser;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPassdecrypt() {
		return passdecrypt;
	}

	public void setPassdecrypt(String passdecrypt) {
		this.passdecrypt = passdecrypt;
	}

	public String getNamagroup() {
		return namagroup;
	}

	public void setNamagroup(String namagroup) {
		this.namagroup = namagroup;
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

	public Integer getBank_id() {
		return bank_id;
	}

	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}

	public Integer getCab_bank_id() {
		return cab_bank_id;
	}

	public void setCab_bank_id(Integer cab_bank_id) {
		this.cab_bank_id = cab_bank_id;
	}

	public String getNamabank() {
		return namabank;
	}

	public void setNamabank(String namabank) {
		this.namabank = namabank;
	}

	public String getNamacabang() {
		return namacabang;
	}

	public void setNamacabang(String namacabang) {
		this.namacabang = namacabang;
	}

	public Integer getMst_product_id() {
		return mst_product_id;
	}

	public void setMst_product_id(Integer mst_product_id) {
		this.mst_product_id = mst_product_id;
	}

	public String getNamaproduct() {
		return namaproduct;
	}

	public void setNamaproduct(String namaproduct) {
		this.namaproduct = namaproduct;
	}
	
	
}