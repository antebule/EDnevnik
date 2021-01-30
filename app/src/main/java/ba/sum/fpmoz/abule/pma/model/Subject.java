package ba.sum.fpmoz.abule.pma.model;

import androidx.annotation.NonNull;

public class Subject {
    public String uid;
    public String name;

    public Subject(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Subject() {};

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
