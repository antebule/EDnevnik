package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import ba.sum.fpmoz.abule.pma.model.Subject;

public class FirebaseSubjectsAdapter extends FirebaseRecyclerAdapter<Subject, FirebaseSubjectsAdapter.SubjectViewHolder> {

    public FirebaseSubjectsAdapter(@NonNull FirebaseRecyclerOptions<Subject> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectViewHolder holder, int position, @NonNull Subject model) {
        holder.subjectName.setText(model.name);
        holder.subjectUid.setText(model.uid);
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectViewHolder(v);
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder{
        TextView subjectName;
        TextView subjectUid;
        FloatingActionButton delete;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectNameTxt);
            subjectUid = itemView.findViewById(R.id.subjectUid);
            delete = itemView.findViewById(R.id.fab_delete_subject);

            delete.setOnClickListener(v -> {
                Dialog dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.delete_dialog);

                TextView title = dialog.findViewById(R.id.deleteTitile);
                TextView text = dialog.findViewById(R.id.deleteTxt);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                Button del = dialog.findViewById(R.id.deleteBtn);

                title.setText("Brisanje predmeta");
                text.setText("Želite li zaista obrisati predmet " + subjectName.getText() + "?");
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });

                del.setOnClickListener(v2 -> {
                    String reference = "ednevnik/korisnici/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/predmeti/" + subjectUid.getText();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                    ref.removeValue();
                    dialog.dismiss();
                    Toast.makeText(itemView.getContext(), "Predmet " + subjectName.getText() + " uspješno obrisan!", Toast.LENGTH_SHORT).show();
                });
                dialog.show();
            });
        }
    }
}
