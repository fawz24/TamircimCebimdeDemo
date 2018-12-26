package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;

//public class PostsActivity extends AppCompatActivity {
//
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    private LinearLayout mContainer;
//    private BottomNavigationView mBottomNavigationView;
//    private Menu mMenu;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_posts);
//
//        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_bottom);
//        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
//
//        mMenu = mBottomNavigationView.getMenu();
//        mMenu.getItem(1).setChecked(true);
//
//        Intent intent = new Intent(this, PostDetailsActivity.class);
////        Bundle bundle = new Bundle();
////        bundle.putString("postId", "TcLxz1GpDn95HTsSRql4");
////        intent.putExtras(bundle);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//
//        mMenu.getItem(1).setChecked(true);
//    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            Intent intent;
//            switch (menuItem.getItemId()){
//                case R.id.nav_home:
//                    intent = new Intent(PostsActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.nav_account:
//                    intent = new Intent(PostsActivity.this, AccountActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.nav_messages:
//                    intent = new Intent(PostsActivity.this, MessagesActivity.class);
//                    startActivity(intent);
//                    break;
//            }
//            return true;
//        }
//    };
//}

public class PostsActivity extends AppCompatActivity {

    private LinearLayout pContainer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private User mUser;

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

        pContainer = findViewById(R.id.postContainer);

        if (currentUser != null){
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            String id = document.getId();
//                            String address = document.get("address").toString();
//                            String email = document.get("email").toString();
//                            String name = document.get("name").toString();
//                            String password = document.get("password").toString();
//                            String phone = document.get("phone").toString();
//                            String type = document.get("type").toString();

                            mUser = new User(document.getId(), document.get("address").toString(), document.get("email").toString(),
                                    document.get("name").toString(), document.get("password").toString(),
                                    document.get("phone").toString(), document.get("type").toString());//id, address, email, name, password, phone, type);

                            fillData();
                        }
                    }
                }
            });
        }else{
            finish();
        }
//        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    public void newPostListener(View v){
        Intent intent = new Intent(this, PostDetailsActivity.class);
        startActivity(intent);
    }
    //.whereEqualTo("clientid", currentUser.getUid())
    private void fillData(){
        final PostsActivity context = this;
        db.collection("request").whereEqualTo("clientid", currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot requestDocument : task.getResult()) {
//                        Create and fill the card
                        CardView card = new CardView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int px = dpToPx(10);
                        params.setMargins(px, px, px, px);
                        card.setLayoutParams(params);
                        card.setContentPadding(px, px, px, px);
                        card.setRadius(dpToPx(5));
                        card.setMaxCardElevation(dpToPx(5));
//                        Add card to container
                        pContainer.addView(card);

//                        Add linear layout
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
//                        Attach the linear layout to the card
                        card.addView(linearLayout);

//                        Create text view for talepbaslik
                        TextView talepbaslik = new TextView(context);
                        talepbaslik.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(40)));
                        talepbaslik.setTextSize(dpToPx(8));
                        talepbaslik.setText(requestDocument.getString("title"));
                        talepbaslik.setGravity(Gravity.CENTER);
                        linearLayout.addView(talepbaslik);

//                        Create view for divider
                        View divider = new View(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));//(int)convertDpToPx(context, 1)
                        params.setMargins(0,  dpToPx(5), 0, dpToPx(5));//(int)convertDpToPx(context, 5)
                        divider.setLayoutParams(params);
                        divider.setBackgroundColor(getResources().getColor(R.color.colorDarkerGray));
                        linearLayout.addView(divider);

//                        Status horizontal linear layout container
                        LinearLayout statusLinear = new LinearLayout(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, dpToPx(5));
                        statusLinear.setLayoutParams(params);
                        statusLinear.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(statusLinear);

//                        create textview for state label
                        TextView state = new TextView(context);
                        state.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(70), ViewGroup.LayoutParams.WRAP_CONTENT));
                        state.setTextSize(dpToPx(6));
                        state.setText("Durum");
                        state.setGravity(Gravity.CENTER_VERTICAL);
                        statusLinear.addView(state);
//
//                        text view for state
                        state = new TextView(context);
                        state.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        state.setTextSize(dpToPx(6));
                        state.setTypeface(null, Typeface.BOLD);
                        state.setText(": " + requestDocument.getString("state"));
                        state.setGravity(Gravity.CENTER_VERTICAL);
                        statusLinear.addView(state);

//                        Category horizontal linear view container
                        LinearLayout categoryLinear = new LinearLayout(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, dpToPx(5));
                        categoryLinear.setLayoutParams(params);
                        categoryLinear.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(categoryLinear);

//                        create text view for category label
                        TextView category = new TextView(context);
                        category.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(70), ViewGroup.LayoutParams.WRAP_CONTENT));
                        category.setTextSize(dpToPx(6));
                        category.setText("Kategori");
                        category.setGravity(Gravity.CENTER_VERTICAL);
                        categoryLinear.addView(category);

//                        text view for category
                        category = new TextView(context);
                        category.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        category.setTextSize(dpToPx(6));
                        category.setText(": " + requestDocument.getString("category"));
                        category.setGravity(Gravity.CENTER_VERTICAL);
                        categoryLinear.addView(category);

                        String companyName = requestDocument.getString("companyname");
                        if (companyName != null){
//                            Company name horizontal linear view container
                            LinearLayout companyLinear = new LinearLayout(context);
                            params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 0, 0, dpToPx(5));
                            companyLinear.setLayoutParams(params);
                            companyLinear.setOrientation(LinearLayout.HORIZONTAL);
                            linearLayout.addView(companyLinear);

//                        create text view for company label
                            TextView company = new TextView(context);
                            company.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(70), ViewGroup.LayoutParams.WRAP_CONTENT));
                            company.setTextSize(dpToPx(6));
                            company.setText("Firma");
                            company.setGravity(Gravity.CENTER_VERTICAL);
                            companyLinear.addView(company);

//                        text view for company
                            company = new TextView(context);
                            company.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            company.setTextSize(dpToPx(6));
                            company.setText(": " + companyName);
                            company.setGravity(Gravity.CENTER_VERTICAL);
                            companyLinear.addView(company);
                        }

//                        Post content horizontal linear layout container
                        LinearLayout postContentLinear = new LinearLayout(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, dpToPx(5));
                        postContentLinear.setLayoutParams(params);
                        postContentLinear.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(postContentLinear);

//                        create text view for post content
                        TextView company = new TextView(context);
                        company.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(70), ViewGroup.LayoutParams.WRAP_CONTENT));
                        company.setTextSize(dpToPx(6));
                        company.setText("Açıklama");
                        company.setGravity(Gravity.CENTER_VERTICAL);
                        postContentLinear.addView(company);

//                        text view for post content
                        company = new TextView(context);
                        company.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        company.setTextSize(dpToPx(6));
                        String content  = requestDocument.getString("content");
                        if (content != null && content.length() > 20){
                            content = content.substring(0, 20) + "...";
                        }
                        company.setText(": " + content);
                        company.setGravity(Gravity.CENTER_VERTICAL);
                        postContentLinear.addView(company);

//                        Including the id of the request to the card
                        card.setTag(requestDocument.getId());

                        card.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, PostDetailsActivity.class);
                                Bundle b = new Bundle();
                                b.putString("postId", requestDocument.getId());
                                intent.putExtras(b);
                                startActivity(intent);
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
}