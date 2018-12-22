package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostsActivity extends AppCompatActivity {

    private LinearLayout pContainer;
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

        pContainer = findViewById(R.id.postContainer);
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
    private void fillData(final Context context){
        db.collection("request").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("content", document.getId() + " => " + document.getData());
//                        Create and fill the card
                        CardView card = new CardView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int px = dpToPx(10); //(int)convertDpToPx(context, 10);
                        params.setMargins(px, px, px, px);
                        card.setLayoutParams(params);
                        card.setContentPadding(px, px, px, px);
                        card.setRadius(dpToPx(5));//convertDpToPx(context, 4)
                        card.setMaxCardElevation(dpToPx(5));//convertDpToPx(context, 5)

//                        Add linear layout
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        linearLayout.setOrientation(LinearLayout.VERTICAL);

//                        Create text view for peer
                        TextView peer = new TextView(context);
                        peer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(40))); //(int)convertDpToPx(context, 40)
                        peer.setTextSize(dpToPx(8));//convertDpToPx(context, 20)
                        peer.setText(document.getString("category"));
                        peer.setGravity(Gravity.CENTER);

//                       Create text view for talepbaslik
                        TextView talepbaslik = new TextView(context);
                        talepbaslik.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(30)));//(int)convertDpToPx(context, 30)
                        talepbaslik.setTextSize(dpToPx(6));//convertDpToPx(context, 15)
                        talepbaslik.setText(document.getString("title"));
                        talepbaslik.setGravity(Gravity.CENTER);

//                        Create view for divider
                        View divider = new View(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));//(int)convertDpToPx(context, 1)
                        params.setMargins(0,  dpToPx(5), 0, 0);//(int)convertDpToPx(context, 5)
                        divider.setLayoutParams(params);
                        divider.setBackgroundColor(getResources().getColor(R.color.colorDarkerGray));

                        LinearLayout contents = new LinearLayout(context);
                        contents.setMinimumHeight(dpToPx(60));//(int)convertDpToPx(context, 60)
                        contents.setOrientation(LinearLayout.VERTICAL);
                        contents.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        List<QueryDocumentSnapshot> list = new ArrayList<>();


                        for (Map<String, Object> ctnt: (List<Map<String, Object>>)document.get("contents")) {
//                        for (Map<String, Object> ctnt: (List<Map<String, Object>>)document.get("contents")) {
//                              Create text view for content
                            TextView content = new TextView(context);
                            params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,  dpToPx(5), 0, dpToPx(5));//(int)convertDpToPx(context, 5)
                            content.setLayoutParams(params);
                            content.setTextSize(dpToPx(7));//convertDpToPx(context, 15)
                            content.setText(ctnt.get("body").toString());
//                            content.setGravity(Gravity.CENTER);

//                            Create a divider
                            View div = new View(context);
                            div.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1)));//(int)convertDpToPx(context, 1)
                            div.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

//                            Attach the new content and divider to the contents linear layout
                            contents.addView(content);
                            contents.addView(div);
                        }

//                        Create the relative layout
                        RelativeLayout relativeLayout = new RelativeLayout(context);
                        relativeLayout.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//                        Create the link
                        TextView link = new TextView(context);
                        RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        par.setMargins(0,  dpToPx(5), 0, dpToPx(5));//(int)convertDpToPx(context, 5)
                        par.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        link.setLayoutParams(par);
                        link.setTextSize(dpToPx(7));//convertDpToPx(context, 15)
                        link.setText("Yeni Mesaj");
                        link.setTextColor(getResources().getColor(R.color.colorSecond));

//                        Attach the link text view to the relative layout
                        //relativeLayout.addView(link);

//                        Attach the relative layout to the contents linear layout
                        //contents.addView(relativeLayout);

//                        Attach elements to linear layout
                        linearLayout.addView(peer);
                        linearLayout.addView(talepbaslik);
                        linearLayout.addView(divider);
                        linearLayout.addView(contents);

//                        Attach the linear layout to the card
                        card.addView(linearLayout);

//                        Including the id of the company to the card
                        card.setTag(document.getId());

                        card.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, (String)v.getTag(), Toast.LENGTH_LONG).show();
                            }
                        });

//                        Add card to container
                        pContainer.addView(card);

                        Toast.makeText(context, "Data got: " + document.getData()/*.get("content").toString()*/, Toast.LENGTH_LONG).show();
                        i += 1;
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
