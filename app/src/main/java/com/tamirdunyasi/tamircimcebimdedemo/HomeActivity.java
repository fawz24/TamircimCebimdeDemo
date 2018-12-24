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
    private String currentUserId;
    private String currentUserDisplayName;
    private String currentUserEmail;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.home_toolbar));
        Bundle bundle = getIntent().getExtras();
        currentUserId = bundle.getString("userId");
        currentUserEmail = bundle.getString("userEmail");
        currentUserDisplayName = bundle.getString("userName");

//        TextView title = findViewById(R.id.toolbar_title);
//        title.setText(String.format("%s%s!", title.getText(), bundle.get("user")));

        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());

//        mViewPager = (ViewPager)findViewById(R.id.container);
//        setUpViewPager(mViewPager);
//        mViewPager.addOnPageChangeListener(pageChangeListener);

        mBottomNavigationView = findViewById(R.id.navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mBottomMenu = mBottomNavigationView.getMenu();

        Toast.makeText(this, "User Id: " + currentUserId, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mBottomMenu.getItem(0).setChecked(true);
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
            Intent intent = null;
            switch (menuItem.getItemId()){
                case R.id.nav_posts:
                    intent = new Intent(HomeActivity.this, PostsActivity.class);
                    break;
                case R.id.nav_messages:
                    intent = new Intent(HomeActivity.this, MessagesActivity.class);
//                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.nav_account:
                    intent = new Intent(HomeActivity.this, AccountActivity.class);
//                    mBottomMenu.clear();
//                    mBottomNavigationView.inflateMenu(R.menu.navigation_company_menu);
//                    mBottomMenu = mBottomNavigationView.getMenu();
                    break;
            }
            if (intent != null){
                Bundle bundle = new Bundle();
                bundle.putString("userEmail", currentUserEmail);
                bundle.putString("userName", currentUserDisplayName);
                bundle.putString("userId", currentUserId);
                intent.putExtras(bundle);
                startActivity(intent);
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
