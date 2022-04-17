package com.example.projectii.controller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.TutorModel;
import com.example.projectii.view.TutorAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FindTutorFragment extends Fragment {
    RecyclerView rv;
    ArrayList<TutorModel> tutorList;
    TutorAdapter tutorAdapter;
    FirebaseFirestore fireStore;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learner_findtutor, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarId);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Choose from the best");
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.white));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching tutors... ");

        setHasOptionsMenu(true);
        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        fireStore = FirebaseFirestore.getInstance();
        tutorList = new ArrayList<>();
        tutorAdapter = new TutorAdapter(tutorList, getContext());
        EventChangeListener();
        rv.setAdapter(tutorAdapter);
        return view;
    }

    private void filter(String newText) {
        ArrayList<TutorModel> filteredList = new ArrayList<>();
        for (TutorModel item : tutorList) {
            if (item.getFullName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        tutorAdapter.filterList(filteredList);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Tutors");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void EventChangeListener() {
        fireStore.collection("Tutors").whereEqualTo("available",true).orderBy("fullName", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error", e.getMessage());
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        tutorList.add(dc.getDocument().toObject(TutorModel.class));
                        tutorAdapter.notifyDataSetChanged();
                    }
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
}
