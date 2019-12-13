package app.user.Entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "store")
public class Store extends User {

    public Store() {
        wines = new HashSet<>();
    }

    public Store(User other)
    {
        super(other);
    }

        @Column(name = "store_name")
        private String storeName;

        @Column(name = "address")
        private String address;

        @Column(name = "city")
        private String city;

        @Column(name = "country")
        private String country;

        @Column(name = "website")
        private String website;

    @ManyToMany
        private Set<Wine> wines;


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

    public Set<Wine> getWines() {
        return wines;
    }

    public void setWines(Set<Wine> wines) {
        this.wines = wines;
    }

    public boolean addWine(Wine wine)
    {
       return wines.add(wine);
    }

    public boolean removeWine(Wine wine)
    {
        return wines.remove(wine);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Store store = (Store) o;
        return Objects.equals(storeName, store.storeName) &&
                Objects.equals(address, store.address) &&
                Objects.equals(city, store.city) &&
                Objects.equals(country, store.country) &&
                Objects.equals(website, store.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeName, address, city, country, website);
    }
}
