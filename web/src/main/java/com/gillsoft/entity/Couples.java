package com.gillsoft.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.EntityResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "couples", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "organization_id", "currency_from", "currency_to" }) })
@NamedQuery(name="Couples.findAll", query="SELECT s FROM Couples s")
@NamedNativeQueries({ @NamedNativeQuery(name = "OrganizationAllQuery", query = "select c.*, sc.*, s.*, cr.*"
		+ " from rate.couples c" + " left join rate.system_couples sc on sc.couple_id = c.id"
		+ " left join rate.systems s on s.system_id = sc.system_id"
		// + " left join rate.couple_rates cr on cr.couple_id = c.id and (? >= cr.date_start and (? <= cr.date_end or cr.date_end is null))"
		+ " left join rate.couple_rates cr on cr.couple_id = c.id and DATE(cr.date_end) >= DATE(now())"
		+ " where c.organization_id = ?", resultSetMapping = "OrganizationAllMapping"),
		@NamedNativeQuery(name = "OrganizationRatesQuery", query = "select c.*, cr.*"
				+ " from rate.couples c left join rate.couple_rates cr on cr.couple_id = c.id and DATE(cr.date_end) >= DATE(now())"
				+ " where c.organization_id = ?", resultSetMapping = "OrganizationRatesMapping"),
		@NamedNativeQuery(name = "OrganizationSystemsQuery", query = "select s.*, sc.*"
				+ " from rate.couples c join rate.system_couples sc on sc.couple_id = c.id"
				+ " join rate.systems s on s.system_id = sc.system_id"
				+ " where c.organization_id = ?", resultSetMapping = "OrganizationSystemsMapping") })
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "OrganizationAllMapping", entities = {
				@EntityResult(entityClass = com.gillsoft.entity.Couples.class),
				@EntityResult(entityClass = com.gillsoft.entity.SystemCouples.class),
				@EntityResult(entityClass = com.gillsoft.entity.Systems.class),
				@EntityResult(entityClass = com.gillsoft.entity.CoupleRates.class) }),
		@SqlResultSetMapping(name = "OrganizationRatesMapping", entities = {
				@EntityResult(entityClass = com.gillsoft.entity.Couples.class),
				@EntityResult(entityClass = com.gillsoft.entity.CoupleRates.class) }),
		@SqlResultSetMapping(name = "OrganizationSystemsMapping", entities = {
				@EntityResult(entityClass = com.gillsoft.entity.Systems.class),
				@EntityResult(entityClass = com.gillsoft.entity.SystemCouples.class)}) })
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