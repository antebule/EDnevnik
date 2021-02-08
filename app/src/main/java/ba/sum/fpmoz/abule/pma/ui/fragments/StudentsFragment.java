package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.User;
import ba.sum.fpmoz.abule.pma.ui.adapters.FirebaseStudentsAdapter;
import ba.sum.fpmoz.abule.pma.model.Student;

public class StudentsFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;

    private RecyclerView studentsRecyclerView;
    private FirebaseStudentsAdapter adapter;
    private FirebaseRecyclerOptions<Student> options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_students, container, false);
        studentsRecyclerView = v.findViewById(R.id.studentsRecyclerView);
        this.studentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/studenti";
        ref = db.getReference(reference);

        options = new FirebaseRecyclerOptions.Builder<Student>().setLifecycleOwner(StudentsFragment.this).setQuery(this.ref, Student.class).build();
        adapter = new FirebaseStudentsAdapter(options);
        studentsRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = v.findViewById(R.id.fabStudents);
        fab.setOnClickListener(v1 -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.new_student_dialog);

            EditText studentName = dialog.findViewById(R.id.studentNameInp1);
            EditText studentSurname = dialog.findViewById(R.id.studentSurnameInp1);
            EditText studentEmail = dialog.findViewById(R.id.studentEmailInp1);
            Button submit = dialog.findViewById(R.id.submitStudentBtn);
            TextView errorMsg = dialog.findViewById(R.id.errorMsg);

            submit.setOnClickListener(v2 -> {
                String email = studentEmail.getText().toString();
                String name = studentName.getText().toString();
                String surname = studentSurname.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(surname)) {
                    errorMsg.setText("Nepotpuni podaci!");
                } else {
                    DatabaseReference users = FirebaseDatabase.getInstance().getReference("ednevnik/korisnici");
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean exists = false;
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                                User user = itemSnapshot.getValue(User.class);
                                if(user.email.equals(email)){
                                    exists = true;
                                }
                            }

                            if(exists){
                                errorMsg.setText("Ovaj email već postoji u bazi!");
                            } else {
                                String newStudentKey = ref.push().getKey();
                                ref.child(newStudentKey).setValue(new Student(newStudentKey, name, surname));
                                users.child(newStudentKey).setValue(new User(newStudentKey, email, "student"));
                                Toast.makeText(getContext(), "Novi student uspješno dodan!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            dialog.show();
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}