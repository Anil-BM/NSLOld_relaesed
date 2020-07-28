package com.nsl.beejtantra.scheduler;

import android.location.Location;

/**
 * Created by suprasoft on 2/1/2018.
 */

public interface OnLocationUpdateListener {
    void onLocationChange(Location location);
   // void onError(EnumUtil.ErrorType errorType);
}
