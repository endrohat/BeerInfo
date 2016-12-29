package com.indraneel.beerinfo.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.indraneel.beerinfo.R;

/**
 * Dialog to show sort selection for beers
 */

public class SortDialog extends DialogFragment {

    public static SortDialog newInstance(int title, int sortIndex) {
        SortDialog sortDialogFragment = new SortDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("sortIndex", sortIndex);
        sortDialogFragment.setArguments(args);
        return sortDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        int sortIndex = getArguments().getInt("sortIndex");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setSingleChoiceItems(R.array.sort_parameters, sortIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        ((DialogItemClickListener) getActivity()).onItemClick(dialogInterface,position);
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }

    /* Interface to handle dialog item clicks */
    public interface DialogItemClickListener {
        void onItemClick(DialogInterface dialogInterface, int position);
    }
}