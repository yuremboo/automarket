package com.automarket.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "STORE")
public class Store {
	private int id;
	private String name;
	private String description;
	private Set<Counter> counters = new HashSet<>();
	private Set<CommodityCirculation> circulations = new HashSet<>();
	private boolean defaultStore;
	
	public Store() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name")
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
	public Set<Counter> getCounters() {
		return counters;
	}
	
	public void setCounters(Set<Counter> counters) {
		this.counters = counters;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
	public Set<CommodityCirculation> getCirculations() {
		return circulations;
	}
	
	public void setCirculations(Set<CommodityCirculation> circulations) {
		this.circulations = circulations;
	}
	
	@Column(name = "isDefault")
    public boolean isDefaultStore() {
        return defaultStore;
    }

    public void setDefaultStore(boolean defaultStore) {
        this.defaultStore = defaultStore;
    }

	@Override
	public String toString() {
		return "Store [id=" + id + ", name=" + name + ", description="
				+ description + ", isDefault=" + defaultStore + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + (defaultStore ? 1231 : 1237);
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
		Store other = (Store) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (defaultStore != other.defaultStore)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}