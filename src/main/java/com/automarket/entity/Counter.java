package com.automarket.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "COUNTER")
public class Counter {
	private int id;
	private Goods goods;
	private Store store;
	private String goodsName;
	private String storeName;
	private int count;
	
	/**
	 * 
	 */
	public Counter() {
		super();
	}

	/**
	 * @param id
	 * @param goods
	 * @param store
	 * @param count
	 */
	public Counter(int id, Goods goods, Store store, int count) {
		this.id = id;
		this.goods = goods;
		this.store = store;
		this.count = count;
		this.goodsName = goods.getName();
		this.storeName = store.getName();
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

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
		this.goodsName = goods.getName();
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
		this.storeName = store.getName();
	}

	@Column(name = "goodsCount")
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Transient
	public String getGoodsName() {
		return goodsName;
	}
	
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	@Transient
	public String getStoreName() {
		return storeName;
	}
	
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Override
	public String toString() {
		return "Counter [id=" + id + ", goods=" + goods + ", store=" + store
				+ ", count=" + count + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((goods == null) ? 0 : goods.hashCode());
		result = prime * result + id;
		result = prime * result + ((store == null) ? 0 : store.hashCode());
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
		Counter other = (Counter) obj;
		if (count != other.count)
			return false;
		if (goods == null) {
			if (other.goods != null)
				return false;
		} else if (!goods.equals(other.goods))
			return false;
		if (id != other.id)
			return false;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		return true;
	}

	
}
