package model.entities;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
@Builder
@ToString
@Getter
@Setter
public class Department implements Serializable {

    private Integer id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
