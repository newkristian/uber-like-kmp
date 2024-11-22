package com.example.uberapp_tim9.unregistered_user.dialogs;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.example.uberapp_tim9.R;


public class LocationDialog extends AlertDialog.Builder{

	public LocationDialog(Context context) {
		super(context);
		setUpDialog();
	}

	private void setUpDialog(){
		setTitle(R.string.permissions);
		setMessage(R.string.location_disabled_message);
		setCancelable(false);
		setPositiveButton(R.string.yes, (dialog, id) -> {
			getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			dialog.dismiss();
		});
		setNegativeButton(R.string.no, (dialog, id) ->
		{
			dialog.cancel();
			System.exit(0);
		});
	}

	public AlertDialog prepareDialog(){
		AlertDialog dialog = create();
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

}

