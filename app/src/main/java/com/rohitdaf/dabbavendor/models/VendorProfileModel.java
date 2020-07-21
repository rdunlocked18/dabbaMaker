package com.rohitdaf.dabbavendor.models;

import com.rohitdaf.dabbavendor.activites.SignupActivity;

public class VendorProfileModel {
    String userId,vendorName,vendorPhone,vendorEmail,vendorShopName,shopAddress,deliverySlot,shidoriRatePerDay,shidoriRatePerWeek,shidoriRatePerMonth;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorShopName() {
        return vendorShopName;
    }

    public void setVendorShopName(String vendorShopName) {
        this.vendorShopName = vendorShopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getDeliverySlot() {
        return deliverySlot;
    }

    public void setDeliverySlot(String deliverySlot) {
        this.deliverySlot = deliverySlot;
    }

    public String getShidoriRatePerDay() {
        return shidoriRatePerDay;
    }

    public void setShidoriRatePerDay(String shidoriRatePerDay) {
        this.shidoriRatePerDay = shidoriRatePerDay;
    }

    public String getShidoriRatePerWeek() {
        return shidoriRatePerWeek;
    }

    public void setShidoriRatePerWeek(String shidoriRatePerWeek) {
        this.shidoriRatePerWeek = shidoriRatePerWeek;
    }

    public String getShidoriRatePerMonth() {
        return shidoriRatePerMonth;
    }

    public void setShidoriRatePerMonth(String shidoriRatePerMonth) {
        this.shidoriRatePerMonth = shidoriRatePerMonth;
    }
}
