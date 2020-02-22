package com.example.etoo.ertugruluzun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*

Bu linkteki kod egzersizini takip ettim
https://codelabs.developers.google.com/codelabs/android-training-create-recycler-view/index.html?index=..%2F..android-training#3

*/
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {
   public List<Earthquake> earthquakeList;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context, List<Earthquake> earthquakeList) {
        this.earthquakeList = earthquakeList;
        layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        return new ItemViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Earthquake currentEarthquake = earthquakeList.get(i);
        if (currentEarthquake != null) {
            String[] splittedPlaceInfo = parsePlace(currentEarthquake.getPlace());
            itemViewHolder.magnitudeTextView.setText(roundDouble(currentEarthquake.getMagnitude()));
            //Bu saat tarih ve saat değişecek hatalı şu an. Unutmamak için
            itemViewHolder.timeTextView.setText(calculatTime(currentEarthquake.getTimeInMilliseconds()));
            itemViewHolder.dateTextView.setText(calculateDate(currentEarthquake.getTimeInMilliseconds()));
            itemViewHolder.distanceTextView.setText(splittedPlaceInfo[0]);
            itemViewHolder.placeTextView.setText(splittedPlaceInfo[1]);
            checkMagnitudeColor(currentEarthquake.getMagnitude(), itemViewHolder.magnitudeTextView);
        }

    }


    // found this method on https://www.baeldung.com/java-round-decimal-number
    private String roundDouble(double mag) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(mag);
    }

    private String[] parsePlace(String place) {
        String[] splitted = new String[2];
        if (place.contains("of")) {
            splitted = place.split("of ");
            //splitted[0] is distance text
            splitted[0] = splitted[0] + "BÖLGESİNDE";

        } else {
            splitted[0] = "YAKININDA";
            splitted[1] = place;
        }
        return splitted;
    }

    private void checkMagnitudeColor(double mag, TextView tv) {
        int roundMag = (int) Math.floor(mag);

        switch (roundMag) {
            case 0:
            case 1:
                tv.setBackgroundResource(R.color.magnitude1);
                break;
            case 2:
                tv.setBackgroundResource(R.color.magnitude2);
                break;
            case 3:
                tv.setBackgroundResource(R.color.magnitude3);
                break;
            case 4:
                tv.setBackgroundResource(R.color.magnitude4);
                break;
            case 5:
                tv.setBackgroundResource(R.color.magnitude5);
                break;
            case 6:
                tv.setBackgroundResource(R.color.magnitude6);
                break;
            case 7:
                tv.setBackgroundResource(R.color.magnitude7);
                break;
            case 8:
                tv.setBackgroundResource(R.color.magnitude8);
                break;
            case 9:
                tv.setBackgroundResource(R.color.magnitude9);
                break;
            case 10:
                tv.setBackgroundResource(R.color.magnitude10plus);
                break;
            default:
                tv.setBackgroundResource(R.color.magnitude10plus);
                break;
        }
    }

    /*
        Converting milliseconds to date and time
        https://stackoverflow.com/questions/10364383/how-to-transform-currenttimemillis-to-a-readable-date-format
        http://www.java2s.com/Code/Java/Development-Class/Convertmillisecondsvaluetodate.htm
        I used this pages.
     */
    private String calculateDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(date);
    }

    private String calculatTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout listItem;
        TextView placeTextView;
        TextView distanceTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView magnitudeTextView;

        public ItemViewHolder(@NonNull final View itemView, final RecyclerViewAdapter adapter) {
            super(itemView);
            listItem = (LinearLayout) itemView.findViewById(R.id.listItem);
            placeTextView = itemView.findViewById(R.id.placeText);
            distanceTextView = itemView.findViewById(R.id.distanceText);
            dateTextView = itemView.findViewById(R.id.dateText);
            timeTextView = itemView.findViewById(R.id.timeText);
            magnitudeTextView = itemView.findViewById(R.id.magnitudeText);

            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Earthquake e = earthquakeList.get(getAdapterPosition());
                    String url = e.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}
