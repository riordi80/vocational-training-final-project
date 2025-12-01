package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;

public class Login extends AppCompatActivity {

    EditText inputUsuario;
    EditText inputPassword;
    Button loginButton;
    Button registrerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        inputUsuario = findViewById(R.id.editTextUsuario);
        inputPassword = findViewById(R.id.editTextPassword);

        loginButton = findViewById(R.id.buttonLogin);
        registrerButton = findViewById(R.id.buttonRegistrer);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ListarArboles.class);

                if (inputUsuario.getText().toString().isBlank()){
                    inputUsuario.setError("Este campo no puede quedar en blanco");
                }else if (inputPassword.getText().toString().isBlank()){
                    inputPassword.setError("Este campo no puede quedar en blanco");
                }else{
                    startActivity(intent);
                }
            }
        });

        registrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registrer.class);
                startActivity(intent);
            }
        });
    }
}