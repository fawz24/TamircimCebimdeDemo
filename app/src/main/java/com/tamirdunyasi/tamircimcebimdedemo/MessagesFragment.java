package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout mContainer;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//         dbRef = firebaseDatabase.getInstance().getReference();
//        dbRef = firebaseDatabase.getReference("message").getDatabase();
//        // Read from the database
//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("content", "Value is: " + value);
//                Toast.makeText(getContext(), "Data got: " + value, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("content", "Failed to read value.", error.toException());
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_messages, container, false);

        return fragmentView;
    }

    @Override
    public void onResume() {
        mContainer = getActivity().findViewById(R.id.messageContainer);

        fillData();

        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void fillData(){
        db.collection("message").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("content", document.getId() + " => " + document.getData());
//                        Create and fill the card
                        CardView card = new CardView(getContext());
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int px = (int)convertDpToPx(getContext(), 10);
                        params.setMargins(px, px, px, px);
                        card.setLayoutParams(params);
                        card.setContentPadding(px, px, px, px);
                        card.setRadius(convertDpToPx(getContext(), 4));
                        card.setMaxCardElevation(convertDpToPx(getContext(), 5));

//                        Add linear layout
                        LinearLayout linearLayout = new LinearLayout(getContext());
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        linearLayout.setOrientation(LinearLayout.VERTICAL);

//                        Create text view for peer
                        TextView peer = new TextView(getContext());
                        peer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)convertDpToPx(getContext(), 40)));
                        peer.setTextSize(convertDpToPx(getContext(), 20));
                        peer.setText(document.getString("companyname"));
                        peer.setGravity(Gravity.CENTER);

//                        Create text view for post
                        TextView post = new TextView(getContext());
                        post.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)convertDpToPx(getContext(), 30)));
                        post.setTextSize(convertDpToPx(getContext(), 15));
                        post.setText(document.getString("requesttitle"));
                        post.setGravity(Gravity.CENTER);

//                        Create view for divider
                        View divider = new View(getContext());
                        params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)convertDpToPx(getContext(), 1));
                        params.setMargins(0,  (int)convertDpToPx(getContext(), 5), 0, 0);
                        divider.setLayoutParams(params);
                        divider.setBackgroundColor(getResources().getColor(R.color.colorDarkerGray));

                        LinearLayout contents = new LinearLayout(getContext());
                        contents.setMinimumHeight((int)convertDpToPx(getContext(), 60));
                        contents.setOrientation(LinearLayout.VERTICAL);
                        contents.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        for (Map<String, Object> ctnt: (List<Map<String, Object>>)document.get("contents")) {
//                              Create text view for content
                            TextView content = new TextView(getContext());
                            params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,  (int)convertDpToPx(getContext(), 5), 0, (int)convertDpToPx(getContext(), 5));
                            content.setLayoutParams(params);
                            content.setTextSize(convertDpToPx(getContext(), 15));
                            content.setText(ctnt.get("body").toString());
//                            content.setGravity(Gravity.CENTER);

//                            Create a divider
                            View div = new View(getContext());
                            divider.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)convertDpToPx(getContext(), 1)));
                            divider.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

//                            Attach the new content and divider to the contents linear layout
                            contents.addView(content);
                            contents.addView(div);
                        }

//                        Create the relative layout
                        RelativeLayout relativeLayout = new RelativeLayout(getContext());
                        relativeLayout.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//                        Create the link
                        TextView link = new TextView(getContext());
                        RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        par.setMargins(0,  (int)convertDpToPx(getContext(), 5), 0, (int)convertDpToPx(getContext(), 5));
                        par.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        link.setLayoutParams(params);
                        link.setTextSize(convertDpToPx(getContext(), 15));
                        link.setText("Yeni Mesaj");
                        link.setTextColor(getResources().getColor(R.color.colorSecond));

//                        Attach the link text view to the relative layout
                        relativeLayout.addView(link);

//                        Attach the relative layout to the contents linear layout
                        contents.addView(relativeLayout);

//                        Attach elements to linear layout
                        linearLayout.addView(peer);
                        linearLayout.addView(post);
                        linearLayout.addView(divider);
                        linearLayout.addView(contents);

//                        Attach the linear layout to the card
                        card.addView(linearLayout);
//                        Add card to container
                        mContainer.addView(card);

                        Toast.makeText(getContext(), "Data got: " + document.getData()/*.get("content").toString()*/, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w("content", "Error getting documents.", task.getException());
                }
            }});

        db.collection("users").document();
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
}
