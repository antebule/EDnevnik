package ba.sum.fpmoz.abule.pma;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectViewHolder extends RecyclerView.ViewHolder{
    TextView subjectName;

    public SubjectViewHolder(@NonNull View itemView) {
        super(itemView);
        subjectName = itemView.findViewById(R.id.subjectNameTxt);
    }
}
