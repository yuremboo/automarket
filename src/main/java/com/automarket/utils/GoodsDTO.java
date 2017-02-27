package com.automarket.utils;


public class GoodsDTO {
	private String name;
	private String description;
	private String store;
	private Double price;
	private Integer count;
	private Integer analogousType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getAnalogousType() {
		return analogousType;
	}

	public void setAnalogousType(Integer analogousType) {
		this.analogousType = analogousType;
	}

	@Override
	public String toString() {
		return "GoodsDTO{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", store='" + store + '\'' +
				", price=" + price +
				", count=" + count +
				", analogousType=" + analogousType +
				'}';
	}
}
