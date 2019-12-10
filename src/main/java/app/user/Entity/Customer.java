package app.user.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "regular")
public class Customer extends User{
    public Customer() {}

    public Customer(User other)
    {
        super(other);
    }
    @Column(name = "first_name")
    private String first_name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_date")
    private String birth_date;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(first_name, customer.first_name) &&
                Objects.equals(surname, customer.surname) &&
                Objects.equals(birth_date, customer.birth_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, surname, birth_date);
    }
}
