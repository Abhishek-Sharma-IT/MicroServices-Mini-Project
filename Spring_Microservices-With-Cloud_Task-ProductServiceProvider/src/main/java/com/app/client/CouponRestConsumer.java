package com.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.app.model.Coupon;

@FeignClient("COUPON-APP")
public interface CouponRestConsumer {

	@GetMapping("/rest/coupan/getByCode/{code}")
	public Coupon getCouponByCode(@PathVariable String code);
	
	
	@GetMapping("/rest/coupan/check/{code}")
	public String checkExpiredOrNot(@PathVariable String code);
}
