package de.chdrescher.piegong.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class RingEvent {

    @Id
    @GeneratedValue
    private int id;

    private Date ringDate;

    private int audioModelId;

    public RingEvent() {
    }

    public RingEvent(Date ringDate, int audioModelId) {
        this.ringDate = ringDate;
        this.audioModelId = audioModelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRingDate() {
        return ringDate;
    }

    public void setRingDate(Date ringDate) {
        this.ringDate = ringDate;
    }

    public int getAudioModelId() {
        return audioModelId;
    }

    public void setAudioModelId(int audioModelId) {
        this.audioModelId = audioModelId;
    }
}
