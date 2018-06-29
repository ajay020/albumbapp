package project.gallery.com.createfolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomGridAdapter extends BaseAdapter {

    private ArrayList<FolderModel> folderNameList;
    private Context context;
    private LayoutInflater inflater;

    public CustomGridAdapter(Context context, ArrayList<FolderModel> folderNameList) {
        this.folderNameList = folderNameList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return folderNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.grid_single_layout,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textViewFolderName.setText(folderNameList.get(position).getFolderName());
        return convertView;
    }

    public static class ViewHolder{

        ImageView imageViewFolder;
        TextView textViewFolderName;

        public ViewHolder(View view) {
            imageViewFolder = (ImageView) view.findViewById(R.id.iv_folder_icon);
            textViewFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
        }
    }
}
