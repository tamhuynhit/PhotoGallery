package me.tamhuynh.photogallery.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.fragments.ListViewGalleryFragment;
import me.tamhuynh.photogallery.fragments.RecyclerViewGalleryFragment;
import me.tamhuynh.photogallery.fragments.SectionFragment;
import me.tamhuynh.photogallery.models.GalleryItem;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabs) TabLayout mTabLayout;
    @BindView(R.id.content_pager) ViewPager mContentPager;

    private SectionFragment[] mSections;
    private ArrayList<GalleryItem> mGalleryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mTabLayout.setupWithViewPager(mContentPager);

        mGalleryItems = new ArrayList<>();
        mGalleryItems.add(
                new GalleryItem("http://tamhuynh.me/blog_files/photo_gallery/1_small.jpeg",
                        "http://tamhuynh.me/blog_files/photo_gallery/1_big.jpeg"));
        mGalleryItems.add(
                new GalleryItem("http://tamhuynh.me/blog_files/photo_gallery/2_small.jpeg",
                        "http://tamhuynh.me/blog_files/photo_gallery/2_big.jpeg"));
        mGalleryItems.add(
                new GalleryItem("http://tamhuynh.me/blog_files/photo_gallery/3_small.jpeg",
                        "http://tamhuynh.me/blog_files/photo_gallery/3_big.jpeg"));
        mGalleryItems.add(
                new GalleryItem("http://tamhuynh.me/blog_files/photo_gallery/4_small.jpeg",
                        "http://tamhuynh.me/blog_files/photo_gallery/4_big.jpeg"));

        mSections = new SectionFragment[] {
                RecyclerViewGalleryFragment.newInstance(mGalleryItems),
                ListViewGalleryFragment.newInstance(mGalleryItems)
        };

        mContentPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
    }

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
