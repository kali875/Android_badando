package com.example.beadando.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beadando.LoginMySql;
import com.example.beadando.MapsActivity;
import com.example.beadando.RegMySql;
import com.example.beadando.User;
import com.example.beadando.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity
{
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    public static boolean login_suc=false;
    public static int User_ID = 0;
    private static int reg_chekc=0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final TextView login_check = binding.LoginCheck;
        final Button reg = binding.reg;
        final EditText email = binding.Email;
        email.setVisibility(View.GONE);
        final EditText phonenumber = binding.PhoneNumber;
        phonenumber.setVisibility(View.GONE);
        final EditText password2 = binding.password2;
        password2.setVisibility(View.GONE);
        reg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(reg_chekc == 0)
                {
                    email.setVisibility(View.VISIBLE);
                    phonenumber.setVisibility(View.VISIBLE);
                    password2.setVisibility(View.VISIBLE);
                }
                if(reg_chekc == 1)
                {
                    email.setVisibility(View.GONE);
                    phonenumber.setVisibility(View.GONE);
                    password2.setVisibility(View.GONE);
                }
                String test = usernameEditText.getText().toString();
                if(!usernameEditText.getText().toString().matches("")  && !passwordEditText.getText().toString().matches("") && !email.getText().toString().matches("") && !phonenumber.getText().toString().matches("") && !password2.getText().toString().matches("") )
                {
                    String password = passwordEditText.getText().toString();
                    String password1 = password2.getText().toString();
                    if(password.equals(password1))
                    {
                        AsyncTask reg = (RegMySql) new RegMySql(login_check).execute(usernameEditText.getText().toString(),passwordEditText.getText().toString(),email.getText().toString(),phonenumber.getText().toString());
                    }
                    else
                    {
                        login_check.setText("Two password is different");
                    }
                 }
                reg_chekc += 1;
                if(reg_chekc > 1)
                {
                    reg_chekc = 0;
                }
            }

        });
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {

                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadingProgressBar.setVisibility(View.VISIBLE);
                AsyncTask login = (LoginMySql) new LoginMySql(login_check).execute(usernameEditText.getText().toString(),passwordEditText.getText().toString());

            }
        });
        login_check.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("Login Succes Fully") ||s.toString().equals(" Registration Succes Fully, please you login"))
                {
                    OpenGoogleActivty();
                }
            }
        });
    }


    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    public void OpenGoogleActivty()
    {
        User.ID=User_ID;

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}