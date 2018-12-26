package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

//    private String currentUserEmail;
//    private String currentUserDisplayName;
//    private String currentUserId;

    private LinearLayout mContainer;
    private BottomNavigationView mBottomNavigationView;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Bundle bundle = getIntent().getExtras();
//        currentUserId = bundle.getString("userId");
//        currentUserEmail = bundle.getString("userEmail");
//        currentUserDisplayName = bundle.getString("userName");

        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mMenu = mBottomNavigationView.getMenu();
        mMenu.getItem(3).setChecked(true);

//        currentUser = dbAuth.getCurrentUser();

//        Toast.makeText(this, "User Id: " + currentUserId, Toast.LENGTH_LONG).show();
//
//        if (currentUser != null){
//            Toast.makeText(this, "User: " + currentUser.getDisplayName().toString(), Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        mMenu.getItem(3).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent intent = null;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    intent = new Intent(AccountActivity.this, HomeActivity.class);
                    break;
                case R.id.nav_posts:
                    intent = new Intent(AccountActivity.this, PostsActivity.class);
                    break;
                case R.id.nav_messages:
                    intent = new Intent(AccountActivity.this, MessagesActivity.class);
                    break;
            }
            if (intent != null){
//                Bundle bundle = new Bundle();
//                bundle.putString("userEmail", currentUserEmail);
//                bundle.putString("userName", currentUserDisplayName);
//                bundle.putString("userId", currentUserId);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
            return true;
        }
    };

    public void logoutListener(View v){
        Helpers.signOut(this, dbAuth);
    }
}
