package com.bitgymup.gymup.admin;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class Variables {
    public static String usuario_s;
    public static String id_gym_n = "";



    public static String getUsuario_s() {
        return usuario_s;
    }

    public static void setUsuario_s(String usuario_s) {
        Variables.usuario_s = usuario_s;
    }

    public static void hideSoftKeyboard (Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}

