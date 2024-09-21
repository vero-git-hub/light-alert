package com.example.lightalert;

import android.os.Bundle;
import android.widget.TextView;

import com.example.lightalert.adapters.ViewPagerAdapter;
import com.example.lightalert.data.ScheduleDataLoader;
import com.example.lightalert.fragments.DayFragment;
import com.example.lightalert.data.Schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView currentDateTextView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDateTextView = findViewById(R.id.currentDate);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        currentDateTextView.setText(currentDate);

        ScheduleDataLoader dataLoader = new ScheduleDataLoader(this);
        JSONObject jsonSchedule = dataLoader.loadScheduleData();
        schedule = new Schedule(jsonSchedule);

        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        String[] days = getResources().getStringArray(R.array.days_of_week);

        for (String day : days) {
            DayFragment fragment = DayFragment.newInstance(day, schedule);
            adapter.addFragment(fragment, day);
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int index = (dayOfWeek + 5) % 7;
        tabLayout.getTabAt(index).select();
    }
}
