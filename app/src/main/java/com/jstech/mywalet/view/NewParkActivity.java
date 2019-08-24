package com.jstech.mywalet.view;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jstech.mywalet.R;
import com.jstech.mywalet.model.Car;

public class NewParkActivity extends AppCompatActivity implements AdapterView.OnClickListener{

    private static final String TAG = "NewParkActivity";
    //For registering the car
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Car car = null;


    Button btnSubmit;
    EditText carNumber;
    EditText userPhone;




    String phoneOtp;
    String msgOtp;
    String passcodeOtp;

    //Base Activity start
    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    //base activity ends


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_park);

        car = new Car();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        btnSubmit = (Button)findViewById(R.id.btnOkay);
        carNumber = (EditText)findViewById(R.id.editTextNumber);
        userPhone = (EditText)findViewById(R.id.editTextPhone);
        btnSubmit.setOnClickListener(this);
    }



    private void signUp() {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        String number = carNumber.getText().toString();
        String phone = userPhone.getText().toString();
        String email = phone+getUid()+"@nobody.com";
        String password = "nobody";
        String passcode = getUid().substring((getUid().length()-4),(getUid().length()));
        car.setNumber(number);
        car.setPhone(phone);
        car.setPassC(passcode);


        passcodeOtp = passcode;
        phoneOtp = phone;
        msgOtp = "Hi, OTP for your car ("+carNumber.getText()+") is "+passcodeOtp+".\nPlease provide it on the time of return!";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser(), car);
                        } else {
                            Toast.makeText(NewParkActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void onAuthSuccess(FirebaseUser fUser, Car car) {
        carNumber.setText("");
        userPhone.setText("");
        Toast.makeText(NewParkActivity.this, "Car will be parked. An OTP will be sent to the car owner via sms.", Toast.LENGTH_LONG).show();
        writeNewUser(fUser.getUid(),car.getNumber(), car.getPhone(), car.getPassC());
    }


    private void writeNewUser(String userId, String carNumber, String userPhone, String passCode) {
        Car car = new Car(carNumber, userPhone, passCode);
        mDatabase.child("Cars").child(userId).setValue(car);
        sendSMS();
        //String passcode = getUid().substring((getUid().length()-4),(getUid().length()));
    }

    void sendSMS()
    {
        //Toast.makeText(NewParkActivity.this, phoneOtp+": "+msgOtp, Toast.LENGTH_LONG).show();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneOtp,null,msgOtp,null,null);
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(carNumber.getText().toString())) {
            carNumber.setError("Required");
            result = false;
        } else {
            carNumber.setError(null);
        }
        if (TextUtils.isEmpty(userPhone.getText().toString())) {
            userPhone.setError("Required");
            result = false;
        } else {
            userPhone.setError(null);
        }
        return result;
    }



    @Override
    public void onClick(View view) {
        signUp();
    }
}
