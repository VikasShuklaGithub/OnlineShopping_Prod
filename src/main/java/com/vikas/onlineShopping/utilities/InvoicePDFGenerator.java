package com.vikas.onlineShopping.utilities;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;
import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.service.CartItemService;
import com.vikas.onlineShopping.service.OrderService;
import com.vikas.onlineShopping.service.UserService;

public class InvoicePDFGenerator {
	
	
		
	
	
	private List<CartItem> cartItemList;
	

	BigDecimal orderTotal;
	
	

	
    
    public  InvoicePDFGenerator(List<CartItem> cartItemList) {
        this.cartItemList=cartItemList;
    }
	
	
	private void writeTableHeader(PdfPTable table)
	{
		PdfPCell cell=new PdfPCell();
		
		cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        
		cell.setPhrase(new Phrase("ID", font));        
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Product", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Order ID", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
        
       
        
      
	}
	
	
	private void writeTableData(PdfPTable table)
	{
		System.out.println("Enter in Cart Item List Invoice");
		for (CartItem item : cartItemList) {
			
            table.addCell(String.valueOf(item.getId()));
            System.out.println(item.getId());
            
            table.addCell(item.getProduct().getProductName());
            System.out.println(item.getProduct().getProductName());      
            
            table.addCell(String.valueOf(item.getQty()));
            System.out.println(item.getQty());
            
            table.addCell(String.valueOf(item.getOrder().getId()));
            System.out.println(item.getOrder().getId());
            
            table.addCell(String.valueOf(item.getSubtotal()));
            System.out.println(item.getSubtotal());

			
        }
	}
	
	public void generateInvoice(HttpServletResponse response) throws DocumentException, IOException
	{
		
		Document document=new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font1.setSize(18);
        font1.setColor(Color.BLACK);
         
        Paragraph p = new Paragraph("Order Invoice", font1);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        
        
        Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font2.setSize(10);
        font2.setColor(Color.BLACK);
        Paragraph p2 = new Paragraph("ABC Online Store "+"\n Hanuman Nagar"+"\nKalyan (East)"+"\nPincode : 421306", font2);
      
        p2.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(p2);
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
        
        for(CartItem ct:cartItemList)
        {
        	orderTotal=ct.getOrder().getOrderTotal();
        	
        }
        Font font3 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font3.setSize(10);
        font3.setColor(Color.BLACK);
        Paragraph p3 = new Paragraph("Total : "+orderTotal, font3);
        p3.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(p3);
        
        
       
        document.close();
	}
	
}
