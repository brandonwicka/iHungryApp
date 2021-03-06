package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

/**
 * Created by brand_000 on 3/6/2016.
 */
public class Tab1 extends Fragment {

    Restaurant mRestaurant;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRestaurant = (Restaurant) getArguments().getSerializable("Restaurant");
        View v =inflater.inflate(R.layout.tab1,container,false);



        if (mRestaurant != null) {
            Log.v("Restaurant", mRestaurant.toString());
            TextView addressTextView = (TextView) v.findViewById(R.id.text_address);
            addressTextView.setText(mRestaurant.getAddress());

            TextView phoneTextView = (TextView) v.findViewById(R.id.textNumber);
            phoneTextView.setText(mRestaurant.getDisplayPhoneNumber());

            TextView websiteTextView = (TextView) v.findViewById(R.id.website_link);
            websiteTextView.setText(mRestaurant.getUrl().split(Pattern.quote("?"))[0]);

            ImageButton websiteButton = (ImageButton) v.findViewById(R.id.website_button);
            websiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRestaurant.getUrl()));
                    startActivity(browserIntent);
                }
            });

            final ImageButton mapButton = (ImageButton) v.findViewById(R.id.imageView2);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    Intent mapIntent = new Intent(getContext(), MapsActivity.class);
                    mapIntent.putExtra(MapsActivity.RESTAURANT_KEY, mRestaurant);
                    getActivity().startActivity(mapIntent);
                    */
                    String uri ="http://maps.google.com/maps?f=d&daddr=" + mRestaurant.getLatitude() + "," + mRestaurant.getLongitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(uri));
                    intent.setComponent(new ComponentName("com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"));
                    startActivity(intent);
                }
            });

            ImageButton phoneButton = (ImageButton) v.findViewById(R.id.phone_button);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                23);
                    } else {
                        PackageManager pm = getContext().getPackageManager();
                        if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + mRestaurant.getCallablePhoneNumber()));
                                startActivity(callIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

            LinearLayout categoryLayout = (LinearLayout) v.findViewById(R.id.categories_panel);
            categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (String category : mRestaurant.getCategories()) {
                TextView textView = new TextView(getContext());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout
                       .LayoutParams.WRAP_CONTENT);
                textView.setPadding(15, 15, 15, 20);
                textView.setTextSize(15);
                textView.setLayoutParams(layoutParams);
                textView.setBackground(getResources().getDrawable(R.drawable.pill));
                textView.setText(category);
                textView.setTextColor(getResources().getColor(R.color.white));
                categoryLayout.addView(textView);
            }

        }


        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 23: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //get Android Studio compiler to shut up
                    }
                    // Success
                    PackageManager pm = getContext().getPackageManager();
                    if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                        try {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + mRestaurant.getCallablePhoneNumber()));
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    // permission denied
                }
                return;
            }

        }
    }
}