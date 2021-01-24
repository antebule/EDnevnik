package ba.sum.fpmoz.abule.pma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import ba.sum.fpmoz.abule.pma.ui.adapters.TabbedAdapter;
import ba.sum.fpmoz.abule.pma.ui.fragments.LoginUserFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.RegisterUserFragment;

public class MainActivity extends AppCompatActivity {
    TabbedAdapter adapter;
    TabLayout layout;
    ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.layout = findViewById(R.id.tabLayoutMain);
        this.pager = findViewById(R.id.viewPagerMain);

        this.adapter = new TabbedAdapter(getSupportFragmentManager());
        this.adapter.addFragment(
                new LoginUserFragment(), "Prijavite se na sustav"
        );

        this.adapter.addFragment(
                new RegisterUserFragment(), "Registracija na sustav"
        );

        this.pager.setAdapter(this.adapter);
        this.layout.setupWithViewPager(this.pager);

    }

}