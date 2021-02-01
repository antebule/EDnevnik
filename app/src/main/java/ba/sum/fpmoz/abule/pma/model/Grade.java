package ba.sum.fpmoz.abule.pma.model;

public class Grade {
    public String uid;
    public String date;
    public String value;
    public String description;
    public String studentID;
    public String subjectID;
    public String subjectName;

    public Grade(String uid, String date, String value, String description, String studentID, String subjectID, String subjectName) {
        this.uid = uid;
        this.date = date;
        this.value = value;
        this.description = description;
        this.studentID = studentID;
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }

    public Grade() {};
}
