package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.ui.adapters.StudentsAdapter;
import ba.sum.fpmoz.abule.pma.model.Student;

public class StudentsFragment extends Fragment {

    View v;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    private ArrayList<Student> studentsList;
    private RecyclerView studentsRecyclerView;
    private String className;

    public StudentsFragment() {};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        studentsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi";
        ref = db.getReference(reference);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentsList = new ArrayList<>();
                // looping through the data
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
//                    System.out.println("Razredi: " + itemSnapshot.child("name").getValue().toString());
                    if(itemSnapshot.child("name").getValue().toString().equals(className)){
                        studentsList = new ArrayList<>();
                        for(DataSnapshot snap : itemSnapshot.child("studenti").getChildren()){
                            Student s = snap.getValue(Student.class);
                            studentsList.add(s);
                        }
                        setAdapter();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ClassViewActivity parentActivity = (ClassViewActivity) getActivity();
        className = parentActivity.getClassName();
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_students, container, false);
        studentsRecyclerView = v.findViewById(R.id.studentsRecyclerView);
        setAdapter();

        FloatingActionButton fab = v.findViewById(R.id.fabStudents);
        fab.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.new_student_dialog);

            EditText studentName = dialog.findViewById(R.id.studentNameInp1);
            EditText studentSurname = dialog.findViewById(R.id.studentSurnameInp1);
            Button submit = dialog.findViewById(R.id.submitStudentBtn);

            submit.setOnClickListener(v1 -> {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            if(itemSnapshot.child("name").getValue().toString().equals(className)){
                                String newStudentKey = itemSnapshot.child("studenti").getRef().push().getKey();
                                itemSnapshot.child("studenti").getRef().child(newStudentKey).setValue(new Student(newStudentKey, studentName.getText().toString(), studentSurname.getText().toString()));
                                dialog.dismiss();
                                Snackbar.make(v, "New class successfully added", Snackbar.LENGTH_SHORT).setActionTextColor(Color.BLUE)
                                        .setAction("OK", null).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });
            dialog.show();
        });

        return v;
    }

    public void setAdapter() {
        StudentsAdapter studentsAdapter = new StudentsAdapter(getContext(), studentsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        studentsRecyclerView.setLayoutManager(layoutManager);
        studentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        studentsRecyclerView.setAdapter(studentsAdapter);
    }
}