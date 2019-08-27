package com.app.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.Coupon;
import com.app.repo.CouponRepository;

@Service
public class CouponServiceImpl implements CouponServices {

	@Autowired
	private CouponRepository repo;
	
	@Override
	@Transactional
	public Coupon saveCoupon(Coupon coupon) {
		return repo.save(coupon); 
	}

	
	@Override
	@Transactional
	@CachePut(value = "coupon-cache",key="#coupon.couponId")
	public Coupon updateCoupon(Coupon coupon) {
		return repo.save(coupon);
	}

	
	
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "coupon-cache",key="#couponId")
	public Coupon getCouponById(Integer couponId) 
	{	
		Optional<Coupon> c= repo.findById(couponId);
		return c.isPresent()?c.get():null;
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public Coupon getCouponByCode(String code) {
		return repo.findByCouponCode(code);
	}

	
	
	@Override
	@Transactional
	@CacheEvict(value = "coupan-cache",key="#coupanId")	
	public void deleteCouponById(Integer couponId) {
		repo.deleteById(couponId);
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public List<Coupon> getAllCoupons() {
		return repo.findAll();
	}

	
	@Override
	public boolean isExpired(String code) {
		Coupon c= repo.findByCouponCodeAndExpDate(code, new Date());
		return (c!=null)?true:false;
	}
	
	
	
	@Override
	public boolean isExist(Integer id) {
		System.out.println(id);
		return repo.existsById(id);
	}
}
