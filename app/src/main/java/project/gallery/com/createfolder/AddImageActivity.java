package project.gallery.com.createfolder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class AddImageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener, AdapterView.OnItemLongClickListener {
    private static final int PICK_PHOTO = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 222;
    private GridView addImageListView;
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<String> selectedImageList;
    private CustomListAdapter adapter;
    private static final String IMAGE_LIST = "image_list";
    private String folder_title;
    private FloatingActionButton fabAddImage;
    private boolean isLongPressed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        getData();
        fabAddImage = (FloatingActionButton) findViewById(R.id.fab_add_image);
        fabAddImage.setOnClickListener(this);
        getImageListFromPreference();
        addImageListView = (GridView) findViewById(R.id.lv_add_image);
        adapter = new CustomListAdapter(this, imageList);
        addImageListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        addImageListView.setOnItemClickListener(this);
        addImageListView.setOnItemLongClickListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            folder_title = bundle.getString("FOLDER_TITLE");
            Log.v("FOLDER_TITLE",folder_title);
        }

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_image:
                //Toast.makeText(this, "add Image", Toast.LENGTH_SHORT).show();
//                openGallery();
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void openGallery() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_PHOTO:
                if(resultCode ==  RESULT_OK) {
                     selectedImageList = (ArrayList<String>) data.getSerializableExtra(IMAGE_LIST);
                    this.imageList.addAll(selectedImageList);
                    adapter.notifyDataSetChanged();
                    saveImageListToPreference(selectedImageList);
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                  if(resultCode == RESULT_OK){
                      Bundle extras = data.getExtras();
                      Bitmap bitmap = (Bitmap) extras.get("data");

                      Uri uri = getImageUri(this,bitmap);
                      // CALL THIS METHOD TO GET THE ACTUAL PATH
                      File finalFilePath = new File(getRealPathFromURI(uri));
//                      Toast.makeText(this,"finalfile= " + finalFilePath,Toast.LENGTH_SHORT).show();
                      selectedImageList = new ArrayList<>();
                      selectedImageList.add(finalFilePath.toString());
                      Log.v("imagepath",finalFilePath.toString());
                      imageList.add(0,finalFilePath.toString());
                      adapter.notifyDataSetChanged();
                      saveImageListToPreference(selectedImageList);

                  }else{
                      Toast.makeText(this,"requestCode = "+requestCode,Toast.LENGTH_SHORT).show();
                  }
                break;
        }

    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);

    }

    private void saveImageListToPreference(ArrayList<String> selectedImageList) {
        ArrayList<String> savedList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        String savedImageStr = prefs.getString(folder_title , null);
        if(savedImageStr != null){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
             ArrayList<String> arrayList =  gson.fromJson(savedImageStr, type);
             savedList.addAll(arrayList);
        }
        savedList.addAll(selectedImageList);
        String json = gson.toJson(savedList);
        editor.putString(folder_title, json);
        editor.apply();

       /* SharedPreferences preferences = getSharedPreferences("PREF_IMAGE_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if () {
            Set<String> set = new HashSet<>();
            set.addAll(selectedImageList);
            editor.putStringSet(folder_title + "IMAGE_SET", set);
            Log.v("IMG_SET",set.toString());
            editor.commit();
        } else {
//            Set<String> stringSet = preferences.getStringSet(folder_title + "IMAGE_SET", null);
                Set<String > imgSet = new HashSet<>();
                imgSet.addAll(imageList);
                editor.putStringSet(folder_title + "IMAGE_SET", imgSet);
                editor.commit();
        }*/
    }


    public void getImageListFromPreference() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(folder_title, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList =   gson.fromJson(json, type);
        if(arrayList != null){
           imageList.addAll(arrayList);
        }

       /* SharedPreferences preferences = getSharedPreferences(getString(R.string.pref_image_file), MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet(folder_title + "IMAGE_SET", null);
        if(stringSet != null){

            imageList.addAll(stringSet);

        }else{
            Toast.makeText(this,"Select Image",Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

       Intent intent = new Intent(this,ShowFullImageActivity.class);
       Bundle bundle = new Bundle();
       String imagePath  = imageList.get(position);
       bundle.putString("FULL_IMAGE_PATH",imagePath);
       bundle.putStringArrayList("IMAGE_PATH_ARRAY_LIST",imageList);
       bundle.putInt("PAGER_POSITION",position);
       intent.putExtras(bundle);
       if(isLongPressed == false) {
           startActivity(intent);
       }
       isLongPressed = false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isLongPressed = true;
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        deleteImage(position);
                        isLongPressed = false;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        isLongPressed = false;
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

        return true;
    }


    private void deleteImage(int position) {
        deleteFromPreference(imageList.get(position));
        imageList.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void deleteFromPreference(String imgPath) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = prefs.getString(folder_title, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList =   gson.fromJson(json, type);
        if(arrayList != null){
            arrayList.remove(imgPath);
            String imgStr = gson.toJson(arrayList);
            editor.putString(folder_title,imgStr);
            editor.apply();
        }


       /* SharedPreferences preferences = getSharedPreferences(getString(R.string.pref_image_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> stringSet = preferences.getStringSet(folder_title + "IMAGE_SET", null);

        if(stringSet != null){
            stringSet.remove(imgPath);
            editor.putStringSet(folder_title + "IMAGE_SET",stringSet);
            editor.commit();
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_image:
                popUp(view);
                break;
        }
    }

    private void popUp(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pop_up_menu);
        popup.show();
    }

    private void openCamera() {
      /*  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*/
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_camera:
                openCamera();
                break;
            case R.id.action_gallery:
                openGallery();
                break;
        }
        return false;
    }


}
