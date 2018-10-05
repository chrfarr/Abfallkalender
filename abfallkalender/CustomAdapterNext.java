package de.farr_net.abfallkalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cfarr on 27.12.16.
 */

class CustomAdapterNext extends ArrayAdapter<DataModelNext>{

    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtDayOfWeek;
        TextView txtDatum;
        TextView txtTyp;
        ImageView imgTyp;
    }

    public CustomAdapterNext(ArrayList<DataModelNext> data, Context context) {
        super(context, R.layout.row_item_next, data);
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelNext dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_next, parent, false);

            viewHolder.txtDayOfWeek = (TextView) convertView.findViewById(R.id.dayofweek);
            viewHolder.txtDatum = (TextView) convertView.findViewById(R.id.datum);
            viewHolder.txtTyp = (TextView) convertView.findViewById(R.id.item_typ_text);
            viewHolder.imgTyp = (ImageView) convertView.findViewById(R.id.item_typ_img);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        //lastPosition = position;

        viewHolder.txtDayOfWeek.setText(dataModel.getWochentag());
        viewHolder.txtDatum.setText(dataModel.getDatum());

        switch(dataModel.getTyp()){
            case "gruen":
                viewHolder.txtTyp.setText("Gelber \"Sack\" ");
                viewHolder.imgTyp.setImageResource(R.mipmap.gruen);
                break;
            case "bio":
                viewHolder.txtTyp.setText("Biotonne");
                viewHolder.imgTyp.setImageResource(R.mipmap.bio);
                break;
            case "rest":
                viewHolder.txtTyp.setText("Hausmüll");
                viewHolder.imgTyp.setImageResource(R.mipmap.rest);
                break;
            case "papier":
                viewHolder.txtTyp.setText("Altpapier");
                viewHolder.imgTyp.setImageResource(R.mipmap.papier);
                break;
            default:
                viewHolder.txtTyp.setText("unknown");
                viewHolder.imgTyp.setImageResource(R.mipmap.rest);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}