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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.abule.pma.ClassViewActivity;
import ba.sum.fpmoz.abule.pma.R;
import ba.sum.fpmoz.abule.pma.model.Subject;
import ba.sum.fpmoz.abule.pma.model.User;

public class FirebaseUsersAdapter extends FirebaseRecyclerAdapter<User, FirebaseUsersAdapter.UserViewHolder> {

    public FirebaseUsersAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        holder.userEmail.setText(model.email);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView userEmail;
        FloatingActionButton delete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.userEmailTxt);
            delete = itemView.findViewById(R.id.fab_delete_user);

            delete.setOnClickListener(v -> {
                Dialog dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.delete_dialog);

                TextView title = dialog.findViewById(R.id.deleteTitile);
                TextView text = dialog.findViewById(R.id.deleteTxt);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                Button del = dialog.findViewById(R.id.deleteBtn);

                title.setText("Brisanje korisnika");
                text.setText("Želite li zaista obrisati korisnika " + userEmail.getText() + "?");
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });

                del.setOnClickListener(v2 -> {
                    String reference = "ednevnik/korisnici";
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userKey = "";
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                                User u = itemSnapshot.getValue(User.class);
                                if(u.email.equals(userEmail.getText())){
                                    userKey = itemSnapshot.getKey();
                                }
                            }
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(reference + "/" + userKey);
                            userRef.removeValue();
                            dialog.dismiss();
                            Toast.makeText(itemView.getContext(), "Korisnik " + userEmail.getText() + " uspješno obrisan!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                });
                dialog.show();
            });
        }
    }
}
