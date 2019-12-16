package app.user.Controller.helpers;

import app.user.Entity.Store;

public class StoreReturn
{
    public StoreReturn()
    {
        success = false;
    }

    public StoreReturn(Store store)
    {
        storeName = store.getStoreName();
        address = store.getAddress();
        city = store.getCity();
        country = store.getCountry();
        website = store.getWebsite();
        success = true;
    }
    public String storeName;
    public String address;
    public String city;
    public String country;
    public String website;
    public boolean success;

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
