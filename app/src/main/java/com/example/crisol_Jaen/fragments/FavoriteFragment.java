package com.example.crisol_Jaen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crisol_Jaen.LoginActivity;
import com.example.crisol_Jaen.R;
import com.example.crisol_Jaen.adapters.FavoriteBooksAdapter;
import com.example.crisol_Jaen.model.Book;
import com.example.crisol_Jaen.model.User;
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

public class FavoriteFragment extends Fragment {

    private FirebaseAuth mAuth;

    private RecyclerView rvFavorites;
    private FavoriteBooksAdapter favoriteBooksAdapter;
    private ArrayList<Book> favoriteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        LinearLayout guestview = root.findViewById(R.id.guestview);
        LinearLayout userview = root.findViewById(R.id.userview);
        if (currentUser != null) {
            guestview.setVisibility(LinearLayout.GONE);

            rvFavorites = root.findViewById(R.id.rvFavorites);
            rvFavorites.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            favoriteBooksAdapter = new FavoriteBooksAdapter(getContext());
            rvFavorites.setAdapter(favoriteBooksAdapter);
            favoriteList = new ArrayList<>();
            getBooks();
        }
        else {
            userview.setVisibility(LinearLayout.GONE);
            // btnLogin
            Button btnLogin = root.findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
        }
        return root;
    }

    private void getBooks() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("user")
                .whereEqualTo("id", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                List<String> favorites = user.getFavoriteBooks();
                                if (favorites == null) { break; } // Si no hay favoritos, termina el método
                                for (String favId : favorites) {
                                    db.collection("book").document(favId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        favoriteList.add(new Book(
                                                                document.getId(),
                                                                document.getString("title"),
                                                                document.getString("summary"),
                                                                document.getString("authorId"),
                                                                document.getString("image"),
                                                                document.getDouble("price")
                                                        ));
                                                        favoriteBooksAdapter.agregarelementos(favoriteList);
                                                    } else { System.out.println("ERROR LIBROS"+ task.getException()); }
                                                }
                                            });
                                } }
                        } else {
                            System.out.println("ERROR LIBROS"+ task.getException());
                        }
                    }
                });
    }
}
