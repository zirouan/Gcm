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
import android.widget.Toast;

import java.util.ArrayList;

import br.com.liveo.liveogcm.gcm.GcmHelper;
import br.com.liveo.liveogcm.util.Constant;
import br.com.liveo.liveogcm.util.Util;

public class MainActivity extends AppCompatActivity {

    private ListView mListaEntrada;
    private int mPosicaoAtualEntrada = 0;

    private ListView mListaSaida;
    private int mPosicaoAtualSaida = 0;

    private ArrayAdapter<String> mlstEntradaAdapter;
    private ArrayList<String> mEntrada = new ArrayList<>();

    private ArrayAdapter<String> mlstSaidaAdapter;
    private DataUpdateReceiver dataUpdateReceiver;
    private ArrayList<String> mSaida = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();

        if (dataUpdateReceiver == null) {
            dataUpdateReceiver = new DataUpdateReceiver();
        }

        IntentFilter intentFilter = new IntentFilter(Util.REFRESH_DATA_INTENT);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(dataUpdateReceiver);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPosicaoAtualEntrada = mListaEntrada.getFirstVisiblePosition();
        mPosicaoAtualSaida = mListaSaida.getFirstVisiblePosition();

        outState.putInt(Constant.STATE_LISTA_ENTRADA, mPosicaoAtualEntrada);
        outState.putStringArrayList(Constant.STATE_LISTA_ENTRADA_DADOS, mEntrada);
        outState.putInt(Constant.STATE_LISTA_SAIDA, mPosicaoAtualSaida);
        outState.putStringArrayList(Constant.STATE_LISTA_SAIDA_DADOS, mSaida);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mPosicaoAtualEntrada = savedInstanceState.getInt(Constant.STATE_LISTA_ENTRADA);
        mEntrada = savedInstanceState.getStringArrayList(Constant.STATE_LISTA_ENTRADA_DADOS);
        mPosicaoAtualSaida = savedInstanceState.getInt(Constant.STATE_LISTA_SAIDA);
        mSaida = savedInstanceState.getStringArrayList(Constant.STATE_LISTA_SAIDA_DADOS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListaEntrada = (ListView) findViewById(R.id.lstEntrada);
        mListaSaida = (ListView) findViewById(R.id.lstSaida);

        if (savedInstanceState != null) {
            mPosicaoAtualEntrada = savedInstanceState.getInt(Constant.STATE_LISTA_ENTRADA);
            mEntrada = savedInstanceState.getStringArrayList(Constant.STATE_LISTA_ENTRADA_DADOS);
            mPosicaoAtualSaida = savedInstanceState.getInt(Constant.STATE_LISTA_SAIDA);
            mSaida = savedInstanceState.getStringArrayList(Constant.STATE_LISTA_SAIDA_DADOS);

            mListaEntrada.setSelection(mPosicaoAtualEntrada);
            mListaSaida.setSelection(mPosicaoAtualSaida);
        }

        mlstEntradaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mEntrada);
        mlstSaidaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mSaida);

        mListaEntrada.setAdapter(mlstEntradaAdapter);
        mListaSaida.setAdapter(mlstSaidaAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInfo(mlstSaidaAdapter, mSaida, getString(R.string.registro_gcm));
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
                            addInfo(mlstSaidaAdapter, mSaida, getString(R.string.gcm_registred_id, regId));
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

                if (msg == null) {
                    msg = getString(R.string.notificacao_sem_texto);
                }

                addInfo(mlstEntradaAdapter, mEntrada, msg);
            }
        }
    }
}
