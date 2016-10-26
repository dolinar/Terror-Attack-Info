package com.example.rok.terroristinfo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class InfoFragment extends Fragment {
    private SharedPreferencesInit spi;
    public static InfoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spi = new SharedPreferencesInit(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.info_frag_view, container, false);

        populateFragment(view);

        return view;

    }

    private void populateFragment(View view) {
        if (view == null) { return; }

        LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.linLayout);

        Data[] data = DataHolder.getData();

        if (data == null) {
            return;
        }

        Set<String> selections = spi.getEventTypeSet();

        Date calendarCurrentDate = calDate(new Date(), daysBeforeX());

        for (int i = 0; i < data.length; i++) {
            Date eventDate = data[i].getDate();

            Date calendarEventDate = calDate(eventDate, 0);

            if(selections != null && selections.contains(data[i].getEventType()) && calendarEventDate.compareTo(calendarCurrentDate) >= 0) {
                //icon + vertical(title + text)
                LinearLayout mainLayout = new LinearLayout(getContext());
                mainLayout.setPadding(5, 10, 5, 10);
                mainLayout.setClickable(true);
                mainLayout.setId(i);
                drawBorder(mainLayout);

                //for title and text
                LinearLayout verticalLayout = new LinearLayout(getContext());
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(12, 0, 0, 0);

                //TITLE
                TextView title = new TextView(getContext());
                title.setTextSize(19);
                title.setTextColor(Color.DKGRAY);
                title.setText(data[i].getLocation() + ",");

                //DATE
                TextView date = new TextView(getContext());
                date.setTextSize(15);
                date.setTextColor(Color.DKGRAY);
                date.setText(data[i].getStringDate(data[i].getDate()));

                //TEXT
                ExpandableTextView text = new ExpandableTextView(getContext());
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
                text.setTextSize(13);
                text.setText('\n' + data[i].getEventInfo());
                text.makeExpandable(5);


                //ICON
                TextView icon = new TextView(getContext());
                icon.setCompoundDrawablesWithIntrinsicBounds(data[i].getIconFromString(data[i].getIcon()
                ), 0, 0, 0);
                onClickListener(icon, mainLayout.getId());

                //BUTTON
                Button btn = new Button(getContext());
                btn.setText("More");
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                mainLayout.addView(icon);

                verticalLayout.addView(title);
                verticalLayout.addView(date);
                verticalLayout.addView(text);

                mainLayout.addView(verticalLayout);


                mainLayout.addView(btn);

                rootLayout.addView(mainLayout);

            }

        }
    }

    private void drawBorder(LinearLayout linLayout) {
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(2, 0xFF000000); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            linLayout.setBackgroundDrawable(border);
        } else {
            linLayout.setBackground(border);
        }
    }

    private void onClickListener(TextView view, final int id) {

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                spi.setTextViewID(id);

                TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
                tabLayout.getTabAt(0).select();
            }
        });
    }



    private int daysBeforeX() {

        String age = spi.getPrefsString("listEventAge", "oneMonth");

        switch (age) {
            case "oneDay":
                return 1;
            case "twoDays":
                return 2;
            case "threeDays":
                return 3;
            case "fourDays":
                return 4;
            case "oneWeek":
                return 7;
            case "twoWeeks":
                return 14;
            case "oneMonth":
                return 31;
            case "twoMonths":
                return 62;
            case "threeMonths":
                return 93;
            case "sixMonths":
                return 186;
            case "oneYear":
                return 365;
            case "twoYears":
                return 730;
            case "unlimited":
                return 999999;
            default:
                return 31;
        }
    }

    private Date calDate(Date date, int age) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, - age);
        return cal.getTime();
    }
}