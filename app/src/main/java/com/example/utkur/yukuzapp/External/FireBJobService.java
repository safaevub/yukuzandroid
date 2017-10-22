package com.example.utkur.yukuzapp.External;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Muhammadjon on 10/21/2017.
 */

public class FireBJobService extends JobService {

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
