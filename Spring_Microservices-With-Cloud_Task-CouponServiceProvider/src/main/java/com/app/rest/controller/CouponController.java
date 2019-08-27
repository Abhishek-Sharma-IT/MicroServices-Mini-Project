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
import com.app.exception.CouponNotFoundException;
import com.app.exception.ValidationError;
import com.app.model.Coupon;
import com.app.service.CouponServices;
import com.app.validator.CouponValidator;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/rest/coupan")
public class CouponController {

	@Autowired
	private CouponServices service;
	
	@Autowired
	private CouponValidator validator;
	
	
	@PostMapping("/save")
	public ResponseEntity<String> saveCoupon(@RequestBody Coupon coupon,Errors errors)
	{
		validator.validate(coupon, errors);
		
		if(!errors.hasErrors())
		{
			service.saveCoupon(coupon);
			return new ResponseEntity<String>("Coupon added successfully",HttpStatus.OK);
		}
		else
		{
			throw new ValidationError("Please Provide Valid Details");
		}
	}
	
	
	
	@GetMapping("/check/{code}")
	public String checkExpiredOrNot(@PathVariable String code)
	{
		return service.isExpired(code)?"Not Expired":"Expired";
	}
	
	
	
	
	@GetMapping("/getOne/{id}")
	@HystrixCommand(fallbackMethod = "getCouponException")
	public Coupon getOneCoupon(@PathVariable Integer id)
	{
		Coupon c = service.getCouponById(id);
		
		if(c!=null)
		{
			return c;
		}
		else
		{
			throw new CouponNotFoundException("No Coupon Found");
		}
		
	}
	
	
	@GetMapping("/getByCode/{code}")
	//@HystrixCommand(fallbackMethod = "getCoupanCodeException")
	public Coupon getCouponByCode(@PathVariable String code)
	{
		System.out.println(code);
		Coupon c = service.getCouponByCode(code);
		
		if(c!=null)
		{
			return c;
		}
		else
		{
			throw new CouponNotFoundException("No Coupon Found");
		}
		
	}
	
	
	
	// fallback method
	public Coupon getCoupanException(Integer id)
	{
		throw new CouponNotFoundException("No Coupon Found");
	}
	
	public Coupon getCoupanCodeException(String code)
	{
		throw new CouponNotFoundException("No Coupon Found");
	}
	
	
	
	@GetMapping("/all")
	public List<Coupon> getAllCoupans()
	{
		List<Coupon> list =service.getAllCoupons();
		System.out.println(list);
		return list;
	}
	
	
	
	
	@PostMapping("/update")
	public ResponseEntity<String> updateCoupan(@RequestBody Coupon coupon,Errors errors)
	{
		if(service.isExist(coupon.getCouponId()))
		{
			
			validator.validate(coupon, errors);
			
			if(!errors.hasErrors())
			{
				service.saveCoupon(coupon);
				return new ResponseEntity<String>("Coupon has been updated successfully",HttpStatus.OK);
			}
			else
			{
				throw new ValidationError("Please Provide Valid Details");
			}
			
		}
		else
		{
			throw new CouponNotFoundException("No Coupon Found");
		}
		
	}
	
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCoupon(@PathVariable Integer id)
	{
		
		if(service.isExist(id))
		{
			service.deleteCouponById(id);
			
			return new ResponseEntity<String>("Coupon Deleted Successfully",HttpStatus.OK);
		}
		else
		{
			throw new CouponNotFoundException("No Coupon Found");
		}
		
	}
}
