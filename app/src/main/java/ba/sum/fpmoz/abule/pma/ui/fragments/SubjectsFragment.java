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
import ba.sum.fpmoz.abule.pma.ui.adapters.FirebaseSubjectsAdapter;
import ba.sum.fpmoz.abule.pma.model.Subject;

public class SubjectsFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;

    private RecyclerView subjectsRecyclerView;
    private FirebaseSubjectsAdapter adapter;
    private FirebaseRecyclerOptions<Subject> options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);
        subjectsRecyclerView = v.findViewById(R.id.subjectsRecyclerView);
        this.subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/predmeti";
        ref = db.getReference(reference);

        options = new FirebaseRecyclerOptions.Builder<Subject>().setLifecycleOwner(SubjectsFragment.this).setQuery(this.ref, Subject.class).build();
        adapter = new FirebaseSubjectsAdapter(options);
        subjectsRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = v.findViewById(R.id.fabSubjects);
        fab.setOnClickListener(v1 -> {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.new_subject_dialog);

            EditText subjectName = dialog.findViewById(R.id.subjectNameInp);
            Button submit = dialog.findViewById(R.id.submitSubjectBtn);
            TextView errorMsg = dialog.findViewById(R.id.errorSubjectMsg);

            submit.setOnClickListener(v2 -> {
                String subject = subjectName.getText().toString();
                if(TextUtils.isEmpty(subject)) {
                    errorMsg.setText("Nepotpuni podaci!");
                } else {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean exists = false;
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                                Subject s = itemSnapshot.getValue(Subject.class);
                                if(s.name.equals(subject)){
                                    exists = true;
                                }
                            }

                            if(exists){
                                errorMsg.setText("Ovaj predmet već postoji u bazi!");
                            } else {
                                String newSubjectKey = ref.push().getKey();
                                ref.child(newSubjectKey).setValue(new Subject(newSubjectKey, subjectName.getText().toString()));
                                Toast.makeText(getContext(), "Novi predmet uspješno dodan!", Toast.LENGTH_SHORT).show();
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