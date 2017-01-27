package com.surendra.androidhomeautomation.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.surendra.androidhomeautomation.R;

public class NumberPickerFragment extends DialogFragment {
    private OnCompleteListener mListener;
    private EditText editNumber;
    private Switch aSwitch;
    private boolean switchState = false;
    private char id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.schedule_timer_layout, null);
        builder.setView(view)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onComplete(id,
                                Integer.parseInt(editNumber.getText().toString()) * 1000,
                                aSwitch.isChecked());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        builder.setMessage("Input the time in seconds.");
        builder.setTitle("Scheduler");
        editNumber = (EditText) view.findViewById(R.id.editTextTimer);
        aSwitch = (Switch) view.findViewById(R.id.switch_timer);
        aSwitch.setChecked(switchState);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnCompleteListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public interface OnCompleteListener {
        void onComplete(char id, int time, boolean switchState);
    }

    public void setId(char id) {
        this.id = id;
    }

    public void setSwitchState(boolean state) {
        switchState = state;
    }

    public void setSwitchMessage(String msg) {
        aSwitch.setText(msg);
    }


}