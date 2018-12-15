package com.webxert.listeningsouls;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webxert.listeningsouls.common.Constants;
import com.webxert.listeningsouls.models.User;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences reader;
    SharedPreferences.Editor writer;
    EditText email, password;
    CheckBox checkBox;
    Button login, signup;
    DatabaseReference users_ref;
    FirebaseAuth m_auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reader = getSharedPreferences(Constants.SH_PREFS, MODE_PRIVATE);
        writer = getSharedPreferences(Constants.SH_PREFS, MODE_PRIVATE).edit();
        checkBox = findViewById(R.id.is_admin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        m_auth = FirebaseAuth.getInstance();
        users_ref = FirebaseDatabase.getInstance().getReference("Users");

        login = findViewById(R.id.login);
        signup = findViewById(R.id.register);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                 finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    if (checkBox.isChecked())
                        writer.putString(Constants.AUTH_, Constants.Authentication.ADMIN.name());
                    else
                        writer.putString(Constants.AUTH_, Constants.Authentication.CUSTOMER.name());
                    writer.putBoolean(Constants.LOGIN_, true);
                    writer.apply();


                    m_auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            });


                }

            }
        });


    }
}
