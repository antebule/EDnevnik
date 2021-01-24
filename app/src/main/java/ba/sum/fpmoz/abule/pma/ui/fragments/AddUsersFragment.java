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

public class AddUsersFragment extends Fragment {
    FirebaseDatabase db;
    DatabaseReference ref;

    EditText studentNameInp;
    EditText studentSurnameInp;
    EditText studentUidInp;
    Button addStudentBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userAdminView = inflater.inflate(R.layout.activity_user_admin, container, false);

        this.studentNameInp = userAdminView.findViewById(R.id.studentNameInp);
        this.studentSurnameInp = userAdminView.findViewById(R.id.studentSurnameInp);
        this.studentUidInp = userAdminView.findViewById(R.id.studentUidInp);
        this.addStudentBtn = userAdminView.findViewById(R.id.addStudentBtn);

        this.db = FirebaseDatabase.getInstance();
        this.ref = db.getReference("ednevnik/ucenici");

        addStudentBtn.setOnClickListener(v -> {
            String studentName = studentNameInp.getText().toString();
            String studentSurname = studentSurnameInp.getText().toString();
            String studentUid = studentUidInp.getText().toString();

            String newStudentKey = ref.push().getKey();
            ref.child(newStudentKey).setValue(new Student(studentUid, studentName, studentSurname));

            Toast.makeText(userAdminView.getContext(), "Uspješno ste dodali učenika", Toast.LENGTH_SHORT).show();

            studentNameInp.setText("");
            studentSurnameInp.setText("");
            studentUidInp.setText("");
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
