package com.example.m14x.loginrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.regex.Pattern;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "JEPB";

    private EditText userTxt;
    private EditText emailTxt;
    private Button regBtn;
    private boolean isUserValid = false;
    private boolean isEmailValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userTxt = (EditText) findViewById(R.id.userTxt);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        regBtn = (Button) findViewById(R.id.registerButton);
        regBtn.setOnClickListener(this);
        //regBtn.setAlpha(0.0f);

       /* Observable.just(emailTxt.getText().toString()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(LOG_TAG,s);
            }
        }); */
        setUp();

    }

    public void setUp(){

        final Observable<Boolean> emailValid = RxTextView.textChanges(emailTxt).map(new Func1<CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence) {

                boolean isValid = Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),charSequence);
                Log.i(LOG_TAG,"Email Status" + "  "+charSequence);

                return isValid;
            }
        });
        RxView.focusChanges(emailTxt)
                .withLatestFrom(emailValid, new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean hasFocus, Boolean m_Vaild) {
                        boolean band = false;
                        // Log.i(LOG_TAG,"Focus "+String.valueOf(hasFocus)+" Valid "+String.valueOf(m_Vaild));
                        if((hasFocus == true && m_Vaild == true) || m_Vaild == true){
                            band = true;
                        }
                        return band;
                    }
                }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                // Log.i(LOG_TAG,"EMAIL VALIDATION"+String.valueOf(aBoolean));
                // Log.i(LOG_TAG,String.valueOf(aBoolean)+" EMAIL ");
                if(aBoolean == false){
                    //Log.i(LOG_TAG,"Enter a valid value");
                    //emailTxt.setError("Email Correct");
                    emailTxt.setError("Enter a valid value");
                }
            }
        });

        final Observable<Boolean> userNameValid = RxTextView.textChanges(userTxt).map(new Func1<CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence) {
                //Log.i(LOG_TAG,charSequence.toString());
                Log.i(LOG_TAG,"Username Status" + "  "+charSequence);
                boolean band = true;
                if(userTxt.length()<4){
                    band = false;
                }
                //Log.i(LOG_TAG,String.valueOf(band));
                return band;
            }
        });

        RxView.focusChanges(userTxt)
                .withLatestFrom(userNameValid, new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean hasFocus, Boolean userValid) {
                        boolean band = false;
                        //Log.i(LOG_TAG,"Focus "+String.valueOf(hasFocus)+" Valid "+String.valueOf(userValid));
                        if((hasFocus == true && userValid == true) || userValid == true){
                            band = true;
                        }
                        return band;
                    }
                }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                // Log.i(LOG_TAG,String.valueOf(aBoolean)+" USER ");
                if(aBoolean == false){
                    //Log.i(LOG_TAG,"Enter a valid value");
                    userTxt.setError("Should be at least 4 characters ");
                }
            }
        });

        Observable.combineLatest(emailValid, userNameValid, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean emailIsValid, Boolean userIsValid) {

                Log.i(LOG_TAG,"Final result "+String.valueOf(emailIsValid)+"-"+String.valueOf(userIsValid));
                boolean band = false;
                if(emailIsValid == true && userIsValid == true){
                    regBtn.setAlpha(1.0f);
                    band = true;
                    isUserValid = true;
                    isEmailValid = true;
                }

                return band;
            }
        }).subscribe(RxView.enabled(regBtn));



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                if (isUserValid == true && isEmailValid == true){
                    Toast.makeText(MainActivity.this, "Attemping to Sign in", Toast.LENGTH_SHORT).show();
                    userTxt.setText("");
                    emailTxt.setText("");
                    userTxt.requestFocus();
                }
                break;

            default:
                break;
        }
    }
}


