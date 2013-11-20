package com.maibro.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateMidnight;
import org.joda.time.Months;
import org.joda.time.Years;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.maibro.model.DropDown;
import com.maibro.model.Menu;
import com.maibro.model.Policy;
import com.maibro.model.User;
import com.maibro.service.DbService;

/**
 * Utility classes, rata2 function/vars disini static saja
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:12:00 AM)
 *
 */
public class Utils{
	
	//Formatter2 default ada disini, tidak perlu di-register satu2 di spring xml
	public static final DateFormat defaultDF = new SimpleDateFormat("dd-MM-yyyy");
	public static final DateFormat yearDF = new SimpleDateFormat("yyyy");
	public static final NumberFormat defaultNF = NumberFormat.getInstance(); //DecimalFormat("#,##0.00;(#,##0.00)")
	
	//konstanta duit
	private static final double 
		//M250 	= new Double("250000000"), //250 jt
		//M450 	= new Double("450000000"), //450 jt
		M500	= new Double("500000000"), //500 jt
		//M600 	= new Double("600000000"), //600 jt
		B1 		= new Double("1000000000"), //1 M
		B15 	= new Double("1500000000"), //1.5 M
		//B2 	= new Double("2000000000"), //2 M
		B3 		= new Double("3000000000"), //3 M
		B5 		= new Double("5000000000"), //5 M
		B12 	= new Double("12000000000"); //12 M
	
	/**
	 * Tarik data tahun aplikasi. Contoh hasilnya "2006-2011" 
	 * 
	 * @param now
	 * @return
	 */
	public static String getCopyrightYears(Date now){
		int tahunAwal = 2013;
		int tahunSekarang = Integer.parseInt(yearDF.format(now));
		
		String tahun;
		if(tahunSekarang > tahunAwal) tahun = tahunAwal + "-" + tahunSekarang;
		else tahun = String.valueOf(tahunAwal);
		return tahun;
	}
	
	/**
	 * Fungsi untuk me-listing semua report yang ada di file properties, dimana key nya harus dimulai dengan report atau subreport 
	 * 
	 * @author Yusuf
	 * @since Jul 8, 2008 (10:56:24 AM)
	 * @param props
	 * @return
	 */
	public static List<String> listAllReports(Properties props) {
		List<String> reportList = new ArrayList<String>();
		for(Iterator it = props.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			if(key.startsWith("report") || key.startsWith("subreport")) {
				reportList.add(key);
			}
		}
		Collections.sort(reportList);
		return reportList;
	}
	
	/**
	 * 
	 * @Method_name	: generateMenu
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Jan 27, 2013 9:56:42 PM
	 * @return_type : String
	 * @Description :modul untuk membuat tree menu yang sesuai dengan ddtopmenubar css 
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static String generateMenu(List<Menu> daftarMenu, String path,User currentUser){
		StringBuffer result = new StringBuffer();
		int tingkat = 0;
		
		List<Integer> parentIdList=new ArrayList<Integer>();
		result.append(" <div id=\"ddtopmenubar\" >\n<ul class=\"wat-cf\">\n");
		
		if(path.contains("maibro"))
			result.append("<li><a href=\""+path+"\">Home</a></li>\n");
		else
			result.append("<li><a href=\"/\">Home</a></li>\n");
		
		//menu tingkat 1
		for(Menu baris : daftarMenu){
			if(baris.parent==1){
				if(baris.urut.intValue()==1){
					
				}
					result.append("<li><a href=\"#\" rel=\"ddsubmenu"+baris.id+"\">"+baris.nama+"</a></li>\n");
					parentIdList.add(baris.id);
				
			}
		}
		result.append("</ul></div>\n");		
		result.append("<script type=\"text/javascript\">ddlevelsmenu.setup(\"ddtopmenubar\", \"topbar\") </script>\n");
		
		List<Integer> parentIdRestricted=new ArrayList<Integer>();
		for(Integer parentId:parentIdList){// bikin sub menu tingkat 2
			
			
			tingkat=0;
			result.append("<ul id=\"ddsubmenu"+parentId+"\" class=\"ddsubmenustyle\">\n");
			boolean lewat=false;
			for(Menu baris : daftarMenu){
				if(baris.parent!=1){
				
					if(baris.parent==parentId){
							

						//row pertama, cukup buka UL utama saja
						if(baris.link == null) {
							result.append("<li><a href=\"#\" >"+baris.nama+"</a><ul>\n");
							//bila ada link, berarti tidak ada submenu lagi 
						} else if(baris.link != null) {
							//SPECIAL MENU 2 : Menu yang butuh parameter kode agen, seperti biodata agen online dan spaj online
							if(baris.link.startsWith("http://")||baris.link.startsWith("https://")){
								result.append("<li><a href=\""+baris.link+" \">"+baris.nama+"</a></li>\n");
							}else{
								result.append("<li><a href=\""+path+ "/" +baris.link+" \">"+baris.nama+"</a></li>\n");
							}
						}
						
					}
					
				
					
				}
				
			}
			

			//terakhir, tutup UL utama
			result.append("</ul></li></ul>\n");
			parentIdRestricted.add(parentId);
		}
		//TODO : masih statis
		String statis="<div id=\"ddtopmenubar\" >"+
				"<ul class=\"wat-cf\">"+
				"<li><a href=\""+path+"/home \">Home</a></li>"+
				"<li><a href=\"#\" rel=\"Admin\">Admin</a></li>"+
				"<li><a href=\"#\" rel=\"Master\" >Master</a></li>"+
				"<li><a href=\"#\" rel=\"Proses\">Proses</a></li>"+
				"<li><a href=\"#\" rel=\"Report\">Report</a></li>"+
				"</ul></div>"+
				"<script type=\"text/javascript\">ddlevelsmenu.setup(\"ddtopmenubar\", \"topbar\") </script>"+
				"<ul id=\"Admin\" class=\"ddsubmenustyle\">"+
				"<li><a href=\""+path+"/admin/groupmenu\" >Group Menu</a></li>"+
				"<li><a href=\""+path+"/admin/menu\" >Menu</a></li>"+
				"<li><a href=\""+path+"/admin/console\">Console</a></li>"+
				"</ul>"+
				"<ul id=\"Master\" class=\"ddsubmenustyle\">"+
				"<li><a href=\""+path+"/master/bank\" >Bank</a></li>"+
				"<li><a href=\""+path+"/master/product\" >Produk</a></li>"+
				"</ul>"+
				"<ul id=\"Proses\" class=\"ddsubmenustyle\">"+
				"<li><a href=\""+path+"/proses/input_ajk\" >Input AJK</a></li>"+
				"<li><a href=\""+path+"/proses/inputfire\" >Input Fire</a></li>"+
				"</ul>"+
				"<ul id=\"Report\" class=\"ddsubmenustyle\">"+
				"<li><a href=\"#\" >Viewer</a></li>"+
				"<li><a href=\"#\" >Kontribusi Premi per Cabang</a></li>"+
				"</ul>";
		
//		return statis;
		//System.out.println(result.toString());
		return result.toString();
	}
	
	/**
	 * 
	 * @Method_name	: errorExtract
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Jan 28, 2013 10:25:56 PM
	 * @return_type : String
	 * @Description : mengekstrak error message ke dalam String
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static String errorExtract(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String	exception = sw.toString();
		
		try {
			sw.close();
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return exception;
	}
	
	/**
	 * Fungsi ini untuk mengecek apakah suatu field ada isinya
	 * @author Yusuf Sutarko
	 * @since May 2, 2007 (7:40:39 AM)
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		if(object==null) return true;
		else if(object instanceof String) {
			String tmp = (String) object;
			if(tmp.trim().equals("")) return true;
			else return false;
		}else if(object instanceof List) {
			List<?> tmp = (List<?>) object;
			return tmp.isEmpty();
		}else if(object instanceof Map){
			return ((Map<?, ?>) object).isEmpty();
//		}else if(object instanceof Integer || object instanceof Long|| object instanceof Double|| object instanceof Float|| 
//				object instanceof BigDecimal || object instanceof Date){
//			return false;
		}
		return true;
	}
	
	public static String convertDateToString(Date tanggal,String format){
		if(tanggal==null)return null;
		else{
			try{
				return new SimpleDateFormat(format).format(tanggal);
			}catch (Exception e) {
				
				return null;
			}
		}
	}
	
	public static String convertDateToString2(Date tanggal,String format)  throws Exception{
		if(tanggal==null)return null;
		else{
			return new SimpleDateFormat(format).format(tanggal);
			
		}
	}
	
	public static Date convertStringToDate(String tanggal,String format){
		if(tanggal==null)return null;
		else{
			try{
				return new SimpleDateFormat(format).parse(tanggal);
			}catch (Exception e) {
				
				return null;
			}
		}
	}
	
	public static Date convertStringToDate2(String tanggal,String format) throws ParseException{
		if(tanggal==null)return null;
		else{
			return new SimpleDateFormat(format).parse(tanggal);
		
		}
	}
	
	/**
	 * 
	 * @Method_name	: reformatDate_ddmmyyyy
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 7, 2013 11:43:43 AM
	 * @return_type : String
	 * @Description : untuk reformat date khusus yang dd/mm/yyyy atau dd-mm-yyyy
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static String reformatDate_ddmmyyyy(String tanggal,String splitby) throws ParseException{
		if(tanggal==null)return null;
		else{
			
			if(tanggal.contains("/"))
				splitby="/";
			else if(tanggal.contains("-"))
				splitby="-";
			
			String[]split=tanggal.split("-");
			
			if(split==null|split.length<3)
				return tanggal;
			
			return rpad("0",split[0], 2)+"/"+rpad("0",split[1], 2)+"/"+split[2];
		
		}
	}
	
	public static List<String> errorBinderToList(BindingResult bindingResult,MessageSource messageSource){
		List<String> errorMessage=new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (Object object : bindingResult.getAllErrors()) {
				    if(object instanceof FieldError) {
				        FieldError fieldError = (FieldError) object;
				        /**
				          * Use null as second parameter if you do not use i18n (internationalization)
				          */
				        errorMessage.add( messageSource.getMessage(fieldError, null));
				    }
				}
		 }
		return errorMessage;
	}
	
	public static List<DropDown> lstjenisProduct(){
		//FIXME by Yusuf : ini jangan dipake lagi nih, hardcode
		List<DropDown> lstjenisProduct=new ArrayList<DropDown>();
		lstjenisProduct.add(new DropDown("1","Life KPR"));
		lstjenisProduct.add(new DropDown("2","Fire KPR"));
		lstjenisProduct.add(new DropDown("2","Life MICRO"));
		return lstjenisProduct;
	}
	
	public static List<DropDown> lstjenisBangunan(){
		//FIXME by Yusuf : ini jangan dipake lagi nih, hardcode
		List<DropDown> lstjenisBangunan=new ArrayList<DropDown>();
		lstjenisBangunan.add(new DropDown("1","Rumah Tinggal / Apartemen"));
		lstjenisBangunan.add(new DropDown("2","Ruko / Rukan / Kios"));
		lstjenisBangunan.add(new DropDown("3","Lainnya"));
		return lstjenisBangunan;
	}
	
//	public static List<DropDown> lstRelasi(){
//		List<DropDown> lstRelasi=new ArrayList<DropDown>();
//		lstRelasi.add(new DropDown("1","Diri Sendiri"));
//		lstRelasi.add(new DropDown("2","Istri / Suami"));
//		lstRelasi.add(new DropDown("3","Orang Tua"));
//		lstRelasi.add(new DropDown("4","Anak Kandung"));
//		return lstRelasi;	
//	}
	
	public static List<DropDown> lstMaster(){
		List<DropDown> lstMaster=new ArrayList<DropDown>();
		lstMaster.add(new DropDown("1","Posisi Dokumen Polis"));
		lstMaster.add(new DropDown("2","Jenis Relasi"));
		lstMaster.add(new DropDown("3","Jenis Grup Polis"));
		lstMaster.add(new DropDown("4","Jenis Produk"));
		lstMaster.add(new DropDown("5","Jenis Bangunan"));
		lstMaster.add(new DropDown("6","Jenis Address"));
		lstMaster.add(new DropDown("7","Jenis Manfaat Life"));
		lstMaster.add(new DropDown("8","Jenis Perusahaan"));
		lstMaster.add(new DropDown("9","Jenis Cara Bayar"));
		lstMaster.add(new DropDown("10","Jenis Medis"));
		lstMaster.add(new DropDown("11","Jenis Rate"));
		lstMaster.add(new DropDown("12","Jenis History"));
		lstMaster.add(new DropDown("13","Posisi Klaim"));
		return lstMaster;	
	}
	
	/**
	 * 
	 * @Method_name	: tranferPosisi
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Feb 10, 2013 9:35:17 PM
	 * @return_type : Integer
	 * @Description : utils untuk transfer posisi sesuai flow
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static Integer tranferPosisi(Integer posisiBefore){
		Integer posisi=posisiBefore;
		if(posisiBefore==1){//transfer dari input ke validasi
			posisi=2;
		}else if(posisiBefore==2){//tranfer dari validasi ke akseptasi
			posisi=3;
		}else if(posisiBefore==3){//transfer dari akseptasi ke upload dokumen 
			posisi=4;//klo fire bisa skip upload KPS
		}else if(posisiBefore==4){//transfer dari upload dokumen ke Input Payment
			posisi=5;
		}else if(posisiBefore==5){//transfer dari Input Payment ke Print Polis
			posisi=6;
		}else if(posisiBefore==6){//transfer dari Print Polis Ke Filling 
			posisi=99;
		}
		
		return posisi;
	}
	
	public static Integer tranferPosisiKlaim(Integer posisiBefore){
		Integer posisi=posisiBefore;
		if(posisiBefore==1){//transfer dari input ke validasi
			posisi=2;
		}else if(posisiBefore==2){//tranfer dari validasi ke akseptasi
			posisi=3;
		}else if(posisiBefore==3){//transfer dari akseptasi ke filling 
			posisi=99;//klo fire bisa skip upload KPS
		}else if(posisiBefore==4){//transfer dari proses ulang ke validasi
			posisi=2;
		}
		
		return posisi;
	}
	
	
	/**
	 * 
	 * @Method_name	: hitUmur
	 * @author 		: Yusuf Sutarko
	 * @since		: 11 Feb 2013
	 * @return_type : int
	 * @Description : utils untuk hitung umur sesuai rumus asuransi (diatas 6 bulan dibulatkan keatas)
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static int hitUmur(Date birthdate, Date begdate){
		DateMidnight awal = new DateMidnight(birthdate);
		DateMidnight akhir = new DateMidnight(begdate);
		
		Years thn = Years.yearsBetween(awal, akhir);
		Months bln = Months.monthsBetween(awal, akhir);
		
		//bila diatas 6 bulan, dibulatkan keatas
		int penambah = 0;
		if(bln.getMonths() % 12 >= 6) penambah = 1;
		
		return thn.getYears() + penambah;
	}
	
	public static List<DropDown> lstJenisBank(){
		List<DropDown> lstJenisBank=new ArrayList<DropDown>();
		lstJenisBank.add(new DropDown("1","BANK"));
		lstJenisBank.add(new DropDown("2","ASURANSI JIWA"));
		lstJenisBank.add(new DropDown("3","BROKER"));
		lstJenisBank.add(new DropDown("4","ASURANSI KERUGIAN"));
		return lstJenisBank;	
	}
	
	public static List<DropDown> lstJenisCabBank(){
		List<DropDown> lstJenisCabBank=new ArrayList<DropDown>();
		lstJenisCabBank.add(new DropDown("0","KP"));
		lstJenisCabBank.add(new DropDown("1","KC"));
		lstJenisCabBank.add(new DropDown("2","KCP"));
		return lstJenisCabBank;	
	}
	
	public static List<DropDown> lstFormClaim(boolean panjang){
		List<DropDown> lstFormClaim=new ArrayList<DropDown>();
		if(panjang){
			lstFormClaim.add(new DropDown("1","CERT","Sertifikat asuransi asli / fotocopy daftar peserta"));
			lstFormClaim.add(new DropDown("2","SPK","Surat pengajuan klaim kematian dari pihak Pemegang Polis / Bank dengan dilampiri print out sisa pinjaman (sampai dengan Tertanggung/Peserta meninggal dunia)"));
			lstFormClaim.add(new DropDown("3","SKM","Surat keterangan meninggal dunia dari instansi yang berwenang"));
			lstFormClaim.add(new DropDown("4","SKD","Formulir Surat Keterangan Dokter harus diisi oleh dokter / Rumah sakit /puskesmas yang memeriksa / merawat tertanggung (formulir dari AJS MSIG)"));
			lstFormClaim.add(new DropDown("5","BAP","Surat berita acara dari kepolisian dalam hal meninggal dunianya tidak wajar atau kecelakaan lalu lintas"));
			lstFormClaim.add(new DropDown("6","ID","Bukti diri dari Tertanggung"));
		}else{
			lstFormClaim.add(new DropDown("1","CERT","Sertifikat Asli / Fotocopy Daftar Peserta"));
			lstFormClaim.add(new DropDown("2","SPK","Surat Pengajuan Klaim kematian & Lampiran Sisa pinjaman"));
			lstFormClaim.add(new DropDown("3","SKM","Surat Keterangan Meninggal Dunia"));
			lstFormClaim.add(new DropDown("4","SKD","Formulir Surat Keterangan Dokter"));
			lstFormClaim.add(new DropDown("5","BAP","Surat Berita Acara Kepolisian"));
			lstFormClaim.add(new DropDown("6","ID","Bukti diri"));
			lstFormClaim.add(new DropDown("7","ELSE","Dokumen Lainnya"));
		}
		return lstFormClaim;	
	}
	
	/**
	 * 
	 * @Method_name	: isFileExist
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Feb 12, 2013 10:43:11 PM
	 * @return_type : boolean
	 * @Description : cek apakah file exist
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static boolean isFileExist(String filename){
		boolean scFile=false;
		FileInputStream in=null;
		try{
			  File l_file = new File(filename);
		     in = new FileInputStream(l_file);				      
		      in.close();
		   
		      scFile=true;
		}catch (Exception e) {
			//e.printStackTrace();
		}finally{
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}
		}
		return scFile;
	}
	
	/**
	 * Method untuk menghapus suatu file dari server
	 * 
	 * @author Yusuf
	 * @since Jul 3, 2008 (1:49:03 PM)
	 * @param destDir lokasi file
	 * @param fileName nama file
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean deleteFile(String destDir, String fileName, HttpServletResponse response) throws FileNotFoundException,
			IOException {
		File file = new File(destDir + "/" + fileName);
		if (file.exists()) return file.delete();
		return false;
	}
	
	/**
	 * Method untuk men-download sebuah file 
	 * 
	 * @author Yusuf
	 * @since Jul 3, 2008 (1:47:27 PM)
	 * @param location lokasi file yg ingin didownload
	 * @param fileName nama file yang ingin didownload
	 * @param response object response
	 * @param inlineOrAttached "inline" apabila ingin langsung ditampilkan di browser atau "attachment" bila ingin keluar dialog "Save As"
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void downloadFile(String location, String fileName, HttpServletResponse response, String inlineOrAttached) 
			throws FileNotFoundException, IOException{
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(location+fileName);
			if (in != null) {
				out = new BufferedOutputStream(response.getOutputStream());
				in = new BufferedInputStream(in);
				//String contentType = "application/unknown";
				response.setHeader("Content-Disposition", inlineOrAttached + "; filename=\"" + fileName + "\"");
				int c;
				while ((c = in.read()) != -1) out.write(c);
			}
		} finally {
			if (in != null) try {in.close(); } catch (Exception e) {}
			if (out != null) try {out.close(); } catch (Exception e) {}
		}				
	}
	
	/**
	 * Method untuk melist file2 yang ada dalam suatu directory
	 * 
	 * @author Yusuf
	 * @since Jul 3, 2008 (1:51:15 PM)
	 * @param dir lokasi file2 yang ingin di listing
	 * @return
	 */
	public static List<DropDown> listFilesInDirectory(String dir) {
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		if(destDir.exists()) {
			String[] children = destDir.list();
			daftar = new ArrayList<DropDown>();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			for(int i=0; i<children.length; i++) {
				File file = new File(destDir+"/"+children[i]);
				daftar.add(new DropDown(children[i], df.format(new Date(file.lastModified())), dir));
			}
		}
		return daftar;
	}
	
	/**
	 * Fungsi yang mengikuti fungsi RPAD di Oracle, contoh: rpad("0", "YUSUF", 10) menghasilkan "00000YUSUF"
	 * 
	 * @author Yusuf
	 * @since Feb 21, 2011 (7:41:43 PM)
	 * @param karakter untuk melengkapi sisa string
	 * @param kata (String) yang mau dipanjangkan
	 * @param panjang dari string hasilnya
	 * @return String hasil penggabungan dari karakter dan kata
	 * @see Fungsi RPAD di Oracle
	 */
	public static String rpad(String karakter, String kata, int panjang) {
		if(kata==null) return null;
		StringBuffer result = new StringBuffer();
		if (kata.length() < panjang) {
			for (int i = 0; i < panjang - kata.length(); i++) {
				result.append(karakter);
			}
			result.append(kata);
			return result.toString();
		} else {
			return kata;
		}
	}
	
	public static String formatPolis(String kata) {
		if (kata == null) {
			return "";
		} else if (kata.length() == 9) {
			return kata.substring(0, 2) + "." + kata.substring(2);
		} else if (kata.length() == 11) {
			return kata.substring(0, 2) + "." + kata.substring(2, 6) + "."
					+ kata.substring(6);
		} else if (kata.length() == 14) {
			return kata.substring(0, 2) + "." + kata.substring(2, 5) + "."
					+ kata.substring(5, 9) + "." + kata.substring(9);
		} else
			return "";
	}

	public static String formatSPAJ(String kata) {
		if (kata == null) {
			return "";
		} else if (kata.length() == 7) {
			return kata;
		} else if (kata.length() == 9) {
			return kata.substring(0, 4) + "." + kata.substring(4);
		} else if (kata.length() == 11) {
			return kata.substring(0, 2) + "." + kata.substring(2, 6) + "."
					+ kata.substring(6);
		} else
			return "";
	}
	
	public static double nvl(Double value){
		if(value != null) return value;
		else return 0.;
	}
	
	public static String nvl(String value){
		if(value != null) return value;
		else return "";
	}
	
	/**
	 * 
	 * @Method_name	: hitungEndDate
	 * @author 		: Yusuf Sutarko
	 * @since		: 7 Mar 13
	 * @return_type : Date
	 * @Description : utils untuk hitung enddate = begdate + ins period - 0 hari
	 * @Revision	:
	 * #====#===========#===================#=========================================#
	 * | ID	|    Date	|	    User		|			Description					  |
	 * #====#===========#===================#=========================================#
	 * |1	|06/05/2013	|		Bertho		|enddate tidak perlu dikurang 1 hari	  |
	 * #====#===========#===================#=========================================#
	 */
	public static Date hitungEndDate(Date begDate, int insPeriod, int jenis){
		DateMidnight begdate = new DateMidnight(begDate);
		DateMidnight enddate = null;
		//bila ins period dalam tahun
		if(jenis == 0){
			enddate = begdate.plusYears(insPeriod).minusDays(0); //enddate = begdate + ins period - 1 hari	
		//bila ins period dalam bulan
		}else if(jenis == 1){
			enddate = begdate.plusMonths(insPeriod).minusDays(0); //enddate = begdate + ins period - 1 hari	
		}
		
		return enddate.toDate();
	}
	
	/**
	 * 
	 * @author Yusuf
	 * @since Mar 7, 2013 (10:29:38 AM)
	 * @Description : Fungsi untuk menghitung semua jenis rate, jenis medis, kondisi standar, rate, premi dari polis KPR Life
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static Map<String, Object> hitungPremiKPRLife(DbService dbService, int umur, double up, int product, int ins){
		//variable2 yg dihitung di fungsi ini
		Integer jenisRate = 1; //normal (default) vs single rate
		Integer jenisMedis = 1; //jenis medis 1 (non medis, default), lainnya diset dibawah
		Boolean standar = true; //kondisi standar vs non standar
		Double rate = null; //rate premi
		Double premi = null; //hasil perhitungan premi asuransi
		
		//tentukan ini normal rate atau single rate
		boolean isSingleRate=false;
		if( (umur >= 50 && up <= M500)) {
			isSingleRate=true;
		}
		
		//normal rate digunakan bila umur <= 50 dan bila umur >= 50 dan up > 500jt
		if((umur <= 50 || (umur >= 50 && up > M500))&!isSingleRate) {
			jenisRate = 1; //normal rate
			
			//tarik normal rate
			rate = dbService.selectRate(1, product, ins, null, umur);
			
			//System.out.println(Utils.defaultNF.format(up));
			//System.out.println(Utils.defaultNF.format(B3));
			
			//ketentuan medis untuk normal rate
			if(umur >= 20 && umur <= 45){
				//Yusuf (7/3/13) Req Bambang, limit nonmedis untuk usia < 45thn dinaikkan dari 2M mjd 3M
				if(up <= B3) {
					jenisMedis = 1; //NM - Non Medical (mengisi Form Kesehatan)
				}else if(up > B3 && up <= B5) {
					jenisMedis = 5; //D - Formulir Pemeriksaan Kesehatan (Oleh Dokter Pemeriksa), Urine, Analisa, Darah Lengkap, ECG, RO Foto, Treadmill Test, HIV Test, Surat Pernyataan Dokter, AFP (Acute Flaccid Paralysis) Test
					standar = false; //kondisi NON-STANDAR
				}else if(up > B5 && up <= B12) {
					jenisMedis = 6; //E+CFQ (Confidential Financial Questionnaire - Formulir Pemeriksaan Kesehatan (Oleh Dokter Pemeriksa), Urine, Analisa, Darah Lengkap, ECG, RO Foto, Treadmill Test, HIV Test, Surat Pernyataan Dokter, AFP (Acute Flaccid Paralysis) Test, USG Abdomen, PSA (Prostate Specific Antigen)
					standar = false; //kondisi NON-STANDAR
				}else if(up > B12) {
					jenisMedis = 7; //* (Perlu Konfirmasi)
					standar = false; //kondisi NON-STANDAR
				}
				
			}else if(umur >= 46 && umur <= 55){
				if(up <= B15) {
					jenisMedis = 1; //NM - Non Medical (mengisi Form Kesehatan)
				}else if(up > B15 && up <= B5) {
					jenisMedis = 5; //D - Formulir Pemeriksaan Kesehatan (Oleh Dokter Pemeriksa), Urine, Analisa, Darah Lengkap, ECG, RO Foto, Treadmill Test, HIV Test, Surat Pernyataan Dokter, AFP (Acute Flaccid Paralysis) Test
					standar = false; //kondisi NON-STANDAR
				}else if(up > B5 && up <= B12) {
					jenisMedis = 6; //E+CFQ (Confidential Financial Questionnaire - Formulir Pemeriksaan Kesehatan (Oleh Dokter Pemeriksa), Urine, Analisa, Darah Lengkap, ECG, RO Foto, Treadmill Test, HIV Test, Surat Pernyataan Dokter, AFP (Acute Flaccid Paralysis) Test, USG Abdomen, PSA (Prostate Specific Antigen)
					standar = false; //kondisi NON-STANDAR
				}else if(up > B12) jenisMedis = 7; //* (Perlu Konfirmasi)
				
			}else if(umur >= 56 && umur <= 60){
				if(up <= B1) {
					jenisMedis = 1; //NM - Non Medical (mengisi Form Kesehatan)
				}else if(up > B1 && up <= B5) {
					jenisMedis = 5; //D - Formulir Pemeriksaan Kesehatan (Oleh Dokter Pemeriksa), Urine, Analisa, Darah Lengkap, ECG, RO Foto, Treadmill Test, HIV Test, Surat Pernyataan Dokter, AFP (Acute Flaccid Paralysis) Test
					standar = false; //kondisi NON-STANDAR
				}else if(up > B5 && up <= B12) {
					jenisMedis = 6; //E+CFQ (Confidential Financial Questionnaire - Formulir Pemeriksaan Kesehatan (Oleh Dokter Pemeriksa), Urine, Analisa, Darah Lengkap, ECG, RO Foto, Treadmill Test, HIV Test, Surat Pernyataan Dokter, AFP (Acute Flaccid Paralysis) Test, USG Abdomen, PSA (Prostate Specific Antigen)
					standar = false; //kondisi NON-STANDAR
				}else if(up > B12) {
					jenisMedis = 7; //* (Perlu Konfirmasi)
					standar = false; //kondisi NON-STANDAR
				}
			}
			
		}else{
			jenisRate = 2; //single rate
			
			//single rate dikali masa asuransi dalam tahun
			rate = dbService.selectRate(2, product, null, null, null) * ins;
			
			if(umur <= 65){
				if(up <= M500) jenisMedis = 8; //Tanpa Pemeriksaan Kesehatan & Free Cover Limit
				else {
					jenisMedis = 9; //SPK (Surat Pernyataan Kesehatan) + PKB (Pemeriksaan Kesehatan Biasa) + ECG (Electro Cardiogram) + ADAUL (Analisa Darah dan Urine Lengkap) +Thorax +TRM (Treadmill Test) + HIV (Aids)
					standar = false; //kondisi NON-STANDAR
				}
			}else{
				jenisMedis = 9; //SPK (Surat Pernyataan Kesehatan) + PKB (Pemeriksaan Kesehatan Biasa) + ECG (Electro Cardiogram) + ADAUL (Analisa Darah dan Urine Lengkap) +Thorax +TRM (Treadmill Test) + HIV (Aids)
				standar = false; //kondisi NON-STANDAR
			}

		}
		
		//hitung premi = rate / 1000 x up
		if(rate != null) {
			premi = rate / 1000 * up;
		}

		//return results
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("jenisRate", jenisRate);
		result.put("jenisMedis", jenisMedis);
		result.put("standar", standar);
		result.put("rate", rate);
		result.put("premi", premi);
		
		//tambahan untuk di kalkulator premi
		if(rate != null){
			result.put("rateDesc", dbService.selectKeteranganMaster(11, jenisRate));
			result.put("medisDesc", dbService.selectKeteranganMaster(10, jenisMedis));
		}else{
			if(umur < 20 || umur > 64){
				result.put("catatan", "Usia masuk yang diperkenankan adalah antara 20 s/d 64 tahun.");
			}
			if((umur + ins) > 65) {
				result.put("catatan2", "Jumlah dari Usia Masuk (x) dan Masa Asuransi (n) maksimal 65 tahun (x+n<=65).");
			}
		}		
		
		return result;
	}
	
	/**
	 * 
	 * @author Yusuf
	 * @since Mar 7, 2013 (10:29:38 AM)
	 * @Description : Fungsi untuk menghitung semua rate, kondisi standar, rate, premi dari polis KPR Fire
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static Map<String, Object> hitungPremiKPRFire(DbService dbService, double up, int product, int ins, int jenis_bangunan){
		//variable2 yg dihitung di fungsi ini
		Double rate = null; //rate premi
		Double premi = null; //hasil perhitungan premi asuransi
		String catatan = null; //catatan

		//tarik rate
		rate = dbService.selectRate(1, product, ins, jenis_bangunan, null);

		//bila jenis bangunan lainnya, premi tidak perlu dihitung!
		if(jenis_bangunan == 3){
			catatan = "\\n- Catatan Asuransi Kebakaran: Untuk jenis bangunan LAINNYA, harap konfirmasi nilai Premi dengan pihak MaiBro.";
		}else{
			//hitung premi rate / 1000 x up
			if(rate != null) {
				premi = rate / 1000 * up;
			}
		}

		//return results
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rate", rate);
		result.put("premi", premi);
		result.put("catatan", catatan);
		
		return result;
	}

	/**
	 * 
	 * @author Yusuf
	 * @since Mar 7, 2013 (10:29:38 AM)
	 * @Description : Fungsi untuk menghitung semua rate, kondisi standar, rate, premi dari polis Micro Life
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static Map<String, Object> hitungPremiMicroLife(DbService dbService, double up, int product, int ins){
		//variable2 yg dihitung di fungsi ini
		Double rate = null; //rate premi
		Double premi = null; //hasil perhitungan premi asuransi

		//tarik rate (krn single rate, maka rate * ins period)
		rate = dbService.selectRate(2, product, 0, null, null) * ins;
		
		//hitung premi rate / 1000 x up
		if(rate != null) {
			premi = rate / 1000 * up;
		}

		//return results
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rate", rate);
		result.put("premi", premi);
		
		//tambahan untuk di kalkulator premi
		result.put("rateDesc", dbService.selectKeteranganMaster(11, 2)); //single rate
		
		return result;
	}
	
	public static List<List> readExcelFile(String dir, String fileName) throws Exception {
        //
        // An excel file name. You can create a file name with a full 
        // path information.
        //
        //String filename = "..\\data.xls";

        //
        // Create an ArrayList to store the data read from excel sheet.
        //
        List sheetData = new ArrayList();

        FileInputStream fis = null;
        try {
            //
            // Create a FileInputStream that will be use to read the 
            // excel file.
            //
            //fis = new FileInputStream(filename);
        	fis = new FileInputStream(dir + fileName);

            //
            // Create an excel workbook from the file system.
            //
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            //
            // Get the first sheet on the workbook.
            //
            HSSFSheet sheet = workbook.getSheetAt(0);

            //
            // When we have a sheet object in hand we can iterator on 
            // each sheet's rows and on each row's cells. We store the 
            // data read on an ArrayList so that we can printed the 
            // content of the excel to the console.
            //
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();

                List data = new ArrayList();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    data.add(cell);
                }

                sheetData.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return sheetData;
        //showExelData(sheetData);
    }
	
	public static HashMap hitTotalPolicy(List<Policy> lspol){
		HashMap map=new HashMap<String, Object>();
		Double totalPremi=0.0,totalNetPremi=0.0,totalBayar=0.0;
		Integer totalAktif=0;
		for(Policy pol:lspol){
			if(pol.getPosisi()!=95){
				totalPremi+=pol.getPremi()==null?0.0:pol.getPremi();
				totalNetPremi+=pol.getPremi_net()==null?0.0:pol.getPremi_net();
				totalBayar+=pol.getNominal_paid()==null?0.0:pol.getNominal_paid();
				totalAktif++;
			}
		}
		map.put("premi", totalPremi);
		map.put("premi_net", totalNetPremi);
		map.put("bayar", totalBayar);
		map.put("aktif", totalAktif);
		return map;
	}
		
}