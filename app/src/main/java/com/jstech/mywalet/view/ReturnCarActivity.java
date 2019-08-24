package com.jstech.mywalet.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jstech.mywalet.R;

public class ReturnCarActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonReturnCar;
    EditText userOtp;
    EditText userPhone;

    DatabaseReference dbRef;
    FirebaseAuth auth;
    Query query;

    String otpDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_car);
        buttonReturnCar = (Button)findViewById(R.id.btnReturnCar);
        userOtp = (EditText)findViewById(R.id.editTextOTP);
        userPhone = (EditText)findViewById(R.id.editTextPhoneReturn);

        dbRef = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();
        query = dbRef.orderByKey();
        buttonReturnCar.setOnClickListener(this);
    }


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
    public void deleteUser(String otp){

        dbRef = FirebaseDatabase.getInstance().getReference();
        query = dbRef.child("Cars").orderByChild("passC").equalTo(otp);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot emSnapshot: dataSnapshot.getChildren()) {
                    emSnapshot.getRef().removeValue();
                }
                userOtp.setText("");
                userPhone.setText("");
                Toast.makeText(getApplicationContext(),"\nOTP matched!", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Title", "onCancelled", databaseError.toException());
            }
        });
        hideProgressDialog();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(userPhone.getText().toString())) {
            userPhone.setError("Required");
            result = false;
        } else {
            userPhone.setError(null);
        }
        if (TextUtils.isEmpty(userOtp.getText().toString())) {
            userOtp.setError("Required");
            result = false;
        } else {
            userOtp.setError(null);
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        validateForm();
        showProgressDialog();
        otpDel = userOtp.getText().toString().trim();
        deleteUser(otpDel);
    }
}
