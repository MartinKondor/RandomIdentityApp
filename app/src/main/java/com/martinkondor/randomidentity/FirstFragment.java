package com.martinkondor.randomidentity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import java.time.Year;
import java.time.ZoneId;
import java.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.martinkondor.randomidentity.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Generator g;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        g = new Generator(getResources());  // Load generator

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                g.getCountryNames(getResources()));
        binding.countryList.setAdapter(adapter);
        binding.countryList.setSelection(225);

        binding.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { generateNewId(getActivity()); }
        });
    }

    private void generateNewId(FragmentActivity ac) {
        boolean isMale = g.getInt(100) % 2 == 0;
        String chosenCountry = ((Spinner) ac.findViewById(R.id.countryList))
                .getSelectedItem()
                .toString();

        // Get new image if possible
        ImageView image = (ImageView) getActivity().findViewById(R.id.image);
        image.setImageResource(isMale ? R.drawable.male : R.drawable.female);

        TextView name = (TextView) ac.findViewById(R.id.name);
        TextView address = (TextView) ac.findViewById(R.id.address);
        TextView city = (TextView) ac.findViewById(R.id.city);
        TextView mother = (TextView) ac.findViewById(R.id.mother);
        TextView phone = (TextView) ac.findViewById(R.id.phone);
        TextView countryCode = (TextView) ac.findViewById(R.id.countryCode);
        TextView birthday = (TextView) ac.findViewById(R.id.birthday);
        //TextView age = (TextView) ac.findViewById(R.id.age);
        TextView email = (TextView) ac.findViewById(R.id.email);
        TextView username = (TextView) ac.findViewById(R.id.username);
        TextView password = (TextView) ac.findViewById(R.id.password);

        String fullName = g.getFullName(isMale);
        TextView[] textViews = new TextView[] {
                name, address, city, mother, phone, countryCode,
                birthday, email, username, password
        };

        name.setText(fullName);
        address.setText(g.getAddress());
        city.setText(g.getCity(chosenCountry));
        mother.setText(g.getFullName(false));
        phone.setText(g.getPhone());
        countryCode.setText(g.getCountryCode(chosenCountry));
        birthday.setText(g.getBirthday());
        //age.setText(Year.now().getValue());// - Integer.parseInt(bday.split(",")[1].trim()));
        email.setText(g.getEmail(fullName));
        username.setText(g.getUsername(fullName));
        password.setText(g.getPassword());

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        // Set click listeners
        for (TextView t : textViews) {
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Copy text on click
                    ClipData clip = ClipData.newPlainText("copied fake data", t.getText());
                    clipboard.setPrimaryClip(clip);

                    // Show that text is copied now
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Text copied to clipboard", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {}
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}


