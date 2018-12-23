package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    private User mUser;

    private String currentUserEmail;
    private String currentUserDisplayName;
    private String currentUserId;

    private LinearLayout mContainer;
    private BottomNavigationView mBottomNavigationView;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        currentUser = dbAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        currentUserId = bundle.getString("userId");
        currentUserEmail = bundle.getString("userEmail");
        currentUserDisplayName = bundle.getString("userName");

        mBottomNavigationView = findViewById(R.id.navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mMenu = mBottomNavigationView.getMenu();
        mMenu.getItem(2).setChecked(true);

        mContainer = findViewById(R.id.messageContainer);

//        Toast.makeText(this, "User Id: " + currentUserId, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "User Id: " + currentUser.getUid(), Toast.LENGTH_LONG).show();

        final MessagesActivity context = this;

        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String id = document.getId();
                        String address = document.get("address").toString();
                        String email = document.get("email").toString();
                        String name = document.get("name").toString();
                        String password = document.get("password").toString();
                        String phone = document.get("phone").toString();
                        String type = document.get("type").toString();

                        mUser = new User(id, address, email, name, password, phone, type);
                        fillData(context);
                    } else {
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        mMenu.getItem(2).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent intent = null;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    intent = new Intent(MessagesActivity.this, HomeActivity.class);
                    break;
                case R.id.nav_posts:
                    intent = new Intent(MessagesActivity.this, PostsActivity.class);
                    break;
                case R.id.nav_account:
                    intent = new Intent(MessagesActivity.this, AccountActivity.class);
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

    public User getmUser() {
        return mUser;
    }

    private void fillData(final MessagesActivity context){
        String user = "clientid";
        User u = context.getmUser();
        if (u.getType().equals("co")){
            user = "companyid";
        }
        db.collection("message").whereEqualTo(user, currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("content", document.getId() + " => " + document.getData());
//                        Create and fill the card
                        CardView card = new CardView(context);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int px = Helpers.dpToPx(context, 10); //(int)convertDpToPx(context, 10);
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
                        peer.setText(document.getString("companyname"));
                        peer.setGravity(Gravity.CENTER);

//                        Create text view for post
                        TextView post = new TextView(context);
                        post.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(30)));//(int)convertDpToPx(context, 30)
                        post.setTextSize(dpToPx(6));//convertDpToPx(context, 15)
                        post.setText(document.getString("requesttitle"));
                        post.setGravity(Gravity.CENTER);

//                        Create view for divider
                        View divider = new View(context);
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
                        params.setMargins(0,  dpToPx(5), 0, 0);//(int)convertDpToPx(context, 5)
                        divider.setLayoutParams(params);
                        divider.setBackgroundColor(getResources().getColor(R.color.colorDarkerGray));

                        final LinearLayout contents = new LinearLayout(context);
                        contents.setMinimumHeight(dpToPx(60));//(int)convertDpToPx(context, 60)
                        contents.setOrientation(LinearLayout.VERTICAL);
                        contents.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        List<QueryDocumentSnapshot> list = new ArrayList<>();

                        db.collection("message").document(document.getId()).collection("contents")
                                .orderBy("date", Query.Direction.DESCENDING).limit(3).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int i = 0;
                                    for (QueryDocumentSnapshot ctnt : task.getResult()) {
//                                        Add message content
                                        addMessageContent(context, contents, ctnt);
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
                                    link.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CardView cardView = (CardView)v.getParent().getParent().getParent().getParent();
                                            Toast.makeText(context, "Link clicked: " + (String)cardView.getTag(), Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(context, MessageDetailsActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("messageId", (String)cardView.getTag());
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);

                                        }
                                    });
//                        Attach the link text view to the relative layout
                                    relativeLayout.addView(link);

//                        Attach the relative layout to the contents linear layout
                                    contents.addView(relativeLayout);
                                }
                            }
                                });
//                        for (Map<String, Object> ctnt: (List<Map<String, Object>>)document.get("contents")) {
////                        for (Map<String, Object> ctnt: (List<Map<String, Object>>)document.get("contents")) {
////                              Create text view for content
//                            TextView content = new TextView(context);
//                            params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                            params.setMargins(0,  dpToPx(5), 0, dpToPx(5));//(int)convertDpToPx(context, 5)
//                            content.setLayoutParams(params);
//                            content.setTextSize(dpToPx(7));//convertDpToPx(context, 15)
//                            content.setText(ctnt.get("body").toString());
////                            content.setGravity(Gravity.CENTER);
//
////                            Create a divider
//                            View div = new View(context);
//                            div.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1)));//(int)convertDpToPx(context, 1)
//                            div.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
//
////                            Attach the new content and divider to the contents linear layout
//                            contents.addView(content);
//                            contents.addView(div);
//                        }

//                        Attach elements to linear layout
                        linearLayout.addView(peer);
                        linearLayout.addView(post);
                        linearLayout.addView(divider);
                        linearLayout.addView(contents);

//                        Attach the linear layout to the card
                        card.addView(linearLayout);

//                        Including the id of the message to the card
                        card.setTag(document.getId());

                        card.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, (String)v.getTag(), Toast.LENGTH_LONG).show();
                            }
                        });

//                        Add card to container
                        mContainer.addView(card);

//                        Toast.makeText(context, "Data got: " + document.getData()/*.get("content").toString()*/, Toast.LENGTH_LONG).show();
                        i += 1;
                    }
                } else {
//                    Log.w("content", "Error getting documents.", task.getException());
                }
            }});
    }

    private void addMessageContent(Context context, LinearLayout contents, QueryDocumentSnapshot ctnt){
//        Create text view for content
        TextView content = new TextView(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,  dpToPx(5), 0, dpToPx(5));//(int)convertDpToPx(context, 5)
        content.setLayoutParams(params);
        content.setTextSize(dpToPx(7));//convertDpToPx(context, 15)
        if (ctnt.get("senderid").toString().equals(mUser.id)){
            content.setPadding(0, 0, Helpers.dpToPx(this, 60), 0);
        }
        else {
            content.setPadding(Helpers.dpToPx(this, 60), 0, 0, 0);
        }
        String body = ctnt.get("body").toString();
        if (body.length() > 20) {
            body = body.substring(0, 20) + "...";
        }
        content.setText(body);
//                            content.setGravity(Gravity.CENTER);

//                            Create a divider
        View div = new View(context);
        div.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1)));//(int)convertDpToPx(context, 1)
        div.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

//                            Attach the new content and divider to the contents linear layout
        contents.addView(content);
        contents.addView(div);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
