package me.tamhuynh.photogallery.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tamhuynh on 3/22/18.
 *
 */
public class GalleryItem implements Parcelable {
    private final static String GALLERY_URL_PREFIX = "";

    private static int mItemCount;

    private int mId;

    private String mThumbnailImg;
    private String mOriginalImg;

    public GalleryItem(String img) {
        mThumbnailImg = GALLERY_URL_PREFIX + img;

        mId = mItemCount++;
    }

    public GalleryItem(String thumbnailImg, String originalImg) {
        this(thumbnailImg);

        mOriginalImg = GALLERY_URL_PREFIX + originalImg;
    }

    private GalleryItem(Parcel in) {
        mId = in.readInt();
        mThumbnailImg = in.readString();
        mOriginalImg = in.readString();
    }

    public int getId() {
        return mId;
    }

    public String getThumbnailImg() {
        return mThumbnailImg;
    }

    public String getOriginalImg() {
        return mOriginalImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mThumbnailImg);
        dest.writeString(mOriginalImg);
    }

    public static final Parcelable.Creator<GalleryItem> CREATOR = new Parcelable.Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel source) {
            return new GalleryItem(source);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };
}
