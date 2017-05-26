package com.automarket.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "GOODS")
public class Goods implements Serializable {

	private Long id;
	private String name;
	private String description;
	private Integer analogousType;
	private Double price;
	private Set<Counter> counters = new HashSet<>();
	private Set<CommodityCirculation> commodityCirculations = new HashSet<>();

	public Goods() {
	}

	public Goods(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "goods")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	public Set<Counter> getCounters() {
		return counters;
	}

	public void setCounters(Set<Counter> counters) {
		this.counters = counters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goods")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	public Set<CommodityCirculation> getCommodityCirculations() {
		return commodityCirculations;
	}

	public void setCommodityCirculations(Set<CommodityCirculation> commodityCirculations) {
		this.commodityCirculations = commodityCirculations;
	}

	@Column(name = "type", nullable = true)
	public Integer getAnalogousType() {
		return analogousType;
	}

	public void setAnalogousType(Integer analogousType) {
		this.analogousType = analogousType;
	}

	@Column(name = "price")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Transient
	public int getTotalItems() {
		int total = 0;
		for(Counter counter : counters) {
			total += counter.getCount();
		}
		return total;
	}

	@Override
	public String toString() {
		return "Goods{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", analogousType=" + analogousType
				+ '}';
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		Goods goods = (Goods) o;
		return Objects.equals(id, goods.id) && Objects.equals(name, goods.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
