package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.StudentViewActivity;
import ba.sum.fpmoz.abule.pma.model.Student;

public class FirebaseStudentsAdapter extends FirebaseRecyclerAdapter<Student, FirebaseStudentsAdapter.StudentViewHolder> {

    public FirebaseStudentsAdapter(@NonNull FirebaseRecyclerOptions<Student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull Student model) {
        holder.studentUid.setText(model.uid);
        holder.studentName.setText(model.name);
        holder.studentSurname.setText(model.surname);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(v);
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView studentUid;
        TextView studentName;
        TextView studentSurname;
        FloatingActionButton delete;
        CardView cardView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentUid = itemView.findViewById(R.id.studentUid);
            studentName = itemView.findViewById(R.id.studentNameTxt);
            studentSurname = itemView.findViewById(R.id.studentSurnameTxt);
            cardView = itemView.findViewById(R.id.studentCardView);
            delete = itemView.findViewById(R.id.fab_delete);

            delete.setOnClickListener(v -> {
                Dialog dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.delete_dialog);

                TextView title = dialog.findViewById(R.id.deleteTitile);
                TextView text = dialog.findViewById(R.id.deleteTxt);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                Button del = dialog.findViewById(R.id.deleteBtn);

                title.setText("Brisanje studenta");
                text.setText("Želite li zaista obrisati studenta " + studentName.getText() + studentSurname.getText() + "?");
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });

                del.setOnClickListener(v2 -> {
                    String reference = "ednevnik/korisnici/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/studenti/" + studentUid.getText();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                    ref.removeValue();
                    dialog.dismiss();
                    Toast.makeText(itemView.getContext(), "Student " + studentName.getText() + " uspješno obrisan!", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "Student " + studentName.getText() + " uspješno obrisan!", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
                });
                dialog.show();
            });

            cardView.setOnClickListener(v -> {
                Intent i = new Intent(itemView.getContext(), StudentViewActivity.class);
                i.putExtra("studentName", studentName.getText().toString());
                i.putExtra("studentSurname", studentSurname.getText().toString());
                i.putExtra("studentUid", studentUid.getText().toString());
                itemView.getContext().startActivity(i);
            });
        }
    }
}
