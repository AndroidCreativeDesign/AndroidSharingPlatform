package life.z_turn.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.avos.avoscloud.AVUser;
import com.umeng.update.UmengUpdateAgent;

import life.z_turn.app.R;
import life.z_turn.app.utils.ExceptionUtil;


/**
 * 设置 界面
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AVUser.getCurrentUser() != null) {
            addPreferencesFromResource(R.xml.settings);
            Preference accountSettings = findPreference(getResources().getString(R.string.preference_key_account_settings));
            accountSettings.setOnPreferenceClickListener(this);
        } else {
            addPreferencesFromResource(R.xml.settings_anonymous);
        }

        Preference appCheck = findPreference(getResources().getString(R.string.preference_key_app_check));
        appCheck.setOnPreferenceClickListener(this);
        String version;
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            version = info.versionName;
            appCheck.setSummary(version);
        } catch (Exception e) {
            ExceptionUtil.printStackTrace(e);
        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(getResources().getString(R.string.preference_key_account_settings))) {
            startActivity(new Intent(getActivity(), AccountSettingsActivity.class));
        }

        if (preference.getKey().equals(getResources().getString(R.string.preference_key_app_check))) {
            UmengUpdateAgent.forceUpdate(getActivity());
        }

        if (preference.getKey().equals(getResources().getString(R.string.preference_key_business_cooperation))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("商务合作");
            builder.show();
        }

        if (preference.getKey().equals(getResources().getString(R.string.preference_key_about_us))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("关于我们");
            builder.show();
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
