package ba.sum.fpmoz.abule.pma.model;

public class User {
    public String uid;
    public String email;
    public String role;

    public User(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }

    public User() {
    }
}
