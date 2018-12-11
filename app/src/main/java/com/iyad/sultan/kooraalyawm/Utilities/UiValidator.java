package com.iyad.sultan.kooraalyawm.Utilities;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.iyad.sultan.kooraalyawm.R;

public class UiValidator {

    public boolean isEditTextEmpty(View v){

        EditText editText = (EditText) v;
        return editText.getText().toString().trim().isEmpty();

    }

    public  boolean isValidEmail(String email){

        return  (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


    public  boolean isValidPassword(String password){

        return password.trim().isEmpty();
    }
}
