package com.example.seireivom;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.seireivom.model.MovieModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MovieDetails extends AppCompatActivity {

    public ImageView imageViewDetails;
    public TextView titleDetails, descDetails;
    public RatingBar ratingBarDetails;
    public String client;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference watchlistRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        imageViewDetails = findViewById(R.id.imageView_details);
        titleDetails = findViewById(R.id.textView_title_details);
        descDetails = findViewById(R.id.textView_desc_details);
        descDetails.setMovementMethod(new ScrollingMovementMethod());
        ratingBarDetails = findViewById(R.id.ratingBar_details);
        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")) {
            MovieModel movieModel = getIntent().getParcelableExtra("movie");
            titleDetails.setText(movieModel.getTitle());
            descDetails.setText(movieModel.getMovie_overview());
            ratingBarDetails.setRating((movieModel.getVote_average()) / 2);

            Log.v("Tagy", "X" + movieModel.getMovie_overview());


            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500/"
                            + movieModel.getPoster_path())
                    .into(imageViewDetails);

            final String[] title = {""};
            Button button = findViewById(R.id.add_to_watchlist);
            client = getString(R.string.facebook_client_token);
            watchlistRef = db.collection("users").document(client).collection("watchlist");
            watchlistRef.whereEqualTo("title", movieModel.getTitle()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    button.setText(R.string.already);
                                    button.setClickable(false);
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                button.setText(R.string.add_to_watchlist);
                                button.setClickable(true);
                            }
                        }
                    });

            button.setOnClickListener(v -> {
                // Add a new document with a generated id.
                Map<String, Object> data = new HashMap<>();
                data.put("title",String.valueOf(titleDetails.getText()));
                data.put("movie_overview",String.valueOf(descDetails.getText()));
                data.put("poster_path",movieModel.getPoster_path());
                data.put("vote_average",Float.toString(movieModel.getVote_average()));
                db.collection("users").document(client).collection("watchlist").document(movieModel.getTitle())
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });

            });
        }
    }
}

