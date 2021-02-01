package ba.sum.fpmoz.abule.pma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.model.Grade;
import ba.sum.fpmoz.abule.pma.ui.adapters.FirebaseGradesAdapter;

public class UserStudentActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseUser user;
    FirebaseAuth mAuth;

    private Button signOutBtn;
    private RecyclerView gradesRecyclerView;
    private FirebaseGradesAdapter adapter;
    private FirebaseRecyclerOptions<Grade> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_student);

        this.mAuth = FirebaseAuth.getInstance();
        // TODO: solve google sign-in user problem
        if(mAuth.getCurrentUser() == null){
            Toast.makeText(this, "No email", Toast.LENGTH_SHORT).show();
        }else {
            this.user = mAuth.getCurrentUser();
            Toast.makeText(this, this.user.getEmail(), Toast.LENGTH_SHORT).show();
        }

        this.db = FirebaseDatabase.getInstance();
//        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/ocjene";
        System.out.println("studentKey: " + getIntent().getStringExtra("studentKey"));
        String reference = "ednevnik/korisnici/" + getIntent().getStringExtra("studentKey") + "/ocjene";
        this.ref = db.getReference(reference);
        this.gradesRecyclerView = findViewById(R.id.gradesRecyclerView1);
        this.gradesRecyclerView.setLayoutManager(new LinearLayoutManager(UserStudentActivity.this));
        options = new FirebaseRecyclerOptions.Builder<Grade>().setLifecycleOwner(UserStudentActivity.this).setQuery(this.ref, Grade.class).build();

        adapter = new FirebaseGradesAdapter(options);
        this.gradesRecyclerView.setAdapter(adapter);

        this.signOutBtn = findViewById(R.id.signOutBtn1);

        this.signOutBtn.setOnClickListener(v -> {
            signOut();
        });
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

    public void signOut() {
        mAuth.signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}