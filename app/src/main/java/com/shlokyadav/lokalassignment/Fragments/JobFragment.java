package com.shlokyadav.lokalassignment.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shlokyadav.lokalassignment.Adapter.JobsAdapter;
import com.shlokyadav.lokalassignment.Model.JobsModel;
import com.shlokyadav.lokalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JobFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView jobs_rv;
    private RequestQueue requestQueue;
    private List<JobsModel> jobsModelList;
    private JobsAdapter jobsAdapter;
    private ProgressBar progressBar;

    public JobFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        requestQueue = Volley.newRequestQueue(requireContext());
        jobsModelList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_job, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        jobs_rv = view.findViewById(R.id.jobs_rv);
        jobs_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        jobsAdapter = new JobsAdapter(requireContext(), jobsModelList);
        jobs_rv.setAdapter(jobsAdapter);


            fetchJobsData();


        return view;
    }



    private void fetchJobsData() {
        String url = "https://testapi.getlokalapp.com/common/jobs?page=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("lokal", "onResponse: "+ response);

                        try {
                            JSONArray jobsArray = response.getJSONArray("results");
                            for (int i = 0; i < jobsArray.length(); i++) {
                                JSONObject job = jobsArray.getJSONObject(i);

                                String title = job.optString("title", "N/A");
                                String id = job.optString("id","N/A");
                                JSONObject primaryDetails = job.optJSONObject("primary_details");
                                String location = primaryDetails != null ? primaryDetails.optString("Place", "N/A") : "N/A";
                                String salary = primaryDetails != null ? primaryDetails.optString("Salary", "N/A") : "N/A";

                                String customLink = job.optString("custom_link", "");
                                String phone = customLink.startsWith("tel:") ? customLink.replace("tel:", "") : "N/A";

                                progressBar.setVisibility(View.GONE);
                                Log.d("LOKAL JOB DATA", "onResponse: "+ title +location+ salary+phone+id);
                                jobsModelList.add(new JobsModel(title, location, salary, phone,id));
                            }
                            jobsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("lokal", "jsonException: "+e);
                            Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("lokal", "jsonError: "+error);
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}
