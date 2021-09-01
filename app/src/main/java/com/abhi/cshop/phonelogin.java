package com.abhi.cshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class phonelogin extends AppCompatActivity {

    RelativeLayout details_layout,otp_layout,phone_layout;
    private FirebaseAuth mAuth;
    private EditText edtPhone, edtOTP,register_address,register_details_username;
    private Button verifyOTPBtn, generateOTPBtn,Register_complete;
    private String verificationId;
    String phone_number;
    private DatabaseReference dbcon;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonelogin);

        phone_layout = (RelativeLayout) findViewById(R.id.phone_layout);
        otp_layout = (RelativeLayout) findViewById(R.id.otp_layout);
        details_layout = (RelativeLayout) findViewById(R.id.details_layout);
        mAuth = FirebaseAuth.getInstance();
        dbcon = FirebaseDatabase.getInstance().getReference();
        register_address = findViewById(R.id.register_address);
        register_details_username = findViewById(R.id.register_details_username);
        pd = new ProgressDialog(this);

        Register_complete =findViewById(R.id.Register_complete);
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);

        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    Toast.makeText(phonelogin.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    phone_number = edtPhone.getText().toString().trim();
                    String phone = "+91" + edtPhone.getText().toString();
                    sendVerificationCode(phone);
                    phone_layout.setVisibility(View.GONE);
                    otp_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    Toast.makeText(phonelogin.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(edtOTP.getText().toString());
                }
            }
        });

        Register_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_address = register_address.getText().toString();
                String txt_username = register_details_username.getText().toString();
                if (TextUtils.isEmpty(txt_address) || TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(phonelogin.this , "Cannot be Empty" , Toast.LENGTH_SHORT).show();
                } else {
                    details(txt_address , txt_username);
                }
            }
        });
    }
    private void verifyCode(String code) {
        if (verificationId!=null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , code);

            signInWithCredential(credential);
        }else{
            Toast.makeText(phonelogin.this, "Try Again", Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("identification", "+91"+phone_number);
                            map.put("id", mAuth.getCurrentUser().getUid());
                            dbcon.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                            if (Globalvariable.check==false){
                                otp_layout.setVisibility(View.GONE);
                                details_layout.setVisibility(View.VISIBLE);
                            }else if (Globalvariable.check == true){
                                Intent i = new Intent(phonelogin.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else {
                            Toast.makeText(phonelogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void details(String txt_address,String txt_username) {
        pd.setMessage("Please Wait!");
        pd.show();
                HashMap<String, Object> map = new HashMap<>();
                map.put("phonenumber", "+91"+phone_number);
                map.put("Username", txt_username);
                map.put("Address", register_address);
                map.put("id", mAuth.getCurrentUser().getUid());
                System.out.println(map+"\n"+mAuth.getCurrentUser().getPhoneNumber());
                dbcon.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(phonelogin.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(phonelogin.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edtOTP.setText(code);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(phonelogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}