package project.gallery.com.createfolder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

class SamplePagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> image_path_array_list ;

    public SamplePagerAdapter(FragmentManager fm, ArrayList<String> image_path_array_list) {
        super(fm);
        this.image_path_array_list = image_path_array_list;
    }

    @Override
    public Fragment getItem(int position) {
        FullImageFragment fragment = new FullImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image_id",image_path_array_list.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (image_path_array_list != null) {
            return image_path_array_list.size();
        }else{
            return  0;
        }
    }
}
