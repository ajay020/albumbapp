package project.gallery.com.createfolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> imageList;

    public CustomListAdapter(AddImageActivity addImageActivity, ArrayList<String> imageList) {
        this.context = addImageActivity;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.row_list_view,parent,false);

        }else{
            view = convertView;
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);


        Glide.with(context).load(imageList.get(position))
                .placeholder(R.drawable.ic_photo_white).centerCrop()
                .into(imageView);

        return view;
    }
}
