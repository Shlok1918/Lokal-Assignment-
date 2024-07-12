package com.shlokyadav.lokalassignment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shlokyadav.lokalassignment.Activities.JobDetailActivity;
import com.shlokyadav.lokalassignment.Model.JobsModel;
import com.shlokyadav.lokalassignment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    Context context;
    List<JobsModel> jobsModelList;

    public JobsAdapter(Context context, List<JobsModel> jobsModelList) {
        this.context = context;
        this.jobsModelList = jobsModelList;
    }

    @NonNull
    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_item,parent,false);

        return  new JobsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsAdapter.ViewHolder holder, int position) {

        JobsModel jobsModel = jobsModelList.get(position);
        holder.title_tv.setText(jobsModel.getTitle());
        holder.location_tv.setText(jobsModel.getLocation());
        holder.salary_tv.setText(jobsModel.getSalary());
        holder.phone_tv.setText(jobsModel.getPhone());


        holder.job_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, JobDetailActivity.class);
                i.putExtra("job_id", jobsModel.getId());
                Log.d("idpassing", "onClick: "+jobsModel.getId());
                context.startActivity(i);
            }
        });

        SharedPreferences sharedPreferences = context.getSharedPreferences("BookmarkedJobs", Context.MODE_PRIVATE);
        String articleKey = "article_" + jobsModel.getTitle();
        boolean isSaved = sharedPreferences.contains(articleKey);
        if (isSaved) {
            holder.bookmark_btn.setImageResource(R.drawable.bookmark_filled_icon);
        } else {
            holder.bookmark_btn.setImageResource(R.drawable.bookmark_blank_icon);
        }

        holder.bookmark_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (sharedPreferences.contains(articleKey)) {
                    editor.remove(articleKey);
                    editor.apply();
                    holder.bookmark_btn.setImageResource(R.drawable.bookmark_blank_icon);
                    Toast.makeText(context, "Bookmarked removed", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id",jobsModel.getId());
                        jsonObject.put("Title", jobsModel.getTitle());
                        jsonObject.put("Location", jobsModel.getLocation());
                        jsonObject.put("Salary", jobsModel.getSalary());
                        jsonObject.put("Phone", jobsModel.getPhone());

                        editor.putString(articleKey, jsonObject.toString());
                        editor.apply();
                        holder.bookmark_btn.setImageResource(R.drawable.bookmark_filled_icon);
                        Toast.makeText(context, "Bookmarked", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged(); // Refresh the RecyclerView
            }
        });

    }

    @Override
    public int getItemCount() {
        return jobsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv,location_tv,salary_tv,phone_tv;
        ImageView bookmark_btn;

        CardView job_card;


        public ViewHolder(@NonNull View v) {
            super(v);

            title_tv = v.findViewById(R.id.title_tv);
            location_tv = v.findViewById(R.id.location_tv);
            salary_tv = v.findViewById(R.id.salary_tv);
            phone_tv = v.findViewById(R.id.phone_tv);
            bookmark_btn = v.findViewById(R.id.bookmark_btn);
            job_card = v.findViewById(R.id.job_card);

        }
    }
}
