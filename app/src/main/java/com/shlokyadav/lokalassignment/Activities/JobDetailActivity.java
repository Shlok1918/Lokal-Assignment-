package com.shlokyadav.lokalassignment.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shlokyadav.lokalassignment.Model.NetworkUtils;
import com.shlokyadav.lokalassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JobDetailActivity extends AppCompatActivity {

    private TextView company_name_tv, job_title_tv, exp_tv, salary_tv,
            qualification_tv, job_type_tv, location_tv, workingHrs_tv, numberOfopenings_tv,
            numberOfApplications_tv, content_tv, phone_no_tv;
    private ImageView whatsapp_link_iv,back_btn;
    private ScrollView scrollView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        scrollView = findViewById(R.id.scroll_view);
        progressBar = findViewById(R.id.progressBar);
        company_name_tv = findViewById(R.id.company_name);
        job_title_tv = findViewById(R.id.job_title_tv);
        exp_tv = findViewById(R.id.exp_tv);
        salary_tv = findViewById(R.id.salary_tv);
        qualification_tv = findViewById(R.id.qualification_tv);
        job_type_tv = findViewById(R.id.job_type_tv);
        location_tv = findViewById(R.id.location_tv);
        workingHrs_tv = findViewById(R.id.workingHrs_tv);
        numberOfopenings_tv = findViewById(R.id.numberOfopenings_tv);
        numberOfApplications_tv = findViewById(R.id.numberOfApplications_tv);
        //content_tv = findViewById(R.id.content_tv);
        phone_no_tv = findViewById(R.id.phone_no_tv);
        whatsapp_link_iv = findViewById(R.id.whatsapp_link);
        back_btn = findViewById(R.id.back_btn);


        String jobId = getIntent().getStringExtra("job_id");
        Log.d("jobId", "onCreate: " + jobId);

        if (!NetworkUtils.isConnectedToInternet(this)) {
            Toast.makeText(this, "No internet connection. Please connect to the internet first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchJobDetails(jobId);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    private void fetchJobDetails(String jobId) {
        String url = "https://testapi.getlokalapp.com/common/jobs";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jobsArray = response.getJSONArray("results");
                            for (int i = 0; i < jobsArray.length(); i++) {
                                JSONObject job = jobsArray.getJSONObject(i);

                                String id = job.optString("id", "N/A");

                                if (job.optString("id").equals(jobId)) {

                                    String title = job.optString("title", "N/A");

                                    JSONObject primaryDetails = job.optJSONObject("primary_details");
                                    String location = primaryDetails != null ? primaryDetails.optString("Place", "N/A") : "N/A";
                                    String salary = primaryDetails != null ? primaryDetails.optString("Salary", "N/A") : "N/A";
                                    String job_type = primaryDetails != null ? primaryDetails.optString("Job_Type", "N/A") : "N/A";
                                    String experience = primaryDetails != null ? primaryDetails.optString("Experience", "N/A") : "N/A";
                                    String qualification = primaryDetails != null ? primaryDetails.optString("Qualification", "N/A") : "N/A";


                                    String customLink = job.optString("custom_link", "");
                                    String phone = customLink.startsWith("tel:") ? customLink.replace("tel:", "") : "N/A";

                                    String company_name = job.optString("company_name", "");
                                    String whatsapp_no = job.optString("whatsapp_no", "N/A");
                                    String expire_on = job.optString("expire_on", "N/A");
                                    String job_hours = job.optString("job_hours", "N/A");
                                    String openings_count = job.optString("openings_count", "N/A");
                                    String other_details = job.optString("other_details", "N/A");
                                    String job_category = job.optString("job_category", "N/A");
                                    String num_applications = job.optString("num_applications", "N/A");
                                    String content = job.optString("content", "N/A");


                                    JSONObject contact_preference = job.optJSONObject("contact_preference");
                                    String whatsapp_link = contact_preference != null ? contact_preference.optString("whatsapp_link", "N/A") : "N/A";
                                    String preferred_call_start_time = contact_preference != null ? contact_preference.optString("preferred_call_start_time", "N/A") : "N/A";
                                    String preferred_call_end_time = contact_preference != null ? contact_preference.optString("preferred_call_end_time", "N/A") : "N/A";

                                    Log.d("lokal Data ", "responseData: " + title + " " + location + " " + salary + " " + job_type + " " + experience + " " +
                                            "" + qualification + " " + phone + " " + company_name + " " + whatsapp_no + " " +
                                            "" + whatsapp_link + " " + preferred_call_start_time + " " + preferred_call_end_time +
                                            " " + expire_on + " " + num_applications + " " + job_category + " " + other_details + " " + openings_count + " " + job_hours + " "
                                            + content);

                                    content = decodeUnicode(content);
                                    JSONObject contentJson = new JSONObject(content);
                                    StringBuilder contentBuilder = new StringBuilder();
                                    for (Iterator<String> it = contentJson.keys(); it.hasNext(); ) {
                                        String key = it.next();
                                        contentBuilder.append(contentJson.getString(key)).append("\n");
                                    }


                                    job_title_tv.setText(title);
                                    company_name_tv.setText(company_name);
                                    location_tv.setText(location);
                                    salary_tv.setText(salary);
                                    job_type_tv.setText(job_type);
                                    exp_tv.setText(experience);
                                    qualification_tv.setText(qualification);
                                    phone_no_tv.setText(phone);
                                    numberOfopenings_tv.setText(openings_count);
                                    numberOfApplications_tv.setText(num_applications);
                                  //  content_tv.setText(content);
                                    workingHrs_tv.setText(job_hours);

                                    progressBar.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);

                                    final String finalWhatsappLink = whatsapp_link;
                                    whatsapp_link_iv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!finalWhatsappLink.equals("N/A")) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(finalWhatsappLink));
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(JobDetailActivity.this, "No WhatsApp link available", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    final String finalPhone = phone;
                                    phone_no_tv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!finalPhone.equals("N/A")) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(Uri.parse("tel:" + finalPhone));
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(JobDetailActivity.this, "No phone number available", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("lokal", "jsonException: " + e);
                            Toast.makeText(JobDetailActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        error.printStackTrace();
                        Log.d("lokal", "jsonError: " + error);
                        Toast.makeText(JobDetailActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private String decodeUnicode(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < unicodeStr.length()) {
            char ch = unicodeStr.charAt(i);
            if (ch == '\\' && i + 1 < unicodeStr.length() && unicodeStr.charAt(i + 1) == 'u') {
                // Read the unicode value
                String unicodeValue = unicodeStr.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(unicodeValue, 16));
                i += 6;
            } else {
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }
}