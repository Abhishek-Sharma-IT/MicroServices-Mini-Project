package com.app.validator;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.app.exception.ApiError;
import com.app.model.Coupon;

@Component
public class CouponValidator implements Validator{

	@Autowired
	private ApiError api=new ApiError();
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(Coupon.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		
		Coupon c = (Coupon) target;
		
		if(StringUtils.isEmpty(c.getCouponId().toString()))
		{
			errors.reject("couponId");
			api.getValidationErrors().add("Please Provide Coupon Id");
		}
		
		if(StringUtils.isEmpty(c.getCouponCode()))
		{
			errors.reject("couponCode");
			api.getValidationErrors().add("Please Provide Coupon Code ");
		}
		
		if(StringUtils.isEmpty(c.getCouponDiscount().toString()))
		{
			errors.reject("couponDiscount");
			api.getValidationErrors().add("Please Provide Coupon Discount ");
		}

		if(c.getCouponDiscount()<0)
		{
			errors.reject("couponDiscount");
			api.getValidationErrors().add("Please Provide Valid Discount ");
		}
		
		if(StringUtils.isEmpty(c.getExpDate()))
		{
			errors.reject("expDate");
			api.getValidationErrors().add("Please Provide Coupon Expiry Date");
		}
		
		if(c.getExpDate().before(new Date()))
		{
			errors.reject("expDate");
			api.getValidationErrors().add("Please Provide Valid Expiry Date");			
		}
		
		
		
		
	}
}
