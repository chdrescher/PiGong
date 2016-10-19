package de.chdrescher.piegong.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class AudioModel {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String filename;

    private boolean active;

    public AudioModel() {
    }

    public AudioModel(String name, String filename, boolean active) {
        this.name = name;
        this.filename = filename;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
