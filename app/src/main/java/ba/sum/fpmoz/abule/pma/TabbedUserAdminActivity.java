package ba.sum.fpmoz.abule.pma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import ba.sum.fpmoz.abule.pma.ui.adapters.TabbedAdapter;
import ba.sum.fpmoz.abule.pma.ui.fragments.AddUsersFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.ListUsersFragment;

public class TabbedUserAdminActivity extends AppCompatActivity {
    TabbedAdapter adapter;
    TabLayout layout;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_user_admin);

        this.layout = findViewById(R.id.tabLayout);
        this.pager = findViewById(R.id.viewPager);

        this.adapter = new TabbedAdapter(getSupportFragmentManager());
        this.adapter.addFragment(new ListUsersFragment(), "Prikaz svih učenika");
        this.adapter.addFragment(new AddUsersFragment(), "Dodavanje novih učenika");

        this.pager.setAdapter(this.adapter);
        this.layout.setupWithViewPager(this.pager);
    }
}