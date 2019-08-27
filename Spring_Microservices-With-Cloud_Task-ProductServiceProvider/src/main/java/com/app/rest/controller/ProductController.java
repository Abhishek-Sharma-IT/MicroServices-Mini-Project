package com.app.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.client.CouponRestConsumer;
import com.app.exception.ProductNotFoundException;
import com.app.exception.ValidationError;
import com.app.model.Coupon;
import com.app.model.Product;
import com.app.service.ProductServices;
import com.app.validator.ProductValidator;

@RestController
@RequestMapping("/rest/product")
public class ProductController {

	@Autowired
	private ProductServices service;
	
	@Autowired
	private CouponRestConsumer consumer;
	

	@Autowired
	private ProductValidator validator;
	
	
	@PostMapping("/save")
	public ResponseEntity<String> saveProduct(@RequestBody Product product,Errors errors)
	{
		validator.validate(product, errors);
		
		if(!errors.hasErrors())
		{
			String res= consumer.checkExpiredOrNot(product.getCoupon().getCouponCode());System.out.println(res);
			
			Coupon c = consumer.getCouponByCode(product.getCoupon().getCouponCode()); System.out.println(c);
			if(c==null)
			{
				return new ResponseEntity<String>("Coupon Not Found",HttpStatus.OK);
			}
			else if(res.equals("Expired"))
			{
				return new ResponseEntity<String>("Coupon has been expired !",HttpStatus.OK);				
			}
			else
			{	
				Double discount = c.getCouponDiscount();
				if(product.getCoupon().getApplied())   // if coupon applied
				{
					discount=0.0;
				}
				
				Double cost = product.getProdCost(); System.out.println(cost+","+discount);
				product.setFinalCost(service.calculateDiscount(cost, discount));
				service.saveProduct(product);
				return new ResponseEntity<String>("Product has been added successfully",HttpStatus.OK);
			}
			
		}
		else
		{
			throw new ValidationError("Please Provide Valid Details");
		}
	}
	
	
	
	@GetMapping("/apply")
	public ResponseEntity<Double> applyCoupon(Double cost,Double disc)
	{
		return new ResponseEntity<Double>(service.calculateDiscount(cost, disc),HttpStatus.OK);
	}
	
	
	
	@GetMapping("/getOne/{id}")
	//@HystrixCommand(fallbackMethod = "getProductException")
	public ResponseEntity<Product> getOneProduct(@PathVariable Integer id)
	{
		Product p = service.getProductById(id);System.out.println(p);
		
		if(p!=null)
		{
			return new ResponseEntity<Product>(p,HttpStatus.OK);
		}
		else
		{
			throw new ProductNotFoundException("No Product Found");
		}
		
	}
	
		
	
	// fallback method
	public ResponseEntity<Product> getProductException(Integer id)
	{
		throw new ProductNotFoundException("No Product Found");
	}
	
	

	
	@GetMapping("/all")
	public List<Product> getAllProducts()
	{
		return service.getAllProducts();
	}
	

	
	@PostMapping("/update")
	public ResponseEntity<String> updateProduct(@RequestBody Product product,Errors errors)
	{
		if(service.isExist(product.getProdId()))
		{
			
			validator.validate(product, errors);
			
			if(!errors.hasErrors())
			{
				service.saveProduct(product);
				return new ResponseEntity<String>("Product has been updated successfully",HttpStatus.OK);
			}
			else
			{
				throw new ValidationError("Please Provide Valid Details");
			}
			
		}
		else
		{
			throw new ProductNotFoundException("No Product Found");
		}
		
	}
	
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id)
	{
		if(service.isExist(id))
		{
			service.deleteProductById(id);
			
			return new ResponseEntity<String>("Product Deleted Successfully",HttpStatus.OK);
		}
		else
		{
			throw new ProductNotFoundException("No Product Found");
		}
		
	}
}
