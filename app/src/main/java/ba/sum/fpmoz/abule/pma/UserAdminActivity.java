package ba.sum.fpmoz.abule.pma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.model.Student;

public class UserAdminActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;

    EditText studentNameInp;
    EditText studentSurnameInp;
    EditText studentUidInp;
    Button addStudentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        this.studentNameInp = findViewById(R.id.studentNameInp);
        this.studentSurnameInp = findViewById(R.id.studentSurnameInp);
        this.studentUidInp = findViewById(R.id.studentUidInp);
        this.addStudentBtn = findViewById(R.id.addStudentBtn);

        this.db = FirebaseDatabase.getInstance();
        this.ref = db.getReference("ednevnik/ucenici");

        addStudentBtn.setOnClickListener(v -> {
            String studentName = studentNameInp.getText().toString();
            String studentSurname = studentSurnameInp.getText().toString();
            String studentUid = studentUidInp.getText().toString();

            ref.push().setValue(new Student(studentUid, studentName, studentSurname));

            Toast.makeText(getApplicationContext(), "Uspješno ste dodali učenika", Toast.LENGTH_SHORT).show();

            studentNameInp.setText("");
            studentSurnameInp.setText("");
            studentUidInp.setText("");
        });

        // dohvacanje podataka
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //do something with data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}