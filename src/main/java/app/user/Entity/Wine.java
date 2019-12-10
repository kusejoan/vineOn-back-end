package app.user.Entity;

import javax.persistence.*;
import java.io.Serializable;
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
    @ManyToMany
    private Set<Store> shops;
/*
    @ManyToMany
    private Set<WineGrade> grades;


 */


}
