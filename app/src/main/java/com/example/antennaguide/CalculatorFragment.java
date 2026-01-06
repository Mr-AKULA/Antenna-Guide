package com.example.antennaguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Locale;

public class CalculatorFragment extends Fragment {

    private Spinner antennaTypeSpinner;
    private EditText frequencyInput;
    private Spinner unitSpinner;
    private Button calculateButton;
    private TextView resultTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        // Инициализация элементов
        antennaTypeSpinner = view.findViewById(R.id.antenna_type_spinner);
        frequencyInput = view.findViewById(R.id.frequency_input);
        unitSpinner = view.findViewById(R.id.unit_spinner);
        calculateButton = view.findViewById(R.id.calculate_button);
        resultTextView = view.findViewById(R.id.result_text);

        // Настройка Spinner для типа антенны
        String[] antennaTypes = {
                "Полуволновой диполь",
                "Антенна Яги-Уда",
                "Патч-антенна",
                "Параболическая антенна"
        };
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                antennaTypes
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        antennaTypeSpinner.setAdapter(typeAdapter);

        // Настройка Spinner для единиц измерения
        String[] units = {"МГц", "ГГц"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                units
        );
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        // Обработчик кнопки расчета
        calculateButton.setOnClickListener(v -> calculate());

        return view;
    }

    private void calculate() {
        String freqStr = frequencyInput.getText().toString().trim();

        if (freqStr.isEmpty()) {
            Toast.makeText(getContext(), "Введите частоту", Toast.LENGTH_SHORT).show();
            return;
        }

        double frequency;
        try {
            frequency = Double.parseDouble(freqStr);
            if (frequency <= 0) {
                Toast.makeText(getContext(), "Частота должна быть положительной", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Некорректное значение частоты", Toast.LENGTH_SHORT).show();
            return;
        }

        // Конвертация в МГц если нужно
        String unit = unitSpinner.getSelectedItem().toString();
        double freqMHz = unit.equals("ГГц") ? frequency * 1000 : frequency;

        int selectedType = antennaTypeSpinner.getSelectedItemPosition();
        String result = "";

        switch (selectedType) {
            case 0: // Диполь
                result = calculateDipole(freqMHz);
                break;
            case 1: // Яги-Уда
                result = calculateYagi(freqMHz);
                break;
            case 2: // Патч
                result = calculatePatch(freqMHz, frequency, unit);
                break;
            case 3: // Параболическая
                result = calculateParabolic(freqMHz);
                break;
        }

        resultTextView.setText(result);
        resultTextView.setVisibility(View.VISIBLE);
    }

    private String calculateDipole(double freqMHz) {
        double armLength = 75.0 / freqMHz;  // λ/4 для одного плеча
        double totalLength = armLength * 2; // λ/2 общая длина

        return String.format(Locale.getDefault(),
                "📡 РАСЧЕТ ПОЛУВОЛНОВОГО ДИПОЛЯ\n\n" +
                        "Формула: L_плеча = 75 / f(МГц)\n" +
                        "        L_общ = 150 / f(МГц)\n\n" +
                        "Частота: %.1f МГц\n\n" +
                        "Длина одного плеча: %.3f м\n" +
                        "Общая длина диполя: %.3f м",
                freqMHz, armLength, totalLength
        );
    }

    private String calculateYagi(double freqMHz) {
        double activeLength = 144.0 / freqMHz;     // Активный элемент (~λ/2)
        double reflectorLength = 152.0 / freqMHz;  // Рефлектор (+5-6%)
        double directorLength = 137.0 / freqMHz;   // Директор (-4-5%)

        return String.format(Locale.getDefault(),
                "📶 РАСЧЕТ АНТЕННЫ ЯГИ-УДА (3 элемента)\n\n" +
                        "Формулы:\n" +
                        "Активный элемент ≈ 144 / f\n" +
                        "Рефлектор ≈ 152 / f (+5.5%)\n" +
                        "Директор ≈ 137 / f (-4.9%)\n\n" +
                        "Частота: %.1f МГц\n\n" +
                        "Активный элемент: %.3f м\n" +
                        "Рефлектор: %.3f м\n" +
                        "Директор: %.3f м",
                freqMHz, activeLength, reflectorLength, directorLength
        );
    }

    private String calculatePatch(double freqMHz, double originalFreq, String unit) {
        double epsilon = 4.4; // Диэлектрическая проницаемость FR4
        double substrateHeight = 1.6; // Толщина подложки в мм

        // Упрощённый расчёт с учётом ε
        double patchLengthMm = 30000.0 / (freqMHz * 2 * Math.sqrt((epsilon + 1) / 2));

        return String.format(Locale.getDefault(),
                "▭ РАСЧЕТ ПАТЧ-АНТЕННЫ (упрощённо)\n\n" +
                        "Формула: L ≈ c / (2f√εeff)\n" +
                        "εeff ≈ (εr+1)/2 для тонкой подложки\n\n" +
                        "Частота: %.1f МГц (%.1f %s)\n" +
                        "εr подложки: %.1f (FR4)\n\n" +
                        "Длина патча: ≈ %.1f мм\n\n" +
                        "Примечание: точный расчёт требует\n" +
                        "учёта fringing fields и геометрии\n" +
                        "подложки",
                freqMHz, originalFreq, unit, epsilon, patchLengthMm
        );
    }

    private String calculateParabolic(double freqMHz) {
        double diameter = 0.6; // Более реалистичный диаметр для расчётов
        double eta = 0.55; // Типичный КПД для небольших антенн
        double wavelength = 300.0 / freqMHz;

        // Усиление в разах
        double gainLinear = eta * Math.pow((Math.PI * diameter / wavelength), 2);
        // Усиление в дБ
        double gainDB = 10 * Math.log10(gainLinear);

        // Ширина диаграммы направленности (приблизительно)
        double beamwidth = 70.0 * wavelength / diameter; // в градусах

        return String.format(Locale.getDefault(),
                "🛰 РАСЧЕТ ПАРАБОЛИЧЕСКОЙ АНТЕННЫ\n\n" +
                        "Формула: G = η·(πD/λ)²\n" +
                        "где:\n" +
                        "η - КПД антенны (≈0.55)\n" +
                        "D - диаметр зеркала\n" +
                        "λ - длина волны\n\n" +
                        "Частота: %.1f МГц (λ=%.3f м)\n" +
                        "Диаметр зеркала: %.1f м\n\n" +
                        "Усиление: %.1f дБ (≈%.0f раз)\n" +
                        "Ширина луча: ≈%.1f°\n" +
                        "КПД: %.0f%%",
                freqMHz, wavelength, diameter, gainDB, gainLinear, beamwidth, eta * 100
        );
    }
}
