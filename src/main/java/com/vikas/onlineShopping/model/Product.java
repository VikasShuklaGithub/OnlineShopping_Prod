package com.vikas.onlineShopping.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.Transient;


import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="product")

public class Product extends Auditable<String> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String productName;
	private String productBrand;
	private String productSize;
	private String productCode;
	public String fileType;
	public  String gender;
	public String category;
	private int productSizeInInch;
	private double productPrice;
	private double productdiscount;
	private double productPriceAfterdiscount;
	private boolean active=true;
	@Column(columnDefinition = "text")
	private String productDescription;
	private int inStockNumber;
	
	@Transient
	private MultipartFile file;
	

	
	
	@Column(length=90,nullable = true)
	public String photo;
	
	@Transient
	public String photImagePath;
	

	
	@OneToMany(mappedBy = "product")
	@JsonIgnore
	private List<BookToCartItem> bookToCartItemList;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductBrand() {
		return productBrand;
	}
	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}
	public String getProductSize() {
		return productSize;
	}
	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPhotImagePath() {
		if (photo == null || id == null)
			return null;
		return "/brand-logos/" + id + "/" + photo;
	}
	
	
	public int getProductSizeInInch() {
		return productSizeInInch;
	}
	public void setProductSizeInInch(int productSizeInInch) {
		this.productSizeInInch = productSizeInInch;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getInStockNumber() {
		return inStockNumber;
	}
	public void setInStockNumber(int inStockNumber) {
		this.inStockNumber = inStockNumber;
	}
	public void setPhotImagePath(String photImagePath) {
		this.photImagePath = photImagePath;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public double getProductdiscount() {
		return productdiscount;
	}
	public void setProductdiscount(double productdiscount) {
		this.productdiscount = productdiscount;
	}
	public double getProductPriceAfterdiscount() {
		return productPriceAfterdiscount;
	}
	public void setProductPriceAfterdiscount(double productPriceAfterdiscount) {
		this.productPriceAfterdiscount = productPriceAfterdiscount;
	}
	
	public List<BookToCartItem> getBookToCartItemList() {
		return bookToCartItemList;
	}
	public void setBookToCartItemList(List<BookToCartItem> bookToCartItemList) {
		this.bookToCartItemList = bookToCartItemList;
	}
	
	
	
	

}
