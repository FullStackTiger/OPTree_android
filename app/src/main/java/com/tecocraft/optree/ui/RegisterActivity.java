package com.tecocraft.optree.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.SignupModel;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tecocraft.optree.R.id.input_password;
import static com.tecocraft.optree.R.string.err_msg_matchpassword;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.input_email)
    EditText inputEmail;

    @BindView(input_password)
    EditText inputPassword;

    @BindView(R.id.input_conpassword)
    EditText inputConPassword;

    @BindView(R.id.btn_signup)
    Button btnSignup;

    Call<SignupModel> call;
    private ProgressDialog pd;
    private CommonUtils commonUtils;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.signup);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimaryDark));
        }

        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        commonUtils = new CommonUtils(RegisterActivity.this);
        sharedPref = new SharedPref(RegisterActivity.this);

//        inputEmail.setText("hirpa@mailinator.com");
//        inputPassword.setText("123456");
//        inputConPassword.setText("123456");
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            CommonUtils.alertDialog(RegisterActivity.this, getString(R.string.err_msg_email));
            //inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            // inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private void submitForm() {

        if (inputEmail.getText().toString().trim().equals("")) {
            commonUtils.alertDialog(RegisterActivity.this, getString(R.string.err_signup_fill), "Error");
        } else if (!isValidEmail(inputEmail.getText().toString().trim())) {
            commonUtils.alertDialog(RegisterActivity.this, "Please enter valid email address", "Error");
        } else if (inputPassword.getText().toString().trim().equals("")) {
            commonUtils.alertDialog(RegisterActivity.this, getString(R.string.err_signup_fill), "Error");
        } else if (!inputConPassword.getText().toString().trim().equals(inputPassword.getText().toString().trim())) {
            commonUtils.alertDialog(RegisterActivity.this, getString(R.string.err_signup_pass_not_match), "Error");
        } else {
            commonUtils.hideKeyboard(RegisterActivity.this);
            register();
        }


//        if (!validateEmail()) {
//            return;
//        }
//
//        if (!validatePassword()) {
//            return;
//        }
//
//        if (!validateConPassword()) {
//            return;
//        }
//        if (!validateBothPassword()) {
//            return;
//        }
//
//        register();
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            CommonUtils.alertDialog(RegisterActivity.this, getString(R.string.err_msg_password));
            //inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            //  inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConPassword() {
        if (inputConPassword.getText().toString().trim().isEmpty()) {
            CommonUtils.alertDialog(RegisterActivity.this, getString(R.string.err_msg_password));
            // inputLayoutConPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputConPassword);
            return false;
        } else {
            //  inputLayoutConPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateBothPassword() {
        if (!inputPassword.getText().toString().equals(inputConPassword.getText().toString())) {
            CommonUtils.alertDialog(RegisterActivity.this, getString(err_msg_matchpassword));
            //  inputLayoutConPassword.setError(getString(R.string.err_msg_matchpassword));
            requestFocus(inputConPassword);
            return false;
        } else {
            // inputLayoutConPassword.setErrorEnabled(false);
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

    private void register() {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.signUp("" + inputEmail.getText().toString().trim(), "" + inputPassword.getText().toString().trim());
            call.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                    if (response.isSuccessful()) {
                        SignupModel model = response.body();

                        if (model.isSuccess()) {
                            sharedPref.setDataInPref(sharedPref.USER_ID, model.getId());

                            alertDialog();
                        } else {
                            Intent intent = new Intent(RegisterActivity.this, ConfirmationActivity.class);
                            intent.putExtra(sharedPref.EMAIL, inputEmail.getText().toString().trim());
                            intent.putExtra(sharedPref.PASSWORD, inputPassword.getText().toString().trim());
                            startActivity(intent);
                        }

//                        if (model.isSuccess()) {
//
//                            sharedPref.setDataInPref(sharedPref.USER_ID, model.getId());
//                            Intent intent = new Intent(RegisterActivity.this, ConfirmationActivity.class);
//                            intent.putExtra(sharedPref.EMAIL, inputEmail.getText().toString());
//                            intent.putExtra(sharedPref.PASSWORD, inputPassword.getText().toString());
//
////                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        } else {
////                            if (model.getMessage().equals("User already exist")) {
////                                sharedPref.setDataInPref(sharedPref.USER_ID, model.getId());
////                                Intent intent = new Intent(RegisterActivity.this, ConfirmationActivity.class);
////                                intent.putExtra(sharedPref.EMAIL, inputEmail.getText().toString());
////                                intent.putExtra(sharedPref.PASSWORD, inputPassword.getText().toString());
////
//////                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(intent);
////                                finish();
////                            } else
////                                Toast.makeText(RegisterActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            sharedPref.setDataInPref(sharedPref.USER_ID, model.getId());
//                            Intent intent = new Intent(RegisterActivity.this, ConfirmationActivity.class);
//                            intent.putExtra(sharedPref.EMAIL, inputEmail.getText().toString());
//                            intent.putExtra(sharedPref.PASSWORD, inputPassword.getText().toString());
////                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                }

                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                }
            });
        } else {
            CommonUtils.alertDialog(RegisterActivity.this, getString(R.string.internet_disconnect));
//            Toast.makeText(RegisterActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void alertDialog() {
        new AlertDialog.Builder(RegisterActivity.this)
                .setMessage("We just sent a code to mail\n" + inputEmail.getText().toString().trim())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();

                        Intent intent = new Intent(RegisterActivity.this, ConfirmationActivity.class);
                        intent.putExtra(sharedPref.EMAIL, inputEmail.getText().toString().trim());
                        intent.putExtra(sharedPref.PASSWORD, inputPassword.getText().toString().trim());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .show();
    }

    @OnClick({R.id.back, R.id.btn_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.btn_signup:
                submitForm();
                break;
        }
    }
}
