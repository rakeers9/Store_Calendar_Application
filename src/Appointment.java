/**
 * Project 4 - Appointment
 * <p>
 * This program initializes appointment attributes: title, maxAttendees, approvedBookings, startTime, and endTime.
 * Each attribute is paired with a getter and setter method, along with a constructor for the class.
 *
 * @author Sruthi Lingam
 * @version November 12, 2023
 */
public class Appointment {
    private String title;
    private int maxAttendees;
    private int approvedBookings;
    private String startTime;
    private String endTime;

    public Appointment(String title, int maxAttendees, int approvedBookings, String startTime, String endTime) {
        this.title = title;
        this.maxAttendees = maxAttendees;
        this.approvedBookings = approvedBookings;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //getter methods
    public String getTitle() {
        return title;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public int getApprovedBookings() {
        return approvedBookings;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    //setter methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public void setApprovedBookings(int approvedBookings) {
        this.approvedBookings = approvedBookings;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String toString() {
        return (getTitle() + "," + getMaxAttendees() + "," + getApprovedBookings() + "," + getStartTime() + "," +
                getEndTime());
    }
}