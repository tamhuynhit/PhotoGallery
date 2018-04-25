package me.tamhuynh.photogallery.activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tamhuynh.photogallery.R;
import me.tamhuynh.photogallery.models.GalleryItem;

public class LevelOneFullPhotoActivity extends AppCompatActivity {

    @BindView(R.id.photo_full_img) ImageView mPhotoImg;

    public final static String PHOTO_GALLERY_ITEM = "photo_gallery_item";

    private GalleryItem mGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo_level_1);
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

    private void loadFullImg() {
        // Load the same thumbnail image for level 1
        Glide.with(this)
                .load(Uri.parse(mGallery.getThumbnailImg()))
                .apply(new RequestOptions().dontAnimate().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mPhotoImg.setImageDrawable(resource);

                        supportStartPostponedEnterTransition();
                    }
                });
    }
}
