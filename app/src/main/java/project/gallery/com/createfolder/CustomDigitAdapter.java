package project.gallery.com.createfolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDigitAdapter extends BaseAdapter {

    private String digitArray[] ;
   // private String alphaberArray[] = new String[11];
    private Context context;
    private LayoutInflater inflater;
    public CustomDigitAdapter(LoginActivity context, String[] digitArray) {
        this.digitArray = digitArray;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return digitArray.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            convertView  = inflater.inflate(R.layout.digit_grid_single,parent,false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        if(digitArray[position].equals("00")) {
            holder.linearLayout.setVisibility(View.INVISIBLE);
//            holder.digitTextView.setVisibility(View.INVISIBLE);
//            holder.alphabetTextView.setVisibility(View.INVISIBLE);
        }else{
            holder.digitTextView.setText(String.valueOf(digitArray[position]));
           // holder.alphabetTextView.setText(alphaberArray[position]);
        }

        if(digitArray[position].equals("/")||digitArray[position].equals("*")||
                digitArray[position].equals("-")||digitArray[position].equals("+")||
                digitArray[position].equals("=")){

            if( digitArray[position].equals("=")){
                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.dark_blue));
            }else{
                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }
            holder.digitTextView.setTextColor(context.getResources().getColor(R.color.white));
        }

        return convertView;
    }

    class Holder {
        TextView digitTextView;
        TextView alphabetTextView;
        LinearLayout linearLayout;
        Holder(View view){
            digitTextView  = (TextView) view.findViewById(R.id.tv_grid_single_digit);
           // alphabetTextView = (TextView) view.findViewById(R.id.tv_alphabet);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_container);
        }
    }
}
