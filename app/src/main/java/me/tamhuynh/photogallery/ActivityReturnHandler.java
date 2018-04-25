package me.tamhuynh.photogallery;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by tamhuynh on 4/25/18.
 *
 * Interface for activity result handler
 */
public interface ActivityReturnHandler {
    void onActivityReenter(Activity activity, int resultCode, Intent data);
}
