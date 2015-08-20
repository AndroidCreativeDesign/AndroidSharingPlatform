package life.z_turn.app.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.avos.avoscloud.AVUser;

import life.z_turn.app.R;


/**
 * Created by daixiaodong on 15/8/15.
 */
public class AccountSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_account_and_password);
        Preference email = findPreference("preference_id_account_email");
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser.getEmail() != null) {
            email.setSummary(currentUser.getEmail());
        } else {
            email.setSummary("绑定邮箱");
        }
        email.setOnPreferenceClickListener(this);

        Preference phone = findPreference("preference_id_account_phone");
        if (currentUser.getMobilePhoneNumber() != null) {
            phone.setSummary(currentUser.getMobilePhoneNumber());
        } else {
            phone.setSummary("绑定手机");
        }
        phone.setOnPreferenceClickListener(this);

        Preference password = findPreference("preference_id_account_setting_password");
        password.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference.getKey().equals("preference_id_account_email")) {
            setEmail();
        }

        if (preference.getKey().equals("preference_id_account_phone")) {
            setPhone();
        }

        return false;
    }

    private void setPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_set_phone);
        builder.show();
    }

    private void setEmail() {

    }
}
