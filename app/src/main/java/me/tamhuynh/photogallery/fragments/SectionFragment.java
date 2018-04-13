package me.tamhuynh.photogallery.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public abstract class SectionFragment extends Fragment {
    public final static String FRAGMENT_ARG_GALLERY = "arg_gallery";

    protected ArrayList<GalleryItem> mGalleryItems;

    protected abstract @LayoutRes int getLayout();
    protected abstract void initView();
    public abstract String getTitle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            mGalleryItems = args.getParcelableArrayList(FRAGMENT_ARG_GALLERY);
        }

        initView();

        return view;
    }
}
