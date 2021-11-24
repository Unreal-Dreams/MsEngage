package com.printhub.printhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.printhub.printhub.HomeScreen.MainnewActivity;
import com.printhub.printhub.registration.DetailActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    EditText emailEditText, passwordEditText;
    ProgressDialog progressDialog;
    SharedPreferences detail = null,cityNameSharedPref,collegeNameSharedPref,userIdSharedPref;
    Button signInButton, signupButton,forgetPass;
    private String firebaseUserId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        emailEditText = findViewById(R.id.email_Edit_Text);
        passwordEditText= findViewById(R.id.password_edit_text);
        signInButton = findViewById(R.id.sign_in_button);
        signupButton= findViewById(R.id.sign_up_button);
        forgetPass=findViewById(R.id.forgetPass);
        detail = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        collegeNameSharedPref = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        cityNameSharedPref = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        userIdSharedPref = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
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
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEditText.getText().toString();
                if(email.length()==0){
                    Toast.makeText(LoginActivity.this, "Please Enter Your Registered email id",
                            Toast.LENGTH_SHORT).show();
                }else{
                    ResetPass(email);
                }
            }
        });

    }

    private void ResetPass(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email Sent To Your Registered MailID",
                                    Toast.LENGTH_SHORT).show();
                        }
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
                            if(user!= null){
                                progressDialog = new ProgressDialog(LoginActivity.this);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setTitle("Getting user data..");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                firebaseUserId= user.getUid();
                                userIdSharedPref.edit().putString("userId",firebaseUserId).apply();
                                db.collection("users").document(firebaseUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            collegeNameSharedPref.edit().putString("collegeName",documentSnapshot.getString("collegeName")).apply();
                                            cityNameSharedPref.edit().putString("cityName", documentSnapshot.getString("cityName")).apply();
                                            db.collection(documentSnapshot.getString("cityName")).document(documentSnapshot.getString("collegeName"))
                                                    .collection("users").document(firebaseUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        progressDialog.dismiss();
                                                        detail.edit().putBoolean("fillDetails", true).apply();
                                                        Intent mainIntent = new Intent(LoginActivity.this, MainnewActivity.class);
                                                        startActivity(mainIntent);
                                                        finish();
                                                    }else{
                                                        progressDialog.dismiss();
                                                        Intent detailFill = new Intent(LoginActivity.this, DetailActivity.class);
                                                        startActivity(detailFill);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }else{
                                            progressDialog.dismiss();
                                            Intent detailFill = new Intent(LoginActivity.this, DetailActivity.class);
                                            startActivity(detailFill);
                                            finish();
                                        }
                                    }
                                });
                            }else{
                                finish();
                            }

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