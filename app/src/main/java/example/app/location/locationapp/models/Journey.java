package example.app.location.locationapp.models;

/**
 * Created by Kuba on 17.06.2017.
 */

public class Journey {

    private int id;
    private long timeStart;
    private long timeEnd;

    public Journey() {
    }

    public Journey(long timeStart) {
        this.timeStart = timeStart;
    }

    public Journey(long timeStart, long timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }
}
