package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.StudentViewActivity;
import ba.sum.fpmoz.abule.pma.model.Grade;
import ba.sum.fpmoz.abule.pma.model.Subject;
import ba.sum.fpmoz.abule.pma.model.User;

public class NewGradeFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase db = FirebaseDatabase.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_grade, container, false);
        EditText gradeDesc = v.findViewById(R.id.gradeDescInp);
        EditText gradeValue = v.findViewById(R.id.gradeValueInp);
        Button submit = v.findViewById(R.id.submitGradeBtn);
        DatePicker date = v.findViewById(R.id.gradeDatePicker);

        Spinner subjectsSpinner = v.findViewById(R.id.subjectsSpinner);
        List<Subject> subjects = new ArrayList<>();
        String subjectsReference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/predmeti";
        DatabaseReference subjectsRef = FirebaseDatabase.getInstance().getReference(subjectsReference);
        subjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Subject s = itemSnapshot.getValue(Subject.class);
                    subjects.add(s);
                }
                ArrayAdapter<Subject> spinnerAdapter = new ArrayAdapter<Subject>(getContext(), android.R.layout.simple_spinner_item, subjects);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectsSpinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submit.setOnClickListener(v1 -> {
            Subject selectedSubject = (Subject) subjectsSpinner.getSelectedItem();
            int year = date.getYear();
            int month = date.getMonth() + 1;
            int day = date.getDayOfMonth();
            String selectedDate = day + "/" + month + "/" + year;
            String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/studenti/" + StudentViewActivity.studentUid + "/ocjene";
            DatabaseReference ref = db.getReference(reference);
            String newGradeKey = ref.push().getKey();
            Grade newGrade = new Grade(newGradeKey,  selectedDate, gradeValue.getText().toString(), gradeDesc.getText().toString(), StudentViewActivity.studentUid, selectedSubject.uid, selectedSubject.name);
            ref.child(newGradeKey).setValue(newGrade);
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("ednevnik/korisnici/" + StudentViewActivity.studentUid + "/ocjene");
            usersRef.child(newGradeKey).setValue(newGrade);
            Toast.makeText(getContext(), "Nova ocjena uspješno dodana!", Toast.LENGTH_SHORT).show();
            gradeValue.setText("");
            gradeDesc.setText("");
        });
        return v;
    }
}
