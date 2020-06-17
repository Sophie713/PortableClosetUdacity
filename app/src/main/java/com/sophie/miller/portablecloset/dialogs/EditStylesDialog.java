package com.sophie.miller.portablecloset.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.sophie.miller.portablecloset.R;

public class EditStylesDialog extends Dialog implements View.OnClickListener {
//todo show and fill with data
    public EditStylesDialog(@NonNull Context context) {
        super(context);
        show(); //todo test
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_styles);

        View closeBtn = findViewById(R.id.edit_styles_close);
        closeBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
