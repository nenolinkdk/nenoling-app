package dk.nenolink.nenoling.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/** Opens optional resource URLs in the device browser; no app INTERNET permission is required. */
public final class ExternalResourceLauncher {
    private ExternalResourceLauncher() {}

    public static boolean open(Context context, String url) {
        if (context == null || url == null || url.trim().isEmpty()) return false;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        if (!(context instanceof android.app.Activity)) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException exception) {
            return false;
        }
    }
}
