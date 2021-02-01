package ba.sum.fpmoz.abule.pma.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ba.sum.fpmoz.abule.pma.R;

public class ListUsersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userListView = inflater.inflate(R.layout.fragment_users_list, container, false);
        return userListView;
    }
}
