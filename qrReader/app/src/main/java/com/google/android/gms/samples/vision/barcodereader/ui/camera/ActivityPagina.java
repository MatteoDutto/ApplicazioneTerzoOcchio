package com.google.android.gms.samples.vision.barcodereader.ui.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.server_client_communication.Luogo;
import com.example.server_client_communication.TerzoOcchio_Server;
import com.google.android.gms.samples.vision.barcodereader.R;

import java.net.MalformedURLException;

public class ActivityPagina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina);

        TextView lblNomeLuogo = findViewById(R.id.lblNomeLuogo);
        TextView lblPeriodo = findViewById(R.id.lblPeriodo);
        TextView lblTesto = findViewById(R.id.lblTesto);

        Intent i = getIntent();
        String pagina = i.getStringExtra("pagina");
        String n_pagina = pagina.substring(47);

        Luogo luogo = new Luogo("Errore", "Errore", "Errore", "Errore");
        TerzoOcchio_Server server = new TerzoOcchio_Server();
        try {
            luogo = server.getQrData(n_pagina);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        lblNomeLuogo.setText(luogo.getNome());
        lblPeriodo.setText(luogo.getPeriodo());
        lblTesto.setText(luogo.getTesto());

    }
}
