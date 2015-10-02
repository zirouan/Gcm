package br.com.liveo.liveogcm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.liveo.liveogcm.gcm.GcmHelper;
import br.com.liveo.liveogcm.util.Util;

public class MainActivity extends AppCompatActivity {

    TextView mTvIdGcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTvIdGcm = (TextView) findViewById(R.id.tvIdGCM);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupGCM();
            }
        });
    }

    private void setupGCM() {
        try {
            if (Util.checkConnection(this)) {
                if (GcmHelper.googlePlayServicesIsAvailable(this)) {
                    GcmHelper.register(this, new GcmHelper.TheRegisterDevice() {
                        @Override
                        public void toRegister(String regId, boolean inBackground) {
                            mTvIdGcm.setText(regId);
                            Toast.makeText(getApplicationContext(), R.string.warning_gcmplay_successful, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(this, R.string.error_gcmplay_unavailable, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, R.string.error_gcmplay_register_failure, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
