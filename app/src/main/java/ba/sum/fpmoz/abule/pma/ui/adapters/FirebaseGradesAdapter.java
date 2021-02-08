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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Grade;
import ba.sum.fpmoz.abule.pma.model.User;

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
        holder.studentUid.setText(model.studentID);
        holder.gradeUid.setText(model.uid);
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
        TextView studentUid;
        TextView gradeUid;
        Button deleteGrade;
//        Button editGrade;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            gradeDesc = itemView.findViewById(R.id.gradeDescTxt);
            gradeDate = itemView.findViewById(R.id.gradeDate);
            gradeValue = itemView.findViewById(R.id.gradeValueTxt);
            gradeSubject = itemView.findViewById(R.id.gradeSubject);
            studentUid = itemView.findViewById(R.id.gradeStudentUid);
            gradeUid = itemView.findViewById(R.id.gradeUid);
            deleteGrade = itemView.findViewById(R.id.deleteGradeBtn);
//            editGrade = itemView.findViewById(R.id.editGradeBtn);

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("ednevnik/korisnici");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()){
                        User user = userSnapshot.getValue(User.class);
//                        System.out.println("userUid: " + user.uid);
//                        System.out.println("logUser: " + userId);
                        if(user.uid.equals(userId)){
                            if(user.role.equals("teacher")){
//                                editGrade.setVisibility(View.VISIBLE);
                                deleteGrade.setVisibility(View.VISIBLE);
//                                editGrade.setOnClickListener(v1 -> {
//                                    System.out.println("Edit clicked!");
//                                });

                                deleteGrade.setOnClickListener(v2 -> {
                                    Dialog dialog = new Dialog(itemView.getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setContentView(R.layout.delete_dialog);

                                    TextView title = dialog.findViewById(R.id.deleteTitile);
                                    TextView text = dialog.findViewById(R.id.deleteTxt);
                                    Button cancel = dialog.findViewById(R.id.cancelBtn);
                                    Button del = dialog.findViewById(R.id.deleteBtn);

                                    title.setText("Brisanje ocjene");
                                    text.setText("Želite li zaista obrisati ocjenu? ");
                                    cancel.setOnClickListener(v3 -> {
                                        dialog.dismiss();
                                    });

                                    del.setOnClickListener(v4 -> {
                                        String reference = "ednevnik/korisnici/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/studenti/" + studentUid.getText().toString() + "/ocjene/" + gradeUid.getText().toString();
                                        String studentRef = "ednevnik/korisnici/" + studentUid.getText().toString() + "/ocjene/" + gradeUid.getText().toString();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(studentRef);
                                        ref.removeValue();
                                        ref2.removeValue();
                                        dialog.dismiss();
                                        Toast.makeText(itemView.getContext(), "Ocjena uspješno obrisana!", Toast.LENGTH_SHORT).show();
                                    });
                                    dialog.show();
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
