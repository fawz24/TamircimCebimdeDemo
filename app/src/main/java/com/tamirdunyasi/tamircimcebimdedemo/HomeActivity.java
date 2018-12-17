package com.tamirdunyasi.tamircimcebimdedemo;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private TabLayout tabLayout;
    private Menu mBottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.home_toolbar));
        Bundle bundle = getIntent().getExtras();
//        TextView title = findViewById(R.id.toolbar_title);
//        title.setText(String.format("%s%s!", title.getText(), bundle.get("user")));

        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager)findViewById(R.id.container);
        setUpViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_bottom);
        mBottomMenu = mBottomNavigationView.getMenu();
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
    }

    private void setUpViewPager(ViewPager viewPager) {
        mFragmentsPagerAdapter.addFragment(new HomeFragment());
        mFragmentsPagerAdapter.addFragment(new PostsFragment());
        mFragmentsPagerAdapter.addFragment(new MessagesFragment());
        mFragmentsPagerAdapter.addFragment(new AccountFragment());
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

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.nav_posts:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.nav_messages:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.nav_account:
                    mViewPager.setCurrentItem(3);
//                    mBottomMenu.clear();
//                    mBottomNavigationView.inflateMenu(R.menu.navigation_company_menu);
                    break;
            }
            return true;
        }
    };

    public Menu getBottomMenu(){
        return mBottomMenu;
    }

    public void setCheckBottomMenuItem(int item){
        mBottomMenu.getItem(item).setChecked(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.appbar, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void menuClicked(View view)
    {
        Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
    }
}
