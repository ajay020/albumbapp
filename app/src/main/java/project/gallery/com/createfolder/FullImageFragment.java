package project.gallery.com.createfolder;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FullImageFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String image_id = getArguments().getString("image_id");
        View view = inflater.inflate(R.layout.fragment_show_full_image,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_fragment_full_view);
        setImage(imageView,image_id);

        return view;
    }

    private void setImage(ImageView imageView, String image_id) {
        Glide.with(this).load(image_id)
                .placeholder(R.drawable.ic_photo_white).centerCrop()
                .into(imageView);
    }
}
