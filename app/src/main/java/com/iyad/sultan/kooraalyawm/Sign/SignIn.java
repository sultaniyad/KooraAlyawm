package com.iyad.sultan.kooraalyawm.Sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Utilities.UiValidator;
// DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
import java.util.Arrays;
import java.util.List;

import javax.xml.validation.Validator;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private static final String USER_UID = "USER_UID";
    private static final int RC_SIGN_IN = 101;

    //Views
    private ProgressBar progressBar;
    private TextView mCrateAccount;
    private EditText mEmail;
    private EditText mPassword;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private ProgressBar mProgressBar;


    private AppCompatButton btnLogin;

    //SharePreference
    SharedPreferences preferences;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        drawUI();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }
    private void drawUI(){
        mCrateAccount =(TextView)findViewById(R.id.link_signup);
        mCrateAccount.setOnClickListener(this);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        mEmail = findViewById(R.id.input_email);
        inputLayoutEmail = findViewById(R.id.layout_input_email);
        mPassword = findViewById(R.id.input_password);
        inputLayoutPassword= findViewById(R.id.layout_input_password);
        mProgressBar = (ProgressBar) findViewById(R.id.signIndeterminateBar);

    }



    // SingIn create a new user
    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setLogo(R.drawable.ic_launcher_foreground).build(),RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RC_SIGN_IN == 101){
            IdpResponse response = IdpResponse.fromResultIntent(data);

                if(resultCode == RESULT_OK){
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(this, "Singed Successfully" + user.getUid(), Toast.LENGTH_SHORT).show();
                    //add user info to rest of tree



                    //updateUI()
                    //Add User UID to DB with user new info
        }
        else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    Toast.makeText(this, "Failed check user or password", Toast.LENGTH_SHORT).show();
                }
        }

    }

    /*SingIn methods*/
    public void singOut(){

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SignIn.this, "SingOut Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
        // [END auth_fui_signout]
    }

    public void signIn(String email,String password){

       showProgressDialog();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                final Handler handler = new Handler();

                if(task.isSuccessful()){

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            showProgressDialog();
                            Toast.makeText(SignIn.this, "Logging Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }, 3000);


                }

                else {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            hideProgressDialog();
                            Toast.makeText(SignIn.this, "Check your email and Password", Toast.LENGTH_LONG).show();
                        }
                    }, 3000);
                }


                  //  hideProgressDialog();
                  //  Log.w(TAG, "signInWithEmail:failure", task.getException());
                //    updateUI(null);
                }
                //



        });


    }

    private void hideProgressDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void updateUI(FirebaseUser user) {



    }


    //ensure Entered input are valid
    private boolean validEmail( UiValidator validator ,String email) {


        // return (validator.isValidEmail(email) && validator.isValidPasswors(password));

        if (!validator.isValidEmail(email)) {
            {
                inputLayoutEmail.setError(getResources().getString(R.string.invalid_email));
                return false;
            }
        } else {
            inputLayoutEmail.setErrorEnabled(false);
            return true;
        }


    }
    private boolean validPassword(UiValidator validator ,String password) {

        if (validator.isValidPassword(password)) {
            inputLayoutPassword.setError(getResources().getString(R.string.invalid_password));
            return false;
        }

        else {
            inputLayoutPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void submitForm(){

        UiValidator mValidator = new UiValidator();

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        //check entered input are correct
        if ((validEmail(mValidator,email) && validPassword(mValidator,password)))
            signIn(email,password);
    }

    //User info if he logged
    public void saveUIDLocally(String UID){

        preferences = getSharedPreferences(USER_UID,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_UID,UID);
        editor.putBoolean(USER_UID,true);
        editor.apply();

    }
    public boolean isLoggedBefore(){
     preferences = getPreferences(MODE_PRIVATE);
        return preferences.getBoolean(USER_UID,false);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser  != null)
        finish();
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.link_signup: createSignInIntent();break;
            case R.id.btn_login: submitForm();

        }
    }
}
