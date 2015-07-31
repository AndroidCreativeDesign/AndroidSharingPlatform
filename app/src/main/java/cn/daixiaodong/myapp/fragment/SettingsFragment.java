package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cn.daixiaodong.myapp.R;

/**
 * 设置
 */
public class SettingsFragment extends PreferenceFragment {


    public static final String KEY_MESSAGE_PUSH = "message_push";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
