package com.zhongke.common.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Fushize on 2020/7/8.
 */

public class ZKInputUtil {
    public static void hideInputMethod(Context act, EditText etext) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(etext.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInputMethod(Context act, View mEditView) {
        try {
            mEditView.requestFocus();
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditView, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}