package com.abhi.cshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.cshop.model.Globalvariable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    public FirebaseAuth mauth;
    public DatabaseReference dbcon;
    private TextView lnklog;
    public EditText newemail;
    public EditText newpass;
    ProgressDialog pd;
    private Button register;
    public EditText usernmae;
    ImageButton phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_register);
        register = (Button) findViewById(R.id.Register);
        lnklog = (TextView) findViewById(R.id.login_link);
        newpass = (EditText) findViewById(R.id.register_password);
        newemail = (EditText) findViewById(R.id.register_email);
        usernmae = (EditText) findViewById(R.id.register_username);
        phone = (ImageButton) findViewById(R.id.phone);


        mauth = FirebaseAuth.getInstance();

        dbcon = FirebaseDatabase.getInstance().getReference();
        pd = new ProgressDialog(this);
        lnklog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this , Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = newemail.getText().toString();
                String txt_password = newpass.getText().toString();
                String txt_username = usernmae.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Register.this , "Empty" , Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Register.this , "small password" , Toast.LENGTH_SHORT).show();
                } else {
                    registeruser(txt_email , txt_password , txt_username);
                }
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globalvariable.check = false;
                Intent intent = new Intent(Register.this,phonelogin.class);
                startActivity(intent);
            }
        });

    }
    public void registeruser(final String email, String password, final String name) {
        pd.setMessage("Please Wait!");
        pd.show();
        mauth.createUserWithEmailAndPassword(email, password).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("identification", email);
                map.put("Username", name);
                map.put("id", mauth.getCurrentUser().getUid());
                dbcon.child("Users").child(Register.this.mauth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                pd.dismiss();
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}