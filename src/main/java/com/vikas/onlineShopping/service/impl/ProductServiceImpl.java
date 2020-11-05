package com.vikas.onlineShopping.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.repository.ProductRepository;
import com.vikas.onlineShopping.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService {

	
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public Product save(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		
		List<Product> productList=productRepository.findAll();
		
		List<Product> activeProductList=new ArrayList<Product>();
		
		for(Product product:productList)
		{
			if(product.isActive())
			{
				activeProductList.add(product);
			}
		}
		return activeProductList;
	}

	@Override
	public boolean saveDataFromUploadFile(MultipartFile file) {
		// TODO Auto-generated method stub
		
		boolean isFlag=false;
		String extension=FilenameUtils.getExtension(file.getOriginalFilename());
		
		if(extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")) {
			isFlag=readDataFromExcel(file);
		}
		return isFlag;
		
	}

	private boolean readDataFromExcel(MultipartFile file) {
		
		Workbook workBook=getWorkBook(file);
		
		Sheet sheet=workBook.getSheetAt(0);
		
		Iterator<Row> rows=sheet.iterator();
		
		rows.next();
		
		while(rows.hasNext())
		{
			Row row=rows.next();
			
			Product product=new Product();
			
			if(row.getCell(0).getCellType()==Cell.CELL_TYPE_STRING)
			{
				product.setProductName(row.getCell(0).getStringCellValue());
			}
			
			if(row.getCell(1).getCellType()==Cell.CELL_TYPE_STRING)
			{
				product.setProductBrand(row.getCell(1).getStringCellValue());
			}
			
			if(row.getCell(2).getCellType()==Cell.CELL_TYPE_STRING)
			{
				product.setProductCode(row.getCell(2).getStringCellValue());
			}
			
			if(row.getCell(3).getCellType()==Cell.CELL_TYPE_STRING)
			{
				product.setProductSize(row.getCell(3).getStringCellValue());
			}
			
			
			
			
			if(row.getCell(4).getCellType()==Cell.CELL_TYPE_NUMERIC)
			{
				product.setProductSizeInInch((int) row.getCell(4).getNumericCellValue());
			}
			
			
			
			
			if(row.getCell(5).getCellType()==Cell.CELL_TYPE_NUMERIC)
			{
				product.setProductPrice(row.getCell(5).getNumericCellValue());
			}
			
			if(row.getCell(6).getCellType()==Cell.CELL_TYPE_NUMERIC)
			{
				product.setProductdiscount(row.getCell(6).getNumericCellValue());
			}
			if(row.getCell(7).getCellType()==Cell.CELL_TYPE_NUMERIC)
			{
				product.setProductPriceAfterdiscount(row.getCell(7).getNumericCellValue());
			}
			
			
			if (row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING) {
				product.setGender(row.getCell(8).getStringCellValue());
				
			}

			if (row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING) {
				product.setCategory(row.getCell(9).getStringCellValue());
				
			}
			
			if(row.getCell(10).getCellType()==Cell.CELL_TYPE_NUMERIC)
			{
				product.setInStockNumber((int) row.getCell(10).getNumericCellValue());
			}
			
			
			if(row.getCell(11).getCellType()==Cell.CELL_TYPE_BOOLEAN)
			{
				product.setActive(true);
			}
			
			if(row.getCell(12).getCellType()==Cell.CELL_TYPE_STRING)
			{
				product.setProductDescription(row.getCell(12).getStringCellValue());
			}	

			if(row.getCell(12).getCellType()==Cell.CELL_TYPE_STRING)
			{
				product.setProductDescription(row.getCell(12).getStringCellValue());
			}	
			
			product.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
			
			productRepository.save(product);
			
		}
		
		
		// TODO Auto-generated method stub
		return true;
	}

	private Workbook getWorkBook(MultipartFile file) {
		
		Workbook workbook=null;
		
		String extension=FilenameUtils.getExtension(file.getOriginalFilename());
		
		if(extension.equalsIgnoreCase("xlsx"))
		{
			try {
				workbook=new XSSFWorkbook(file.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(extension.equalsIgnoreCase("xls"))
		{
			try {
				workbook=new HSSFWorkbook(file.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		return workbook;
	}

	@Override
	public void removeById(long id) {
		productRepository.deleteById(id);
		
	}

	@Override
	public Product findbyId(Long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public List<Product> findByCategory(String category) {
		// TODO Auto-generated method stub
		
		List<Product> productList=productRepository.findByCategory(category);
		
		List<Product> activeProductList=new ArrayList<Product>();
		
		for(Product product:productList)
		{
			if(product.isActive())
			{
				activeProductList.add(product);
			}
		}
		return activeProductList;
	}

}
