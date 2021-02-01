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
import ba.sum.fpmoz.abule.pma.model.Class;

public class FirebaseClassesAdapter extends FirebaseRecyclerAdapter<Class, FirebaseClassesAdapter.ClassViewHolder> {

    public FirebaseClassesAdapter(@NonNull FirebaseRecyclerOptions<Class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull Class model) {
        holder.className.setText(model.name);
        holder.classUid.setText(model.uid);
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassViewHolder(v);
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder{
        TextView className;
        TextView classUid;
        CardView classCardView;
        FloatingActionButton delete;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.classNameTxt);
            classUid = itemView.findViewById(R.id.classUid);
            classCardView = itemView.findViewById(R.id.classCardView);
            delete = itemView.findViewById(R.id.fab_delete_class);

            delete.setOnClickListener(v -> {
                Dialog dialog = new Dialog(itemView.getContext());
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
                    String reference = "ednevnik/korisnici/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/razredi/"  + classUid.getText();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                    ref.removeValue();
                    dialog.dismiss();
                    Toast.makeText(itemView.getContext(), "Razred " + className.getText() + " uspješno obrisan!", Toast.LENGTH_SHORT).show();
                });
                dialog.show();
            });

            classCardView.setOnClickListener(v -> {
                Intent i = new Intent(itemView.getContext(), ClassViewActivity.class);
                i.putExtra("selectedClass", className.getText().toString());
                i.putExtra("classUid", classUid.getText());
                itemView.getContext().startActivity(i);
            });
        }
    }
}
