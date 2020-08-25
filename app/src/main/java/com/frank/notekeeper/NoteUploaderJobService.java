package com.frank.notekeeper;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.AsyncTask;

public class NoteUploaderJobService extends JobService {

    public static final String EXTRA_DATA_URI = "com.frank.notekeeper.extras.DATA_URI";
    private NoteUploader mNoteUploader;

    public NoteUploaderJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        @SuppressLint("StaticFieldLeak") AsyncTask<JobParameters, Void, Void> task = new AsyncTask<JobParameters, Void, Void>() {

            @Override
            protected Void doInBackground(JobParameters... backgroundParams) {

                JobParameters jobParams = backgroundParams[0];

                String stringDataUri = jobParams.getExtras().getString(EXTRA_DATA_URI);
                Uri dataUri = Uri.parse(stringDataUri);
                mNoteUploader.doUpload(dataUri);


                if ( ! mNoteUploader.isCanceled()) {
                    jobFinished(jobParams, false);
                }

                return null;
            }
        };
        mNoteUploader = new NoteUploader(this);
        task.execute(params);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mNoteUploader.cancel();
        return true;
    }


}
