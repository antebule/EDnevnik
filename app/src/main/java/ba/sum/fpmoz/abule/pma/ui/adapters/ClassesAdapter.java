package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import ba.sum.fpmoz.abule.pma.ClassesActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Class;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {
    private ArrayList<Class> classesList;
    private Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ClassesAdapter(ArrayList<Class> classesList){
        this.classesList = classesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView className;
        private TextView classUid;
        private CardView classCardView;
        private FloatingActionButton delete;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            context = itemView.getContext();
            className = itemView.findViewById(R.id.classNameTxt);
            classUid = itemView.findViewById(R.id.classUid);
            classCardView = itemView.findViewById(R.id.classCardView);
            delete = itemView.findViewById(R.id.fab_delete_class);

            delete.setOnClickListener(v -> {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.delete_dialog);

                TextView title = dialog.findViewById(R.id.deleteTitile);
                TextView text = dialog.findViewById(R.id.deleteTxt);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                Button del = dialog.findViewById(R.id.deleteBtn);

                title.setText("Brisanje razreda");
                text.setText("Želite li zaista obrisati razred " + className.getText() + "?");
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });

                del.setOnClickListener(v2 -> {
                    String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/"  + classUid.getText();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                    ref.removeValue();
                    dialog.dismiss();
                    Snackbar.make(v, "New class successfully added", Snackbar.LENGTH_SHORT).setAction("OK", null).show();
                });
                dialog.show();
            });

            classCardView.setOnClickListener(v -> {
                Intent i = new Intent(context, ClassViewActivity.class);
                i.putExtra("selectedClass", className.getText().toString());
                i.putExtra("classUid", classUid.getText());
                context.startActivity(i);
            });
        }
    }

    @NonNull
    @Override
    public ClassesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ClassesAdapter.MyViewHolder holder, int position) {
        String className = classesList.get(position).name;
        String classUid = classesList.get(position).uid;
        holder.className.setText(className);
        holder.classUid.setText(classUid);
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }
}
