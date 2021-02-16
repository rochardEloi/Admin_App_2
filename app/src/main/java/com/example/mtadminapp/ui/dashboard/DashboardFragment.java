package com.example.mtadminapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.concurrent.ExecutionException;

public class DashboardFragment extends Fragment {
    Button activer, desactiver, capacite_rateplain;
    EditText capacite, rateplain;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        activer = root.findViewById(R.id.btn_actif);
        desactiver = root.findViewById(R.id.btn_inactif);
        capacite_rateplain = root.findViewById(R.id.btn_capacite_rateplain);
        capacite = root.findViewById(R.id.capacite);
        rateplain = root.findViewById(R.id.rateplain);

        activer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageSender m = new MessageSender();
                try {
                    String reponse = m.execute("updateStatut Actif").get();
                    m.cancel(true);
                    if(!reponse.equals("echec") && !reponse.equals("ECHEC")){
                        Toast.makeText(getContext(), "Activation Reussi", Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(getContext(), "Une erreur s'est produit, recommence", Toast.LENGTH_LONG).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        desactiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageSender m = new MessageSender();
                try {
                    String reponse = m.execute("updateStatut Inactif").get();
                    m.cancel(true);
                    if(!reponse.equals("echec") && !reponse.equals("ECHEC")){
                        Toast.makeText(getContext(), "Desactivation Reussi", Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(getContext(), "Une erreur s'est produit, recommence", Toast.LENGTH_LONG).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        capacite_rateplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(capacite.getText().toString().equals("") || rateplain.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Aucun champ ne doit etre vide", Toast.LENGTH_LONG).show();
                    return;
                }
                MessageSender m = new MessageSender();
                try {
                    String reponse = m.execute("rateplain "+capacite.getText().toString()+" "+rateplain.getText().toString()).get();
                    m.cancel(true);
                    if(!reponse.equals("echec") && !reponse.equals("ECHEC")){
                        Toast.makeText(getContext(), "Capacite : "+capacite.getText().toString()+" Rateplain : "+rateplain.getText().toString(), Toast.LENGTH_LONG).show();
                        capacite.setText("");
                    }else
                        Toast.makeText(getContext(), "Une erreur s'est produit, recommence", Toast.LENGTH_LONG).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        return root;
    }
}