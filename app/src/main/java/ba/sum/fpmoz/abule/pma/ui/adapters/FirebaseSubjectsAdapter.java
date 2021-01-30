package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Subject;

public class FirebaseSubjectsAdapter extends FirebaseRecyclerAdapter<Subject, FirebaseSubjectsAdapter.SubjectViewHolder> {

    public FirebaseSubjectsAdapter(@NonNull FirebaseRecyclerOptions<Subject> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectViewHolder holder, int position, @NonNull Subject model) {
        holder.subjectName.setText(model.name);
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectViewHolder(v);
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder{
        TextView subjectName;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectNameTxt);
        }
    }
}
