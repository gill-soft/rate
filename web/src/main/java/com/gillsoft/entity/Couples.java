package com.gillsoft.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "couples", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "organization_id", "currency_from", "currency_to" }) })
@NamedQuery(name="Couples.findAll", query="SELECT s FROM Couples s")
@JsonInclude(value = Include.NON_NULL)
public class Couples implements Serializable {

	private static final long serialVersionUID = 5037541916003226213L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	@JsonProperty("id")
	private Integer id;

	@Column(name="organization_id", nullable=false)
	@JsonProperty("organization_id")
	private String organizationId;

	@Column(name="currency_from", nullable=false)
	@JsonProperty("currency_from")
	private String currencyFrom;

	@Column(name="currency_to", nullable=false)
	@JsonProperty("currency_to")
	private String currencyTo;

	public Couples() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrganizationId() {
		return this.organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getCurrencyFrom() {
		return this.currencyFrom;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public String getCurrencyTo() {
		return this.currencyTo;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}

}