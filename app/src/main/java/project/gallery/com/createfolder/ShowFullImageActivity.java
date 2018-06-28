package project.gallery.com.createfolder;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShowFullImageActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView ,ivCancel;
    String full_image_path;
    ViewPager pager;
    ArrayList<String> image_path_array_list;
    int pagerStartingPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_image);
       // imageView = (ImageView) findViewById(R.id.iv_full_image);
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(this);
        getImagePath();
        pager = (ViewPager) findViewById(R.id.view_pager_full_image);
        pager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager(),image_path_array_list));
        pager.setCurrentItem(pagerStartingPosition);
        //setImage();
    }
/*
    private void setImage() {
        Glide.with(this).load(full_image_path)
                .placeholder(R.drawable.ic_photo_white).centerCrop()
                .into(imageView);
    }*/

    private void getImagePath() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            image_path_array_list = bundle.getStringArrayList("IMAGE_PATH_ARRAY_LIST");
            full_image_path = bundle.getString("FULL_IMAGE_PATH");
            pagerStartingPosition = bundle.getInt("PAGER_POSITION");
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.iv_cancel){
            finish();
        }
    }
}
