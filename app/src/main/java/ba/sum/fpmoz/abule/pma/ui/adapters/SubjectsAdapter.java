package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Subject;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {
    private ArrayList<Subject> subjectsList;
    private Context context;

    public SubjectsAdapter(Context context, ArrayList<Subject> subjectList){
        this.context = context;
        this.subjectsList = subjectList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView subjectName;
//        private CardView studentCardView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
//            context = itemView.getContext();
            subjectName = itemView.findViewById(R.id.subjectNameTxt);

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

        holder.subjectName.setText(subjectName);
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }
}
