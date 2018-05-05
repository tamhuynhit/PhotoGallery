package me.tamhuynh.photogallery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.ActivityReturnHandler;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.fragments.LevelFiveGalleryFragment;
import me.tamhuynh.photogallery.fragments.LevelFourGalleryFragment;
import me.tamhuynh.photogallery.fragments.LevelOneGalleryFragment;
import me.tamhuynh.photogallery.fragments.LevelThreeGalleryFragment;
import me.tamhuynh.photogallery.fragments.LevelTwoGalleryFragment;
import me.tamhuynh.photogallery.fragments.SectionFragment;
import me.tamhuynh.photogallery.models.GalleryItem;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.tabs) TabLayout mTabLayout;
    @BindView(R.id.content_pager) ViewPager mContentPager;
    @BindView(R.id.level_title_txt) TextView mLevelTitleTxt;
    @BindView(R.id.level_description_txt) TextView mLevelDescriptionTxt;

    private SectionFragment[] mSections;
    private SectionFragment mCurrentSection;

    private ArrayList<GalleryItem> mGalleryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTabLayout.setupWithViewPager(mContentPager);

        mGalleryItems = new ArrayList<>();
        mGalleryItems.add(new GalleryItem("1_small.jpeg", "1_big.jpeg"));
        mGalleryItems.add(new GalleryItem("2_small.jpeg", "2_big.jpeg"));
        mGalleryItems.add(new GalleryItem("3_small.jpeg", "3_big.jpeg"));
        mGalleryItems.add(new GalleryItem("4_small.jpeg", "4_big.jpeg"));

        mSections = new SectionFragment[] {
                LevelOneGalleryFragment.newInstance(mGalleryItems),
                LevelTwoGalleryFragment.newInstance(mGalleryItems),
                LevelThreeGalleryFragment.newInstance(mGalleryItems),
                LevelFourGalleryFragment.newInstance(mGalleryItems),
                LevelFiveGalleryFragment.newInstance(mGalleryItems)
        };

        mContentPager.addOnPageChangeListener(this);
        mContentPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        onPageSelected(0);
    }

    /**
     * Catch Reenter event (handle by level 4 section only)
     */
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (mCurrentSection instanceof ActivityReturnHandler) {
            ((ActivityReturnHandler) mCurrentSection).onActivityReenter(this, resultCode, data);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        mCurrentSection = mSections[position];

        mLevelTitleTxt.setText(mCurrentSection.getTitle());
        mLevelDescriptionTxt.setText(mCurrentSection.getDescription());
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        private SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                return mSections[position];
            } catch (NullPointerException | IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        public int getCount() {
            return mSections != null ? mSections.length : 0;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Fragment fragment = getItem(position);
            if (fragment instanceof SectionFragment) {
                return ((SectionFragment) fragment).getTitle();
            }

            return "";
        }
    }
}
