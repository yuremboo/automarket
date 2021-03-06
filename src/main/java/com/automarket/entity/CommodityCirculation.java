package com.automarket.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@NamedQueries({
        @NamedQuery(
                name = "removeZeros",
                query = "delete from CommodityCirculation C where C.count=:count"
        )
}
)
@Entity
@Table(name="COMMODITY_CIRCULATION")
public class CommodityCirculation {
	private long id;
	private Date date;
	private Goods goods;
	private int count;
	private boolean sale;
	private Store store;
	private String goodsName;
	private String storeName;
    private String saleProp;
    private Double salePrice;
	
	public CommodityCirculation() {
	}
	
	public CommodityCirculation(Goods goods, int count, Store store) {
		this.goods = goods;
		this.count = count;
		this.store = store;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
		this.goodsName = goods.getName();
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
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
        if (sale) {
            this.saleProp = "Продаж";
        } else {
            this.saleProp = "Надходження";
        }
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
		this.storeName = store.getName();
	}

	@Column(name = "salePrice")
	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
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
    @Transient
    public String getSaleProp() {
        return saleProp;
    }
    public void setSaleProp(String saleProp) {
        this.saleProp = saleProp;
    }

    @PrePersist
    public void prePersist() {
	    this.date = new Date();
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
