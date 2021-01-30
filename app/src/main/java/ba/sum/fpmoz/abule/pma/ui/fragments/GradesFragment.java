package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.StudentViewActivity;
import ba.sum.fpmoz.abule.pma.model.Grade;
import ba.sum.fpmoz.abule.pma.ui.adapters.FirebaseGradesAdapter;

public class GradesFragment extends Fragment {
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth mAuth;

    private RecyclerView gradesRecyclerView;
    private FirebaseGradesAdapter adapter;
    private FirebaseRecyclerOptions<Grade> options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grades, container, false);
        this.gradesRecyclerView = v.findViewById(R.id.gradesRecyclerView);
        this.gradesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseDatabase.getInstance();
        String reference = "ednevnik/korisnici/" + mAuth.getCurrentUser().getUid() + "/razredi/" + ClassViewActivity.classUid + "/studenti/" + StudentViewActivity.studentUid + "/ocjene";
        this.ref = db.getReference(reference);

        options = new FirebaseRecyclerOptions.Builder<Grade>().setLifecycleOwner(GradesFragment.this).setQuery(this.ref, Grade.class).build();

        adapter = new FirebaseGradesAdapter(options);
        gradesRecyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
