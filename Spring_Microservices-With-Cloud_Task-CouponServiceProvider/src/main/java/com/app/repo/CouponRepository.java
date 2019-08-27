package com.app.repo;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.app.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

        public Coupon findByCouponCode(String code);
	
	    @Query("from com.app.model.Coupon as c where c.couponCode =:code and c.expDate >=:date ")
	    public Coupon findByCouponCodeAndExpDate(String code, Date date);
}
