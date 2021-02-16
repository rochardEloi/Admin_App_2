package com.example.mtadminapp.ui.notifications;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mtadminapp.MessageSender;
import com.example.mtadminapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NotificationsFragment extends Fragment {
    TextView numeroCompteur, puissance, prix, capacite, argentDepense, argentRestant, wifiStrength, status;
    String chaine;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        numeroCompteur = root.findViewById(R.id.numero_compteur);
        puissance = root.findViewById(R.id.puissance);
        prix = root.findViewById(R.id.prix);
        capacite = root.findViewById(R.id.capacite);
        argentDepense = root.findViewById(R.id.argent_depense);
        argentRestant = root.findViewById(R.id.argent_restant);
        wifiStrength = root.findViewById(R.id.wifi_strength);
        status = root.findViewById(R.id.status);
        final Socket[] socket = new Socket[1];
        Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                // loop until the thread is interrupted
                boolean change_ = true;
                while (change_){
                    try{
                        InetAddress adr = InetAddress.getByName("192.168.4.22");
                        // adresse IP du serveur
                        // ouverture de connexion avec le serveur sur le port 7777
                        socket[0] = new Socket(adr, 5045);
                        // construction de flux objets à partir des flux de la socket
                        BufferedWriter output;
                        output = new BufferedWriter(new OutputStreamWriter(socket[0].getOutputStream()));
                        BufferedReader input;
                        input = new BufferedReader(new InputStreamReader(socket[0].getInputStream()));

                        // écriture d'une chaîne dans le flux de sortie : c'est-à-dire envoi de
                        // données au serveur
                        output.write(new String("globalInfo"));
                        output.flush();
                        // attente de réception de données venant du serveur (avec le readObject)
                        // on sait qu'on attend une chaîne, on peut donc faire un cast directement
                        chaine = (String) input.readLine();

                        socket[0].close();

                    }catch(IOException e){
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Il y a une erreur, recommence", Toast.LENGTH_LONG).show();
                        return;
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            numeroCompteur.setText("Numero Compteur : "+getInfoFromJson(chaine, "numero_compteur"));
                            puissance.setText("Puissance : "+120*Float.valueOf(getInfoFromJson(chaine, "amperage"))+" watts");
                            prix.setText("Prix : "+getInfoFromJson(chaine, "prix"));
                            capacite.setText("Capacite : "+getInfoFromJson(chaine, "capasite"));
                            argentDepense.setText("Balance : "+getInfoFromJson(chaine, "argent_depense"));
                            argentRestant.setText("Depense : "+getInfoFromJson(chaine, "argent_restant"));
                            wifiStrength.setText("Intensite Signal : "+getInfoFromJson(chaine, "wifi_strength"));
                            status.setText("Statut : "+ getInfoFromJson(chaine,"status"));
                        }
                    });

                }


            }
        }).start();

        return root;
    }

    public String getInfoFromJson(String message,String info) {

        try {
            JSONObject jsonObj = new JSONObject(message);
            String value = jsonObj.getString(info);
            return value;

        } catch (JSONException e) {
            e.printStackTrace();
            //addBalance(info)

            return "echec";
        }



    }
}