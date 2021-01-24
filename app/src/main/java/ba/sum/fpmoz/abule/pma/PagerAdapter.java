package ba.sum.fpmoz.abule.pma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ba.sum.fpmoz.abule.pma.ui.fragments.StudentsFragment;
import ba.sum.fpmoz.abule.pma.ui.fragments.SubjectsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private String [] tabTitles = new String[]{"Students", "Subjects"};

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new StudentsFragment();
            case 1:
                return new SubjectsFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
