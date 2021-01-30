package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Subject;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {
    private ArrayList<Subject> subjectsList;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public SubjectsAdapter(Context context, ArrayList<Subject> subjectList){
        this.context = context;
        this.subjectsList = subjectList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView subjectName;
        private TextView subjectUid;
        private FloatingActionButton delete;
//        private CardView studentCardView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
//            context = itemView.getContext();
            subjectName = itemView.findViewById(R.id.subjectNameTxt);
            subjectUid = itemView.findViewById(R.id.subjectUid);
            delete = itemView.findViewById(R.id.fab_delete_subject);

            delete.setOnClickListener(v -> {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.delete_dialog);

                TextView title = dialog.findViewById(R.id.deleteTitile);
                TextView text = dialog.findViewById(R.id.deleteTxt);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                Button del = dialog.findViewById(R.id.deleteBtn);

                title.setText("Brisanje studenta");
                text.setText("Želite li zaista obrisati predmet " + subjectName.getText() + "?");
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });

                del.setOnClickListener(v2 -> {
                    String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/predmeti/" + subjectUid.getText();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                    ref.removeValue();
                    dialog.dismiss();
                    Snackbar.make(v, "New class successfully added", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
                });
                dialog.show();
            });
            //TODO: implement on click listener for subjects
//            studentCardView = itemView.findViewById(R.id.studentCardView);
//
//            studentCardView.setOnClickListener(v -> {
//                Intent i = new Intent(context, StudentViewActivity.class);
//                i.putExtra("selectedStudent", studentName.getText().toString());
//                context.startActivity(i);
//            });
        }
    }

    @NonNull
    @Override
    public SubjectsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull SubjectsAdapter.MyViewHolder holder, int position) {
        String subjectName = subjectsList.get(position).name;
        String subjectUid = subjectsList.get(position).uid;

        holder.subjectUid.setText(subjectUid);
        holder.subjectName.setText(subjectName);
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }
}
