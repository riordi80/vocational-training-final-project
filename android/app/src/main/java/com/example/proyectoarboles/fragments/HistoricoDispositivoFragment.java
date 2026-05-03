package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.dto.LecturaMuestraProjection;
import com.example.proyectoarboles.model.DispositivoEsp32;
import com.example.proyectoarboles.model.Lectura;
import com.example.proyectoarboles.model.PageResponse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoDispositivoFragment extends Fragment {

    private static final String TAG = "HistoricoDispositivo";
    private static final int PAGE_SIZE = 20;
    private static final String ARG_DISPOSITIVO_ID = "dispositivo_id";
    private static final String ARG_DISPOSITIVO_NOMBRE = "dispositivo_nombre";

    private Long dispositivoId;
    private String periodoActivo = "SEMANA";
    private int paginaActual = 0;
    private int totalPaginas = 0;

    private TextView tvInfoDispositivo, tvInfoGrafica1, tvInfoGraficaCO2;
    private TextView tvTituloTabla, tvPaginaInfo, tvNoLecturas;
    private RadioGroup radioGroupPeriodo;
    private LineChart lineChartSensores, lineChartCO2;
    private Button btnVolver, btnPaginaAnterior, btnPaginaSiguiente;
    private LinearLayout layoutTablaLecturas;

    public static HistoricoDispositivoFragment newInstance(long dispositivoId, String dispositivoNombre) {
        HistoricoDispositivoFragment fragment = new HistoricoDispositivoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DISPOSITIVO_ID, dispositivoId);
        args.putString(ARG_DISPOSITIVO_NOMBRE, dispositivoNombre != null ? dispositivoNombre : "");
        fragment.setArguments(args);
        return fragment;
    }

    private int getDias(String periodo) {
        switch (periodo) {
            case "DIA": return 1;
            case "SEMANA": return 7;
            case "MES": return 30;
            case "SEMESTRE": return 180;
            case "ANIO": return 365;
            default: return 7;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historico_dispositivo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarVistas(view);
        configurarCharts();
        configurarListeners();

        dispositivoId = getArguments() != null ? getArguments().getLong(ARG_DISPOSITIVO_ID, -1) : -1;
        if (dispositivoId != -1) {
            cargarInfoDispositivo();
            cargarGrafica();
            cargarTabla();
        } else {
            Toast.makeText(requireContext(), "Error: ID de dispositivo no válido", Toast.LENGTH_LONG).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void inicializarVistas(View view) {
        tvInfoDispositivo = view.findViewById(R.id.textViewInfoDispositivo);
        tvInfoGrafica1 = view.findViewById(R.id.textViewInfoGrafica1);
        tvInfoGraficaCO2 = view.findViewById(R.id.textViewInfoGraficaCO2);
        tvTituloTabla = view.findViewById(R.id.textViewTituloTabla);
        tvPaginaInfo = view.findViewById(R.id.textViewPaginaInfo);
        tvNoLecturas = view.findViewById(R.id.textViewNoLecturas);
        radioGroupPeriodo = view.findViewById(R.id.radioGroupPeriodoHistorico);
        lineChartSensores = view.findViewById(R.id.lineChartSensores);
        lineChartCO2 = view.findViewById(R.id.lineChartCO2);
        btnVolver = view.findViewById(R.id.buttonVolverHistorico);
        btnPaginaAnterior = view.findViewById(R.id.buttonPaginaAnterior);
        btnPaginaSiguiente = view.findViewById(R.id.buttonPaginaSiguiente);
        layoutTablaLecturas = view.findViewById(R.id.layoutTablaLecturas);
    }

    private void configurarCharts() {
        for (LineChart chart : new LineChart[]{lineChartSensores, lineChartCO2}) {
            chart.getDescription().setEnabled(false);
            chart.setTouchEnabled(true);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            chart.setPinchZoom(true);
            chart.getLegend().setEnabled(true);
            chart.getLegend().setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            xAxis.setLabelCount(5, true);
        }
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        btnPaginaAnterior.setOnClickListener(v -> {
            if (paginaActual > 0) {
                paginaActual--;
                cargarTabla();
            }
        });

        btnPaginaSiguiente.setOnClickListener(v -> {
            if (paginaActual < totalPaginas - 1) {
                paginaActual++;
                cargarTabla();
            }
        });

        radioGroupPeriodo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioDia) periodoActivo = "DIA";
            else if (checkedId == R.id.radioSemana) periodoActivo = "SEMANA";
            else if (checkedId == R.id.radioMes) periodoActivo = "MES";
            else if (checkedId == R.id.radioSemestre) periodoActivo = "SEMESTRE";
            else if (checkedId == R.id.radioAnio) periodoActivo = "ANIO";
            paginaActual = 0;
            cargarGrafica();
            cargarTabla();
        });
    }

    private void cargarInfoDispositivo() {
        Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi().obtenerDispositivoPorId(dispositivoId);
        call.enqueue(new Callback<DispositivoEsp32>() {
            @Override
            public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    DispositivoEsp32 d = response.body();
                    String info = "Dispositivo: " + d.getMacAddress();
                    if (d.getCentroEducativo() != null && d.getCentroEducativo().getNombre() != null) {
                        info += " · " + d.getCentroEducativo().getNombre();
                    }
                    tvInfoDispositivo.setText(info);
                }
            }

            @Override
            public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                Log.e(TAG, "Error cargando info dispositivo: " + t.getMessage());
            }
        });
    }

    private void cargarGrafica() {
        lineChartSensores.setNoDataText("Cargando...");
        lineChartCO2.setNoDataText("Cargando...");
        lineChartSensores.invalidate();
        lineChartCO2.invalidate();

        Call<List<LecturaMuestraProjection>> call = RetrofitClient.getLecturaApi()
                .obtenerLecturasParaGrafica(dispositivoId, periodoActivo);
        call.enqueue(new Callback<List<LecturaMuestraProjection>>() {
            @Override
            public void onResponse(Call<List<LecturaMuestraProjection>> call, Response<List<LecturaMuestraProjection>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    List<LecturaMuestraProjection> lecturas = response.body();
                    tvInfoGrafica1.setText(lecturas.size() + " lecturas");
                    tvInfoGraficaCO2.setText(lecturas.stream().filter(l -> l.getCo2() != null).count() + " lecturas con datos");

                    if (lecturas.isEmpty()) {
                        lineChartSensores.setNoDataText("Sin datos en este periodo");
                        lineChartCO2.setNoDataText("Sin datos de CO2");
                        lineChartSensores.invalidate();
                        lineChartCO2.invalidate();
                    } else {
                        dibujarGraficaSensores(lecturas);
                        dibujarGraficaCO2(lecturas);
                    }
                } else {
                    Log.e(TAG, "Error cargando gráfica: " + response.code());
                    lineChartSensores.setNoDataText("Error al cargar datos");
                    lineChartCO2.setNoDataText("Error al cargar datos");
                    lineChartSensores.invalidate();
                    lineChartCO2.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<LecturaMuestraProjection>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión gráfica: " + t.getMessage());
                lineChartSensores.setNoDataText("Error de conexión");
                lineChartCO2.setNoDataText("Error de conexión");
                lineChartSensores.invalidate();
                lineChartCO2.invalidate();
            }
        });
    }

    private void dibujarGraficaSensores(List<LecturaMuestraProjection> lecturas) {
        ArrayList<Entry> tempEntries = new ArrayList<>();
        ArrayList<Entry> humAmbEntries = new ArrayList<>();
        ArrayList<Entry> humSueloEntries = new ArrayList<>();
        ArrayList<Entry> luz1Entries = new ArrayList<>();
        ArrayList<Entry> luz2Entries = new ArrayList<>();

        for (int i = 0; i < lecturas.size(); i++) {
            LecturaMuestraProjection l = lecturas.get(i);
            if (l.getTemperatura() != null) tempEntries.add(new Entry(i, l.getTemperatura().floatValue()));
            if (l.getHumedadAmbiente() != null) humAmbEntries.add(new Entry(i, l.getHumedadAmbiente().floatValue()));
            if (l.getHumedadSuelo() != null) humSueloEntries.add(new Entry(i, l.getHumedadSuelo().floatValue()));
            if (l.getLuz1() != null) luz1Entries.add(new Entry(i, l.getLuz1().floatValue()));
            if (l.getLuz2() != null) luz2Entries.add(new Entry(i, l.getLuz2().floatValue()));
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(crearDataSet(tempEntries, "Temperatura (°C)", 0xFFF97316));
        dataSets.add(crearDataSet(humAmbEntries, "Hum. Ambiente (%)", 0xFF3b82f6));
        dataSets.add(crearDataSet(humSueloEntries, "Hum. Suelo (%)", 0xFF22c55e));
        if (!luz1Entries.isEmpty()) dataSets.add(crearDataSet(luz1Entries, "Luz 1 (%)", 0xFFEAB308));
        if (!luz2Entries.isEmpty()) dataSets.add(crearDataSet(luz2Entries, "Luz 2 (%)", 0xFFA855F7));

        lineChartSensores.setData(new LineData(dataSets));
        lineChartSensores.invalidate();
    }

    private void dibujarGraficaCO2(List<LecturaMuestraProjection> lecturas) {
        ArrayList<Entry> co2Entries = new ArrayList<>();
        for (int i = 0; i < lecturas.size(); i++) {
            LecturaMuestraProjection l = lecturas.get(i);
            if (l.getCo2() != null) co2Entries.add(new Entry(i, l.getCo2().floatValue()));
        }

        if (co2Entries.isEmpty()) {
            lineChartCO2.setNoDataText("Sin datos de CO2 en este periodo");
            lineChartCO2.invalidate();
            return;
        }

        LineData lineData = new LineData(crearDataSet(co2Entries, "CO2 (ppm)", 0xFFEF4444));
        lineChartCO2.setData(lineData);
        lineChartCO2.getAxisLeft().setValueFormatter(
                new com.github.mikephil.charting.formatter.DefaultValueFormatter(0)
        );
        lineChartCO2.invalidate();
    }

    private LineDataSet crearDataSet(List<Entry> entries, String label, int color) {
        LineDataSet ds = new LineDataSet(entries, label);
        ds.setColor(color);
        ds.setCircleColor(color);
        ds.setLineWidth(2f);
        ds.setCircleRadius(2f);
        ds.setDrawValues(false);
        ds.setDrawCircles(entries.size() <= 50);
        return ds;
    }

    private void cargarTabla() {
        btnPaginaAnterior.setEnabled(false);
        btnPaginaSiguiente.setEnabled(false);

        int dias = getDias(periodoActivo);
        String hasta = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -dias);
        String desde = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(cal.getTime());

        Call<PageResponse<Lectura>> call = RetrofitClient.getLecturaApi()
                .obtenerLecturasEnRango(dispositivoId, desde, hasta, paginaActual, PAGE_SIZE);
        call.enqueue(new Callback<PageResponse<Lectura>>() {
            @Override
            public void onResponse(Call<PageResponse<Lectura>> call, Response<PageResponse<Lectura>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    PageResponse<Lectura> page = response.body();
                    totalPaginas = page.getTotalPages();
                    int total = page.getTotalElements();
                    List<Lectura> lecturas = page.getContent();

                    tvTituloTabla.setText("Lecturas individuales (" + total + " en este periodo)");
                    tvPaginaInfo.setText("Pág. " + (paginaActual + 1) + "/" + Math.max(1, totalPaginas));

                    btnPaginaAnterior.setEnabled(paginaActual > 0);
                    btnPaginaSiguiente.setEnabled(paginaActual < totalPaginas - 1);

                    layoutTablaLecturas.removeAllViews();
                    if (lecturas == null || lecturas.isEmpty()) {
                        tvNoLecturas.setVisibility(View.VISIBLE);
                    } else {
                        tvNoLecturas.setVisibility(View.GONE);
                        construirTabla(lecturas);
                    }
                } else {
                    Log.e(TAG, "Error cargando tabla: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PageResponse<Lectura>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión tabla: " + t.getMessage());
            }
        });
    }

    private void construirTabla(List<Lectura> lecturas) {
        LinearLayout cabecera = crearFilaTabla(
                "Fecha/Hora", "Temp.", "Hum.Amb.", "Hum.Suelo", "CO2", "Luz1", "Luz2", true);
        layoutTablaLecturas.addView(cabecera);

        for (Lectura l : lecturas) {
            String ts = formatearTimestamp(l.getTimestamp());
            String temp = l.getTemperatura() != null ? String.format(Locale.getDefault(), "%.1f", l.getTemperatura()) : "-";
            String humAmb = l.getHumedadAmbiente() != null ? String.format(Locale.getDefault(), "%.1f", l.getHumedadAmbiente()) : "-";
            String humSuelo = l.getHumedadSuelo() != null ? String.format(Locale.getDefault(), "%.1f", l.getHumedadSuelo()) : "-";
            String co2 = l.getCo2() != null ? String.format(Locale.getDefault(), "%.0f", l.getCo2()) : "-";
            String luz1 = l.getLuz1() != null ? String.format(Locale.getDefault(), "%.1f", l.getLuz1()) : "-";
            String luz2 = l.getLuz2() != null ? String.format(Locale.getDefault(), "%.1f", l.getLuz2()) : "-";

            layoutTablaLecturas.addView(crearFilaTabla(ts, temp, humAmb, humSuelo, co2, luz1, luz2, false));
        }
    }

    private LinearLayout crearFilaTabla(String ts, String temp, String humAmb, String humSuelo,
                                         String co2, String luz1, String luz2, boolean esHeader) {
        LinearLayout fila = new LinearLayout(requireContext());
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setBackgroundColor(esHeader ? 0xFF16a34a : 0xFFFFFFFF);

        int textColor = esHeader ? 0xFFFFFFFF : 0xFF374151;
        int textStyle = esHeader ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL;

        String[] valores = {ts, temp, humAmb, humSuelo, co2, luz1, luz2};
        float[] pesos = {2f, 1f, 1f, 1f, 1f, 1f, 1f};

        for (int i = 0; i < valores.length; i++) {
            TextView tv = new TextView(requireContext());
            tv.setText(valores[i]);
            tv.setTextSize(11);
            tv.setTextColor(textColor);
            tv.setTypeface(null, textStyle);
            tv.setPadding(6, 8, 6, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, pesos[i]);
            tv.setLayoutParams(params);
            fila.addView(tv);
        }

        return fila;
    }

    private String formatearTimestamp(String ts) {
        if (ts == null || ts.isEmpty()) return "-";
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = entrada.parse(ts.substring(0, 19));
            SimpleDateFormat salida = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
            return salida.format(date);
        } catch (Exception e) {
            return ts.length() > 16 ? ts.substring(5, 16) : ts;
        }
    }
}
