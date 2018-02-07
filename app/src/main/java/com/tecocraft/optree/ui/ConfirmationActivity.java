package com.tecocraft.optree.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.ConfirmModel;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    @BindView(R.id.input_code)
    EditText inputCode;
    private ProgressDialog pd;
    private CommonUtils commonUtils;
    private SharedPref sharedPref;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(ConfirmationActivity.this, R.color.white));
        }

        pd = new ProgressDialog(ConfirmationActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        commonUtils = new CommonUtils(ConfirmationActivity.this);
        sharedPref = new SharedPref(ConfirmationActivity.this);
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString(sharedPref.EMAIL);
        password = bundle.getString(sharedPref.PASSWORD);
    }

    @OnClick({ R.id.btn_home, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                if (!validateCode()) {
                    return;
                }
                commonUtils.hideKeyboard(ConfirmationActivity.this);
                setCode();
                break;

            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void setCode() {
        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ConfirmModel> call = apiService.confirmCode(email, password, inputCode.getText().toString());
            call.enqueue(new Callback<ConfirmModel>() {
                @Override
                public void onResponse(Call<ConfirmModel> call, Response<ConfirmModel> response) {
                    if (response.isSuccessful()) {
                        ConfirmModel model = response.body();
                        if (model.getSuccess()) {
                            sharedPref.setBoolean("isLogin", true);
                            sharedPref.setDataInPref(sharedPref.USER_ID, model.getId());
//                            Toast.makeText(ConfirmationActivity.this, "You are register successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ConfirmationActivity.this, "Something went wrong while get confirmation code.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();

                }

                @Override
                public void onFailure(Call<ConfirmModel> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                }
            });
        } else {
            CommonUtils.alertDialog(ConfirmationActivity.this,getString(R.string.internet_disconnect));
//            Toast.makeText(ConfirmationActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCode() {
        String email = inputCode.getText().toString().trim();

        if (email.isEmpty()) {
            CommonUtils.alertDialog(ConfirmationActivity.this, getString(R.string.err_confirm_Code));
            //inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputCode);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
