package project.gallery.com.createfolder;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EditFolderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText edit;
    GridView gridView;
    CustomGridAdapter adapter;
    private static String folder_name_key = "folder_list";
    ArrayList<String> folderNameList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);
        gridView = (GridView) findViewById(R.id.gv_edit_folder_name);
        getFolderNameFromPreference();
        adapter = new CustomGridAdapter(this, folderNameList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
        AlertDialog.Builder alert;
        LayoutInflater li = LayoutInflater.from(this);
        View popView = li.inflate(R.layout.edit_popup, null);
        alert = new AlertDialog.Builder(this);
        alert.setView(popView);
        final AlertDialog Alert1;
        Alert1 = alert.create();
        Alert1.show();
        Alert1.setCancelable(true);
        Button btnEdit = (Button) Alert1.findViewById(R.id.btn_edit_folder_name);
        edit = (EditText) Alert1.findViewById(R.id.et_folder_name);
        edit.setText(folderNameList.get(position));
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().length() != 0) {

                    saveFolderNameInToPreference(edit.getText().toString().trim(),position);
//                     adapter.notifyDataSetChanged();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Folder Name Empty", Toast.LENGTH_SHORT).show();
                }
                Alert1.dismiss();
            }
        });
    }

    private void saveFolderNameInToPreference(String folderName, int position) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        folderNameList.set(position,folderName);

        String json = gson.toJson(folderNameList);
        editor.putString(folder_name_key, json);
        editor.apply();
    }

    private void getFolderNameFromPreference() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(folder_name_key, null);

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        if (arrayList != null) {
            folderNameList.addAll(arrayList);

        } else {
            Toast.makeText(this, "arrlist is null", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
