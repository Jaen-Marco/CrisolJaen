package com.example.crisol_Jaen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crisol_Jaen.model.Book;
import com.example.crisol_Jaen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ImageView ivImage = findViewById(R.id.ivImage);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        Button btnBuy = findViewById(R.id.btnBuy);
        TextView tvSummary = findViewById(R.id.tvSummary);

        if(getIntent().hasExtra("book")){
            book = getIntent().getParcelableExtra("book");
            // Utilizando la librería Picasso
            Picasso.get().load(book.getImage()).into(ivImage);
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthorId());
            btnBuy.setText("Comprar por " + book.getPrice());
            tvSummary.setText(book.getSummary());

            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser currentUser = mAuth.getCurrentUser();

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser != null) {
                        Intent intPay = new Intent(BookDetailActivity.this, PayActivity.class);
                        intPay.putExtra("book", book);
                        startActivity(intPay);
                    } else {
                        startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
                    }
                }
            });
        }

        // toolbar
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        initMenu();
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.topfavorite:
                        addOrDeleteFavoriteBook(item);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void addOrDeleteFavoriteBook(MenuItem item)
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // add or delete
        final Drawable iconFav = ContextCompat.getDrawable(BookDetailActivity.this, R.drawable.ic_favorite_black_24dp);
        final Drawable iconNoFav = ContextCompat.getDrawable(BookDetailActivity.this, R.drawable.ic_favorite_border_black_24dp);
        if (currentUser != null) {
            db.collection("user")
                    .whereEqualTo("id", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    if (user.getFavoriteBooks() == null) {
                                        user.setFavoriteBooks(new ArrayList<String>());
                                    }
                                    if (!user.getFavoriteBooks().contains(book.getId())) {
                                        ActionMenuItemView menu = findViewById(R.id.topfavorite);
                                        menu.setIcon(iconFav);

                                        user.setFavoriteBook(book.getId());

                                        db.collection("user").document(document.getId())
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(BookDetailActivity.this, "Marcado como favorito.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(BookDetailActivity.this, "Error al agregar favorito.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        ActionMenuItemView menu = findViewById(R.id.topfavorite);
                                        menu.setIcon(iconNoFav);

                                        user.deleteFavoriteBook(book.getId());

                                        db.collection("user").document(document.getId())
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(BookDetailActivity.this, "Eliminado de favoritos.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(BookDetailActivity.this, "Error al eliminar favorito.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
        else {
            // local storage
        }
    }

    private void initMenu() {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.collection("user")
                    .whereEqualTo("id", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    List<String> favorites = user.getFavoriteBooks();
                                    if (favorites == null) {
                                        break;
                                    }
                                    if (!favorites.isEmpty()) {
                                        for (String fav : favorites) {
                                            if (fav.equals(book.getId())) {
                                                ActionMenuItemView menu = findViewById(R.id.topfavorite);
                                                menu.setIcon(ContextCompat.getDrawable(BookDetailActivity.this, R.drawable.ic_favorite_black_24dp));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
        else {
            // local storage
        }
    }
}
