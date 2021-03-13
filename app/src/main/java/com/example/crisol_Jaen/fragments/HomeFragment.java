package com.example.crisol_Jaen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crisol_Jaen.R;
import com.example.crisol_Jaen.adapters.FavoritesHomeAdapter;
import com.example.crisol_Jaen.adapters.HomeBooksAdapter;
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

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private RecyclerView rvRecents, rvDiscover, rvFavorites;
    private ArrayList<Book> recentsList, discoverList, favoritesList;
    private HomeBooksAdapter recentsAdapter, discoverAdapter;
    private FavoritesHomeAdapter favoritesAdapter;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        rvRecents = root.findViewById(R.id.rvRecents);
        rvRecents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentsAdapter = new HomeBooksAdapter(getContext());
        rvRecents.setAdapter(recentsAdapter);
        recentsList = new ArrayList<>();


        rvDiscover = root.findViewById(R.id.rvDiscover);
        rvDiscover.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        discoverAdapter = new HomeBooksAdapter(getContext());
        rvDiscover.setAdapter(discoverAdapter);
        discoverList = new ArrayList<>();


        rvFavorites = root.findViewById(R.id.rvFavoritesHome);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        favoritesAdapter = new FavoritesHomeAdapter(getContext());
        rvFavorites.setAdapter(favoritesAdapter);
        favoritesList = new ArrayList<>();
        getBooks();
        getFavorites();

        return root;
    }

    private void getBooks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("book")
                .get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                                if (count <= 5) {
                                    recentsList.add(new Book(
                                            document.getId(),
                                            document.getString("title"),
                                            document.getString("summary"),
                                            document.getString("authorId"),
                                            document.getString("image"),
                                            document.getDouble("price")
                                    ));
                                } else {
                                    discoverList.add(new Book(
                                            document.getId(),
                                            document.getString("title"),
                                            document.getString("summary"),
                                            document.getString("authorId"),
                                            document.getString("image"),
                                            document.getDouble("price")
                                    ));
                                }}
                            recentsAdapter.agregarelementos(recentsList);
                            discoverAdapter.agregarelementos(discoverList);
                        } else {
                            System.out.println("ERROR LIBROS" + task.getException());
                        }
                    }
                });
    }

    private void getFavorites() {
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
                                if (favorites == null) {
                                    break;
                                }
                                for (String favId : favorites) {
                                    db.collection("book").document(favId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        favoritesList.add(new Book(
                                                                document.getId(),
                                                                document.getString("title"),
                                                                document.getString("summary"),
                                                                document.getString("authorId"),
                                                                document.getString("image"),
                                                                document.getDouble("price")
                                                        ));
                                                        favoritesAdapter.agregarFavoritos(favoritesList);
                                                    } else {
                                                        System.out.println("ERROR LIBROS" + task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                        } else {
                            System.out.println("ERROR LIBROS" + task.getException());
                        }
                    }
                });
    }
}