package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Grade;
import ba.sum.fpmoz.abule.pma.model.Subject;

public class FirebaseGradesAdapter extends FirebaseRecyclerAdapter<Grade, FirebaseGradesAdapter.GradeViewHolder> {

    public FirebaseGradesAdapter(@NonNull FirebaseRecyclerOptions<Grade> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GradeViewHolder holder, int position, @NonNull Grade model) {
        holder.gradeDesc.setText(model.description);
        holder.gradeDate.setText(model.date);
        holder.gradeValue.setText(model.value);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ednevnik/korisnici/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/predmeti/" + model.subjectID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String subjectName = snapshot.getValue(Subject.class).name;
                holder.gradeSubject.setText(subjectName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
