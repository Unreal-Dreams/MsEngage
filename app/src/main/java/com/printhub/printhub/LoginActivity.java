package com.printhub.printhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.printhub.printhub.HomeScreen.MainnewActivity;
import com.printhub.printhub.registration.DetailActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    EditText emailEditText, passwordEditText;
    Button signInButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        emailEditText = findViewById(R.id.email_Edit_Text);
        passwordEditText= findViewById(R.id.password_edit_text);
        signInButton = findViewById(R.id.sign_in_button);
        signupButton= findViewById(R.id.sign_up_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });
    }

    private void signUp(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            startActivity(new Intent(LoginActivity.this, DetailActivity.class));

                            Intent myIntent = new Intent(LoginActivity.this, DetailActivity.class);
                            String[] split = email.split("@");
                            String temp = split[1];
                            String[] split2=temp.split(".");
                            String collegeName=split2[0];
                            Log.e("name", "College Name"+collegeName);
//                            myIntent.putExtra("name",collegeName);

                            startActivity(myIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainnewActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}