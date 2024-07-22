package com.meganar.smart;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.smart.R;

public class DialogManager {

    public static void showCustomAlertDialog(Context context, final ColorDialogCallback callback) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.color_palette);
        final TextView blueColor = dialog.findViewById(R.id.blueColor);
        blueColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(blueColor.getText().toString());
                dialog.cancel();
            }
        });
        final TextView greenColor = dialog.findViewById(R.id.greenColor);
       greenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(greenColor.getText().toString());
                dialog.cancel();
            }
        });
        final TextView redColor = dialog.findViewById(R.id.redColor);
        redColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(redColor.getText().toString());
                dialog.cancel();
            }
        });
        final TextView purpleColor = dialog.findViewById(R.id.purpleColor);
        purpleColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(purpleColor.getText().toString());
                dialog.cancel();
            }
        });
        final TextView bluegreenColor = dialog.findViewById(R.id.bluegreenColor);
        bluegreenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(bluegreenColor.getText().toString());
                dialog.cancel();
            }
        });
        final TextView greyColor = dialog.findViewById(R.id.greyColor);
        greyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(greyColor.getText().toString());
                dialog.cancel();
            }
        });
        final TextView orangeColor = dialog.findViewById(R.id.orangeColor);
        orangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(orangeColor.getText().toString());
                dialog.cancel();
            }
        });
        final TextView pinkColor = dialog.findViewById(R.id.pinkColor);
        pinkColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChosen(pinkColor.getText().toString());
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
