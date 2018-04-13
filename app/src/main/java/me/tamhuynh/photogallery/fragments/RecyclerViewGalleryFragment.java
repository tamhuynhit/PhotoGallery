package me.tamhuynh.photogallery.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.activities.PhotoActivity;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class RecyclerViewGalleryFragment extends SectionFragment {
    @BindView(R.id.photo_recyclerview) RecyclerView mPhotoList;

    public static RecyclerViewGalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        RecyclerViewGalleryFragment fragment = new RecyclerViewGalleryFragment();
        Bundle data = new Bundle();
        data.putParcelableArrayList(SectionFragment.FRAGMENT_ARG_GALLERY, galleryItems);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_gallery_recyclerview;
    }

    @Override
    public String getTitle() {
        return "RecyclerView";
    }

    @Override
    protected void initView() {
        mPhotoList.setHasFixedSize(false);
        mPhotoList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mPhotoList.setAdapter(new RecyclerViewGalleryAdapter());
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPhotoImg;

        ViewHolder(View itemView) {
            super(itemView);

            mPhotoImg = itemView.findViewById(R.id.photo_img);
        }
    }

    private class RecyclerViewGalleryAdapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_poster_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
            // Double check if the fragment is detached
            if (getActivity() == null)
                return;

            GalleryItem galleryItem = mGalleryItems.get(position);

            // Set the transition name
            ViewCompat.setTransitionName(holder.mPhotoImg, String.valueOf(galleryItem.getId()));

            // Load the thumbnail image to the list
            Glide.with(getActivity())
                    .load(Uri.parse(galleryItem.getThumbnailImg()))
                    .apply(new RequestOptions().skipMemoryCache(false))
                    .into(holder.mPhotoImg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PhotoActivity.class);
                    intent.putExtra(PhotoActivity.PHOTO_GALLERY_LIST, mGalleryItems);
                    intent.putExtra(PhotoActivity.PHOTO_FOCUSED_INDEX, holder.getAdapterPosition());

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(), holder.mPhotoImg, ViewCompat.getTransitionName(holder.mPhotoImg));

                    getActivity().startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGalleryItems != null ? mGalleryItems.size() : 0;
        }
    }
}
