package com.maibro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.maibro.utils.Utils;

/**
 * Domain object untuk table POLICY
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:21 AM)
 *
 */
public class Policy implements Serializable {

	private static final long serialVersionUID = -5032254942414657390L;

	//kolom2 tabel
	private Integer id;
	private Integer fire_id;
	private Integer customer_id;
	private Integer posisi;
	private String spaj_no;
	private String policy_no;
	private Double plafon_kredit;
	private Date spaj_date;
	private Integer ins_period;
	private Integer ins_period_bln;
	private Date beg_date;
	private Date end_date;
	private Integer jenis_manfaat;
	private Integer pay_mode;
	private String gol_debitur;
	private String kode_cabang;
	private String username;
	private String dealref;
	private Integer group_policy_id;
	
	private Integer asuransi_id;
	private String asuransi_desc;
	private Integer flag_akseptasi;
	private Integer flag_akseptasi_mb;
	private Date tgl_aksep;
	private Date tgl_aksep_end;
	private String desc_akseptasi;
	private Date tgl_print;
	private Date tgl_print_spaj;
	private Date tgl_upload_spaj;
	private Date tgl_upload_kuesioner;
	private Date tgl_kirim_asuransi;
	private Integer flag_view_asuransi;
	private Date tgl_view_asuransi;
	private Date tgl_upload_no_klaim;
	private Date tgl_upload_ktp;
	
	private String filetype_spaj;
	private String filetype_kuesioner;
	private String filetype_klaim;
	private String filetype_ktp;
	
	private Integer createby;
	private Date createdate;
	private Integer modifyby;
	private Date modifydate;
	
	//kolom2 tambahan
	private String mode;	
	private String debitur;
	private String produk;
	private String createuser;
	private String modifyuser;
	private Double nominal_paid;
	private Date tgl_paid;
	private String inputpayuser;
	private Double premi;
	private Double extrapremi;
	private Double totalpremi;
	private Double up;
	private Double rate;
	private Integer jenis;
	private Integer flag_paid;
	private Double nominal_remain;
	private Double diskon_premi;
	private Double ppn_premi;
	private Double pph_premi;
	private Double diskon;
	private Double ppn;
	private Double pph;
	private Double premi_net;
	private String deductible;
	private Integer noclaim;
	
	private String beg_dateStr;
	private String end_dateStr;
	private String spaj_dateStr;
	private String namaasuransi;
	private String namabank;
	private String namacabbank;
	
	private Customer customer;
	private Bank bank;
	private Beneficiary beneficiary;
	private Agent agent;
	private Product product;
	private History history;
	private String catatan;
	private String fileUpload;

	private String posisiKet;
	private String namacustomer;
	private String namaproduk;
	private String keteranganCancel;
	
	private String tgl_aksepStr;
	private String tgl_aksep_endStr;
	
	private String no_pk;
	
	public String getKeteranganCancel() {
		return keteranganCancel;
	}

	public void setKeteranganCancel(String keteranganCancel) {
		this.keteranganCancel = keteranganCancel;
	}

	public String getNamacustomer() {
		return namacustomer;
	}

	public void setNamacustomer(String namacustomer) {
		this.namacustomer = namacustomer;
	}

	public String getNamaproduk() {
		return namaproduk;
	}

	public void setNamaproduk(String namaproduk) {
		this.namaproduk = namaproduk;
	}

	public Integer getFlag_akseptasi_mb() {
		return flag_akseptasi_mb;
	}

	public void setFlag_akseptasi_mb(Integer flag_akseptasi_mb) {
		this.flag_akseptasi_mb = flag_akseptasi_mb;
	}

	public String getPosisiKet() {
		return posisiKet;
	}

	public void setPosisiKet(String posisiKet) {
		this.posisiKet = posisiKet;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Policy() {
		this.customer=new Customer();
		this.bank=new Bank();
		this.beneficiary=new Beneficiary();
		this.agent=new Agent();
		this.product=new Product();
		this.history=new History();
	}
	
	public Policy(String no_spaj, Integer akseptasi, String desc_akseptasi,Date tgl_aksep, Double rate, Double premi_pokok,Double extra_premi,Double biaya_admin, Double premi_netto, String no_polis){
		this.customer=new Customer();
		this.bank=new Bank();
		this.beneficiary=new Beneficiary();
		this.agent=new Agent();
		this.product=new Product();
		this.history=new History();
		
		this.spaj_no = no_spaj;
		this.flag_akseptasi = akseptasi;
		this.tgl_aksep= tgl_aksep;
		this.desc_akseptasi=desc_akseptasi;
		this.product.rate=rate;
		this.product.premi_pokok=premi_pokok;
		this.product.premi_extra=extra_premi;
		this.product.biaya_admin=biaya_admin;
		this.product.premi=premi_netto;
		this.policy_no=no_polis;
	}
	
	public Policy(String dealref,String nama, String tempatlahir,Date tgllahir, Date tglrealisasi,String pekerjaan, Integer lamabulan,Double rateasuransi,
			Double premiasuransi,Double plafondkredit,String goldebitur,String kodecabang,String username, String alamat1,String alamat2, String alamat3,String alamat4,
			String	alamat5,String kodepos){
		this.customer=new Customer();
		this.bank=new Bank();
		this.beneficiary=new Beneficiary();
		this.agent=new Agent();
		this.product=new Product();
		this.history=new History();
		
		
		
		this.beg_date=tglrealisasi;
		this.customer.setPekerjaan(pekerjaan);
		this.ins_period_bln=lamabulan;
		this.ins_period=lamabulan/12;
		
		this.product.rate=rateasuransi;
		this.product.premi=premiasuransi;		
		this.product.up=plafondkredit;
		this.plafon_kredit=plafondkredit;
		
		this.dealref=dealref;
		if(!Utils.isEmpty(dealref)&&dealref.toUpperCase().contains("E")){
			BigDecimal bd = new BigDecimal(dealref);
			this.dealref=bd.toPlainString();
		}
		this.gol_debitur=goldebitur;		
		this.username=username;
		
		this.kode_cabang=Utils.rpad("0", kodecabang, 4);
		
		this.customer.setNama(nama);
		this.customer.setTempat_lahir(tempatlahir);
		this.customer.setTgl_lahir(tgllahir);
		this.customer.getAddress().setAlamat1(alamat1);
		this.customer.getAddress().setAlamat2(alamat2);
		this.customer.getAddress().setAlamat3(alamat3);
		this.customer.getAddress().setAlamat4(alamat4);
		this.customer.getAddress().setAlamat5(alamat5);
		this.customer.getAddress().setAlamat_rumah(alamat1+" \n"+alamat2+" \n"+alamat3+" \n"+alamat4+" \n"+alamat5);
		this.customer.getAddress().setKodepos_rumah(kodepos);
		
	}
	
	public String getModifyuser() {
		return modifyuser;
	}
	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
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
	public History getHistory() {
		return history;
	}
	public void setHistory(History history) {
		this.history = history;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getFire_id() {
		return fire_id;
	}
	public void setFire_id(Integer fire_id) {
		this.fire_id = fire_id;
	}
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public Beneficiary getBeneficiary() {
		return beneficiary;
	}
	public void setBeneficiary(Beneficiary beneficiary) {
		this.beneficiary = beneficiary;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getDebitur() {
		return debitur;
	}
	public void setDebitur(String debitur) {
		this.debitur = debitur;
	}
	public String getProduk() {
		return produk;
	}
	public void setProduk(String produk) {
		this.produk = produk;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}
	public String getSpaj_no() {
		return spaj_no;
	}
	public void setSpaj_no(String spaj_no) {
		this.spaj_no = spaj_no;
	}
	public String getPolicy_no() {
		return policy_no;
	}
	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}
	public Double getPlafon_kredit() {
		return plafon_kredit;
	}
	public void setPlafon_kredit(Double plafon_kredit) {
		this.plafon_kredit = plafon_kredit;
	}
	public Date getSpaj_date() {
		return spaj_date;
	}
	public void setSpaj_date(Date spaj_date) {
		this.spaj_date = spaj_date;
		if(spaj_date!=null){
			spaj_dateStr=Utils.convertDateToString(spaj_date, "dd/MM/yyyy");
		}
	}
	public Integer getIns_period() {
		return ins_period;
	}
	public void setIns_period(Integer ins_period) {
		this.ins_period = ins_period;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
		if(beg_date!=null){
			beg_dateStr=Utils.convertDateToString(beg_date, "dd/MM/yyyy");
		}
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
		if(end_date!=null){
			end_dateStr=Utils.convertDateToString(end_date, "dd/MM/yyyy");
		}
	}
	public Integer getJenis_manfaat() {
		return jenis_manfaat;
	}
	public void setJenis_manfaat(Integer jenis_manfaat) {
		this.jenis_manfaat = jenis_manfaat;
	}
	public Integer getPay_mode() {
		return pay_mode;
	}
	public void setPay_mode(Integer pay_mode) {
		this.pay_mode = pay_mode;
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
	public String getBeg_dateStr() {
		return beg_dateStr;
	}
	public void setBeg_dateStr(String beg_dateStr) {
		this.beg_dateStr = beg_dateStr;
		if(!Utils.isEmpty(beg_dateStr)){
			beg_date=Utils.convertStringToDate(beg_dateStr, "dd/MM/yyyy");
		}
	}
	public String getEnd_dateStr() {
		return end_dateStr;
	}
	public void setEnd_dateStr(String end_dateStr) {
		this.end_dateStr = end_dateStr;
		if(!Utils.isEmpty(end_dateStr)){
			end_date=Utils.convertStringToDate(end_dateStr, "dd/MM/yyyy");
		}
	}
	public String getSpaj_dateStr() {
		return spaj_dateStr;
	}
	public void setSpaj_dateStr(String spaj_dateStr) {
		this.spaj_dateStr = spaj_dateStr;
		if(!Utils.isEmpty(spaj_dateStr)){
			spaj_date=Utils.convertStringToDate(spaj_dateStr, "dd/MM/yyyy");
		}
	}

	public String getGol_debitur() {
		return gol_debitur;
	}

	public void setGol_debitur(String gol_debitur) {
		this.gol_debitur = gol_debitur;
	}

	public String getKode_cabang() {
		return kode_cabang;
	}

	public void setKode_cabang(String kode_cabang) {
		this.kode_cabang = kode_cabang;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDealref() {
		return dealref;
	}

	public void setDealref(String dealref) {
		this.dealref = dealref;
	}

	public Integer getGroup_policy_id() {
		return group_policy_id;
	}

	public void setGroup_policy_id(Integer group_policy_id) {
		this.group_policy_id = group_policy_id;
	}

	

	public Integer getAsuransi_id() {
		return asuransi_id;
	}

	public void setAsuransi_id(Integer asuransi_id) {
		this.asuransi_id = asuransi_id;
	}

	public Double getNominal_paid() {
		return nominal_paid;
	}

	public void setNominal_paid(Double nominal_paid) {
		this.nominal_paid = nominal_paid;
	}

	public Date getTgl_paid() {
		return tgl_paid;
	}

	public void setTgl_paid(Date tgl_paid) {
		this.tgl_paid = tgl_paid;
	}

	public Double getPremi() {
		return premi;
	}

	public void setPremi(Double premi) {
		this.premi = premi;
	}

	public Double getUp() {
		return up;
	}

	public void setUp(Double up) {
		this.up = up;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getInputpayuser() {
		return inputpayuser;
	}

	public void setInputpayuser(String inputpayuser) {
		this.inputpayuser = inputpayuser;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public Integer getIns_period_bln() {
		return ins_period_bln;
	}

	public void setIns_period_bln(Integer ins_period_bln) {
		this.ins_period_bln = ins_period_bln;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getAsuransi_desc() {
		return asuransi_desc;
	}

	public void setAsuransi_desc(String asuransi_desc) {
		this.asuransi_desc = asuransi_desc;
	}

	public Integer getFlag_akseptasi() {
		return flag_akseptasi;
	}

	public void setFlag_akseptasi(Integer flag_akseptasi) {
		this.flag_akseptasi = flag_akseptasi;
	}

	public Date getTgl_aksep() {
		return tgl_aksep;
	}

	public void setTgl_aksep(Date tgl_aksep) {
		this.tgl_aksep = tgl_aksep;
		if(tgl_aksep!=null){
			tgl_aksepStr=Utils.convertDateToString(tgl_aksep, "dd/MM/yyyy");
		}
	}

	public String getDesc_akseptasi() {
		return desc_akseptasi;
	}

	public void setDesc_akseptasi(String desc_akseptasi) {
		this.desc_akseptasi = desc_akseptasi;
	}

	public Date getTgl_print() {
		return tgl_print;
	}

	public void setTgl_print(Date tgl_print) {
		this.tgl_print = tgl_print;
	}

	public String getNamaasuransi() {
		return namaasuransi;
	}

	public void setNamaasuransi(String namaasuransi) {
		this.namaasuransi = namaasuransi;
	}

	public String getNamabank() {
		return namabank;
	}

	public void setNamabank(String namabank) {
		this.namabank = namabank;
	}

	public String getNamacabbank() {
		return namacabbank;
	}

	public void setNamacabbank(String namacabbank) {
		this.namacabbank = namacabbank;
	}

	public Date getTgl_print_spaj() {
		return tgl_print_spaj;
	}

	public void setTgl_print_spaj(Date tgl_print_spaj) {
		this.tgl_print_spaj = tgl_print_spaj;
	}

	public Date getTgl_upload_spaj() {
		return tgl_upload_spaj;
	}

	public void setTgl_upload_spaj(Date tgl_upload_spaj) {
		this.tgl_upload_spaj = tgl_upload_spaj;
	}

	public Integer getFlag_paid() {
		return flag_paid;
	}

	public void setFlag_paid(Integer flag_paid) {
		this.flag_paid = flag_paid;
	}

	public Double getNominal_remain() {
		return nominal_remain;
	}

	public void setNominal_remain(Double nominal_remain) {
		this.nominal_remain = nominal_remain;
	}

	public Date getTgl_upload_kuesioner() {
		return tgl_upload_kuesioner;
	}

	public void setTgl_upload_kuesioner(Date tgl_upload_kuesioner) {
		this.tgl_upload_kuesioner = tgl_upload_kuesioner;
	}

	public Double getDiskon_premi() {
		return diskon_premi;
	}

	public void setDiskon_premi(Double diskon_premi) {
		this.diskon_premi = diskon_premi;
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

	public Double getDiskon() {
		return diskon;
	}

	public void setDiskon(Double diskon) {
		this.diskon = diskon;
	}

	
	public Double getPpn() {
		return ppn;
	}

	public void setPpn(Double ppn) {
		this.ppn = ppn;
	}

	public Double getPph() {
		return pph;
	}

	public void setPph(Double pph) {
		this.pph = pph;
	}

	public Double getPremi_net() {
		return premi_net;
	}

	public void setPremi_net(Double premi_net) {
		this.premi_net = premi_net;
	}

	public Date getTgl_kirim_asuransi() {
		return tgl_kirim_asuransi;
	}

	public void setTgl_kirim_asuransi(Date tgl_kirim_asuransi) {
		this.tgl_kirim_asuransi = tgl_kirim_asuransi;
	}

	public Integer getFlag_view_asuransi() {
		return flag_view_asuransi;
	}

	public void setFlag_view_asuransi(Integer flag_view_asuransi) {
		this.flag_view_asuransi = flag_view_asuransi;
	}

	public Date getTgl_view_asuransi() {
		return tgl_view_asuransi;
	}

	public void setTgl_view_asuransi(Date tgl_view_asuransi) {
		this.tgl_view_asuransi = tgl_view_asuransi;
	}

	public String getDeductible() {
		return deductible;
	}

	public void setDeductible(String deductible) {
		this.deductible = deductible;
	}

	public Date getTgl_upload_no_klaim() {
		return tgl_upload_no_klaim;
	}

	public void setTgl_upload_no_klaim(Date tgl_upload_no_klaim) {
		this.tgl_upload_no_klaim = tgl_upload_no_klaim;
	}

	public Date getTgl_upload_ktp() {
		return tgl_upload_ktp;
	}

	public void setTgl_upload_ktp(Date tgl_upload_ktp) {
		this.tgl_upload_ktp = tgl_upload_ktp;
	}

	public Integer getNoclaim() {
		return noclaim;
	}

	public void setNoclaim(Integer noclaim) {
		this.noclaim = noclaim;
	}

	public Double getExtrapremi() {
		return extrapremi;
	}

	public void setExtrapremi(Double extrapremi) {
		this.extrapremi = extrapremi;
	}

	public Double getTotalpremi() {
		return totalpremi;
	}

	public void setTotalpremi(Double totalpremi) {
		this.totalpremi = totalpremi;
	}

	public String getFiletype_spaj() {
		return filetype_spaj;
	}

	public void setFiletype_spaj(String filetype_spaj) {
		this.filetype_spaj = filetype_spaj;
	}

	public String getFiletype_kuesioner() {
		return filetype_kuesioner;
	}

	public void setFiletype_kuesioner(String filetype_kuesioner) {
		this.filetype_kuesioner = filetype_kuesioner;
	}

	public String getFiletype_klaim() {
		return filetype_klaim;
	}

	public void setFiletype_klaim(String filetype_klaim) {
		this.filetype_klaim = filetype_klaim;
	}

	public String getFiletype_ktp() {
		return filetype_ktp;
	}

	public void setFiletype_ktp(String filetype_ktp) {
		this.filetype_ktp = filetype_ktp;
	}

	public String getNo_pk() {
		return no_pk;
	}

	public void setNo_pk(String no_pk) {
		this.no_pk = no_pk;
	}

	public Date getTgl_aksep_end() {
		return tgl_aksep_end;
	}

	public void setTgl_aksep_end(Date tgl_aksep_end) {
		this.tgl_aksep_end = tgl_aksep_end;
		if(tgl_aksep_end!=null){
			tgl_aksepStr=Utils.convertDateToString(tgl_aksep_end, "dd/MM/yyyy");
		}
	}

	public String getTgl_aksepStr() {
		return tgl_aksepStr;
	}

	public void setTgl_aksepStr(String tgl_aksepStr) {
		this.tgl_aksepStr = tgl_aksepStr;
		if(!Utils.isEmpty(tgl_aksepStr)){
			tgl_aksep=Utils.convertStringToDate(tgl_aksepStr, "dd/MM/yyyy");
		}
	}

	public String getTgl_aksep_endStr() {
		return tgl_aksep_endStr;
	}

	public void setTgl_aksep_endStr(String tgl_aksep_endStr) {
		this.tgl_aksep_endStr = tgl_aksep_endStr;
		if(!Utils.isEmpty(tgl_aksep_endStr)){
			tgl_aksep_end=Utils.convertStringToDate(tgl_aksep_endStr, "dd/MM/yyyy");
		}
	}

	
	
	
	
	
}