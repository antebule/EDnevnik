package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private TextView messageTxt;
    private EditText emailInp;
    private EditText passwordInp;
    private Button loginBtn;
    private SignInButton signInButton;

    public LoginUserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View loginView = inflater.inflate(R.layout.fragment_login_user, container, false);
        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //626740173712-l49cg3pjtrkqotea6rh3emeej9k6tobc.apps.googleusercontent.com
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // TODO: finish checking for last signed in user and update ui accordingly
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());



        // Set the dimensions of the sign-in button.
        this.signInButton = loginView.findViewById(R.id.sign_in_button);
        this.signInButton.setSize(SignInButton.SIZE_STANDARD);

        this.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }
        });



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
                            login();
                        } else {
                            errorMsg();
                        }
                    });
            }
        });
        return loginView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("signIn", "Signing in...");
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, TODO: show authenticated UI.
//            updateUI(account);
            login();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signIn Error", "signInResult:failed code=" + e.getStatusCode());
            errorMsg();
        }
    }

    private void login() {
        messageTxt.setText("Uspješno ste se prijavili na sustav.");
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
                                break;
                            case "admin":
                                startActivity(new Intent(getContext(), UserAdminActivity.class));
                                break;
                            default:
                                Intent i = new Intent(getContext(), UserStudentActivity.class);
                                i.putExtra("studentKey", itemSnapshot.getKey());
                                startActivity(i);
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