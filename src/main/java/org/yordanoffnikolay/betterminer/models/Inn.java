package org.yordanoffnikolay.betterminer.models;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "inns")
public class Inn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;

    public Inn() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inn inn = (Inn) o;
        return id == inn.id && Objects.equals(name, inn.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
