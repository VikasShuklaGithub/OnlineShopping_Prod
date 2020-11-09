package com.vikas.onlineShopping.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.repository.ProductRepository;
import com.vikas.onlineShopping.service.ProductService;

@Controller
public class ProductController {
	

	private static final Logger LOGGER=LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRepository productRepository;
	
	@RequestMapping(value="/showAddProductPage",method = RequestMethod.GET)
	public String showAddProductPage(Model model)
	{
		Product product=new Product();
		model.addAttribute("product", product);
		return "addProduct";
	}
	
	
	@RequestMapping(value="/addProduct",method = RequestMethod.POST)
	public String addProduct(
			@ModelAttribute("product") Product product,
			Model model,
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException
			
	{
		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		System.out.println("File Name is : "+fileName);
		
		product.setPhoto(fileName);
		/*
		 * DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Calendar
		 * cal=Calendar.getInstance();
		 * 
		 * product.setCreatedDate(dateFormat.format(cal));
		 */
		Product saveProduct = productService.save(product);
		
		LOGGER.info("MESSAGE LOGGER");
		
		String uploadDir="./brand-logos/" +saveProduct.getId();
		
		System.out.println("uploadDir : "+uploadDir);
		Path uploadPath=Paths.get(uploadDir);
		
		System.out.println("uploadPath : "+uploadPath);
		
		if(!Files.exists(uploadPath))
		{
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		InputStream inputStream=multipartFile.getInputStream();
		Path filePath=uploadPath.resolve(fileName);
		
		System.out.println("File Path 1 : "+filePath.toString());
		
		System.out.println("File Path 1 : "+filePath.toFile().getAbsolutePath());
		
		try {
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IOException("Could not Saved Upload File : "+fileName);
		}
		
		model.addAttribute("product", product);
		return "redirect:productList";
	}
	
	
	@RequestMapping(value = "/addProductListPage", method = RequestMethod.GET)
	public String addProductListPage(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		return "productList";
	}
	 
	
	@RequestMapping(value="/productList", method = RequestMethod.GET)
	public String productList(Model model)
	{
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		return "productList";
	}
	
	
	
	
	
	@RequestMapping(value="/fileUpload")
	public String uploadFile(@ModelAttribute("product") Product product,RedirectAttributes redirectAttributes)
	{
		boolean isFlag=productService.saveDataFromUploadFile(product.getFile());
		
		if(isFlag)
		{
			redirectAttributes.addFlashAttribute("successMessage", "File Uploaded Successsfully");
		}
		else
		{
			redirectAttributes.addFlashAttribute("errorMessage", "File Upload Failed");
		}
		
		return "redirect:productList";
	}
	
	@RequestMapping(value="/remove",method = RequestMethod.POST)
	public String remove(@ModelAttribute("id") String id,Model model)
	{
		productService.removeById(Long.parseLong(id.substring(11)));
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		return "redirect:/productList";
		
	}

	@RequestMapping("/updateProduct")
	public String updateProduct(@RequestParam("id") Long id,Model model)
	{
		Product product=productService.findbyId(id);
		model.addAttribute("product",product);
		return "updateProduct";
	}
	
	
	@RequestMapping(value="/updateProduct",method=RequestMethod.POST)
	public String updateProductPost(@ModelAttribute("product") Product product,
			@RequestParam("fileImage") MultipartFile multipartFile,Model model,
			RedirectAttributes redirectAttributes) throws IOException
	{
		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		
		  //String fileName=StringUtils.getFilename(multipartFile.getOriginalFilename());
		  
		  System.out.println("File Name is : "+fileName);
		  
		  product.setPhoto(fileName);
		 

		Product saveProduct = productService.save(product);
		
		
		String uploadDir = "./brand-logos/" + saveProduct.getId();

		System.out.println("uploadDir : " + uploadDir);
		Path uploadPath = Paths.get(uploadDir);

		System.out.println("uploadPath : " + uploadPath);

		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		InputStream inputStream = multipartFile.getInputStream();
		Path filePath = uploadPath.resolve(fileName);
		
		

		System.out.println("File Path 1 : " + filePath.toString());

		System.out.println("File Path 1 : " + filePath.toFile().getAbsolutePath());

		try {
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) 
		{ 
			throw new IOException ("Could not Saved Upload File : " + fileName);
		}
		
		
		
		
		model.addAttribute("product", product);
		return "redirect:/productList?id="+product.getId();
	}
	
	
	
	
	@RequestMapping("/updatePhoto")
	public String updatePhoto(@RequestParam("id") Long id,Model model)
	{
		Product product=productService.findbyId(id);
		model.addAttribute("product",product);
		return "ChangeImage";
	}
	
	@RequestMapping(value="/updatePhoto",method=RequestMethod.POST)
	public String updatePhotoPost(@ModelAttribute("product") Product product,
			@RequestParam("fileImage") MultipartFile multipartFile,Model model,
			RedirectAttributes redirectAttributes) throws IOException
	{
		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		
		
		System.out.println("File Name is : "+fileName);
		
		product.setPhoto(fileName);

		Product saveProduct = productService.save(product);
		
		String uploadDir="./brand-logos/" +saveProduct.getId();
		
		System.out.println("uploadDir : "+uploadDir);
		Path uploadPath=Paths.get(uploadDir);
		
		System.out.println("uploadPath : "+uploadPath);
		
		if(!Files.exists(uploadPath))
		{
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		InputStream inputStream=multipartFile.getInputStream();
		Path filePath=uploadPath.resolve(fileName);
		
		System.out.println("File Path 1 : "+filePath.toString());
		
		System.out.println("File Path 1 : "+filePath.toFile().getAbsolutePath());
		
		try {
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IOException("Could not Saved Upload File : "+fileName);
		}
		
		model.addAttribute("product", product);
		return "redirect:/productList?id="+product.getId();
	}
	
	@RequestMapping(value = "findProducts", method = RequestMethod.POST)
	public String findProducts(

			@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,

			Model model)

	{

		List<Product> products = productRepository.findProduct(from);

		System.out.println("From : " + from);

		System.out.println("Display Product : " + products);

		model.addAttribute("products", products);
		return "displayProducts";
	}
	
}
