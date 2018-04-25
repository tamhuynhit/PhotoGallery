package me.tamhuynh.photogallery.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.tamhuynh.photogallery.ActivityReturnHandler;
import me.tamhuynh.photogallery.activities.LevelFourFullPhotoActivity;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class LevelFourGalleryFragment extends SectionFragment implements ActivityReturnHandler {

    public static LevelFourGalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        LevelFourGalleryFragment fragment = new LevelFourGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(SectionFragment.FRAGMENT_ARG_GALLERY, galleryItems);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public String getTitle() {
        return "Level 4";
    }

    @Override
    public String getDescription() {
        return "Shared Element Level 4:\n" +
                "- Shared RecyclerView item to a ViewPager fullscreen layout\n" +
                "- Two views load 2 different image: thumbnail size and full size\n" +
                "- Return transition: Automatically return to CURRENT selected shared view in ViewPager";
    }

    @Override
    protected void initView() {}

    @Override
    protected void handleItemClicked(ViewHolder holder, GalleryItem item) {
        Intent intent = new Intent(getActivity(), LevelFourFullPhotoActivity.class);
        intent.putExtra(LevelFourFullPhotoActivity.PHOTO_GALLERY_LIST, mGalleryItems);
        intent.putExtra(LevelFourFullPhotoActivity.PHOTO_FOCUSED_INDEX, holder.getAdapterPosition());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), holder.mPhotoImg, ViewCompat.getTransitionName(holder.mPhotoImg));

        getActivity().startActivity(intent, options.toBundle());
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onActivityReenter(final Activity activity, int resultCode, Intent data) {
        if (resultCode != LevelFourFullPhotoActivity.RESULT_PHOTO_CLOSED || data == null)
            return;

        final int selectedIndex = data.getIntExtra(LevelFourFullPhotoActivity.PHOTO_FOCUSED_INDEX, -1);
        if (selectedIndex == -1)
            return;

        // Scroll to the new selected view in case it's not currently visible on the screen
        mPhotoList.scrollToPosition(selectedIndex);

        final CustomSharedElementCallback callback = new CustomSharedElementCallback();
        getActivity().setExitSharedElementCallback(callback);

        // Listen for the transition end and clear all registered callback
        getActivity().getWindow().getSharedElementExitTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}

            @Override
            public void onTransitionEnd(Transition transition) {
                removeCallback();
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                removeCallback();
            }

            private void removeCallback() {
                if (getActivity() != null) {
                    getActivity().getWindow().getSharedElementExitTransition().removeListener(this);
                    getActivity().setExitSharedElementCallback((SharedElementCallback) null);
                }
            }
        });

        // Pause transition until the selected view is fully drawn
        getActivity().supportPostponeEnterTransition();

        // Listen for the RecyclerView pre draw to make sure the selected view is visible,
        //  and findViewHolderForAdapterPosition will return a non null ViewHolder
        mPhotoList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mPhotoList.getViewTreeObserver().removeOnPreDrawListener(this);

                RecyclerView.ViewHolder holder = mPhotoList.findViewHolderForAdapterPosition(selectedIndex);
                if (holder instanceof ViewHolder) {
                    callback.setView(((ViewHolder) holder).mPhotoImg);
                }

                // Continue the transition
                getActivity().supportStartPostponedEnterTransition();

                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static class CustomSharedElementCallback extends SharedElementCallback {
        private View mView;

        /**
         * Set the transtion View to the callback, this should be called before starting the transition so the View is not null
         */
        public void setView(View view) {
            mView = view;
        }

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            // Clear all current shared views and names
            names.clear();
            sharedElements.clear();

            // Store new selected view and name
            String transitionName = ViewCompat.getTransitionName(mView);
            names.add(transitionName);
            sharedElements.put(transitionName, mView);
        }
    }
}
