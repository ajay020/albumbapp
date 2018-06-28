package project.gallery.com.createfolder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String digitArray[] = {"C", "+/-", "%", "/", "7", "8", "9", "*", "4", "5", "6", "-", "1", "2", "3", "+", "0", "00", ".", "="};
    // private String alphabetArray[] = {"", "ABC", "DEF", "GHI", "JKL", "MNO", "PQR", "STU", "VWX", "", "YZ"};
    private int[] password = new int[4];
    private int[] savedPassword = new int[4];
    private GridView gridView;
    private CustomDigitAdapter adapter;
    private TextView textViewFirst, textViewSecond, textViewThird, textViewFourth, tvTitlePasscode;
    private int dotPosition = 0;
    private EditText etPassword;
    private boolean positiveOrNegative = false;
    private String passwordString;
    private String savedPasswordString = "";
    private final static String PASSWORD_KEY = "password_key";
    private boolean isFirstTimeLogin = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getPasswordFromPref().equals("")) {
            isFirstTimeLogin = true;
        } else {
            isFirstTimeLogin = false;
        }
        etPassword = (EditText) findViewById(R.id.et_password);
     /*   textViewFirst = (TextView) findViewById(R.id.tv_first_dot);
        textViewSecond = (TextView) findViewById(R.id.tv_second_dot);
        textViewThird = (TextView) findViewById(R.id.tv_third_dot);
        textViewFourth = (TextView) findViewById(R.id.tv_fourth_dot);
        tvTitlePasscode = (TextView) findViewById(R.id.tv_login_title);*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String strWrongPassword = bundle.getString("WRONG_PASSWORD", "");
            if (!strWrongPassword.equals("")) {
                tvTitlePasscode.setText(strWrongPassword);
            }
        }

        gridView = (GridView) findViewById(R.id.gv_gird_view_digit);
        adapter = new CustomDigitAdapter(this, digitArray);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        // selectDotPoint(position);

        if (position != 17) {
            if (digitArray[position].equals("C")) {
                cancelPassword();
            } else if (digitArray[position].equals("+/-")) {
                if (positiveOrNegative == true) {
                    String str = etPassword.getText().toString();

                    if (str.length() > 0 && str.charAt(0) == '-') {
                        etPassword.getText().replace(0, 1, "+");
                    } else {
                        etPassword.getText().insert(0, "+");
                    }

                    positiveOrNegative = false;
                } else {
                    String str = etPassword.getText().toString();
                    if (str.length() > 0 && str.charAt(0) == '+') {
                        etPassword.getText().replace(0, 1, "-");
                    } else {
                        etPassword.getText().insert(0, "-");
                    }
                    positiveOrNegative = true;
                }
            } else if (digitArray[position].equals("=")) {
                passwordString = etPassword.getText().toString();

                if (isFirstTimeLogin == true) {
                    if (!savedPasswordString.equals("") && savedPasswordString.equals(passwordString)) {
                        Toast.makeText(this, "Right password", Toast.LENGTH_SHORT).show();
                        savePasswordIntoPreference(savedPasswordString);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (!savedPasswordString.equals("") && !savedPasswordString.equals(passwordString)) {
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                        savedPasswordString = "";
                        etPassword.getText().clear();
                    } else if (etPassword.length() == 0) {
                        Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                    } else if (savedPasswordString.equals("")) {
                        savedPasswordString = passwordString;
                        Toast.makeText(this, "ReEnter password", Toast.LENGTH_SHORT).show();
                        etPassword.getText().clear();
                    }

                } else {

                    if (!passwordString.equals(getPasswordFromPref())) {
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                        etPassword.getText().clear();
                    } else  {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            } else {
                String passwordDigit = digitArray[position];
                etPassword.append(passwordDigit);
            }
        }
    }

    private void cancelPassword() {
        int length = etPassword.getText().length();
        if (length > 0) {
            etPassword.getText().delete(length - 1, length);
        }
    }

   /* private void selectDotPoint(int position) {
        switch (dotPosition++) {
            case 0:
                textViewFirst.setBackgroundResource(R.drawable.solid_dot_circule);
                password[0] = position;
                break;
            case 1:
                textViewSecond.setBackgroundResource(R.drawable.solid_dot_circule);
                password[1] = position;
                break;
            case 2:
                textViewThird.setBackgroundResource(R.drawable.solid_dot_circule);
                password[2] = position;
                break;
            case 3:
                textViewFourth.setBackgroundResource(R.drawable.solid_dot_circule);
                password[3] = position;
                int[] savedPrefList = getPasswordFromPref();
                if(savedPrefList != null) {
                    if (isPasswordMatchedToPrefPass(savedPrefList)) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                         Toast.makeText(this, "pref doesn't have password", Toast.LENGTH_SHORT).show();
//                        resetDotPoint();
                        reloadActivity();
                    }
                }else{
                    if (tvTitlePasscode.getText().toString().equals("Retype Passcode")) {
                        if (isPasswordMatch()) {
                            Toast.makeText(this, "correct password", Toast.LENGTH_SHORT).show();
                            savePasswordIntoPreference();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            resetDotPoint();
                            clearPassword();
                            reloadActivity();
                        }
                    }

                    if (tvTitlePasscode.getText().toString().equals("Enter Passcode")) {
                        savePassword();
                        tvTitlePasscode.setText("Retype Passcode");
                        resetDotPoint();
                    }
                }



                break;
        }
    }
   */


    private String getPasswordFromPref() {
        SharedPreferences preferences = getSharedPreferences("PASSWORD_PREF", MODE_PRIVATE);
        String savedString = preferences.getString(PASSWORD_KEY, "");
        return savedString;
    }


    private void savePasswordIntoPreference(String savedPasswordString) {
        SharedPreferences preferences = getSharedPreferences("PASSWORD_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PASSWORD_KEY, savedPasswordString);
        editor.commit();
    }

    private void resetDotPoint() {
        dotPosition = 0;
        textViewFirst.setBackgroundResource(R.drawable.dot_circule);
        textViewSecond.setBackgroundResource(R.drawable.dot_circule);
        textViewThird.setBackgroundResource(R.drawable.dot_circule);
        textViewFourth.setBackgroundResource(R.drawable.dot_circule);
    }
}
