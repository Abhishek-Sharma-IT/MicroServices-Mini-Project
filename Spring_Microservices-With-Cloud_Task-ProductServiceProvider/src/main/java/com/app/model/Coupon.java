package com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    private Integer couponId;
	private String couponCode;
	private Double couponDiscount;
	private String expDate;
	@JsonIgnore
	private Boolean applied=false;
}
