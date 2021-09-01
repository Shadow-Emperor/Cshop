package com.abhi.cshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abhi.cshop.model.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class LoginSignup extends AppCompatActivity {

    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashscreenscreen);
        setContentView(R.layout.activity_login_signup);
        login=findViewById(R.id.LoginI);
        register=findViewById(R.id.RegisterI);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSignup.this.startActivity(new Intent(LoginSignup.this, Login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSignup.this.startActivity(new Intent(LoginSignup.this, Register.class));
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}