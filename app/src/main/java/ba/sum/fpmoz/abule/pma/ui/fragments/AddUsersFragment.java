package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Student;
import ba.sum.fpmoz.abule.pma.model.User;

public class AddUsersFragment extends Fragment {
    FirebaseDatabase db;
    DatabaseReference ref;

    EditText teacherNameInp;
    EditText teacherSurnameInp;
    EditText teacherEmailInp;
    Button addTeacherBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userAdminView = inflater.inflate(R.layout.fragment_add_users, container, false);

        this.teacherNameInp = userAdminView.findViewById(R.id.teacherNameInp);
        this.teacherSurnameInp = userAdminView.findViewById(R.id.teacherSurnameInp);
        this.teacherEmailInp = userAdminView.findViewById(R.id.teacherEmailInp);
        this.addTeacherBtn = userAdminView.findViewById(R.id.addTeacherBtn);

        this.db = FirebaseDatabase.getInstance();
        this.ref = db.getReference("ednevnik/korisnici");

        addTeacherBtn.setOnClickListener(v -> {
//            String teacherName = teacherNameInp.getText().toString();
//            String teacherSurname = teacherSurnameInp.getText().toString();
            String teacherEmail = teacherEmailInp.getText().toString();

            String newTeacherKey = ref.push().getKey();
            ref.child(newTeacherKey).setValue(new User(newTeacherKey, teacherEmail, "teacher"));

            Toast.makeText(userAdminView.getContext(), "Uspješno ste dodali profesora", Toast.LENGTH_SHORT).show();

            teacherNameInp.setText("");
            teacherSurnameInp.setText("");
            teacherEmailInp.setText("");
        });

        // dohvacanje podataka
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //do something with data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return userAdminView;
    }
}
