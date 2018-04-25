package me.tamhuynh.photogallery.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 * Base class for each shared element transition level fragment
 * See: blog.tamhuynh.me for the tutorial
 */
public abstract class SectionFragment extends Fragment {
    @BindView(R.id.photo_recyclerview) RecyclerView mPhotoList;

    public final static String FRAGMENT_ARG_GALLERY = "arg_gallery";

    protected ArrayList<GalleryItem> mGalleryItems;
    protected ImageView mSelectedImageView;

    protected abstract void initView();
    public abstract String getTitle();
    public abstract String getDescription();
    protected abstract void handleItemClicked(ViewHolder holder, GalleryItem item);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poster_list, container, false);

        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            mGalleryItems = args.getParcelableArrayList(FRAGMENT_ARG_GALLERY);
        }

        mPhotoList.setHasFixedSize(false);
        mPhotoList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mPhotoList.setAdapter(new RecyclerViewGalleryAdapter());

        initView();

        return view;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mPhotoImg;

        ViewHolder(View itemView) {
            super(itemView);

            mPhotoImg = itemView.findViewById(R.id.photo_img);
        }
    }

    class RecyclerViewGalleryAdapter extends RecyclerView.Adapter<ViewHolder> {
        private AdapterItemListener mListener;

        public void setListener(AdapterItemListener listener) {
            mListener = listener;
        }

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

            final GalleryItem galleryItem = mGalleryItems.get(position);

            // Set the transition name to each ImageView
            ViewCompat.setTransitionName(holder.mPhotoImg, String.valueOf(galleryItem.getId()));

            // Load the thumbnail image to the list, cache it into memory and data so the fullscreen thumbnail can be loaded faster
            Glide.with(getActivity())
                    .load(Uri.parse(galleryItem.getThumbnailImg()))
                    .apply(new RequestOptions().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(holder.mPhotoImg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedImageView = holder.mPhotoImg;

                    handleItemClicked(holder, galleryItem);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGalleryItems != null ? mGalleryItems.size() : 0;
        }

        @Override
        public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
            super.onViewAttachedToWindow(holder);

            if (mListener != null)
                mListener.onAttachedToWindow(holder);
        }
    }

    public interface AdapterItemListener {
        void onAttachedToWindow(@NonNull ViewHolder holder);
    }
}
