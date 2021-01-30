package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.StudentViewActivity;
import ba.sum.fpmoz.abule.pma.model.Student;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {
    private ArrayList<Student> studentsList;
    private Context context;
    private String uid;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public StudentsAdapter(Context context, ArrayList<Student> classesList){
        this.context = context;
        this.studentsList = classesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView studentName;
        private TextView studentSurame;
        private TextView studentUid;
        private FloatingActionButton delete;
        private CardView studentCardView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
//            context = itemView.getContext();
            studentName = itemView.findViewById(R.id.studentNameTxt);
            studentSurame = itemView.findViewById(R.id.studentSurnameTxt);
            studentUid = itemView.findViewById(R.id.studentUid);
            studentCardView = itemView.findViewById(R.id.studentCardView);

            delete = itemView.findViewById(R.id.fab_delete);

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
                text.setText("Želite li zaista obrisati studenta " + studentName.getText() + studentSurame.getText() + "?");
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });

                del.setOnClickListener(v2 -> {
                    String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/studenti/" + studentUid.getText();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                    ref.removeValue();
                    dialog.dismiss();
                    Snackbar.make(v, "New class successfully added", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
                });
                dialog.show();
            });

            studentCardView.setOnClickListener(v -> {
                Intent i = new Intent(context, StudentViewActivity.class);
                i.putExtra("studentName", studentName.getText().toString());
                i.putExtra("studentSurname", studentSurame.getText().toString());
                i.putExtra("studentUid", studentUid.getText().toString());
                context.startActivity(i);
            });
        }
    }

    @NonNull
    @Override
    public StudentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull StudentsAdapter.MyViewHolder holder, int position) {
        String studentName = studentsList.get(position).name;
        String studentSurname = studentsList.get(position).surname;
        String studentUid = studentsList.get(position).uid;

        holder.studentName.setText(studentName);
        holder.studentSurame.setText(studentSurname);
        holder.studentUid.setText(studentUid);
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }
}
