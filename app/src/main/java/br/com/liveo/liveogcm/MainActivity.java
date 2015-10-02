package br.com.liveo.liveogcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.liveo.liveogcm.gcm.GcmHelper;
import br.com.liveo.liveogcm.util.Util;

public class MainActivity extends AppCompatActivity {

    DataUpdateReceiver dataUpdateReceiver;

    ListView mListaEntrada;
    ArrayAdapter<String> mlstEntradaAdapter;
    ArrayList<String> mEntrada;

    ListView mListaSaida;
    ArrayAdapter<String> mlstSaidaAdapter;
    ArrayList<String> mSaida;

    @Override
    protected void onResume() {
        super.onResume();

        if (dataUpdateReceiver == null)
            dataUpdateReceiver = new DataUpdateReceiver();

        IntentFilter intentFilter = new IntentFilter(Util.REFRESH_DATA_INTENT);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListaEntrada = (ListView) findViewById(R.id.lstEntrada);
        mListaSaida = (ListView) findViewById(R.id.lstSaida);

        mEntrada = new ArrayList<String>();
        mSaida = new ArrayList<String>();

        mlstEntradaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mEntrada);
        mlstSaidaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSaida);

        mListaEntrada.setAdapter(mlstEntradaAdapter);
        mListaSaida.setAdapter(mlstSaidaAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInfo(mlstSaidaAdapter, mSaida, "Registrando GCM ID...");
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
                            addInfo(mlstSaidaAdapter, mSaida, "GCM ID: " + regId);
                        }
                    });

                } else {
                    Toast.makeText(this, R.string.error_gcmplay_unavailable, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.error_gcmplay_register_failure, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void addInfo(ArrayAdapter<String> adapter, ArrayList<String> lista, String texto) {
        lista.add(texto);
        adapter.notifyDataSetChanged();
    }

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Util.REFRESH_DATA_INTENT)) {
                String msg = intent.getStringExtra("gcm_texto");

                if (msg == null)
                    msg = "Notificação sem texto";

                addInfo(mlstEntradaAdapter, mEntrada, msg);
            }
        }
    }
}
