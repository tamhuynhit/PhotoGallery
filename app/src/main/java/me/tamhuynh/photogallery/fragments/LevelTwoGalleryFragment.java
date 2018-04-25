package me.tamhuynh.photogallery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;

import java.util.ArrayList;

import me.tamhuynh.photogallery.activities.LevelTwoFullPhotoActivity;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class LevelTwoGalleryFragment extends SectionFragment {

    public static LevelTwoGalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        LevelTwoGalleryFragment fragment = new LevelTwoGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(SectionFragment.FRAGMENT_ARG_GALLERY, galleryItems);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public String getTitle() {
        return "Level 2";
    }

    @Override
    protected void initView() {}

    @Override
    protected void handleItemClicked(SectionFragment.ViewHolder holder, GalleryItem item) {
        Intent intent = new Intent(getActivity(), LevelTwoFullPhotoActivity.class);
        intent.putExtra(LevelTwoFullPhotoActivity.PHOTO_GALLERY_LIST, mGalleryItems);
        intent.putExtra(LevelTwoFullPhotoActivity.PHOTO_FOCUSED_INDEX, holder.getAdapterPosition());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), holder.mPhotoImg, ViewCompat.getTransitionName(holder.mPhotoImg));

        getActivity().startActivity(intent, options.toBundle());
    }
}
