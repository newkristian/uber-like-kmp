package com.example.uberapp_tim9.passenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.core.util.Pair;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.dtos.PassengerReportDTO;
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerReportActivity extends AppCompatActivity {
    LinearLayout graphsLayout;
    Button pickDateRangeButton;

    TextView fromTextView;
    TextView toTextView;

    TextView ridesPerDaySumTextView;
    TextView ridesPerDayAvgTextView;

    TextView distancePerDaySumTextView;
    TextView distancePerDayAvgTextView;

    TextView moneySpentPerDaySumTextView;
    TextView moneySpentPerDayAvgTextView;

    GraphView rideNumberGraph;
    GraphView distanceGraph;
    GraphView moneySpentGraph;

    MaterialDatePicker dateRangePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_report);

        initializeToolbar();
        initializeElements();
        initializeGraphs();

        DataPoint[] dayToRideNumber = new DataPoint[] {
                new DataPoint(1, 3),
                new DataPoint(2, 1),
                new DataPoint(3, 2),
                new DataPoint(4, 0),
                new DataPoint(5, 5)
        };

        DataPoint[] dayToDistance = new DataPoint[] {
                new DataPoint(1, 10),
                new DataPoint(2, 5),
                new DataPoint(3, 7),
                new DataPoint(4, 0),
                new DataPoint(5, 15)
        };

        DataPoint[] dayToMoneySpent = new DataPoint[] {
                new DataPoint(1, 1000),
                new DataPoint(2, 500),
                new DataPoint(3, 700),
                new DataPoint(4, 0),
                new DataPoint(5, 1500)
        };

        setupDateRangePicker();
        loadGraphs(dayToRideNumber, dayToDistance, dayToMoneySpent);
    }

    private void getReport(LocalDateTime from, LocalDateTime to) {
        List<PassengerReportDTO>[] report = new List[1];
        Call<ResponseBody> getReportForPassenger =
                RestApiManager.
                restApiInterfacePassenger.
                getReportForPassenger(LoggedUserInfo.id, from, to);
        getReportForPassenger.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<PassengerReportDTO>>(){}.getType();
                        report[0] = gson.fromJson(json, listType);

                        displayReport(report[0]);
                    } catch (Exception ex) {
                        Log.e("ERROR", ex.getMessage());
                    }
                } else {
                    Log.d("PassengerReportActivity", "Error: " + response.code());
                    Log.d("PassengerReportActivity", "Error: " + response.message());
                    try {
                        Log.d("PassengerReportActivity", "Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("PassengerReportActivity", "Error: " + response.headers());
                    Toast.makeText(PassengerReportActivity.this, "Došlo je do greške pri prikazu izveštaja", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void displayReport(List<PassengerReportDTO> report) {
        DataPoint[] dayToRideNumber = new DataPoint[report.size()];
        DataPoint[] dayToDistance = new DataPoint[report.size()];
        DataPoint[] dayToMoneySpent = new DataPoint[report.size()];

        for (int i = 0; i < report.size(); i++) {
            dayToRideNumber[i] = new DataPoint(i + 1, report.get(i).getNumberOfRides());
            dayToDistance[i] = new DataPoint(i + 1, report.get(i).getTotalDistance());
            dayToMoneySpent[i] = new DataPoint(i + 1, report.get(i).getTotalCost());
        }

        loadGraphs(dayToRideNumber, dayToDistance, dayToMoneySpent);
        graphsLayout.setVisibility(View.VISIBLE);
    }

    private void initializeGraphs() {
        rideNumberGraph = findViewById(R.id.rideNumberGraph);
        rideNumberGraph.setTitle("Broj vožnji po danu");
        rideNumberGraph.setTitleColor(getResources().getColor(R.color.black));
        rideNumberGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.black));
        rideNumberGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.black));
        rideNumberGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.dark_grey));

        distanceGraph = findViewById(R.id.distanceGraph);
        distanceGraph.setTitle("Distanca pređena po danu");
        distanceGraph.setTitleColor(getResources().getColor(R.color.black));
        distanceGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.black));
        distanceGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.black));
        distanceGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.dark_grey));

        moneySpentGraph = findViewById(R.id.moneySpentGraph);
        moneySpentGraph.setTitle("Novac potrošen po danu");
        moneySpentGraph.setTitleColor(getResources().getColor(R.color.black));
        moneySpentGraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.black));
        moneySpentGraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.black));
        moneySpentGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.dark_grey));
    }

    private void initializeToolbar() {
        setSupportActionBar(findViewById(R.id.toolbarPassengerReport));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);
    }

    private void initializeElements() {
        graphsLayout = findViewById(R.id.graphsLayout);

        pickDateRangeButton = findViewById(R.id.pickDateRangeButton);

        fromTextView = findViewById(R.id.fromTextView);
        toTextView = findViewById(R.id.toTextView);

        ridesPerDaySumTextView = findViewById(R.id.ridesPerDaySumTextView);
        ridesPerDayAvgTextView = findViewById(R.id.ridesPerDayAvgTextView);

        distancePerDaySumTextView = findViewById(R.id.distanceSumTextView);
        distancePerDayAvgTextView = findViewById(R.id.distanceAvgTextView);

        moneySpentPerDaySumTextView = findViewById(R.id.moneySpentSumTextView);
        moneySpentPerDayAvgTextView = findViewById(R.id.moneySpentAvgTextView);
    }

    private void setupDateRangePicker() {
        dateRangePicker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTitleText("Odaberite opseg datuma za izveštaj")
                .setPositiveButtonText("Potvrdi")
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

                    Date from = new Date(selection.first);
                    Date to = new Date(selection.second);

                    fromTextView.setText(String.format("Od: %s", sdf.format(from)));
                    toTextView.setText(String.format("Do: %s", sdf.format(to)));

                    LocalDateTime fromLocalDateTime = LocalDateTime.ofInstant(from.toInstant(),
                            ZoneId.systemDefault());
                    LocalDateTime toLocalDateTime = LocalDateTime.ofInstant(to.toInstant(),
                            ZoneId.systemDefault());

                    getReport(fromLocalDateTime, toLocalDateTime);
                }
                dateRangePicker.dismiss();
            }
        });
        pickDateRangeButton.setOnClickListener(v -> dateRangePicker.show(getSupportFragmentManager(), dateRangePicker.toString()));
    }

    private void loadGraphs(DataPoint[] dayToRideNumber, DataPoint[] dayToDistance, DataPoint[] dayToMoneySpent) {
        loadDayToRideGraph(dayToRideNumber);
        loadDayToDistanceGraph(dayToDistance);
        loadDayToMoneySpentGraph(dayToMoneySpent);
    }

    private void loadDayToMoneySpentGraph(DataPoint[] dayToMoneySpent) {
        moneySpentGraph.removeAllSeries();

        int moneySpentSum = Arrays.stream(dayToMoneySpent).mapToInt(dp -> (int) dp.getY()).sum();
        double moneySpentAverage = moneySpentSum / (double) dayToMoneySpent.length;
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

    private void loadDayToDistanceGraph(DataPoint[] dayToDistance) {
        distanceGraph.removeAllSeries();

        int distanceSum = Arrays.stream(dayToDistance).mapToInt(dp -> (int) dp.getY()).sum();
        double distanceAverage = distanceSum / (double) dayToDistance.length;
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
    }

    private void loadDayToRideGraph(DataPoint[] dayToRideNumber) {
        rideNumberGraph.removeAllSeries();

        int rideNumberSum = Arrays.stream(dayToRideNumber).mapToInt(dp -> (int) dp.getY()).sum();
        double rideNumberAverage = rideNumberSum / (double) dayToRideNumber.length;
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}