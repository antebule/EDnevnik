package ba.sum.fpmoz.abule.pma.model;

import java.sql.Date;

public class Grade {
    public String uid;
    public String date;
    public String value;
    public String description;
    public String studentID;
    public String subjectID;

    public Grade(String uid, String date, String value, String description, String studentID, String subjectID) {
        this.uid = uid;
        this.date = date;
        this.value = value;
        this.description = description;
        this.studentID = studentID;
        this.subjectID = subjectID;
    }

    public Grade() {};
}
