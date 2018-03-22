package me.tamhuynh.photogallery.fragments;

import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import me.tamhuynh.photogallery.R;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class RecyclerViewGalleryFragment extends SectionFragment {
    @BindView(R.id.photo_list) RecyclerView mPhotoList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_gallery_recyclerview;
    }

    @Override
    protected void initView() {

    }
}
