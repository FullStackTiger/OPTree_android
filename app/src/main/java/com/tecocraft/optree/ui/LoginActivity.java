package com.tecocraft.optree.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.favourite.addFavourite;
import com.tecocraft.optree.model.login.LoginModel;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //save checkbox
    private String useremail,password;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private EditText inputEmail, inputPassword;
    Button btnSignUp, btnLogin;
    Call<LoginModel> call;
    private ProgressDialog pd;
    private CommonUtils commonUtils;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawableResource(R.drawable.login);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryDark));
        }

        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Loading...");
        commonUtils = new CommonUtils(LoginActivity.this);
        sharedPref = new SharedPref(LoginActivity.this);


        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        //btnLogin.setOnClickListener(this);
        saveLoginCheckBox=(CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences=getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor=loginPreferences.edit();
        saveLogin=loginPreferences.getBoolean("saveLogin", false);
        if(saveLogin==true){
            inputEmail.setText(loginPreferences.getString("useremail",""));
            inputPassword.setText(loginPreferences.getString("password",""));

        }
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputEmail.getWindowToken(),0);
                useremail=inputEmail.getText().toString();
                password=inputPassword.getText().toString();
                if(saveLoginCheckBox.isChecked()){
                    loginPrefsEditor.putBoolean("saveLogin",true);
                    loginPrefsEditor.putString("useremail",useremail);
                    loginPrefsEditor.putString("password",password);
                    loginPrefsEditor.commit();

                } else{
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                //doSomethingElse();
                submitForm();

            }
        });
    }

    private void doSomethingElse() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        LoginActivity.this.finish();
    }


    private boolean validateEmail(EditText inputEmail) {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            CommonUtils.alertDialog(LoginActivity.this, "Invalid email address","Error");
            // inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            // inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private void submitForm() {

        if (inputEmail.getText().toString().trim().equals("")) {
            commonUtils.alertDialog(LoginActivity.this,getString(R.string.err_login),"Error");
        }else if (inputPassword.getText().toString().trim().equals("")){
            commonUtils.alertDialog(LoginActivity.this,getString(R.string.err_login),"Error");
        }else{
            login();
        }
//        if (!validateEmail(inputEmail)) {
//            return;
//        }
//
//        if (!validatePassword()) {
//            return;
//        }
// login();
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            CommonUtils.alertDialog(LoginActivity.this, getString(R.string.err_msg_password));
            // inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            //inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void login() {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.login("" + inputEmail.getText().toString().trim(), "" + inputPassword.getText().toString().trim());
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()) {
                        LoginModel model = response.body();
                        if (model.isSuccess()) {
                            sharedPref.setBoolean(sharedPref.LOGIN, true);
                            sharedPref.setDataInPref(sharedPref.USER_ID, model.getId());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            CommonUtils.alertDialog(LoginActivity.this,model.getMessage(),"Error");
//                            Toast.makeText(LoginActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();

                }
            });
        } else {
            CommonUtils.alertDialog(LoginActivity.this,getString(R.string.internet_disconnect));
//            Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public Dialog createCustomLoader() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_forgot_password);

        final EditText input_email = (EditText) dialog.findViewById(R.id.input_email);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvSend = (TextView) dialog.findViewById(R.id.tvSend);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail(input_email)) {
                    return;
                }
                commonUtils.hideKeyboard(LoginActivity.this,input_email);
                sendPassword(input_email.getText().toString().trim(),dialog);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

//    public void showForgotPassDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.layout_forgot_password, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText input_email = (EditText) dialogView.findViewById(R.id.input_email);
//
//        dialogBuilder.setTitle("Forgot Password");
////        dialogBuilder.setMessage("Enter text below");
//        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //do something with edt.getText().toString();
//                Toast.makeText(LoginActivity.this, "email "+input_email.getText().toString(), Toast.LENGTH_SHORT).show();
//                if (!validateEmail(input_email)) {
//                    return;
//                }
//                sendPassword(input_email.getText().toString(), dialog);
//
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
//    }

    @OnClick(R.id.btnForgotPassword)
    public void onClick() {
        createCustomLoader();
    }

    private void sendPassword(final String email, final Dialog dialog) {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<addFavourite> call = apiService.forgetPassword(email);
            call.enqueue(new Callback<addFavourite>() {
                @Override
                public void onResponse(Call<addFavourite> call, Response<addFavourite> response) {
                    if (response.isSuccessful()) {
                        addFavourite model = response.body();
                        if (model.getSuccess()) {
                            dialog.dismiss();
                            CommonUtils.alertDialog(LoginActivity.this, "We sent changed password to "+email
                                    +". You can login with that confirm code.","Success");
//                            Toast.makeText(LoginActivity.this, "Please check email address for password.", Toast.LENGTH_SHORT).show();
                        } else {
                            CommonUtils.alertDialog(LoginActivity.this, "That email address is not exist","Error");
//                            Toast.makeText(LoginActivity.this, "Something went wrong while change password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }

                @Override
                public void onFailure(Call<addFavourite> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }
            });
        } else {
            CommonUtils.alertDialog(LoginActivity.this,getString(R.string.internet_disconnect));
//            Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
