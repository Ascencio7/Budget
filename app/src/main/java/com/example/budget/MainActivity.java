package com.example.budget;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etCategory, etAmount;
    Button btnAmn, btnShow, btnSalir;

    Spinner spinner;
    FrameLayout chartContainer;
    HashMap<String, Float> gastos = new HashMap<>();

    String[] tipos = {"Pastel","Barras", "Lineal", "Radar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCategory = findViewById(R.id.etCategory);
        etAmount = findViewById(R.id.etAmount);
        btnAmn = findViewById(R.id.btnAmn);
        btnShow = findViewById(R.id.btnShow);
        btnSalir = findViewById(R.id.btnSalir);
        spinner = findViewById(R.id.spinner);
        chartContainer = findViewById(R.id.chartContainer);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        spinner.setAdapter(adapter);

        btnAmn.setOnClickListener(v -> agregarGasto());
        btnShow.setOnClickListener(v -> mostrarGrafico());
        
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmar salida");
                builder.setMessage("¿Desea salir?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    private void mostrarGrafico() {

        if(gastos.isEmpty()){
            Toast.makeText(this, "No hay datos que mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        chartContainer.removeAllViews();

        switch (spinner.getSelectedItemPosition()){
            case 0: mostrarPastel(); break;
            case 1: mostrarBarras(); break;
            case 2: mostrarLineal(); break;
            case 3: mostrarRadar(); break;
        }

        /*chartContainer.removeAllViews();

        if(spinner.getSelectedItemPosition() == 0){
            mostrarPastel();
        }else{
            mostrarBarras();
        }*/

    }

    private void mostrarBarras() {
        BarChart chart = new BarChart(this);
        chartContainer.addView(chart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for(Map.Entry<String, Float> e: gastos.entrySet()){
            entries.add(new BarEntry(i++, e.getValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Gastos en $");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        chart.setData(data);
        chart.setFitBars(true);
        chart.getDescription().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void mostrarPastel() {
        PieChart chart = new PieChart(this);
        chartContainer.addView(chart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for(Map.Entry<String, Float>  e: gastos.entrySet()){
            entries.add(new PieEntry(e.getValue(), e.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Gastos en $");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void agregarGasto() {
        String cat = etCategory.getText().toString();
        String montoStr = etAmount.getText().toString();

        if(cat.isEmpty() || montoStr.isEmpty()){
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        float monto = Float.parseFloat(montoStr);
        gastos.put(cat, gastos.getOrDefault(cat, 0f ) + monto);

        etCategory.setText("");
        etAmount.setText("");
        Toast.makeText(this, "Gasto agregado", Toast.LENGTH_SHORT).show();
    }

    private void mostrarLineal(){
        LineChart chart = new LineChart(this);
        chartContainer.addView(chart);

        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;

        for(Map.Entry<String, Float> e: gastos.entrySet()){
            entries.add(new Entry(i++, e.getValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Gastos $");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        LineData data = new LineData(dataSet);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.animateX(1000);
        chart.invalidate();
    }

    private void mostrarRadar(){
        RadarChart chart = new RadarChart(this);
        chartContainer.addView(chart);

        ArrayList<RadarEntry> entries = new ArrayList<>();
        for(Map.Entry<String, Float> e: gastos.entrySet()){
            entries.add(new RadarEntry(e.getValue()));
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "Gastos $");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        RadarData data = new RadarData(dataSet);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
    }

}
