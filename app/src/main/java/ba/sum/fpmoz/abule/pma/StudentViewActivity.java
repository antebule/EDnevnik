package ba.sum.fpmoz.abule.pma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import ba.sum.fpmoz.abule.pma.ui.adapters.TabbedAdapter;
import ba.sum.fpmoz.abule.pma.ui.fragments.GradesFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.NewGradeFragment;

public class StudentViewActivity extends AppCompatActivity {
    private TextView studentNameTxt;
    public static String studentUid;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabbedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        this.studentNameTxt = findViewById(R.id.studentViewNameTxt);
        this.studentNameTxt.setText(getIntent().getStringExtra("studentName") + " " + getIntent().getStringExtra("studentSurname"));
        this.studentUid = getIntent().getStringExtra("studentUid");

        this.tabLayout = findViewById(R.id.studentViewTabLayout);
        this.viewPager = findViewById(R.id.studentViewPager);

        this.adapter = new TabbedAdapter(getSupportFragmentManager());
        this.adapter.addFragment(new GradesFragment(), "Ocjene");
        this.adapter.addFragment(new NewGradeFragment(), "Nova ocjena");

        this.viewPager.setAdapter(this.adapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }
}