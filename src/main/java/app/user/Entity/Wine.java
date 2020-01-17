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
    private String wineName;

    @Column(name = "country")
    private String country;

    @Column(name = "year")
    private Long year;

    @Column(name = "color")
    private String color;

    @Column(name = "type")
    private String type;

    @ManyToMany(mappedBy = "wines")
    private Set<Store> store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWineName() {
        return wineName;
    }

    public void setWineName(String wineName) {
        this.wineName = wineName;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                Objects.equals(wineName, wine.wineName) &&
                Objects.equals(country, wine.country) &&
                Objects.equals(year, wine.year) &&
                Objects.equals(color, wine.color) &&
                Objects.equals(type, wine.type);
    }


    /*
    @ManyToMany
    private Set<WineGrade> grades;


 */


}
