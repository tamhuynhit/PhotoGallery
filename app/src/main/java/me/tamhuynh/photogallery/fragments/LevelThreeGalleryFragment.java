package me.tamhuynh.photogallery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;

import java.util.ArrayList;

import me.tamhuynh.photogallery.activities.LevelThreeFullPhotoActivity;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class LevelThreeGalleryFragment extends SectionFragment {

    public static LevelThreeGalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        LevelThreeGalleryFragment fragment = new LevelThreeGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(SectionFragment.FRAGMENT_ARG_GALLERY, galleryItems);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public String getTitle() {
        return "Level 3";
    }

    @Override
    public String getDescription() {
        return "Shared Element Level 3:\n" +
                "- Shared RecyclerView item to a ViewPager fullscreen layout\n" +
                "- Two views load 2 different image: thumbnail size and full size\n" +
                "- Return transition: Automatically return to PREVIOUS selected shared view";
    }

    @Override
    protected void initView() {}

    @Override
    protected void handleItemClicked(SectionFragment.ViewHolder holder, GalleryItem item) {
        Intent intent = new Intent(getActivity(), LevelThreeFullPhotoActivity.class);
        intent.putExtra(LevelThreeFullPhotoActivity.PHOTO_GALLERY_LIST, mGalleryItems);
        intent.putExtra(LevelThreeFullPhotoActivity.PHOTO_FOCUSED_INDEX, holder.getAdapterPosition());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), holder.mPhotoImg, ViewCompat.getTransitionName(holder.mPhotoImg));

        getActivity().startActivity(intent, options.toBundle());
    }
}
