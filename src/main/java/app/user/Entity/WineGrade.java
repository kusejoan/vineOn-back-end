package app.user.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="wine_grades")
public class WineGrade {
    public WineGrade() {}

    public WineGrade(User user, Wine wine, Long grade, String description) {
        this.user = user;
        this.wine = wine;
        this.grade = grade;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JsonBackReference
    private User user;

    @ManyToOne()
    @JsonBackReference
    private Wine wine;

    @Column(name = "grade")
    private Long grade;

    @Column(name = "description")
    private String description;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wine getWine() {
        return wine;
    }

    public void setWine(Wine wine) {
        this.wine = wine;
    }

    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WineGrade wineGrade = (WineGrade) o;
        return Objects.equals(grade, wineGrade.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, wine, grade, description);
    }
}
