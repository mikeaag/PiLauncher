package uk.co.xenonsoft.pilauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import java.net.URI;
import java.net.URISyntaxException;


public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                getFragmentManager().beginTransaction().replace(R.id.container, new PrefsFragment()).addToBackStack(null).commit();
                Log.v("action_settings", "Clicked!");
                //return true;
                break;

            case R.id.action_reset:
                Log.v("action_reset", "Clicked!");
                //return true;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArmLauncherClicked(View view) {
        Switch s = (Switch) view;
        boolean isArmed = s.isChecked();

        // Disarm until confirmation
        disarmLauncher(s);

        if (isArmed) {
            showConfirmArmDialog(s);
        }
    }

    public void armLauncher(Switch s) {
        s.setChecked(true);

    }

    public void disarmLauncher(Switch s) {
        s.setChecked(false);
    }

    public void showConfirmArmDialog(final Switch s) {
        new AlertDialog.Builder(this)
                .setTitle("Arm Launcher?")
                .setMessage("Are you sure you want to arm the launcher?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        armLauncher(s);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        disarmLauncher(s);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void onLaunchFirework(View view) {
        Integer FireworkNumber = 0;
        switch (view.getId()) {
            case R.id.btnLaunchFirework1:
                FireworkNumber = 1;
                break;

            case R.id.btnLaunchFirework2:
                FireworkNumber = 2;
                break;

            case R.id.btnLaunchFirework3:
                FireworkNumber = 3;
                break;

            case R.id.btnLaunchFirework4:
                FireworkNumber = 4;
                break;

            case R.id.btnLaunchFirework5:
                FireworkNumber = 5;
                break;

            case R.id.btnLaunchFirework6:
                FireworkNumber = 6;
                break;

            case R.id.btnLaunchFirework7:
                FireworkNumber = 7;
                break;

            case R.id.btnLaunchFirework8:
                FireworkNumber = 8;
                break;
        }

        View armSwitch = findViewById(R.id.armSwitch);
        boolean isArmed = ((Switch) armSwitch).isChecked();

        if (isArmed) {
            launchFirework(FireworkNumber);
        } else {
            armLauncherFirstMessage();
        }
    }

    public void launchFirework(Integer FireworkNumber) {

        String finalUrl;
        String defaultUrl = "http://192.168.1.85/PiLaunchCommand/Launch?firework=##ID##";

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String preferenceUrl = getPrefs.getString("url", defaultUrl);

        if (!preferenceUrl.equals("")) {
            finalUrl = replacePlaceHolderWithId(preferenceUrl, FireworkNumber);
        } else {
            finalUrl = replacePlaceHolderWithId(defaultUrl, FireworkNumber);
        }

        HttpGetter myHttpGetter = new HttpGetter();
        try {
            myHttpGetter.execute(new URI(finalUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public String replacePlaceHolderWithId(String url, Integer id) {
        return url.replaceAll("##ID##", id.toString());
    }

    public void armLauncherFirstMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Arm Launcher First")
                .setMessage("You must arm the launcher before you can launch a firework")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_launcher, container, false);
        }
    }

}
