package com.tamirdunyasi.tamircimcebimdedemo;

import android.annotation.TargetApi;
import android.content.Intent;
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

import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
//    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private TabLayout tabLayout;
    private Menu mBottomMenu;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.home_toolbar));
        Bundle bundle = getIntent().getExtras();
//        TextView title = findViewById(R.id.toolbar_title);
//        title.setText(String.format("%s%s!", title.getText(), bundle.get("user")));

        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());

//        mViewPager = (ViewPager)findViewById(R.id.container);
//        setUpViewPager(mViewPager);
//        mViewPager.addOnPageChangeListener(pageChangeListener);

        mBottomNavigationView = findViewById(R.id.navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mBottomMenu = mBottomNavigationView.getMenu();
    }

    private void setUpViewPager(ViewPager viewPager) {
        mFragmentsPagerAdapter.addFragment(new HomeFragment());
        mFragmentsPagerAdapter.addFragment(new PostsFragment());
        mFragmentsPagerAdapter.addFragment(new MessagesFragment());
        mFragmentsPagerAdapter.addFragment(new AccountFragment());
        viewPager.setAdapter(mFragmentsPagerAdapter);
    }

//    private  ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int i, float v, int i1) {
//
//        }
//
//        @Override
//        public void onPageSelected(int i) {
//            setCheckBottomMenuItem(i);
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int i) {
//
//        }
//    };

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent intent;
            switch (menuItem.getItemId()){
                case R.id.nav_posts:
                    intent = new Intent(HomeActivity.this, PostsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_messages:
                    intent = new Intent(HomeActivity.this, MessagesActivity.class);
                    startActivity(intent);
//                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.nav_account:
                    intent = new Intent(HomeActivity.this, AccountActivity.class);
                    startActivity(intent);
//                    mBottomMenu.clear();
//                    mBottomNavigationView.inflateMenu(R.menu.navigation_company_menu);
//                    mBottomMenu = mBottomNavigationView.getMenu();
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
