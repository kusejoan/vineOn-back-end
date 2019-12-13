package app.user.Controller;

import app.user.Entity.Store;

public class StoreInfo
{
    StoreInfo()
    {
        success = false;
    }

    StoreInfo(Store store)
    {
        storeName = store.getStoreName();
        address = store.getAddress();
        city = store.getCity();
        country = store.getCountry();
        website = store.getWebsite();
        success = true;
    }
    String storeName;
    String address;
    String city;
    String country;
    String website;
    boolean success;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
