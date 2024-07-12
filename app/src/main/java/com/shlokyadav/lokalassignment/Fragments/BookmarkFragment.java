package com.shlokyadav.lokalassignment.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shlokyadav.lokalassignment.Adapter.JobsAdapter;
import com.shlokyadav.lokalassignment.Model.JobsModel;
import com.shlokyadav.lokalassignment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookmarkFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<JobsModel> jobsModelList;
    private JobsAdapter jobsAdapter;

    private RecyclerView bookmark_rv;
    private TextView noBookmarksTv;

    public BookmarkFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        bookmark_rv = view.findViewById(R.id.bookmark_rv);
        noBookmarksTv = view.findViewById(R.id.no_bookmarks_tv);
        jobsModelList = new ArrayList<>();
        bookmark_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        jobsAdapter = new JobsAdapter(requireContext(), jobsModelList);
        bookmark_rv.setAdapter(jobsAdapter);

        loadBookmarked();

        return view;
    }

    private void loadBookmarked() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("BookmarkedJobs", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        if (allEntries.isEmpty()) {
            noBookmarksTv.setVisibility(View.VISIBLE);
            bookmark_rv.setVisibility(View.GONE);
        } else {
            noBookmarksTv.setVisibility(View.GONE);
            bookmark_rv.setVisibility(View.VISIBLE);
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                try {
                    String jsonString = (String) entry.getValue();
                    JSONObject articleObject = new JSONObject(jsonString);

                    String title = articleObject.getString("Title");
                    String location = articleObject.getString("Location");
                    String salary = articleObject.getString("Salary");
                    String phone = articleObject.getString("Phone");
                    String id = articleObject.getString("id");

                    JobsModel jobs = new JobsModel(title, location, salary, phone, id);
                    jobsModelList.add(jobs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jobsAdapter.notifyDataSetChanged();
        }
    }
}
