package life.z_turn.app.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import life.z_turn.app.R;

/**
 * 设置
 */
public class SettingsFragment extends PreferenceFragment {


    public static final String KEY_MESSAGE_PUSH = "message_push";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}
