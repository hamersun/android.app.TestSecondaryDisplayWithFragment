package com.example.hamer.testsecondarydisplaywithfragment;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.media.MediaRouter.SimpleCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    MediaRouter router = null;
    PresentationFragment preso = null;
    SimpleCallback cb = null;
    View inline = null;
    TextView prose = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inline = findViewById(R.id.preso);
        prose = (TextView) findViewById(R.id.prose);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (cb == null) {
            cb = new RouteCallback();
            router = (MediaRouter) getSystemService(MEDIA_ROUTER_SERVICE);
        }

        handleRoute(router.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_VIDEO));
        router.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO, cb);
    }

    @Override
    protected void onStop() {
        clearPreso(false);

        if (router != null) {
            router.removeCallback(cb);
        }

        super.onStop();
    }

    private void handleRoute(RouteInfo route) {
        if (route == null) {
            clearPreso(true);
        }
        else {
            Display display = route.getPresentationDisplay();

            if (route.isEnabled() && display != null) {
                if (preso == null) {
                    showPreso(route);
                    Log.d(getClass().getSimpleName(), "enabled route");
                }
                else if (preso.getDisplay().getDisplayId() != display.getDisplayId()) {
                    clearPreso(true);
                    showPreso(route);
                    Log.d(getClass().getSimpleName(), "switched route");
                }
                else {
                    // no-op
                }
            }
        }
    }

    private void clearPreso(boolean switchToInline) {
        if (switchToInline) {
            inline.setVisibility(View.VISIBLE);
            prose.setText(R.string.primary);
            getFragmentManager().beginTransaction().add(R.id.preso, buildPreso(null)).commit();
        }

        if (preso != null) {
            preso.dismiss();
            preso = null;
        }
    }

    private void showPreso(RouteInfo route) {
        if (inline.getVisibility() == View.VISIBLE) {
            inline.setVisibility(View.GONE);
            prose.setText(R.string.secondary);

            Fragment f = getFragmentManager().findFragmentById(R.id.preso);
            getFragmentManager().beginTransaction().remove(f).commit();
        }

        preso = buildPreso(route.getPresentationDisplay());
        preso.show(getFragmentManager(), "preso");
    }

    private PresentationFragment buildPreso(Display display) {
        return (SamplePresentationFragment.newInstance(this, display,
                                                        "http://www.google.com"));
    }

    private class RouteCallback extends SimpleCallback {

        @Override
        public void onRoutePresentationDisplayChanged(MediaRouter router, RouteInfo info) {

            handleRoute(info);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
