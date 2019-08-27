package com.app.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Coupon")
public class Coupon implements Serializable{

	@Id
	@Column(name="coupon_Id")
	private Integer couponId;
	
	@Column(name="coupon_Code")
	private String couponCode;
	
	@Column(name="coupon_Discount")
	private Double couponDiscount;
	
	@Column(name="coupon_Expiry")
	@Temporal(TemporalType.DATE)
	private Date expDate;
	
	@JsonIgnore
	@Column(name="coupon_Valid")
	private boolean isValid;


}
