package com.pennapps2019.application.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.pennapps2019.application.MapsActivity;
import com.pennapps2019.application.R;

import java.util.Objects;

public class OnboardingActivity extends FragmentActivity {

    private static final int NUM_PAGES = 2;

    // Pager widget
    // Handles animation and allows swiping horizontally to access previous and next steps.
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.activity_onboarding);

        // Create adapter to provide the pages to the view pager widget.
        viewPager.setAdapter(new OnboardingFragmentPagerAdapter(this));
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first screen, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }
        else {
            // Otherwise, select the previous screen.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    // Pager adapter returning the onboarding fragment objects in sequence.
    private class OnboardingFragmentPagerAdapter extends FragmentStateAdapter {
        OnboardingFragmentPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        @NonNull
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new Onboarding1Fragment();
            }
            else {
                // TODO: Implement other fragments
                return new Onboarding1Fragment();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private class Onboarding1Fragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(
                    R.layout.fragment_onboarding_1, container, false
            );
            linkSkipButton(view);
            return view;
        }

        private void linkSkipButton(View view) {
            // Set button behaviour
            final Button button = view.findViewById(R.id.button_skip_onboarding);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MapsActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
            });
        }

    }


}