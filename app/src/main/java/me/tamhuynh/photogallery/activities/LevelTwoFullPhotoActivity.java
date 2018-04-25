package me.tamhuynh.photogallery.activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.models.GalleryItem;

/**
 * Created by tamhuynh on 3/22/18.
 *
 * Level 2: Single fullscreen photo: Load the previous photo as thumbnail then replace with the new full-resolution photo
 */
public class LevelTwoFullPhotoActivity extends AppCompatActivity {

    @BindView(R.id.photo_full_img) ImageView mPhotoImg;

    public final static String PHOTO_GALLERY_ITEM = "photo_gallery_item";

    private GalleryItem mGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_single_photo);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            mGallery = data.getParcelable(PHOTO_GALLERY_ITEM);

            supportPostponeEnterTransition();

            // Set the transition name from the selected item
            ViewCompat.setTransitionName(mPhotoImg, String.valueOf(mGallery.getId()));
        }

        loadFullImg();
    }

    /**
     * Load thumbnail low-resolution photo first and continue the transition, then start loading the full-resolution photo
     */
    private void loadFullImg() {
        // Define thumbnail request, using the thumbnail img (which is already been cached if loaded in previous list activity)
        RequestBuilder<Drawable> thumbnailBuilder =  Glide.with(this)
                .load(Uri.parse(mGallery.getThumbnailImg()))
                .apply(new RequestOptions().dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Continue the transition
                        supportStartPostponedEnterTransition();

                        return false;
                    }

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();

                        Toast.makeText(LevelTwoFullPhotoActivity.this, "Load image fail", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });

        // Load full-resolution poster with the defined thumbnail request
        Glide.with(this)
                .load(Uri.parse(mGallery.getOriginalImg()))
                .thumbnail(thumbnailBuilder)
                .apply(new RequestOptions().dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(mPhotoImg);
    }
}
