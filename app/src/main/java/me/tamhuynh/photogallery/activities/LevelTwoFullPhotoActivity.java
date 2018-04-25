package me.tamhuynh.photogallery.activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.models.GalleryItem;

public class LevelTwoFullPhotoActivity extends AppCompatActivity {

    @BindView(R.id.photo_full_list) ViewPager mPhotoPager;
//    @BindView(R.id.pager_indicator_container) RadioGroup mIndicatorContainer;

    public final static String PHOTO_GALLERY_LIST = "photo_gallery_list";
    public final static String PHOTO_FOCUSED_INDEX = "photo_focused_index";

    private final static String LAYOUT_TAG = "PhotoItem";

    private ArrayList<GalleryItem> mGalleryItems;
    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo_level_2);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            mGalleryItems = data.getParcelableArrayList(PHOTO_GALLERY_LIST);

            mCurrentIndex = data.getInt(PHOTO_FOCUSED_INDEX);

            mPhotoPager.setAdapter(new PhotoPagerAdapter());
            mPhotoPager.setCurrentItem(mCurrentIndex);

            supportPostponeEnterTransition();
        }
    }

    private class PhotoPagerAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            GalleryItem item = mGalleryItems.get(position);

            final PhotoView photoView = new PhotoView(LevelTwoFullPhotoActivity.this);
            photoView.setTag(LAYOUT_TAG + position);
            ViewCompat.setTransitionName(photoView, String.valueOf(item.getId()));

            // Define thumbnail request, using the thumbnail img (which is already been cached if loaded in previous list activity)
            RequestBuilder<Drawable> thumbnailBuilder =  Glide.with(LevelTwoFullPhotoActivity.this)
                    .load(Uri.parse(item.getThumbnailImg()))
                    .apply(new RequestOptions().dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE));

            // Start the pending transition after the selected thumbnail is loaded
            if (position == mCurrentIndex) {
                thumbnailBuilder = thumbnailBuilder.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();

                        return false;
                    }
                });
            }

            // Load original poster using the thumbnail request
            Glide.with(LevelTwoFullPhotoActivity.this)
                    .load(Uri.parse(item.getOriginalImg()))
                    .thumbnail(thumbnailBuilder)
                    .apply(new RequestOptions().dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            photoView.setImageDrawable(resource);
                        }
                    });

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mGalleryItems.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
