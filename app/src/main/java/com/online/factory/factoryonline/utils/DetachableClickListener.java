package com.online.factory.factoryonline.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.ViewTreeObserver;

public final class DetachableClickListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    public static DetachableClickListener wrap(DialogInterface.OnClickListener delegate) {
        return new DetachableClickListener(delegate);
    }

    public static DetachableClickListener wrap(DialogInterface.OnCancelListener delegate) {
        return new DetachableClickListener(delegate);
    }

    public static DetachableClickListener wrap(DialogInterface.OnDismissListener delegate) {
        return new DetachableClickListener(delegate);
    }

    private DialogInterface.OnClickListener delegateOrNull;

    private DialogInterface.OnCancelListener delegateCancelListener;

    private DialogInterface.OnDismissListener delegateDismissListener;

    private DetachableClickListener(DialogInterface.OnClickListener delegate) {
        this.delegateOrNull = delegate;
    }

    private DetachableClickListener(DialogInterface.OnCancelListener delegateCancelListener) {
        this.delegateCancelListener = delegateCancelListener;
    }

    public DetachableClickListener(DialogInterface.OnDismissListener delegateDismissListener) {
        this.delegateDismissListener = delegateDismissListener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (delegateOrNull != null) {
            delegateOrNull.onClick(dialog, which);
        }
    }

    public void clearOnDetach(Dialog dialog) {
        dialog.getWindow()
                .getDecorView()
                .getViewTreeObserver()
                .addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                    @Override
                    public void onWindowAttached() {
                    }

                    @Override
                    public void onWindowDetached() {
                        delegateOrNull = null;
                        delegateCancelListener = null;
                        delegateDismissListener = null;
                    }
                });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (delegateCancelListener != null) {
            delegateCancelListener.onCancel(dialog);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (delegateDismissListener != null) {
            delegateDismissListener.onDismiss(dialog);
        }
    }
}