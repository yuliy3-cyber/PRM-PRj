package com.example.movieticketapp.Activity.Helper;

import com.github.mikephil.charting.formatter.ValueFormatter;
import java.text.DecimalFormat;

public class Formater extends ValueFormatter {

    private static final String[] SUFFIX = {"", "k", "m", "b", "t"}; // K, M, B, T
    private static final int MAX_SUFFIX_INDEX = SUFFIX.length - 1;

    private final DecimalFormat format;
    private String appendix = "";

    public Formater() {
        this.format = new DecimalFormat("###,###.#");
    }

    public Formater(String appendix) {
        this();
        this.appendix = appendix;
    }

    public void setAppendix(String appendix) {
        this.appendix = appendix;
    }

    @Override
    public String getFormattedValue(float value) {
        return makePretty(value) + appendix;
    }

    private String makePretty(double number) {
        if (number < 1000) {
            // < 1000 giữ nguyên số
            return format.format(number);
        }

        int exp = (int) (Math.log10(number) / 3); // chia theo nghìn
        if (exp > MAX_SUFFIX_INDEX) exp = MAX_SUFFIX_INDEX;

        double scaled = number / Math.pow(1000, exp);
        String formatted = format.format(scaled);

        // Loại bỏ dấu .0 dư thừa
        if (formatted.endsWith(".0")) {
            formatted = formatted.substring(0, formatted.length() - 2);
        }

        return formatted + SUFFIX[exp];
    }
}
