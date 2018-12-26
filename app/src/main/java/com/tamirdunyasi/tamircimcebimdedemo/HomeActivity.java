package com.tamirdunyasi.tamircimcebimdedemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
    //    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private TabLayout tabLayout;
    private Menu mBottomMenu;
    private LinearLayout hContainer;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.home_toolbar));
//        Bundle bundle = getIntent().getExtras();
//        TextView title = findViewById(R.id.toolbar_title);
//        title.setText(String.format("%s%s!", title.getText(), bundle.get("user")));

        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());

//        mViewPager = (ViewPager)findViewById(R.id.container);
//        setUpViewPager(mViewPager);
//        mViewPager.addOnPageChangeListener(pageChangeListener);

        mBottomNavigationView = findViewById(R.id.navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mBottomMenu = mBottomNavigationView.getMenu();
        hContainer = findViewById(R.id.firmaContainer);

        fillData(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

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
    }

    private void fillData(final Context context){
        db.collection("company").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    final int i = 0;
                    for (final QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("content", document.getId() + " => " + document.getData());

//                        Create and fill the card
                        CardView card = new CardView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int px = dpToPx(10);
                        params.setMargins(px, px, px, px);
                        card.setLayoutParams(params);
                        card.setContentPadding(px, px, px, px);
                        card.setRadius(dpToPx(5));
                        card.setMaxCardElevation(dpToPx(5));
//                        Add card to container
                        hContainer.addView(card);

//                        Add linear layout
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
//                        Attach the linear layout to the card
                        card.addView(linearLayout);

                        //  Create text view for company name
                        final TextView companyName = new TextView(context);
                        companyName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(30)));
                        companyName.setTextSize(dpToPx(8));
                        companyName.setGravity(Gravity.CENTER);
                        linearLayout.addView(companyName);

//                        Create text view for company title
                        final TextView title = new TextView(context);
                        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(40)));
                        title.setTextSize(dpToPx(6));
                        title.setText(document.getString("title"));
                        title.setGravity(Gravity.CENTER);
                        linearLayout.addView(title);

//                        Create view for divider
                        View divider = new View(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
                        params.setMargins(0,  dpToPx(5), 0, dpToPx(10));
                        divider.setLayoutParams(params);
                        divider.setBackgroundColor(getResources().getColor(R.color.colorDarkerGray));
                        linearLayout.addView(divider);

//                        Text view for labels
                        TextView label;

//                        Category horizontal linear layout container
                        LinearLayout categoryLinear = new LinearLayout(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, dpToPx(5));
                        categoryLinear.setLayoutParams(params);
                        categoryLinear.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(categoryLinear);

//                        create text view for category label
                        label = new TextView(context);
                        label.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(90), ViewGroup.LayoutParams.WRAP_CONTENT));
                        label.setTextSize(dpToPx(6));
                        label.setText("Kategori");
                        label.setGravity(Gravity.CENTER_VERTICAL);
                        categoryLinear.addView(label);

//                        text view for category
                        label = new TextView(context);
                        label.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        label.setTextSize(dpToPx(6));
                        String category = ": ";
                        int i = 0;
                        List<String> categories = (List<String>)document.get("category");
                        for (; i < categories.size() - 1; i++) {
                            category += categories.get(i) + ", ";
                        }
                        category += categories.get(i);
                        label.setText(category);
                        label.setGravity(Gravity.CENTER_VERTICAL);
                        categoryLinear.addView(label);

//                        Email horizontal linear layout container
                        LinearLayout emailLinear = new LinearLayout(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, dpToPx(5));
                        emailLinear.setLayoutParams(params);
                        emailLinear.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(emailLinear);

//                        create text view for email label
                        label = new TextView(context);
                        label.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(90), ViewGroup.LayoutParams.WRAP_CONTENT));
                        label.setTextSize(dpToPx(6));
                        label.setText("Email");
                        label.setGravity(Gravity.CENTER_VERTICAL);
                        emailLinear.addView(label);

//                       create text view for company email
                        final TextView companyEmail = new TextView(context);
                        companyEmail.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(30)));
                        companyEmail.setTextSize(dpToPx(6));
                        companyEmail.setGravity(Gravity.CENTER);
                        emailLinear.addView(companyEmail);

//                        Phone horizontal linear layout container
                        LinearLayout phoneLinear = new LinearLayout(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, dpToPx(5));
                        phoneLinear.setLayoutParams(params);
                        phoneLinear.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(phoneLinear);

//                        create text view for company phone label
                        label = new TextView(context);
                        label.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(90), ViewGroup.LayoutParams.WRAP_CONTENT));
                        label.setTextSize(dpToPx(6));
                        label.setText("Telefon");
                        label.setGravity(Gravity.CENTER_VERTICAL);
                        phoneLinear.addView(label);

//                       create text view for company phone
                        final TextView companyPhone = new TextView(context);
                        companyPhone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(30)));
                        companyPhone.setTextSize(dpToPx(6));
                        companyPhone.setGravity(Gravity.CENTER);
                        phoneLinear.addView(companyPhone);

//                        Address horizontal linear layout container
                        LinearLayout addressLinear = new LinearLayout(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, dpToPx(5));
                        addressLinear.setLayoutParams(params);
                        addressLinear.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(addressLinear);

//                        create textview for address label
                        label = new TextView(context);
                        label.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(90), ViewGroup.LayoutParams.WRAP_CONTENT));
                        label.setTextSize(dpToPx(6));
                        label.setText("Adres");
                        label.setGravity(Gravity.CENTER_VERTICAL);
                        addressLinear.addView(label);

//                        Create text view for address
                        final TextView companyAddress = new TextView(context);
                        companyAddress.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        companyAddress.setTextSize(dpToPx(6));
                        companyAddress.setGravity(Gravity.CENTER);
                        addressLinear.addView(companyAddress);

//                        Including the id of the company to the card
                        card.setTag(document.getId());

//                        Get company title
                        db.collection("users").document(document.getId()).get().addOnCompleteListener(HomeActivity.this, new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        companyName.setText(document.getString("name"));
                                        companyAddress.setText(": " + document.getString("address"));
                                        companyEmail.setText(": " + document.getString("email"));
                                        companyPhone.setText(": " + document.getString("phone"));
//                                        setTextViewText(companyName,document.get("name").toString());
//                                        setTextViewText(firmaadres, "Firma Adres : " +  document.get("address").toString());
//                                        setTextViewText(firmaemail, "Email : " + document.get("email").toString());
//                                        setTextViewText(firmatel, "Firma Telefon : " + document.get("phone").toString() );

//                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
//                                                Log.d(TAG, "No such document");
                                    }
                                } else {
//                                            Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });

                    }
                } else {
                    Log.w("content", "Error getting documents.", task.getException());
                }
            }});

        db.collection("users").document();
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setTextViewText(TextView textView, String title) {
        textView.setText(title);
    }
}

//
//import android.annotation.TargetApi;
//import android.content.Intent;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.Toolbar;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class HomeActivity extends AppCompatActivity {
//
//    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
////    private ViewPager mViewPager;
//    private BottomNavigationView mBottomNavigationView;
//    private TabLayout tabLayout;
//    private Menu mBottomMenu;
//    private String currentUserId;
//    private String currentUserDisplayName;
//    private String currentUserEmail;
//
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
////        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.home_toolbar));
//        Bundle bundle = getIntent().getExtras();
//        currentUserId = bundle.getString("userId");
//        currentUserEmail = bundle.getString("userEmail");
//        currentUserDisplayName = bundle.getString("userName");
//
////        TextView title = findViewById(R.id.toolbar_title);
////        title.setText(String.format("%s%s!", title.getText(), bundle.get("user")));
//
//        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());
//
////        mViewPager = (ViewPager)findViewById(R.id.container);
////        setUpViewPager(mViewPager);
////        mViewPager.addOnPageChangeListener(pageChangeListener);
//
//        mBottomNavigationView = findViewById(R.id.navigation_bottom);
//        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
//
//        mBottomMenu = mBottomNavigationView.getMenu();
//
//        Toast.makeText(this, "User Id: " + currentUserId, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        mBottomMenu.getItem(0).setChecked(true);
//    }
//
//    private void setUpViewPager(ViewPager viewPager) {
//        mFragmentsPagerAdapter.addFragment(new HomeFragment());
//        mFragmentsPagerAdapter.addFragment(new PostsFragment());
//        mFragmentsPagerAdapter.addFragment(new MessagesFragment());
//        mFragmentsPagerAdapter.addFragment(new AccountFragment());
//        viewPager.setAdapter(mFragmentsPagerAdapter);
//    }
//
////    private  ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
////        @Override
////        public void onPageScrolled(int i, float v, int i1) {
////
////        }
////
////        @Override
////        public void onPageSelected(int i) {
////            setCheckBottomMenuItem(i);
////        }
////
////        @Override
////        public void onPageScrollStateChanged(int i) {
////
////        }
////    };
//
//    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            Intent intent = null;
//            switch (menuItem.getItemId()){
//                case R.id.nav_posts:
//                    intent = new Intent(HomeActivity.this, PostsActivity.class);
//                    break;
//                case R.id.nav_messages:
//                    intent = new Intent(HomeActivity.this, MessagesActivity.class);
////                    mViewPager.setCurrentItem(2);
//                    break;
//                case R.id.nav_account:
//                    intent = new Intent(HomeActivity.this, AccountActivity.class);
////                    mBottomMenu.clear();
////                    mBottomNavigationView.inflateMenu(R.menu.navigation_company_menu);
////                    mBottomMenu = mBottomNavigationView.getMenu();
//                    break;
//            }
//            if (intent != null){
//                Bundle bundle = new Bundle();
//                bundle.putString("userEmail", currentUserEmail);
//                bundle.putString("userName", currentUserDisplayName);
//                bundle.putString("userId", currentUserId);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//            return true;
//        }
//    };
//
//    public Menu getBottomMenu(){
//        return mBottomMenu;
//    }
//
//    public void setCheckBottomMenuItem(int item){
//        mBottomMenu.getItem(item).setChecked(true);
//    }
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.appbar, menu);
////        return super.onCreateOptionsMenu(menu);
////    }
//
//    public void menuClicked(View view)
//    {
//        Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
//    }
//}
