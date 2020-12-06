package com.sonisuciadi.si52.myasynctak;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements  MyAsyncTaksCallBack{
    public static final String TAG="MyAsynctaks";
    private TextView title;
    private ProgressBar progressBar;
    private Button btnStart;
    private MyAsyncTask myAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.tvTitle);
        progressBar=findViewById(R.id.progresBar);
        btnStart=findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAsyncTask!=null){
                    AsyncTask.Status status= myAsyncTask.getStatus();
                    switch (status){
                        case PENDING:
                            myAsyncTask.execute();
                            break;
                        case RUNNING:
                            Toast.makeText(MainActivity.this, "Asyntask is Running", Toast.LENGTH_SHORT).show();
                            break;
                        case FINISHED:
                            myAsyncTask=new MyAsyncTask(MainActivity.this);
                            myAsyncTask.execute();
                            break;
                    }
                }else {
                    myAsyncTask=new MyAsyncTask(MainActivity.this);
                    myAsyncTask.execute();
                }
            }
        });
    }

    @Override
    public void onPreExecute() {
        progressBar.setProgress(0);

    }

    @Override
    public void onUpdateProgres(Long value) {
        final double maxprogres= 10000.0;
        double progres=100*(value/maxprogres);
        progressBar.setProgress((int) progres);
    }

    @Override
    public void onPostExecute(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private static class MyAsyncTask extends AsyncTask<Void, Long,Void>{
        WeakReference<MyAsyncTaksCallBack> myAsyncTaksCallBackWeakReference;

        public MyAsyncTask(MyAsyncTaksCallBack myAsyncTaksCallBack) {
            this.myAsyncTaksCallBackWeakReference = new WeakReference<>(myAsyncTaksCallBack);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.myAsyncTaksCallBackWeakReference.get().onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            long delayTime=2000;
            long startTime=0;

            for (int i=0; i<5;i++){
                try {
                    Thread.sleep(delayTime);
                    publishProgress(startTime+=delayTime);
                }catch (Exception e){
                    Log.d(TAG, e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {

            super.onProgressUpdate(values);
            long value=values[0];
            this.myAsyncTaksCallBackWeakReference.get().onUpdateProgres(value);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.myAsyncTaksCallBackWeakReference.get().onPostExecute("Finish");
        }
    }

}