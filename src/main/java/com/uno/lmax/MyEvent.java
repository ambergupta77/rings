package com.uno.lmax;

import java.util.Objects;

public class MyEvent {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyEvent myEvent = (MyEvent) o;
        return id == myEvent.id &&
                Objects.equals(name, myEvent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public void reset() {
        this.name = null;
        id = -1;
    }
}
