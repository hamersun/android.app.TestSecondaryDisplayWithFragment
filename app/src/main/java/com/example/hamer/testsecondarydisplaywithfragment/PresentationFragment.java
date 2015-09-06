package com.example.hamer.testsecondarydisplaywithfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;


/**
 * A placeholder fragment containing a simple view.
 */
public class PresentationFragment extends DialogFragment {

    private Display display = null;
    private Presentation preso = null;

    public PresentationFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (preso == null) {
            return (super.onCreateDialog(savedInstanceState));
        }

        return preso;
    }

    public void setDisplay(Context ctxt, Display display) {
        if (display == null) {
            preso = null;
        }
        else {
            preso = new Presentation(ctxt, display, getTheme());
        }
        this.display = display;
    }

    public Display getDisplay() {
        return (display);
    }

    protected Context getContext() {
        if (preso != null) {
            return (preso.getContext());
        }

        return (getActivity());
    }
}
