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
import ba.sum.fpmoz.abule.pma.model.Student;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {
    private ArrayList<Student> studentsList;
    private Context context;

    public StudentsAdapter(Context context, ArrayList<Student> classesList){
        this.context = context;
        this.studentsList = classesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView studentName;
        private TextView studentSurame;
//        private CardView studentCardView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
//            context = itemView.getContext();
            studentName = itemView.findViewById(R.id.studentNameTxt);
            studentSurame = itemView.findViewById(R.id.studentSurnameTxt);

            //TODO: implement on click listener for students
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
    public StudentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull StudentsAdapter.MyViewHolder holder, int position) {
        String studentName = studentsList.get(position).name;
        String studentSurname = studentsList.get(position).surname;

        holder.studentName.setText(studentName);
        holder.studentSurame.setText(studentSurname);
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }
}
