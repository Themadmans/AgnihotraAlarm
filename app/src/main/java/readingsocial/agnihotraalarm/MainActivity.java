package readingsocial.agnihotraalarm;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {
public static int location = 1;  // Default is Bhopal , 2 is farm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // to keep screen active


    }

    public void getmylocation {
        LocationListnr locationListnr = new LocationListnr(getApplicationContext());
        double latitude = locationListnr.getLatitude();
        double longitude = locationListnr.getLongitude();
       // Toast.makeText(getApplicationContext(), " Your location is : " + latitude + " , " + longitude ,Toast.LENGTH_SHORT).show();
        if ( longitude > 78.4 && longitude <78.8) { location=2;
       Toast.makeText(getApplicationContext()," ?? ????? ?? ?? ?? !",Toast.LENGTH_SHORT ).show();
        }  // Assuming more than 78 (ACtual 78.6) to be farm
        else if (longitude > 77.2 && longitude <77.65) {location=1;
           Toast.makeText(getApplicationContext()," ?? ????? ??? ??? !",Toast.LENGTH_SHORT ).show();
        }  // less than 78 to be Bhopal (ACtual 77.45)
        else {
            Toast.makeText(getApplicationContext()," ",Toast.LENGTH_SHORT ).show();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.english:
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
           //     locationchangeto(1);
                setLangRecreate("en");

                break;
            case R.id.hindi:
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
             //   locationchangeto(1);
                setLangRecreate("hi");

                break;
            case R.id.Bhopal :
                locationchangeto(1);
                break;
            case R.id.Farm:
                locationchangeto(2);
        }
        return true;
    }

private void locationchangeto(int loc) {
    AssetManager am = this.getAssets();

    long date = System.currentTimeMillis();

    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, h:mm a");
    String dateString = sdf.format(date);


    String CURRENT_MONTH = (dateString.substring(0,3));
    String CURRENT_DATE= (dateString.substring(4,6));
    String DATE_STRING = "";
    int MONTH_FOUND = 0;
    TextView textView1 = (TextView) findViewById(R.id.textViewloc);


    InputStream inputStream;
//String loca= "";
    String data = "";
    StringBuffer sbuffer = new StringBuffer();
    try {
if(loc==2) {
    inputStream = am.open("agnihotra.txt");
    textView1.setText(getString(R.string.locfarm));
    location=2;
}
else {
    inputStream = am.open("agnihotrabpl.txt");
    textView1.setText(getString(R.string.locbpl));
    location=1;
}
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((data = bufferedReader.readLine()) != null)
        {
            if(data.contains(CURRENT_MONTH))
            {
                MONTH_FOUND = 1;

            }
            if(MONTH_FOUND==1 && data.startsWith(CURRENT_DATE))
            {
                DATE_STRING = data;
                MONTH_FOUND=0;
            }

        }

    }
    catch (IOException e)
    {
        Toast.makeText(this,"Unable to open", Toast.LENGTH_SHORT).show();
    }

    //  Toast.makeText(this,"Mil Jaya " + DATE_STRING , Toast.LENGTH_SHORT).show();
    TextView textView = (TextView) findViewById(R.id.text1);
    textView.setText(getString(R.string.todayeng) + " " + CURRENT_MONTH + " " + CURRENT_DATE + " \n"+ getString(R.string.sunr) + " :  " + DATE_STRING.substring(3,11) + " \n " + getString(R.string.suns) + " : " + DATE_STRING.substring(11));


}

    public void onResume() {
        super.onResume();
                locationchangeto(location);
      }

    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
    //    Locale.setDefault(locale);
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
     recreate();
    }
}
