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
import ba.sum.fpmoz.abule.pma.model.Grade;

public class FirebaseGradesAdapter extends FirebaseRecyclerAdapter<Grade, FirebaseGradesAdapter.GradeViewHolder> {

    public FirebaseGradesAdapter(@NonNull FirebaseRecyclerOptions<Grade> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GradeViewHolder holder, int position, @NonNull Grade model) {
        holder.gradeDesc.setText(model.description);
        holder.gradeDate.setText(model.date);
        holder.gradeValue.setText(model.value);
        holder.gradeSubject.setText(model.subjectName);
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new GradeViewHolder(v);
    }

    public class GradeViewHolder extends RecyclerView.ViewHolder{
        TextView gradeDesc;
        TextView gradeDate;
        TextView gradeValue;
        TextView gradeSubject;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            gradeDesc = itemView.findViewById(R.id.gradeDescTxt);
            gradeDate = itemView.findViewById(R.id.gradeDate);
            gradeValue = itemView.findViewById(R.id.gradeValueTxt);
            gradeSubject = itemView.findViewById(R.id.gradeSubject);
        }
    }
}
