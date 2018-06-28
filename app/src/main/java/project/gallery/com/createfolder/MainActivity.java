package project.gallery.com.createfolder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    ArrayList<String> folderNameList = new ArrayList<String>();
    private CustomGridAdapter gridAdapter;
    /**
     * Declaring an ArrayAdapter to set items to ListView
     */
    GridView gridView;
    EditText edit;
    TextView textView;
    boolean isLongClick;
    SharedPreferences preferences;
    private static String folder_name_key = "folder_list";
    private String folder_name;
    private boolean isPause = false;
    private boolean isBackPressed = false;
    private Button btnEditPassword;
    private final static String PASSWORD_KEY = "password_key";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("FOLDER_PREF", MODE_PRIVATE);
        getFolderNameFromPreference();
        Toast.makeText(getApplicationContext(), "onCreate main activity", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        btnEditPassword = (Button) findViewById(R.id.btn_edit_password);
        gridView = (GridView) findViewById(R.id.list);
        Button btn = (Button) findViewById(R.id.btnAdd);
        textView = (TextView) findViewById(R.id.txtItem);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data();
            }
        });
        gridAdapter = new CustomGridAdapter(this, folderNameList);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword();
            }
        });
    }

    private void editPassword() {
        AlertDialog.Builder alert;
        LayoutInflater li = LayoutInflater.from(this);
        View popView = li.inflate(R.layout.editpassword_popup, null);
        alert = new AlertDialog.Builder(this);
        alert.setView(popView);
        final AlertDialog Alert1;
        Alert1 = alert.create();
        Alert1.show();
        Alert1.setCancelable(true);
        Button btnEdit = (Button) Alert1.findViewById(R.id.btn_edit_popup);
        final EditText editText = (EditText) Alert1.findViewById(R.id.et_edit_password_popup);
        String password = getPasswordFromPref();
        editText.setText(password);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() > 0) {
                    savePasswordIntoPreference(editText.getText().toString());
                }
                Alert1.dismiss();

            }
        });
    }

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

    private void getFolderNameFromPreference() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(folder_name_key, null);

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        if (arrayList != null) {
            folderNameList.clear();
            folderNameList.addAll(arrayList);

        } else {
            Toast.makeText(this, "arrlist is null", Toast.LENGTH_LONG).show();
        }
    }

    public void data() {

        AlertDialog.Builder alert;
        LayoutInflater li = LayoutInflater.from(this);
        View popView = li.inflate(R.layout.popup, null);
        alert = new AlertDialog.Builder(this);
        alert.setView(popView);
        final AlertDialog Alert1;
        Alert1 = alert.create();
        Alert1.show();
        Alert1.setCancelable(true);
        Button btnAdd = (Button) Alert1.findViewById(R.id.btnAdd);
        edit = (EditText) Alert1.findViewById(R.id.txtItem);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().length() != 0) {
                    folder_name = edit.getText().toString().trim();
                    saveFolderNameInToPreference(edit.getText().toString().trim());

                } else {
                    Toast.makeText(getApplicationContext(), "Folder Name Empty", Toast.LENGTH_SHORT).show();
                }
                Alert1.dismiss();

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "OnResume main activity = " + isPause, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "OnStart main activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "OnStop main activity", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "OnRestart main activity", Toast.LENGTH_SHORT).show();
        getFolderNameFromPreference();
        gridAdapter.notifyDataSetChanged();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "OnBackPressed main activity", Toast.LENGTH_SHORT).show();
        isBackPressed = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "OnDestroy main activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_folder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.acton_edit_folder:
                Intent intent = new Intent(this, EditFolderActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFolderNameInToPreference(String folderName) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        folderNameList.add(folderName);

        String json = gson.toJson(folderNameList);
        editor.putString(folder_name_key, json);
        editor.apply();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(this, AddImageActivity.class);

        intent.putExtra("FOLDER_TITLE", folderNameList.get(position));
        if (isLongClick == false) {
            startActivity(intent);
        }
        isLongClick = false;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isLongClick = true;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        deleteFolder(position);
                        isLongClick = false;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
//                        Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_SHORT).show();
                        isLongClick = false;
                        break;
                }

            }

        };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

        return true;

    }

    private void deleteFolder(int position) {
        deleteFromPreference(position);
        folderNameList.remove(position);
        gridAdapter.notifyDataSetChanged();
    }

    private void deleteFromPreference(int position) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = prefs.getString(folder_name_key, null);
        editor.remove(folderNameList.get(position));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        if (arrayList != null) {
            arrayList.remove(position);
            String jsonStr = gson.toJson(arrayList);
            editor.putString(folder_name_key, jsonStr);
            editor.apply();
        }

    }

}