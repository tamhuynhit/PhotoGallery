package me.tamhuynh.photogallery.fragments;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class ListViewGalleryFragment extends SectionFragment {
    @BindView(R.id.photo_listview) ListView mPhotoList;

    public static ListViewGalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        ListViewGalleryFragment fragment = new ListViewGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(SectionFragment.FRAGMENT_ARG_GALLERY, galleryItems);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_gallery_listview;
    }

    @Override
    public String getTitle() {
        return "ListView";
    }

    @Override
    protected void initView() {

    }
}
