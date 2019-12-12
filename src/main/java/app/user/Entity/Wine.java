package app.user.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "wines")
public class Wine implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "year")
    private Long year;
    @ManyToMany(mappedBy = "wines")
    private Set<Store> store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Set<Store> getStore() {
        return store;
    }

    public void setStore(Set<Store> shops) {
        this.store = shops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wine wine = (Wine) o;
        return Objects.equals(id, wine.id) &&
                Objects.equals(name, wine.name) &&
                Objects.equals(country, wine.country) &&
                Objects.equals(year, wine.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, year);
    }

    /*
    @ManyToMany
    private Set<WineGrade> grades;


 */


}
