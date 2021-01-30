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
import ba.sum.fpmoz.abule.pma.ui.adapters.SubjectsAdapter;
import ba.sum.fpmoz.abule.pma.model.Subject;

public class SubjectsFragment extends Fragment {

    View v;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    private ArrayList<Subject> subjectsList;
    private RecyclerView subjectsRecyclerView;
    private String className;

    public SubjectsFragment() {};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subjectsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/predmeti";
        System.out.println(reference);
        ref = db.getReference(reference);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // looping through the data
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Subject s = itemSnapshot.getValue(Subject.class);
                    subjectsList.add(s);
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_subjects, container, false);
        subjectsRecyclerView = v.findViewById(R.id.subjectsRecyclerView);
        setAdapter();

        FloatingActionButton fab = v.findViewById(R.id.fabSubjects);
        fab.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.new_subject_dialog);

            EditText subjectName = dialog.findViewById(R.id.subjectNameInp);
            Button submit = dialog.findViewById(R.id.submitSubjectBtn);

            submit.setOnClickListener(v1 -> {
                String newSubjectKey = ref.push().getKey();
                ref.child(newSubjectKey).setValue(new Subject(newSubjectKey, subjectName.getText().toString()));
            });
            dialog.show();
        });

        return v;
    }

    public void setAdapter() {
        SubjectsAdapter subjectsAdapter = new SubjectsAdapter(getContext(), subjectsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        subjectsRecyclerView.setLayoutManager(layoutManager);
        subjectsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        subjectsRecyclerView.setAdapter(subjectsAdapter);
    }
}