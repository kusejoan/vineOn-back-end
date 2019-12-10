package app.user.Entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "store")
public class Store extends User {

    public Store() {}

    public Store(User other)
    {
        super(other);
    }

    @Column(name = "store_name")
    private String store_name;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "webside")
    private String webside;


    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
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

    public String getWebside() {
        return webside;
    }

    public void setWebside(String webside) {
        this.webside = webside;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Store store = (Store) o;
        return Objects.equals(store_name, store.store_name) &&
                Objects.equals(address, store.address) &&
                Objects.equals(city, store.city) &&
                Objects.equals(country, store.country) &&
                Objects.equals(webside, store.webside);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store_name, address, city, country, webside);
    }
}
