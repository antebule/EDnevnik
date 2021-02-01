package ba.sum.fpmoz.abule.pma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import ba.sum.fpmoz.abule.pma.ui.adapters.TabbedAdapter;
import ba.sum.fpmoz.abule.pma.ui.fragments.StudentsFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.SubjectsFragment;

public class ClassViewActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabbedAdapter adapter;
    public  static String selectedClass;
    public static String classUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_view);

        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("selectedClass");
        classUid = intent.getStringExtra("classUid");
        setTitle("Razred " + selectedClass);

        this.tabLayout = findViewById(R.id.tabLayout);
        this.viewPager = findViewById(R.id.viewPager);

        this.adapter = new TabbedAdapter(getSupportFragmentManager());
        this.adapter.addFragment(new StudentsFragment(), "Studenti");
        this.adapter.addFragment(new SubjectsFragment(), "Predmeti");

        this.viewPager.setAdapter(this.adapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }
}