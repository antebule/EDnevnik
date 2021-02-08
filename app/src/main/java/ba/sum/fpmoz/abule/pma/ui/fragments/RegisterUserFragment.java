package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.User;

public class RegisterUserFragment extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;

    private TextView messageTxt;
    private EditText displayNameInp;
    private EditText emailInp;
    private EditText passwordInp;
    private EditText passwordCnfInp;
    private Button registerBtn;

    public RegisterUserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View registerView = inflater.inflate(R.layout.fragment_register_user, container, false);
        mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseDatabase.getInstance();
        this.ref = db.getReference("ednevnik/korisnici");

        this.messageTxt = registerView.findViewById(R.id.registerMsg);
        this.displayNameInp = registerView.findViewById(R.id.displayNameInp);
        this.emailInp = registerView.findViewById(R.id.emialInp1);
        this.passwordInp = registerView.findViewById(R.id.passwordInp1);
        this.passwordCnfInp = registerView.findViewById(R.id.passwordCnfInp);
        this.registerBtn = registerView.findViewById(R.id.registerBtn1);

        this.registerBtn.setOnClickListener(v -> {
            final String displayName = displayNameInp.getText().toString();
            String email = emailInp.getText().toString();
            String password = passwordInp.getText().toString();
            String passwordCnf = passwordCnfInp.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordCnf)){
                messageTxt.setText("Nepotpuni podaci");
            } else if (!password.equals(passwordCnf)){
                messageTxt.setText("Lozinke se ne podudaraju");
            } else {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean exists = false;
                    String uid = "";
                    String userRole = "";
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                            User u = itemSnapshot.getValue(User.class);
                            if(u.email.equals(email)){
                                uid = u.uid;
                                userRole = u.role;
                                exists = true;
                            }
                        }
                        if(exists){
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest
                                            .Builder()
                                            .setDisplayName(displayName)
                                            .build();
                                    user.updateProfile(changeRequest).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()){
                                            emailInp.setText("");
                                            passwordInp.setText("");
                                            passwordCnfInp.setText("");
                                            messageTxt.setText("Vaš korisnički račun je uspješno napravljen, možete se logirati na sustav.");
                                            messageTxt.setTextColor(Color.GREEN);
                                            displayNameInp.setText("");

                                            if(userRole.equals("student")){
                                                ref.child(uid).child("uid").setValue(user.getUid());
                                            } else {
                                                User newUser = new User(user.getUid(), user.getEmail(), userRole);
                                                ref.child(user.getUid()).setValue(newUser);
                                                ref.child(uid).removeValue();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Nastala je greška s registracijom na sustav: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            messageTxt.setText("Ne postoji korisnik s tim email-om u bazi, obratite se profesoru ili administratoru da napravi Vaš korisnički račun");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return registerView;
    }
}