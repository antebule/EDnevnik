package ba.sum.fpmoz.abule.pma.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Class;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {
    private ArrayList<Class> classesList;
    private Context context;

    public ClassesAdapter(ArrayList<Class> classesList){
        this.classesList = classesList;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView className;
        private CardView classCardView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            context = itemView.getContext();
            className = itemView.findViewById(R.id.classNameTxt);
            classCardView = itemView.findViewById(R.id.classCardView);

            classCardView.setOnClickListener(v -> {
                Intent i = new Intent(context, ClassViewActivity.class);
                i.putExtra("selectedClass", className.getText().toString());
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
        holder.className.setText(className);
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }
}
