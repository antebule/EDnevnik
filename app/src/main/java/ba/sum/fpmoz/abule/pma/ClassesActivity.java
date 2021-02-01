package ba.sum.fpmoz.abule.pma;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ba.sum.fpmoz.abule.pma.model.Class;
import ba.sum.fpmoz.abule.pma.ui.adapters.FirebaseClassesAdapter;

public class ClassesActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseUser user;
    FirebaseAuth mAuth;

    private Button signOutBtn;
    private RecyclerView classesRecyclerView;
    private FirebaseClassesAdapter adapter;
    private FirebaseRecyclerOptions<Class> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        setTitle("Razredi");

        this.mAuth = FirebaseAuth.getInstance();
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

        this.classesRecyclerView.setLayoutManager(new LinearLayoutManager(ClassesActivity.this));
        options = new FirebaseRecyclerOptions.Builder<Class>().setLifecycleOwner(ClassesActivity.this).setQuery(this.ref, Class.class).build();
        adapter = new FirebaseClassesAdapter(options);
        classesRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Dialog dialog = new Dialog(view.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.new_class);

            EditText className = dialog.findViewById(R.id.studentNameInp1);
            Button submit = dialog.findViewById(R.id.submitStudentBtn);

            submit.setOnClickListener(v -> {
                String newClassKey = ref.push().getKey();
                ref.child(newClassKey).setValue(new Class(newClassKey, className.getText().toString()));
                dialog.dismiss();
                Toast.makeText(ClassesActivity.this, "Novi razred uspješno dodan!", Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        });
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

    public void signOut() {
        mAuth.signOut();
        Intent i = new Intent(ClassesActivity.this, MainActivity.class);
        startActivity(i);
    }
}