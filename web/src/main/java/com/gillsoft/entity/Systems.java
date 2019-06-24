package com.gillsoft.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="systems")
@NamedQuery(name="Systems.findAll", query="SELECT s FROM Systems s")
@JsonInclude(value = Include.NON_NULL)
public class Systems implements Serializable {

	private static final long serialVersionUID = -5261026356672271432L;

	@Id
	@Column(name="system_id", unique=true, nullable=false)
	@GeneratedValue
	@JsonProperty("id")
	private Integer id;

	@Column(name="plugin_name", nullable=false)
	@JsonProperty("plugin_name")
	private String pluginName;

	private String description;

	private String currency;

	@Column(name="run_hour")
	@JsonProperty("run_hour")
	private Integer runHour;

	@Column(name="days_shift")
	@JsonProperty("days_shift")
	private Integer daysShift;

	public Systems() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setSystemId(Integer id) {
		this.id = id;
	}

	public String getPluginName() {
		return this.pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getRunHour() {
		return this.runHour;
	}

	public void setRunHour(Integer runHour) {
		this.runHour = runHour;
	}

	public Integer getDaysShift() {
		return this.daysShift;
	}

	public void setDaysShift(Integer daysShift) {
		this.daysShift = daysShift;
	}

}