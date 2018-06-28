package project.gallery.com.createfolder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final String IMAGE_LIST = "image_list";
    /**
     * The images.
     */
    private ArrayList<String> images = new ArrayList<>();
    public ArrayList<String> selectedImages = new ArrayList<>();
    private boolean [] imageSelection  = new boolean[10000];
    private ImageAdapter adapter;
    private Button btnSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(this);
        final GridView gallery = (GridView) findViewById(R.id.galleryGridView);
        adapter = new ImageAdapter(this);
        gallery.setAdapter(adapter);
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            images = getAllShownImagesPath(this);
            imageSelection = new boolean[images.size()];
//            Log.v("count" , images.size()+"");
            Collections.reverse(images);

            adapter.notifyDataSetChanged();
        }

     /*   gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                *//*if (null != images && !images.isEmpty()) {
                    selectedImages.add(images.get(position));

//                    gallery.getChildAt(position).setBackgroundColor(Color.BLUE);
//                      images.get(position).charAt(getResources().getColor(R.color.black));
                }*//*
            }

        });*/
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_LIST, selectedImages);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_LIST, selectedImages);
        setResult(Activity.RESULT_OK, intent);
        finish();
        //super.onBackPressed();
    }

    class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.single_grid, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.viewFront.setId(position);

            holder.viewFront.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                    CheckBox cb = holder.checkbox;
                    int id = view.getId();
                    Log.v("image_selection","length = "+ imageSelection.length+"");
                    if(imageSelection != null && imageSelection.length != 0) {
                        if (imageSelection[id]) {
                            cb.setChecked(false);
                            imageSelection[id] = false;
                            if (null != images && !images.isEmpty()) {
                                selectedImages.remove(images.get(position));
                            }
                        } else {
                            cb.setChecked(true);
                            imageSelection[id] = true;
                            if (null != images && !images.isEmpty()) {
                                selectedImages.add(images.get(position));
                            }
                        }
                    }

                }
            });

          /*  holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    int id = cb.getId();
                    Log.v("checkboxid",id+"");
                    Log.v("imagesel",imageSelection[position]+"");
                   // Toast.makeText(getApplicationContext(),""+imageSelection[position],Toast.LENGTH_LONG).show();

                        if (imageSelection[id]) {
                            cb.setChecked(false);
                            imageSelection[id] = false;
                            if (null != images && !images.isEmpty()) {
                                selectedImages.remove(images.get(position));
                            }
                        } else {
                            cb.setChecked(true);
                            imageSelection[id] = true;
                            if (null != images && !images.isEmpty()) {
                                selectedImages.add(images.get(position));
                            }
                        }
                    }
            });*/

            if(imageSelection != null && imageSelection.length != 0) {
                holder.checkbox.setChecked(imageSelection[position]);
            }
            Glide.with(context).load(images.get(position))
                    .placeholder(R.drawable.ic_photo_white).centerCrop()
                    .into(holder.imageview);
            return convertView;
        }
        class ViewHolder {
            ImageView imageview;
            CheckBox checkbox;
            View viewFront;
            int id;

            ViewHolder(View view){
                imageview = (ImageView) view.findViewById(R.id.iv_grid_image_view);
                checkbox = (CheckBox) view.findViewById(R.id.cb_check_box);
                viewFront = view.findViewById(R.id.view_front);
            }
        }
    }

    /**
     * Getting All Images Path.
     *
     * @param activity the activity
     * @return ArrayList with images Path
     */
    private ArrayList<String> getAllShownImagesPath(GalleryActivity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                    images = getAllShownImagesPath(this);
                    Collections.reverse(images);
                   adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

}
