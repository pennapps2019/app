package com.pennapps2019.application.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.pennapps2019.application.MapsActivity;
import com.pennapps2019.application.R;

import java.util.Objects;

// Fragment must be a public static class
public class Onboarding2Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_onboarding_2, container, false
        );
        linkEndButton(view);
        return view;
    }

    private void linkEndButton(View view) {
        // Set button behaviour
        final Button button = view.findViewById(R.id.button_finish_onboarding);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapsActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

}