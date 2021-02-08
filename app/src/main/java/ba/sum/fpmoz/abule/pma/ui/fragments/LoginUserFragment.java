package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.ClassesActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.UserAdminActivity;
import ba.sum.fpmoz.abule.pma.UserStudentActivity;
import ba.sum.fpmoz.abule.pma.model.User;

public class LoginUserFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TextView messageTxt;
    private EditText emailInp;
    private EditText passwordInp;
    private Button loginBtn;

    public LoginUserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View loginView = inflater.inflate(R.layout.fragment_login_user, container, false);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            login();
        }
        this.messageTxt = loginView.findViewById(R.id.messageTxt);
        this.emailInp = loginView.findViewById(R.id.emailInp);
        this.passwordInp = loginView.findViewById(R.id.passwordInp);
        this.loginBtn = loginView.findViewById(R.id.loginBtn);

        this.loginBtn.setOnClickListener(v -> {
            String email = emailInp.getText().toString();
            String password = passwordInp.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                errorMsg();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            messageTxt.setText("Uspješno ste se prijavili na sustav.");
                            messageTxt.setTextColor(Color.GREEN);
                            login();
                        } else {
                            errorMsg();
                        }
                    });
            }
        });
        return loginView;
    }

    private void login() {
        String userMail = mAuth.getCurrentUser().getEmail();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("ednevnik/korisnici");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                    User u  = itemSnapshot.getValue(User.class);
                    if(u.email.equals(userMail)){
                        switch (u.role) {
                            case "teacher":
                                startActivity(new Intent(getContext(), ClassesActivity.class));
                                getActivity().finishAffinity();
                                break;
                            case "admin":
                                startActivity(new Intent(getContext(), UserAdminActivity.class));
                                getActivity().finishAffinity();
                                break;
                            default:
                                Intent i = new Intent(getContext(), UserStudentActivity.class);
                                i.putExtra("studentKey", itemSnapshot.getKey());
                                startActivity(i);
                                getActivity().finishAffinity();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void errorMsg() {
        messageTxt.setText("Nastao je problem s prijavom.");
    }
}