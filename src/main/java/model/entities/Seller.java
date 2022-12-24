package model.entities;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
public class Seller implements Serializable {

    private Integer id;
    private String name;
    private String email;
    private Date birthDate;
    private Double baseSalary;
    private Department department;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(id, seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
