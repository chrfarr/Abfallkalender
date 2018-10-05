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

class CustomAdapterAll extends ArrayAdapter<DataModelAll>{

    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtDayOfWeek;
        TextView txtDatum;
        TextView txtSonstiges;
        TextView txtItemRestText;
        TextView txtItemPapierText;
        TextView txtItemGruenText;
        TextView txtItemBioText;
        ImageView imgItemRest;
        ImageView imgItemPapier;
        ImageView imgItemGruen;
        ImageView imgItemBio;
    }

    public CustomAdapterAll(ArrayList<DataModelAll> data, Context context) {
        super(context, R.layout.row_item_all, data);
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelAll dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_all, parent, false);

            viewHolder.txtDayOfWeek = (TextView) convertView.findViewById(R.id.dayofweek);
            viewHolder.txtDatum = (TextView) convertView.findViewById(R.id.datum);
            viewHolder.txtSonstiges = (TextView) convertView.findViewById(R.id.sonstiges);
            viewHolder.txtItemRestText = (TextView) convertView.findViewById(R.id.item_rest_text);
            viewHolder.txtItemPapierText = (TextView) convertView.findViewById(R.id.item_papier_text);
            viewHolder.txtItemGruenText = (TextView) convertView.findViewById(R.id.item_gruen_text);
            viewHolder.txtItemBioText = (TextView) convertView.findViewById(R.id.item_bio_text);
            viewHolder.imgItemRest = (ImageView) convertView.findViewById(R.id.item_rest);
            viewHolder.imgItemPapier = (ImageView) convertView.findViewById(R.id.item_papier);
            viewHolder.imgItemGruen = (ImageView) convertView.findViewById(R.id.item_gruen);
            viewHolder.imgItemBio = (ImageView) convertView.findViewById(R.id.item_bio);

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
        viewHolder.txtSonstiges.setText(dataModel.getSonstiges());
        viewHolder.txtItemRestText.setText(dataModel.getRest());
        viewHolder.txtItemPapierText.setText(dataModel.getPapier());
        viewHolder.txtItemGruenText.setText(dataModel.getGruen());
        viewHolder.txtItemBioText.setText(dataModel.getBio());

        viewHolder.imgItemRest.setImageResource(R.mipmap.rest);
        viewHolder.imgItemPapier.setImageResource(R.mipmap.papier);
        viewHolder.imgItemGruen.setImageResource(R.mipmap.gruen);
        viewHolder.imgItemBio.setImageResource(R.mipmap.bio);

        // Return the completed view to render on screen
        return convertView;
    }
}