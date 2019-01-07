package com.moving.admin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
	/**
    *
    */
   private static final long serialVersionUID = 1L;
   
   public Order() {
	   
   }
   
   public Order(Long id, Long price, String orderName) {
	   this.id = id;
	   this.price = price;
	   this.orderName = orderName;
   }
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;
   
   @Column(name = "price")
   private Long price;
   
   @Column(name = "order_name")
   private String orderName;
   
   @Column(name = "user_id")
   private int userId;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPrice() {
		return price;
	}
	
	public void setPrice(Long price) {
		this.price = price;
	}
	
	public String getOrderName() {
		return orderName;
	}
	
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
