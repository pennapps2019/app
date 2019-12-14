package com.pennapps2019.application.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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

        viewPager = findViewById(R.id.pager);

        // Create adapter to provide the pages to the view pager widget.
        viewPager.setAdapter(new OnboardingFragmentPagerAdapter(this));
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        }
        else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    // Pager adapter returning the 5 onboarding fragment objects in sequence.
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

}
