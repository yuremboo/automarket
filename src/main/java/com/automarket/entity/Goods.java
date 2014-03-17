package com.automarket.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "GOODS")
public class Goods {
	private long id;
	private String name;
	private String description;
	private Set<Counter> counters = new HashSet<>();
	private Set<CommodityCirculation> commodityCirculations = new HashSet<>();
	
	public Goods() {
		
	}
	
	public Goods(long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goods")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	public Set<Counter> getCounters() {
		return counters;
	}
	
	public void setCounters(Set<Counter> counters) {
		this.counters = counters;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goods")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	public Set<CommodityCirculation> getCommodityCirculations() {
		return commodityCirculations;
	}
	
	public void setCommodityCirculations(
			Set<CommodityCirculation> commodityCirculations) {
		this.commodityCirculations = commodityCirculations;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", name=" + name + ", description="
				+ description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goods other = (Goods) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
