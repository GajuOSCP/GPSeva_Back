package com.example.Gpseva.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId; // submissionId / user identifier

	private Long registrationId;

	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorpaySignature;

	private int amount; // in paise
	private String currency;

	private String status; // CREATED, PAID, FAILED
	private String receipt;

	private LocalDateTime createdAt;
	private LocalDateTime paidAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}

	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}

	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}

	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}

	public String getRazorpaySignature() {
		return razorpaySignature;
	}

	public void setRazorpaySignature(String razorpaySignature) {
		this.razorpaySignature = razorpaySignature;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", userId=" + userId + ", registrationId=" + registrationId + ", razorpayOrderId="
				+ razorpayOrderId + ", razorpayPaymentId=" + razorpayPaymentId + ", razorpaySignature="
				+ razorpaySignature + ", amount=" + amount + ", currency=" + currency + ", status=" + status
				+ ", receipt=" + receipt + ", createdAt=" + createdAt + ", paidAt=" + paidAt + "]";
	}

	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}
	

	public Payment(Long id, String userId, Long registrationId, String razorpayOrderId, String razorpayPaymentId,
			String razorpaySignature, int amount, String currency, String status, String receipt,
			LocalDateTime createdAt, LocalDateTime paidAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.registrationId = registrationId;
		this.razorpayOrderId = razorpayOrderId;
		this.razorpayPaymentId = razorpayPaymentId;
		this.razorpaySignature = razorpaySignature;
		this.amount = amount;
		this.currency = currency;
		this.status = status;
		this.receipt = receipt;
		this.createdAt = createdAt;
		this.paidAt = paidAt;
	}

}
