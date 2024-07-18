package com.example.lightalert.util;

import com.example.lightalert.R;
import com.google.android.material.tabs.TabLayout;

public class ColorUtil {

    public static void applyHighlightToCurrentDay(TabLayout tabLayout, int currentDayIndex) {
        TabLayout.Tab tab = tabLayout.getTabAt(currentDayIndex);
        if (tab != null && tab.view != null) {
            tab.view.setBackgroundResource(R.drawable.tab_highlight);
        }
    }
}