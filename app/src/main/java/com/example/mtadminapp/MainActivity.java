package com.example.mtadminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    String ssid;
    EditText username,password;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String monUsername = "1234";
        String monPassword = "1234";
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn = findViewById(R.id.btn_connexion);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
             ssid = wifiInfo.getSSID();

            //Toast.makeText(getApplicationContext(), ssid.substring(1, 11), Toast.LENGTH_LONG).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifyIfIsCompteur = ssid.substring(1, 11);
                if(!verifyIfIsCompteur.equals("MAGI_TECK_")){
                    Toast.makeText(getApplicationContext(), "SSID non valide", Toast.LENGTH_LONG).show();
                    return;
                }                Toast.makeText(getApplicationContext(), username.getText().toString()+" "+password.getText().toString(), Toast.LENGTH_LONG).show();
                if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Aucun champ ne doit etre vide ", Toast.LENGTH_LONG).show();
                }else{
                    if(username.getText().toString().equals(monUsername) && password.getText().toString().equals(monPassword)){
                        Intent intent = new Intent(MainActivity.this, menu.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Username ou mot de passe incorrect ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });





        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageSender messageSender = new MessageSender();
                String message = "globalInfo";
                try {
                    String reponse = messageSender.execute(message).get();
                    messageSender.cancel(false);
                    textView.setText(reponse);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });*/
    }

   /* public class MessageSender extends AsyncTask<String, Void, String> {
        private Context context;

        Socket s;
        DataOutputStream dos;
        PrintWriter pw;
        @Override
        protected String doInBackground(String... voids) {
                 String message = voids[0];
                 System.out.println("Message : "+ message);
                 String chaine;
                try {
                    Socket socket;
                    // adresse IP du serveur
                    InetAddress adr = InetAddress.getByName("192.168.4.22");

                    // ouverture de connexion avec le serveur sur le port 7777
                    socket = new Socket(adr, 5045);
                    // construction de flux objets à partir des flux de la socket
                    BufferedWriter output;
                    output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader input;
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // écriture d'une chaîne dans le flux de sortie : c'est-à-dire envoi de
                    // données au serveur
                    output.write(new String(message));
                    output.flush();
                    // attente de réception de données venant du serveur (avec le readObject)
                    // on sait qu'on attend une chaîne, on peut donc faire un cast directement
                    chaine = (String) input.readLine();
                    System.out.println("\nReponse :  "+chaine);
                    socket.close();

                        return chaine;

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("\nReponse : Non Non ");

                    return "Non Non";
                }

               // return null;

        }

    }*/


}