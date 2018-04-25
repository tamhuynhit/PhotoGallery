package me.tamhuynh.photogallery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;

import java.util.ArrayList;

import me.tamhuynh.photogallery.activities.LevelOneFullPhotoActivity;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class LevelOneGalleryFragment extends SectionFragment {

    public static LevelOneGalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        LevelOneGalleryFragment fragment = new LevelOneGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(SectionFragment.FRAGMENT_ARG_GALLERY, galleryItems);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public String getTitle() {
        return "Level 1";
    }

    @Override
    public String getDescription() {
        return "Shared Element Level 1:\n" +
                "- Shared RecyclerView item to 1 fullscreen photo\n" +
                "- Two views load the same image\n" +
                "- Return transition: Automatically return to PREVIOUS shared view";
    }

    @Override
    protected void initView() {}

    @Override
    protected void handleItemClicked(ViewHolder holder, GalleryItem item) {
        // Show fullscreen activity with single photo
        Intent intent = new Intent(getActivity(), LevelOneFullPhotoActivity.class);
        intent.putExtra(LevelOneFullPhotoActivity.PHOTO_GALLERY_ITEM, item);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), holder.mPhotoImg, ViewCompat.getTransitionName(holder.mPhotoImg));

        getActivity().startActivity(intent, options.toBundle());
    }
}
