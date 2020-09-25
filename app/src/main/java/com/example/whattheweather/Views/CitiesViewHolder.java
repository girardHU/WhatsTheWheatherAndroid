package com.example.whattheweather.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.whattheweather.R;

import java.io.InputStream;

public class CitiesViewHolder extends RecyclerView.ViewHolder {

    LinearLayout layout;
    TextView textView;
    ImageView trash;
    ImageView flag;

    public CitiesViewHolder(View view) {
        super(view);
        this.layout = view.findViewById(R.id.layout);
        this.textView = view.findViewById(R.id.city_item);
        this.flag = view.findViewById(R.id.flag_img);
        this.trash = view.findViewById((R.id.dustbin));
        this.trash.setImageResource(R.drawable.trash);
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView flag;

        public DownloadImageTask(ImageView imageView) {
            this.flag = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            flag.setImageBitmap(result);
        }
    }
}