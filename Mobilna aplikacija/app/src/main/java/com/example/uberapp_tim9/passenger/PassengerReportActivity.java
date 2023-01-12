package com.example.uberapp_tim9.passenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.core.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim9.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PassengerReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_report);

        setSupportActionBar(findViewById(R.id.toolbarPassengerReport));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);

        LinearLayout graphsLayout = findViewById(R.id.graphsLayout);

        Button pickDateRangeButton = findViewById(R.id.pickDateRangeButton);

        TextView fromTextView = findViewById(R.id.fromTextView);
        TextView toTextView = findViewById(R.id.toTextView);

        TextView ridesPerDaySumTextView = findViewById(R.id.ridesPerDaySumTextView);
        TextView ridesPerDayAvgTextView = findViewById(R.id.ridesPerDayAvgTextView);

        TextView distancePerDaySumTextView = findViewById(R.id.distanceSumTextView);
        TextView distancePerDayAvgTextView = findViewById(R.id.distanceAvgTextView);

        TextView moneySpentPerDaySumTextView = findViewById(R.id.moneySpentSumTextView);
        TextView moneySpentPerDayAvgTextView = findViewById(R.id.moneySpentAvgTextView);

        GraphView rideNumberGraph = findViewById(R.id.rideNumberGraph);
        rideNumberGraph.setTitle("Broj vožnji po danu");
        rideNumberGraph.setTitleColor(getResources().getColor(R.color.black));
        rideNumberGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.black));
        rideNumberGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.black));
        rideNumberGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.dark_grey));

        GraphView distanceGraph = findViewById(R.id.distanceGraph);
        distanceGraph.setTitle("Distanca pređena po danu");
        distanceGraph.setTitleColor(getResources().getColor(R.color.black));
        distanceGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.black));
        distanceGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.black));
        distanceGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.dark_grey));

        GraphView moneySpentGraph = findViewById(R.id.moneySpentGraph);
        moneySpentGraph.setTitle("Novac potrošen po danu");
        moneySpentGraph.setTitleColor(getResources().getColor(R.color.black));
        moneySpentGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.black));
        moneySpentGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.black));
        moneySpentGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.dark_grey));

        DataPoint[] dayToRideNumber = new DataPoint[] {
                new DataPoint(1, 3),
                new DataPoint(2, 1),
                new DataPoint(3, 2),
                new DataPoint(4, 0),
                new DataPoint(5, 5)
        };

        int rideNumberSum = Arrays.stream(dayToRideNumber).mapToInt(dp -> (int) dp.getY()).sum();
        double rideNumberAverage = rideNumberSum / (double) dayToRideNumber.length;

        DataPoint[] dayToDistance = new DataPoint[] {
                new DataPoint(1, 10),
                new DataPoint(2, 5),
                new DataPoint(3, 7),
                new DataPoint(4, 0),
                new DataPoint(5, 15)
        };

        int distanceSum = Arrays.stream(dayToDistance).mapToInt(dp -> (int) dp.getY()).sum();
        double distanceAverage = distanceSum / (double) dayToDistance.length;

        DataPoint[] dayToMoneySpent = new DataPoint[] {
                new DataPoint(1, 1000),
                new DataPoint(2, 500),
                new DataPoint(3, 700),
                new DataPoint(4, 0),
                new DataPoint(5, 1500)
        };

        int moneySpentSum = Arrays.stream(dayToMoneySpent).mapToInt(dp -> (int) dp.getY()).sum();
        double moneySpentAverage = moneySpentSum / (double) dayToMoneySpent.length;

        MaterialDatePicker dateRangePicker = MaterialDatePicker.Builder.
                dateRangePicker().
                setTitleText("Odaberite opseg datuma za izveštaj")
                .build();

        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                if (selection.second < selection.first || selection.second > System.currentTimeMillis()) {
                    fromTextView.setText("Nije odabran validan opseg datuma");
                    toTextView.setText("Nije odabran validan opseg datuma");

                    graphsLayout.setVisibility(View.GONE);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    fromTextView.setText(String.format("Od: %s", sdf.format(new Date(selection.first))));
                    toTextView.setText(String.format("Do: %s", sdf.format(new Date(selection.second))));

                    graphsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        pickDateRangeButton.setOnClickListener(v -> dateRangePicker.show(getSupportFragmentManager(), dateRangePicker.toString()));

        try {
            LineGraphSeries <DataPoint> series = new LineGraphSeries<>(dayToRideNumber);

            series.setTitle("Broj vožnji");
            rideNumberGraph.getLegendRenderer().setVisible(true);
            rideNumberGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            rideNumberGraph.addSeries(series);

            ridesPerDaySumTextView.setText(String.format(Locale.getDefault(), "Ukupno: %d", rideNumberSum));
            ridesPerDayAvgTextView.setText(String.format(Locale.getDefault(), "Prosečno: %.2f", rideNumberAverage));
        } catch (IllegalArgumentException e) {
            Toast.makeText(PassengerReportActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

         try {
            LineGraphSeries <DataPoint> series = new LineGraphSeries<>(dayToDistance);

            series.setTitle("Distanca (km)");
            series.setColor(getResources().getColor(R.color.bright_red));
            distanceGraph.getLegendRenderer().setVisible(true);
            distanceGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            distanceGraph.addSeries(series);

            distancePerDaySumTextView.setText(String.format(Locale.getDefault(), "Ukupno: %d", distanceSum));
            distancePerDayAvgTextView.setText(String.format(Locale.getDefault(), "Prosečno: %.2f", distanceAverage));
        } catch (IllegalArgumentException e) {
            Toast.makeText(PassengerReportActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            LineGraphSeries <DataPoint> series = new LineGraphSeries<>(dayToMoneySpent);

            series.setTitle("Potrošen novac (RSD)");
            series.setColor(getResources().getColor(R.color.light_yellow));
            moneySpentGraph.getLegendRenderer().setVisible(true);
            moneySpentGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            moneySpentGraph.addSeries(series);

            moneySpentPerDaySumTextView.setText(String.format(Locale.getDefault(), "Ukupno: %d", moneySpentSum));
            moneySpentPerDayAvgTextView.setText(String.format(Locale.getDefault(), "Prosečno: %.2f", moneySpentAverage));
        } catch (IllegalArgumentException e) {
            Toast.makeText(PassengerReportActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}