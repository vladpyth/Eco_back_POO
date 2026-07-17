package com.example.eco_service.config;

import com.example.eco_service.dto.main_dto.WasteTypeReportDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class PdfReportGenerator {

    private static final float MARGIN = 45f;
    private static final float ROW_HEIGHT = 12f;
    private static final float HEADER_GAP = 14f;
    private static final float SECTION_GAP = 28f;
    private static final float WASTE_BLOCK_GAP = 18f;
    private static final PDRectangle LANDSCAPE_A4 = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());

    private static final int COL_NAME_OBJ = 0;
    private static final int COL_ADDRESS_OBJ = 1;
    private static final int COL_PHONE_OBJ = 2;
    private static final int COL_NAME_OWN = 3;
    private static final int COL_ADDRESS_OWN = 4;
    private static final int COL_PHONE_OWN = 5;
    private static final int COL_USE_TRASH = 6;
    private static final int COL_ACCEPT_TRASH = 7;
    private static final int COLUMN_COUNT = 8;

    private static final PDType1Font HELVETICA = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDType1Font HELVETICA_BOLD = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private static final String FONT_CLASSPATH = "fonts/DejaVuSans.ttf";

    /** Fallback на Helvetica только для текущей генерации (без кириллицы). */
    private boolean useStandardFonts = false;

    public byte[] generateReport(List<WasteTypeReportDto> data) throws IOException {
        useStandardFonts = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            // Пытаемся загрузить шрифт, если не удалось — используем стандартные
            PDType0Font font = null;
            try {
                font = loadFont(document);
                log.info("Custom font loaded successfully");
            } catch (IOException e) {
                log.warn("Failed to load custom font, falling back to standard PDF fonts: {}", e.getMessage());
                useStandardFonts = true;
            }

            PDPage page = new PDPage(LANDSCAPE_A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float yPosition = pageTop(page);
            float[] colStarts = columnStarts(page);
            float colWidth = columnWidth(page);

            yPosition = drawHeader(contentStream, page, font, yPosition);

            int drawnWasteTypes = 0;
            for (WasteTypeReportDto wasteType : data) {
                // Нет предприятий по этому отходу — блок не показываем
                if (wasteType.getFactories() == null || wasteType.getFactories().isEmpty()) {
                    continue;
                }

                if (drawnWasteTypes > 0) {
                    yPosition -= WASTE_BLOCK_GAP;
                }
                if (yPosition < 150) {
                    contentStream.close();
                    page = new PDPage(LANDSCAPE_A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = pageTop(page);
                    colStarts = columnStarts(page);
                    colWidth = columnWidth(page);
                }

                yPosition = drawWasteTypeHeader(contentStream, font, wasteType, yPosition);
                yPosition = drawTableHeaders(contentStream, font, colStarts, colWidth, yPosition);
                float tableTopY = yPosition + ROW_HEIGHT;
                List<Float> horizontalSeparators = new ArrayList<>();
                horizontalSeparators.add(yPosition);

                for (WasteTypeReportDto.FactoryForWasteReportDto factory : wasteType.getFactories()) {
                    if (yPosition < 100) {
                        drawInnerGrid(contentStream, colStarts, colWidth, tableTopY, yPosition, horizontalSeparators);
                        contentStream.close();
                        page = new PDPage(LANDSCAPE_A4);
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        yPosition = pageTop(page);
                        colStarts = columnStarts(page);
                        colWidth = columnWidth(page);
                        yPosition = drawWasteTypeHeader(contentStream, font, wasteType, yPosition);
                        yPosition = drawTableHeaders(contentStream, font, colStarts, colWidth, yPosition);
                        tableTopY = yPosition + ROW_HEIGHT;
                        horizontalSeparators = new ArrayList<>();
                        horizontalSeparators.add(yPosition);
                    }

                    int lineCount = drawFactoryRow(contentStream, font, colStarts, colWidth, yPosition, factory);
                    yPosition -= ROW_HEIGHT * lineCount;
                    horizontalSeparators.add(yPosition);
                }

                drawInnerGrid(contentStream, colStarts, colWidth, tableTopY, yPosition, horizontalSeparators);
                yPosition -= SECTION_GAP;
                drawnWasteTypes++;
            }

            contentStream.close();
            addPageNumbers(document, font);
            document.save(baos);

            log.info("PDF generated successfully with {} waste types (of {} in request)",
                    drawnWasteTypes, data.size());
        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            throw new IOException("Failed to generate PDF report: " + e.getMessage(), e);
        }

        return baos.toByteArray();
    }

    // ==================== ЗАГРУЗКА ШРИФТА ====================

    private PDType0Font loadFont(PDDocument document) throws IOException {
        try (InputStream is = openFontStream()) {
            log.info("Loading font from classpath: {}", FONT_CLASSPATH);
            return PDType0Font.load(document, is);
        }
    }

    private InputStream openFontStream() throws IOException {
        ClassPathResource resource = new ClassPathResource(FONT_CLASSPATH);
        if (resource.exists()) {
            return resource.getInputStream();
        }

        InputStream is = getClass().getClassLoader().getResourceAsStream(FONT_CLASSPATH);
        if (is != null) {
            return is;
        }

        throw new IOException(
                "Font not found: " + FONT_CLASSPATH + ". Place DejaVuSans.ttf in src/main/resources/fonts/");
    }

    // ==================== МЕТОДЫ РИСОВАНИЯ (адаптированы под стандартные шрифты при необходимости) ====================

    private float drawHeader(PDPageContentStream contentStream, PDPage page,
                             PDType0Font font, float yPosition) throws IOException {
        String title = "Реестр объектов хранения, захоронения и обезвреживания отходов";
        String subtitle = "(обезвреживание)";

        if (useStandardFonts || font == null) {
            // Используем стандартные шрифты
            float pageWidth = page.getMediaBox().getWidth();
            float titleWidth = HELVETICA_BOLD.getStringWidth(title) / 1000f * 13f;
            contentStream.beginText();
            contentStream.setFont(HELVETICA_BOLD, 13);
            contentStream.newLineAtOffset((pageWidth - titleWidth) / 2f, yPosition);
            contentStream.showText(title);
            contentStream.endText();
            yPosition -= 16;

            float subWidth = HELVETICA_BOLD.getStringWidth(subtitle) / 1000f * 12f;
            contentStream.beginText();
            contentStream.setFont(HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset((pageWidth - subWidth) / 2f, yPosition);
            contentStream.showText(subtitle);
            contentStream.endText();
            yPosition -= 40;
        } else {
            writeCenteredText(contentStream, page, font, 13, yPosition, title);
            yPosition -= 16;
            writeCenteredText(contentStream, page, font, 12, yPosition, subtitle);
            yPosition -= 40;
        }
        return yPosition;
    }

    private float drawWasteTypeHeader(PDPageContentStream contentStream, PDType0Font font,
                                      WasteTypeReportDto wasteType, float yPosition) throws IOException {
        String codeText = "Код отхода: " + wasteType.getCodeTrash();
        String nameText = "Наименование: " + wasteType.getNameTrash();

        if (useStandardFonts || font == null) {
            writeTextStandard(contentStream, 11, MARGIN, yPosition, codeText);
            writeTextStandard(contentStream, 11, MARGIN + 200, yPosition, nameText);
        } else {
            writeText(contentStream, font, 11, MARGIN, yPosition, codeText);
            writeText(contentStream, font, 11, MARGIN + 200, yPosition, nameText);
        }
        return yPosition - HEADER_GAP - 8;
    }

    private float drawTableHeaders(PDPageContentStream contentStream, PDType0Font font,
                                   float[] colStarts, float colWidth, float yPosition) throws IOException {
        String[] headers = {
                "Наименование объекта", "Место нахождения объекта", "Телефон объекта",
                "Собственник", "Место нахождения собственника", "Телефон собственника",
                "Обезвреживает собственные", "Принимает от других"
        };

        int maxLines = 0;
        float currentY = yPosition;

        for (int i = 0; i < headers.length; i++) {
            int lines;
            if (useStandardFonts || font == null) {
                lines = drawWrappedTextStandard(contentStream, colStarts[i], currentY, colWidth - 8, headers[i]);
            } else {
                lines = drawWrappedText(contentStream, font, 9, colStarts[i], currentY, colWidth - 8, headers[i]);
            }
            maxLines = Math.max(maxLines, lines);
        }
        return currentY - (ROW_HEIGHT * maxLines) - 2;
    }

    private int drawFactoryRow(PDPageContentStream contentStream, PDType0Font font,
                               float[] colStarts, float colWidth, float yPosition,
                               WasteTypeReportDto.FactoryForWasteReportDto factory) throws IOException {
        String checkboxYes = "✓";
        String checkboxNo = "✗";

        int l1, l2, l3, l4, l5, l6, l7, l8;

        if (useStandardFonts || font == null) {
            l1 = drawWrappedTextStandard(contentStream, colStarts[COL_NAME_OBJ], yPosition, colWidth - 8,
                    formatObjectNameWithMeta(factory.getNameObj(), factory.getRegistrationNumber(), factory.getYnp()));
            l2 = drawWrappedTextStandard(contentStream, colStarts[COL_ADDRESS_OBJ], yPosition, colWidth - 8, getValue(factory.getAddressObj()));
            l3 = drawWrappedTextStandard(contentStream, colStarts[COL_PHONE_OBJ], yPosition, colWidth - 8, getValue(factory.getPhoneObj()));
            l4 = drawWrappedTextStandard(contentStream, colStarts[COL_NAME_OWN], yPosition, colWidth - 8, getValue(factory.getNameOwn()));
            l5 = drawWrappedTextStandard(contentStream, colStarts[COL_ADDRESS_OWN], yPosition, colWidth - 8, getValue(factory.getAddressOwn()));
            l6 = drawWrappedTextStandard(contentStream, colStarts[COL_PHONE_OWN], yPosition, colWidth - 8, getValue(factory.getPhoneOwn()));
            l7 = drawWrappedTextStandard(contentStream, colStarts[COL_USE_TRASH], yPosition, colWidth - 8,
                    Boolean.TRUE.equals(factory.getObjUseTrash()) ? checkboxYes : checkboxNo);
            l8 = drawWrappedTextStandard(contentStream, colStarts[COL_ACCEPT_TRASH], yPosition, colWidth - 8,
                    Boolean.TRUE.equals(factory.getObjAcceptTrash()) ? checkboxYes : checkboxNo);
        } else {
            l1 = drawWrappedText(contentStream, font, 8, colStarts[COL_NAME_OBJ], yPosition, colWidth - 8,
                    formatObjectNameWithMeta(factory.getNameObj(), factory.getRegistrationNumber(), factory.getYnp()));
            l2 = drawWrappedText(contentStream, font, 8, colStarts[COL_ADDRESS_OBJ], yPosition, colWidth - 8, getValue(factory.getAddressObj()));
            l3 = drawWrappedText(contentStream, font, 8, colStarts[COL_PHONE_OBJ], yPosition, colWidth - 8, getValue(factory.getPhoneObj()));
            l4 = drawWrappedText(contentStream, font, 8, colStarts[COL_NAME_OWN], yPosition, colWidth - 8, getValue(factory.getNameOwn()));
            l5 = drawWrappedText(contentStream, font, 8, colStarts[COL_ADDRESS_OWN], yPosition, colWidth - 8, getValue(factory.getAddressOwn()));
            l6 = drawWrappedText(contentStream, font, 8, colStarts[COL_PHONE_OWN], yPosition, colWidth - 8, getValue(factory.getPhoneOwn()));
            l7 = drawWrappedText(contentStream, font, 8, colStarts[COL_USE_TRASH], yPosition, colWidth - 8,
                    Boolean.TRUE.equals(factory.getObjUseTrash()) ? checkboxYes : checkboxNo);
            l8 = drawWrappedText(contentStream, font, 8, colStarts[COL_ACCEPT_TRASH], yPosition, colWidth - 8,
                    Boolean.TRUE.equals(factory.getObjAcceptTrash()) ? checkboxYes : checkboxNo);
        }

        return Math.max(Math.max(Math.max(l1, l2), Math.max(l3, l4)),
                Math.max(Math.max(l5, l6), Math.max(l7, l8)));
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ (стандартные шрифты) ====================

    private void writeTextStandard(PDPageContentStream contentStream, int size,
                                   float x, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(HELVETICA, size);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private int drawWrappedTextStandard(PDPageContentStream contentStream,
                                        float x, float y, float maxWidth, String text) throws IOException {
        List<String> lines = wrapLinesStandard(text, maxWidth);
        float lineY = y;
        for (String line : lines) {
            writeTextStandard(contentStream, 8, x, lineY, line);
            lineY -= ROW_HEIGHT;
        }
        return lines.size();
    }

    private List<String> wrapLinesStandard(String text, float maxWidth) throws IOException {
        List<String> out = new ArrayList<>();
        String normalized = text == null ? "—" : text.replace("\r", "");
        String[] forcedLines = normalized.split("\n", -1);

        for (String forced : forcedLines) {
            String part = forced.trim();
            if (part.isEmpty()) continue;

            String[] words = part.split("\\s+");
            StringBuilder line = new StringBuilder();

            for (String w : words) {
                String candidate = line.length() == 0 ? w : line + " " + w;
                float width = HELVETICA.getStringWidth(candidate) / 1000f * 8f;
                if (width <= maxWidth || line.length() == 0) {
                    line.setLength(0);
                    line.append(candidate);
                } else {
                    out.add(line.toString());
                    line.setLength(0);
                    line.append(w);
                }
            }
            if (line.length() > 0) out.add(line.toString());
        }
        if (out.isEmpty()) out.add("—");
        return out;
    }

    // ==================== ОБЩИЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private String getValue(Object value) {
        if (value == null) return "—";
        String str = String.valueOf(value);
        return str.isEmpty() || str.equals("null") ? "—" : str;
    }

    /** Наименование объекта + реестровый номер и УНП (как в реестре). */
    private String formatObjectNameWithMeta(String name, String registrationNumber, String ynp) {
        String orgName = getValue(name);
        String reg = getValue(registrationNumber);
        String unp = getValue(ynp);
        return orgName + "\nРеестровый номер: " + reg + "\nУНП: " + unp;
    }

    private void writeText(PDPageContentStream contentStream, PDType0Font font, int size,
                           float x, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void writeCenteredText(PDPageContentStream contentStream, PDPage page,
                                   PDType0Font font, int size, float y, String text) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000 * size;
        float x = (page.getMediaBox().getWidth() - textWidth) / 2f;
        writeText(contentStream, font, size, x, y, text);
    }

    private float pageTop(PDPage page) {
        return page.getMediaBox().getHeight() - MARGIN;
    }

    private float[] columnStarts(PDPage page) {
        float usableWidth = page.getMediaBox().getWidth() - (MARGIN * 2);
        float colWidth = usableWidth / COLUMN_COUNT;
        float[] starts = new float[COLUMN_COUNT];
        for (int i = 0; i < COLUMN_COUNT; i++) {
            starts[i] = MARGIN + (i * colWidth);
        }
        return starts;
    }

    private float columnWidth(PDPage page) {
        float usableWidth = page.getMediaBox().getWidth() - (MARGIN * 2);
        return usableWidth / COLUMN_COUNT;
    }

    private int drawWrappedText(PDPageContentStream contentStream, PDType0Font font, int size,
                                float x, float y, float maxWidth, String text) throws IOException {
        List<String> lines = wrapLines(font, size, text, maxWidth);
        float lineY = y;
        for (String line : lines) {
            writeText(contentStream, font, size, x, lineY, line);
            lineY -= ROW_HEIGHT;
        }
        return lines.size();
    }

    private List<String> wrapLines(PDType0Font font, int size, String text, float maxWidth) throws IOException {
        List<String> out = new ArrayList<>();
        String normalized = text == null ? "—" : text.replace("\r", "");
        String[] forcedLines = normalized.split("\n", -1);

        for (String forced : forcedLines) {
            String part = forced.trim();
            if (part.isEmpty()) continue;

            String[] words = part.split("\\s+");
            StringBuilder line = new StringBuilder();

            for (String w : words) {
                String candidate = line.length() == 0 ? w : line + " " + w;
                float width = font.getStringWidth(candidate) / 1000f * size;
                if (width <= maxWidth || line.length() == 0) {
                    line.setLength(0);
                    line.append(candidate);
                } else {
                    out.add(line.toString());
                    line.setLength(0);
                    line.append(w);
                }
            }
            if (line.length() > 0) out.add(line.toString());
        }
        if (out.isEmpty()) out.add("—");
        return out;
    }

    private void drawInnerGrid(PDPageContentStream contentStream, float[] colStarts,
                               float colWidth, float topY, float bottomY,
                               List<Float> horizontalYs) throws IOException {
        if (topY <= bottomY) return;

        contentStream.setLineWidth(0.6f);
        float verticalShift = 3f;

        for (int i = 1; i < colStarts.length; i++) {
            float x = colStarts[i] - verticalShift;
            contentStream.moveTo(x, topY);
            contentStream.lineTo(x, bottomY);
        }

        float left = colStarts[0];
        float right = colStarts[0] + (colWidth * colStarts.length);
        float textSafeLift = ROW_HEIGHT * 0.75f;

        for (Float y : horizontalYs) {
            if (y == null) continue;
            float yLine = y + textSafeLift;
            if (yLine >= topY || yLine <= bottomY) continue;
            contentStream.moveTo(left, yLine);
            contentStream.lineTo(right, yLine);
        }
        contentStream.stroke();
    }

    private void addPageNumbers(PDDocument document, PDType0Font font) throws IOException {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int totalPages = document.getNumberOfPages();

        for (int i = 0; i < totalPages; i++) {
            PDPage page = document.getPage(i);
            String pageLabel = (i + 1) + " / " + totalPages;

            if (useStandardFonts || font == null) {
                float textWidth = HELVETICA.getStringWidth(pageLabel) / 1000f * 9f;
                float xRight = page.getMediaBox().getWidth() - MARGIN - textWidth;
                float yBottom = MARGIN - 8;
                try (PDPageContentStream pageNoStream =
                             new PDPageContentStream(document, page, AppendMode.APPEND, true, true)) {
                    writeTextStandard(pageNoStream, 9, MARGIN, yBottom, dateStr);
                    writeTextStandard(pageNoStream, 9, xRight, yBottom, pageLabel);
                }
            } else {
                float textWidth = font.getStringWidth(pageLabel) / 1000f * 9f;
                float xRight = page.getMediaBox().getWidth() - MARGIN - textWidth;
                float yBottom = MARGIN - 8;
                try (PDPageContentStream pageNoStream =
                             new PDPageContentStream(document, page, AppendMode.APPEND, true, true)) {
                    writeText(pageNoStream, font, 9, MARGIN, yBottom, dateStr);
                    writeText(pageNoStream, font, 9, xRight, yBottom, pageLabel);
                }
            }
        }
    }


}