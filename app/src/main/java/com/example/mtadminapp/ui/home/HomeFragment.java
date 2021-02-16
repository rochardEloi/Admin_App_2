package com.example.mtadminapp.ui.home;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    EditText lajan;
    Button btn_lajan;
    TextView balanceClient;
    String chaine;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        lajan = root.findViewById(R.id.lajan);
        btn_lajan = root.findViewById(R.id.btn_lajan);
        balanceClient = root.findViewById(R.id.balance_client);



        btn_lajan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageSender m = new MessageSender();
                String message = "globalInfo";
                lajan.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if(lajan.getText().toString().equals("")){
                    return;
                }
                try {
                    String reponse = m.execute(message).get();
                    m.cancel(true);
                    if(!reponse.equals("echec")){
                        String argentString = getInfoFromJson(reponse, "argent_depense");
                        if(!argentString.equals("echec")){
                            float argentFloat = Float.valueOf(argentString) + Float.valueOf(lajan.getText().toString());
                            MessageSender sendBalance = new MessageSender();
                            reponse = sendBalance.execute("recharge "+argentFloat).get();
                            if(!reponse.equals("echec"))
                            {

                                Toast.makeText(getContext(), "Nouvelle Balance : "+argentFloat, Toast.LENGTH_LONG).show();
                                balanceClient.setText("Balance : "+argentFloat+" gourges");
                                lajan.setText("");

                            }else
                                Toast.makeText(getContext(), "Il y a une erreur, veuiller recommencer", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Erreur de communication, Reseau Inactif", Toast.LENGTH_LONG).show();
                    }


                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erreur de communication", Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erreur de communication", Toast.LENGTH_LONG).show();
                }

            }
        });


        final Socket[] socket = new Socket[1];
        Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
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
                            balanceClient.setText("Balance : "+getInfoFromJson(chaine, "argent_depense")+" Gourdes");
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