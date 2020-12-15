package com.udacity.vehicles.domain.car;

import java.math.BigDecimal;

public class Price {

    private String currency;
    private BigDecimal price;
    private Integer vehicleId;

    public Price() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
}
