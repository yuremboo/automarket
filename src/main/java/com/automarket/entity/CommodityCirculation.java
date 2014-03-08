package com.automarket.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="COMMODITY_CIRCULATION")
public class CommodityCirculation {
	private long id;
	private Date date;
	private Goods goods;
	private int count;
	private boolean isSale;
	
	public CommodityCirculation() {
		super();
	}
	
	public CommodityCirculation(long id, Date date, Goods goods, int count) {
		super();
		this.id = id;
		this.date = date;
		this.goods = goods;
		this.count = count;
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
	
	@Column(name = "cdate")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	@Column(name = "goodsCount")
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Column(name = "isSale")
	public boolean isSale() {
		return isSale;
	}
	public void setSale(boolean isSale) {
		this.isSale = isSale;
	}
	@Override
	public String toString() {
		return "CommodityCirculation [id=" + id + ", date=" + date + ", goods="
				+ goods + ", count=" + count + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((goods == null) ? 0 : goods.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
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
		CommodityCirculation other = (CommodityCirculation) obj;
		if (count != other.count)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (goods == null) {
			if (other.goods != null)
				return false;
		} else if (!goods.equals(other.goods))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
		
}
