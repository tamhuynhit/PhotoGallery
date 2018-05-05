package me.tamhuynh.photogallery.fragments;

import android.os.Bundle;

import java.util.ArrayList;

import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 * Everything is the same as level 4 except the title and description
 */
public class LevelFiveGalleryFragment extends LevelFourGalleryFragment {

    public static LevelFiveGalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        LevelFiveGalleryFragment fragment = new LevelFiveGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(SectionFragment.FRAGMENT_ARG_GALLERY, galleryItems);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public String getTitle() {
        return "Level 5";
    }

    @Override
    public String getDescription() {
        return "Shared Element Level 5:\n" +
                "- Shared RecyclerView item to a ViewPager fullscreen layout\n" +
                "- Two views load 2 different image: thumbnail size and full size\n" +
                "- Return transition: Automatically return to CURRENT selected shared view in ViewPager\n" +
                "- Pause changing drawable if the full size image loaded faster than the transition";
    }
}
