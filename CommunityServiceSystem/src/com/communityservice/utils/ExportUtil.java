package com.communityservice.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ExportUtil {
    private ExportUtil() {}

    public static void writeCsv(Path file, List<String[]> rows) throws IOException {
        if (file.toAbsolutePath().getParent() != null) Files.createDirectories(file.toAbsolutePath().getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (String[] row : rows) {
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) writer.write(',');
                    writer.write(csv(row[i]));
                }
                writer.newLine();
            }
        }
    }

    private static String csv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"").replace("\r", " ").replace("\n", " ") + "\"";
    }

    public static void writeTextPdf(Path file, String title, List<String> lines) throws IOException {
        if (file.toAbsolutePath().getParent() != null) Files.createDirectories(file.toAbsolutePath().getParent());

        StringBuilder stream = new StringBuilder();
        stream.append("BT\n/F1 18 Tf\n72 750 Td\n");
        stream.append("(").append(pdfEscape(title)).append(") Tj\n");
        stream.append("/F1 11 Tf\n0 -28 Td\n");
        int lineCount = 0;
        for (String line : lines) {
            if (lineCount++ >= 24) break;
            stream.append("(").append(pdfEscape(line)).append(") Tj\n0 -18 Td\n");
        }
        stream.append("ET\n");

        byte[] content = stream.toString().getBytes(StandardCharsets.ISO_8859_1);
        String[] objectParts = new String[] {
                "<< /Type /Catalog /Pages 2 0 R >>",
                "<< /Type /Pages /Kids [3 0 R] /Count 1 >>",
                "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 5 0 R >> >> /Contents 4 0 R >>",
                "<< /Length " + content.length + " >>\nstream\n" + new String(content, StandardCharsets.ISO_8859_1) + "endstream",
                "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>"
        };

        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        int[] offsets = new int[6];
        for (int i = 0; i < objectParts.length; i++) {
            offsets[i + 1] = pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length;
            pdf.append(i + 1).append(" 0 obj\n").append(objectParts[i]).append("\nendobj\n");
        }
        int xref = pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length;
        pdf.append("xref\n0 6\n0000000000 65535 f \n");
        for (int i = 1; i <= 5; i++) {
            pdf.append(String.format("%010d 00000 n \n", offsets[i]));
        }
        pdf.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n").append(xref).append("\n%%EOF\n");

        Files.write(file, pdf.toString().getBytes(StandardCharsets.ISO_8859_1));
    }

    private static String pdfEscape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")
                .replace("\r", " ").replace("\n", " ");
    }
}
