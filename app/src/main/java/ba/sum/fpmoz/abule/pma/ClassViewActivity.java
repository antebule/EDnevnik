package ba.sum.fpmoz.abule.pma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ba.sum.fpmoz.abule.pma.model.Class;
import ba.sum.fpmoz.abule.pma.model.Student;
import ba.sum.fpmoz.abule.pma.ui.adapters.TabbedAdapter;
import ba.sum.fpmoz.abule.pma.ui.fragments.AddUsersFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.ListUsersFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.StudentsFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.SubjectsFragment;

public class ClassViewActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
//    private TabItem tabStudents;
//    private TabItem tabSubjects;
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
        Toast.makeText(ClassViewActivity.this, selectedClass + " selected", Toast.LENGTH_SHORT).show();

        this.tabLayout = findViewById(R.id.tabLayout);
//        this.tabStudents = findViewById(R.id.tabStudents);
//        this.tabSubjects = findViewById(R.id.tabSubjects);
        this.viewPager = findViewById(R.id.viewPager);

        this.adapter = new TabbedAdapter(getSupportFragmentManager());
        this.adapter.addFragment(new StudentsFragment(), "Studenti");
        this.adapter.addFragment(new SubjectsFragment(), "Predmeti");

        this.viewPager.setAdapter(this.adapter);
        this.tabLayout.setupWithViewPager(this.viewPager);

//        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        // solved swiping problems
//        tabLayout.setupWithViewPager(viewPager);
    }
}