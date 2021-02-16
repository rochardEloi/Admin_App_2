package com.example.mtadminapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MessageSender extends AsyncTask<String, Void, String> {
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

            return "echec";
        }

        // return null;

    }

}