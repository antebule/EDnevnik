package ba.sum.fpmoz.abule.pma;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.model.Class;
import ba.sum.fpmoz.abule.pma.ui.adapters.ClassesAdapter;

public class ClassesActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private ArrayList<Class> classesList;
    private RecyclerView classesRecyclerView;
    private Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

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
        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi";
        this.ref = db.getReference(reference);
        this.classesRecyclerView = findViewById(R.id.classesRecyclerView);
        this.signOutBtn = findViewById(R.id.signOutBtn);

        this.signOutBtn.setOnClickListener(v -> {
            signOut();
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classesList = new ArrayList<>();
                // looping through the data
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
//                    String razred = itemSnapshot.child("name").getValue().toString();
                    Class c = itemSnapshot.getValue(Class.class);
                    classesList.add(c);
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Dialog dialog = new Dialog(ClassesActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.new_class);

            EditText className = dialog.findViewById(R.id.studentNameInp1);
            Button submit = dialog.findViewById(R.id.submitStudentBtn);

            submit.setOnClickListener(v -> {
                String newClassKey = ref.push().getKey();
                ref.child(newClassKey).setValue(new Class(newClassKey, className.getText().toString()));
                dialog.dismiss();
                Snackbar.make(view, "New class successfully added", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
            });
            dialog.show();
        });
    }

    public void setAdapter() {
        ClassesAdapter classesAdapter = new ClassesAdapter(classesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        classesRecyclerView.setLayoutManager(layoutManager);
        classesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        classesRecyclerView.setAdapter(classesAdapter);
    }

    public void signOut() {
        mAuth.signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}