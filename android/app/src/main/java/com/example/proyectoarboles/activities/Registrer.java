package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoarboles.R;

public class Registrer extends AppCompatActivity {

    EditText inputNombre;
    EditText inputUsuario;
    EditText inputPassword;
    Button volverButton;
    Button registrerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrer);

        inputNombre = findViewById(R.id.editTextNombreRegistrer);
        inputUsuario = findViewById(R.id.editTextUsuarioRegistrer);
        inputPassword = findViewById(R.id.editTextPasswordRegistrer);

        volverButton = findViewById(R.id.buttonVolver);
        registrerButton = findViewById(R.id.buttonRegistrer);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrer.this, Login.class);
                startActivity(intent);
            }
        });

        registrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrer.this, ListarArboles.class);

                if (inputNombre.getText().toString().isBlank()){
                    inputNombre.setError("Este campo no puede quedar en blanco");
                }else if (inputUsuario.getText().toString().isBlank()){
                    inputUsuario.setError("Este campo no puede quedar en blanco");
                }else if (inputPassword.getText().toString().isBlank()){
                    inputPassword.setError("Este campo no puede quedar en blanco");
                }else{
                    startActivity(intent);
                }

            }
        });
    }
}