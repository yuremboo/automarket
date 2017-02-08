package com.automarket.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.io.Serializable;
import java.util.HashSet;
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
	private Set<Goods> myAnalogs = new HashSet<>();
	private Set<Goods> analogsToMe = new HashSet<>();
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

	@ManyToMany(cascade = { javax.persistence.CascadeType.ALL })
	@JoinTable(name = "ANALOGS", joinColumns = { @JoinColumn(name = "GOODS_ID") }, inverseJoinColumns = { @JoinColumn(name = "ANALOG_ID") })
	public Set<Goods> getMyAnalogs() {
		return myAnalogs;
	}

	public void setMyAnalogs(Set<Goods> myAnalogs) {
		this.myAnalogs = myAnalogs;
	}

	@ManyToMany(mappedBy = "myAnalogs")
	public Set<Goods> getAnalogsToMe() {
		return analogsToMe;
	}

	public void setAnalogsToMe(Set<Goods> analogsToMe) {
		this.analogsToMe = analogsToMe;
	}

	@Transient
	public int getTotalItems() {
		int total = 0;
		for(Counter counter : counters) {
			total += counter.getCount();
		}
		return total;
	}

	@Transient
	public Set<Goods> getAllAnalogs() {
		Set<Goods> goods = new HashSet<>(myAnalogs);
		goods.addAll(analogsToMe);
		return goods;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Goods other = (Goods) obj;
		if(description == null) {
			if(other.description != null)
				return false;
		} else if(!description.equals(other.description))
			return false;
		if(id != other.id)
			return false;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		return true;
	}
}
