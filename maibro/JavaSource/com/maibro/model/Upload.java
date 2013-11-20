package com.maibro.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;



/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Feb 6, 2013 2:43:00 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Upload implements Serializable{

	private static final long serialVersionUID = -8590880740701254294L;
	public Integer importStartLine;
	public MultipartFile uploadFile;
	
	public List<GroupPolicy> groupPolicy;
	public Policy policy;
	
	public Integer jenisUpload;
	
	public Integer mst_product_id;
	
	public String mode;
	
	public Integer getImportStartLine() {
		return importStartLine;
	}
	public void setImportStartLine(Integer importStartLine) {
		this.importStartLine = importStartLine;
	}
	public MultipartFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	public Integer getJenisUpload() {
		return jenisUpload;
	}
	
	/**
	 * 
	 * @Method_name	: setJenisUpload
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Feb 8, 2013 6:37:39 PM
	 * @return_type : void
	 * @Description :
	 * 	1: upload micro polis
	 *  2: upload rate
	 *  3: upload klaim
	 *  4: upload akseptasi
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public void setJenisUpload(Integer jenisUpload) {
		this.jenisUpload = jenisUpload;
	}
	public List<GroupPolicy> getGroupPolicy() {
		return groupPolicy;
	}
	public void setGroupPolicy(List<GroupPolicy> groupPolicy) {
		this.groupPolicy = groupPolicy;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getMst_product_id() {
		return mst_product_id;
	}
	public void setMst_product_id(Integer mst_product_id) {
		this.mst_product_id = mst_product_id;
	}
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
	
	
	
	
}
