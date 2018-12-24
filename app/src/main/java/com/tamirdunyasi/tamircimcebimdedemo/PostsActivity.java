package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.firestore.FirebaseFirestore;

public class PostsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private LinearLayout mContainer;
    private BottomNavigationView mBottomNavigationView;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mMenu = mBottomNavigationView.getMenu();
        mMenu.getItem(1).setChecked(true);

        Intent intent = new Intent(this, PostDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("postId", "TcLxz1GpDn95HTsSRql4");
//        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        mMenu.getItem(1).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent intent;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    intent = new Intent(PostsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_account:
                    intent = new Intent(PostsActivity.this, AccountActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_messages:
                    intent = new Intent(PostsActivity.this, MessagesActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };
}
