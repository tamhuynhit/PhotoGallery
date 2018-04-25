package me.tamhuynh.photogallery.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 * Level 4: ViewPager fullscreen photo: Load the previous photo as thumbnail then replace with the new full-resolution photo
 * Capture the new selected photo and re-config the return shared element transition when getting back
 */
public class LevelFourFullPhotoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.photo_full_list) ViewPager mPhotoPager;

    public final static String PHOTO_GALLERY_LIST = "photo_gallery_list";
    public final static String PHOTO_FOCUSED_INDEX = "photo_focused_index";

    public final static int RESULT_PHOTO_CLOSED = 100;

    private final static String LAYOUT_TAG_PREFIX = "PhotoItem";

    private ArrayList<GalleryItem> mGalleryItems;
    private int mCurrentIndex;

    private final SharedElementCallback mFinishSharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            View selectedView = getSelectedView();
            if (selectedView == null)
                return;

            // Clear all current shared views and names
            names.clear();
            sharedElements.clear();

            // Store new selected view and name
            String transitionName = ViewCompat.getTransitionName(selectedView);
            names.add(transitionName);
            sharedElements.put(transitionName, selectedView);

            setExitSharedElementCallback((SharedElementCallback) null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_viewpager_photo);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            mGalleryItems = data.getParcelableArrayList(PHOTO_GALLERY_LIST);

            mCurrentIndex = data.getInt(PHOTO_FOCUSED_INDEX);

            mPhotoPager.addOnPageChangeListener(this);
            mPhotoPager.setAdapter(new PhotoPagerAdapter());
            mPhotoPager.setCurrentItem(mCurrentIndex);

            supportPostponeEnterTransition();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void finishAfterTransition() {
        setEnterSharedElementCallback(mFinishSharedElementCallback);

        Intent intent = new Intent();
        intent.putExtra(PHOTO_FOCUSED_INDEX, mCurrentIndex);
        setResult(RESULT_PHOTO_CLOSED, intent);

        super.finishAfterTransition();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    private View getSelectedView() {
        try {
            return mPhotoPager.findViewWithTag(LAYOUT_TAG_PREFIX + mCurrentIndex);
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            return null;
        }
    }

    private class PhotoPagerAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            GalleryItem item = mGalleryItems.get(position);

            final PhotoView photoView = new PhotoView(LevelFourFullPhotoActivity.this);

            // The tag is needed for later getSelectedItem from the ViewPager
            photoView.setTag(LAYOUT_TAG_PREFIX + position);

            ViewCompat.setTransitionName(photoView, String.valueOf(item.getId()));

            // Define thumbnail request, using the thumbnail img (which is already been cached if loaded in previous list activity)
            RequestBuilder<Drawable> thumbnailBuilder =  Glide.with(LevelFourFullPhotoActivity.this)
                    .load(Uri.parse(item.getThumbnailImg()))
                    .apply(new RequestOptions().dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE));

            // Start the pending transition after the selected thumbnail is loaded
            if (position == mCurrentIndex) {
                thumbnailBuilder = thumbnailBuilder.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Continue the transition
                        supportStartPostponedEnterTransition();

                        return false;
                    }

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();

                        Toast.makeText(LevelFourFullPhotoActivity.this, "Load image fail", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });
            }

            // Load full-resolution poster with the thumbnail request
            // Glide doesn't allow to simply use .into(photoView) when we use setTag() above
            Glide.with(LevelFourFullPhotoActivity.this)
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
