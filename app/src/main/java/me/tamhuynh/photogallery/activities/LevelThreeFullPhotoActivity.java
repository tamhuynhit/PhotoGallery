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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 * Level 3: ViewPager fullscreen photo: Load the previous photo as thumbnail then replace with the new full-resolution photo
 */
public class LevelThreeFullPhotoActivity extends AppCompatActivity {

    @BindView(R.id.photo_full_list) ViewPager mPhotoPager;

    public final static String PHOTO_GALLERY_LIST = "photo_gallery_list";
    public final static String PHOTO_FOCUSED_INDEX = "photo_focused_index";

    private ArrayList<GalleryItem> mGalleryItems;
    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_viewpager_photo);
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

            final PhotoView photoView = new PhotoView(LevelThreeFullPhotoActivity.this);
            ViewCompat.setTransitionName(photoView, String.valueOf(item.getId()));

            // Define thumbnail request, using the thumbnail img (which is already been cached if loaded in previous list activity)
            RequestBuilder<Drawable> thumbnailBuilder =  Glide.with(LevelThreeFullPhotoActivity.this)
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

                        Toast.makeText(LevelThreeFullPhotoActivity.this, "Load image fail", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });
            }

            // Load full-resolution poster using the thumbnail request
            Glide.with(LevelThreeFullPhotoActivity.this)
                    .load(Uri.parse(item.getOriginalImg()))
                    .thumbnail(thumbnailBuilder)
                    .apply(new RequestOptions().dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(photoView);

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
