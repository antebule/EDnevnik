package ba.sum.fpmoz.abule.pma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.model.Class;
import ba.sum.fpmoz.abule.pma.model.Subject;
import ba.sum.fpmoz.abule.pma.ui.adapters.ClassesAdapter;
import ba.sum.fpmoz.abule.pma.ui.adapters.FirebaseSubjectsAdapter;
import ba.sum.fpmoz.abule.pma.ui.adapters.SubjectsAdapter;

public class UserStudentActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseUser user;
    FirebaseAuth mAuth;
//    private ArrayList<Subject> subjectsList;
    private RecyclerView subjectsRecyclerView;
    private Button signOutBtn;
//    private SubjectsAdapter subjectsAdapter;

    private FirebaseSubjectsAdapter adapter;
    private FirebaseRecyclerOptions<Subject> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_student);

        this.mAuth = FirebaseAuth.getInstance();
//        this.user = mAuth.getCurrentUser();
//        System.out.println(mAuth.getCurrentUser() == null);
        // TODO: solve google sign-in user problem
        if(mAuth.getCurrentUser() == null){
            Toast.makeText(this, "No email", Toast.LENGTH_SHORT).show();
        }else {
            this.user = mAuth.getCurrentUser();
            Toast.makeText(this, this.user.getEmail(), Toast.LENGTH_SHORT).show();
        }

        this.db = FirebaseDatabase.getInstance();
        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/predmeti";
        this.ref = db.getReference(reference);
        this.subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView1);
//        this.subjectsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(UserStudentActivity.this));
        options = new FirebaseRecyclerOptions.Builder<Subject>().setLifecycleOwner(UserStudentActivity.this).setQuery(this.ref, Subject.class).build();

        adapter = new FirebaseSubjectsAdapter(options);
        subjectsRecyclerView.setAdapter(adapter);

        this.signOutBtn = findViewById(R.id.signOutBtn1);

        this.signOutBtn.setOnClickListener(v -> {
            signOut();
        });

//        subjectsList = new ArrayList<>();
//
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                subjectsList.clear();
//                // looping through the data
//                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
////                    String subject = itemSnapshot.child("name").getValue().toString();
//                    Subject s = itemSnapshot.getValue(Subject.class);
//                    subjectsList.add(s);
//                }
////                subjectsAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fabStudent1);
        fab.setOnClickListener(view -> {
            Dialog dialog = new Dialog(UserStudentActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.new_subject_dialog);

            EditText subjectName = dialog.findViewById(R.id.subjectNameInp);
            Button submit = dialog.findViewById(R.id.submitSubjectBtn);

            submit.setOnClickListener(v -> {
                String newSubjectKey = ref.push().getKey();
                ref.child(newSubjectKey).setValue(new Subject(newSubjectKey, subjectName.getText().toString()));
                dialog.dismiss();
                //                Snackbar.make(view, "New subject successfully added", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
                Toast.makeText(UserStudentActivity.this, "New subject successfully added", Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        });

//        setAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

//    public void setAdapter() {
//        subjectsAdapter = new SubjectsAdapter(getApplicationContext(), subjectsList);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        subjectsRecyclerView.setLayoutManager(layoutManager);
//        subjectsRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        subjectsRecyclerView.setAdapter(subjectsAdapter);
//    }


    public void signOut() {
        mAuth.signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}