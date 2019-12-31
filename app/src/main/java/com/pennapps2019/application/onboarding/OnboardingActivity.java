package com.pennapps2019.application.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.pennapps2019.application.MapsActivity;
import com.pennapps2019.application.R;

public class OnboardingActivity extends FragmentActivity {

    private static final int NUM_PAGES = 2;

    // Pager widget
    // Handles animation and allows swiping horizontally to access previous and next steps.
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.activity_onboarding_content);

        // Create adapter to provide the pages to the view pager widget.
        viewPager.setAdapter(new OnboardingFragmentPagerAdapter(this));

        linkActionButton();

        registerCurrentPageAndAction();
    }

    private void linkActionButton() {
        // Set button behaviour
        final Context context = this;
        final Button button = findViewById(R.id.btn_onboarding_action);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(context, MapsActivity.class));
                finish();
            }
        });
    }


    private void registerCurrentPageAndAction() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }

            @Override
            public void onPageSelected(int pos) {

                // Set text for action button
                int lastPage = NUM_PAGES - 1;
                String text = (pos == lastPage) ?
                        getString(R.string.finish) :
                        getString(R.string.skip);

                TextView onboardingAction = findViewById(R.id.btn_onboarding_action);
                onboardingAction.setText(text);

                // Set page markers
                View page_0_inactive = findViewById(R.id.onboarding_page_0_indicator);
                View page_1_inactive = findViewById(R.id.onboarding_page_1_indicator);
                if (pos == 0) {
                    page_0_inactive.setVisibility(View.INVISIBLE);
                    page_1_inactive.setVisibility(View.VISIBLE);
                }
                else {
                    page_0_inactive.setVisibility(View.VISIBLE);
                    page_1_inactive.setVisibility(View.INVISIBLE);
                }

            }
        });
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
                return new Onboarding2Fragment();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }



}