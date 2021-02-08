package ba.sum.fpmoz.abule.pma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.model.User;
import ba.sum.fpmoz.abule.pma.ui.adapters.FirebaseUsersAdapter;

public class UserAdminActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private RecyclerView usersRecyclerView;
    private Button signOutBtn;
    private FirebaseUsersAdapter adapter;
    private FirebaseRecyclerOptions<User> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        this.mAuth = FirebaseAuth.getInstance();
        // TODO: solve google sign-in user problem
        if(mAuth.getCurrentUser() == null){
            Toast.makeText(this, "No email", Toast.LENGTH_SHORT).show();
        }else {
            this.user = mAuth.getCurrentUser();
            Toast.makeText(this, this.user.getEmail(), Toast.LENGTH_SHORT).show();
        }

        this.db = FirebaseDatabase.getInstance();
        String reference = "ednevnik/korisnici";
        this.ref = db.getReference(reference);
        this.usersRecyclerView = findViewById(R.id.usersRecyclerView);
        this.usersRecyclerView.setLayoutManager(new LinearLayoutManager(UserAdminActivity.this));
        options = new FirebaseRecyclerOptions.Builder<User>().setLifecycleOwner(UserAdminActivity.this).setQuery(this.ref, User.class).build();

        adapter = new FirebaseUsersAdapter(options);
        usersRecyclerView.setAdapter(adapter);

        this.signOutBtn = findViewById(R.id.signOutBtnAdmin);

        this.signOutBtn.setOnClickListener(v -> {
            signOut();
        });

        FloatingActionButton fab = findViewById(R.id.fabAddUser);
        fab.setOnClickListener(view -> {
            Dialog dialog = new Dialog(UserAdminActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.add_users_dialog);

            TextView errorMsg = dialog.findViewById(R.id.errorAdminMsgTxt);
            EditText userEmail = dialog.findViewById(R.id.teacherEmailInp);
            Button submit = dialog.findViewById(R.id.addTeacherBtn);

            submit.setOnClickListener(v -> {
                String email = userEmail.getText().toString();
                if(TextUtils.isEmpty(email)){
                    errorMsg.setText("Nepotpuni podaci!");
                } else {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean exists = false;
                            for (DataSnapshot userSnapshot : snapshot.getChildren()){
                                User user = userSnapshot.getValue(User.class);
                                if(user.email.equals(email)){
                                    exists = true;
                                }
                            }

                            if(exists){
                                errorMsg.setText("Korisnik s istim emailom već postoji u bazi!");
                            } else {
                                String newUserKey = ref.push().getKey();
                                String email = userEmail.getText().toString();
                                ref.child(newUserKey).setValue(new User(newUserKey, email, "teacher"));
                                dialog.dismiss();
                                Toast.makeText(UserAdminActivity.this, "Novi profesor uspješno dodan", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
//                String newUserKey = ref.push().getKey();
//                String email = userEmail.getText().toString();
//                ref.child(newUserKey).setValue(new User(newUserKey, email, "teacher"));
//                dialog.dismiss();
//                Toast.makeText(UserAdminActivity.this, "New user successfully added", Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        });
    }

    public void signOut() {
        mAuth.signOut();
        Intent i = new Intent(UserAdminActivity.this, MainActivity.class);
        startActivity(i);
    }
}