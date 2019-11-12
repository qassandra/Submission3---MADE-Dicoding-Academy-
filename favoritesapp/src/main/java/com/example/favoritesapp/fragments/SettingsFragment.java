package com.example.favoritesapp.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.favoritesapp.R;
import com.example.favoritesapp.reminder.ReleaseReminder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private final ReleaseReminder movieReminder = new ReleaseReminder();
    private String daily_remainder;
    private String release_remainder;
    private SwitchPreference dailySwitch;
    private SwitchPreference releaseSwitch;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        daily_remainder = getString(R.string.key_daily);
        release_remainder = getString(R.string.key_release);
        addPreferencesFromResource(R.xml.notifications);

        dailySwitch = (SwitchPreference) findPreference(daily_remainder);
        releaseSwitch = (SwitchPreference) findPreference(release_remainder);

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        dailySwitch.setChecked(sharedPreferences.getBoolean(daily_remainder, false));
        releaseSwitch.setChecked(sharedPreferences.getBoolean(release_remainder, false));

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(daily_remainder)) {
            boolean daily = sharedPreferences.getBoolean(daily_remainder, false);
            dailySwitch.setChecked(daily);
            if (daily) {
                String timeDaily = "07:00";
                movieReminder.setRepeatingAlarm(getActivity(), ReleaseReminder.TYPE_DAILY, timeDaily, getString(R.string.reminder_title), getString(R.string.reminder_message));
            } else {
                movieReminder.cancelAlarm(getActivity(), ReleaseReminder.TYPE_DAILY);
            }
        }

        if (key.equals(release_remainder)) {
            boolean release = sharedPreferences.getBoolean(release_remainder, false);
            releaseSwitch.setChecked(release);
            if (release) {
                String timeRelease = "08:00";
                movieReminder.setRepeatingAlarm(getActivity(), ReleaseReminder.TYPE_RELEASE, timeRelease, getString(R.string.release_title), getString(R.string.release_message));
            } else {
                movieReminder.cancelAlarm(getActivity(), ReleaseReminder.TYPE_RELEASE);
            }
        }
    }
}
