package com.example.movieticketapp.Activity.Helper;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class NlpUtils {

    /**
     * Loại bỏ dấu tiếng Việt, chuyển các ký tự Unicode có dấu
     * thành ký tự Latin cơ bản. Ví dụ: "Đặng Mạnh" -> "Dang Manh"
     *
     * @param text Chuỗi đầu vào (có thể có dấu)
     * @return Chuỗi đã được loại bỏ dấu
     */
    public static String removeAccent(String text) {
        if (text == null) return null;

        // Chuẩn hóa Unicode (NFD tách dấu ra)
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        // Xóa toàn bộ ký tự dấu
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("");

        // Thay thế riêng cho đ và Đ
        result = result.replaceAll("đ", "d");
        result = result.replaceAll("Đ", "D");

        return result;
    }
}
