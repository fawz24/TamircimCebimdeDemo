package com.tamirdunyasi.tamircimcebimdedemo;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {

    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.main_activity_label);

        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.registerContainer);
        setUpViewPager(mViewPager);
    }

    private void setUpViewPager(@NotNull ViewPager viewPager) {
        mFragmentsPagerAdapter.addFragment(new PersonalRegisterFragment());
        mFragmentsPagerAdapter.addFragment(new CompanyRegisterFragment());
        viewPager.setAdapter(mFragmentsPagerAdapter);
    }
}
