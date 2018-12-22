package com.tamirdunyasi.tamircimcebimdedemo;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {

    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
    private BottomNavigationView mBottomNavigationView;
    private Menu mBottomMenu;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.main_activity_label);

        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());
        mBottomNavigationView = findViewById(R.id.register_navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mBottomMenu = mBottomNavigationView.getMenu();

        mViewPager = findViewById(R.id.registerContainer);
        setUpViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    private void setUpViewPager(@NotNull ViewPager viewPager) {
        mFragmentsPagerAdapter.addFragment(new PersonalRegisterFragment());
        mFragmentsPagerAdapter.addFragment(new CompanyRegisterFragment());
        viewPager.setAdapter(mFragmentsPagerAdapter);
    }

    private  ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            setCheckBottomMenuItem(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void setCheckBottomMenuItem(int item){
        mBottomMenu.getItem(item).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_person:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.nav_company:
                    mViewPager.setCurrentItem(1);
                    break;
            }
            return true;
        }
    };
}
