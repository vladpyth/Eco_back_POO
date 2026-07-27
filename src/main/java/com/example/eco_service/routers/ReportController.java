package com.example.eco_service.routers;


import com.example.eco_service.config.PdfReportGenerator;
import com.example.eco_service.dto.main_dto.WasteTypeReportDto;
import com.example.eco_service.dto.main_dto.WasteTypeReportDto;
import com.example.eco_service.entities.Region;
import com.example.eco_service.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final PdfReportGenerator pdfGenerator;

    @GetMapping("/client-data")
    @Operation(summary = "получить отчет")
    public ResponseEntity<List<WasteTypeReportDto>> getAllForReport() {
        return ResponseEntity.ok(reportService.getAllForReport());
    }

    @GetMapping("/client-data/portal")
    @Operation(summary = "Публичный реестр (плоско, с пагинацией)")
    public ResponseEntity<com.example.eco_service.dto.response.PageResponse<com.example.eco_service.dto.main_dto.PortalWasteRowDto>> getPortalTable(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        return ResponseEntity.ok(reportService.getPortalTablePaged(page, size, q, sort, dir));
    }

    @GetMapping("/pdf")
    @Operation(summary = "Сгенерировать PDF отчёт по всем типам отходов")
    public ResponseEntity<byte[]> generatePdfReport() throws IOException {
        List<WasteTypeReportDto> data = reportService.getAllForReport();
        byte[] pdfBytes = pdfGenerator.generateReport(data);

        String filename = "report_waste_types_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/pdf/{id}")
    @Operation(summary = "Сгенерировать PDF отчёт по конкретному типу отхода")
    public ResponseEntity<byte[]> generatePdfReportById(
            @Parameter(description = "ID типа отхода")
            @PathVariable Long id) throws IOException {

        WasteTypeReportDto data = reportService.getForReportById(id);
        byte[] pdfBytes = pdfGenerator.generateReport(List.of(data));

        String filename = "report_waste_type_" + data.getCodeTrash() + "_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/pdf/code/{codeTrash}")
    @Operation(summary = "Сгенерировать PDF отчёт по коду отхода")
    public ResponseEntity<byte[]> generatePdfReportByCode(
            @Parameter(description = "Код отхода")
            @PathVariable Integer codeTrash) throws IOException {

        WasteTypeReportDto data = reportService.getForReportByCode(codeTrash);
        byte[] pdfBytes = pdfGenerator.generateReport(List.of(data));

        String filename = "report_waste_type_" + codeTrash + "_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/pdf/class/{classDanger}")
    @Operation(summary = "Сгенерировать PDF отчёт по классу опасности")
    public ResponseEntity<byte[]> generatePdfReportByClassDanger(
            @Parameter(description = "Класс опасности (1-5)")
            @PathVariable Integer classDanger) throws IOException {

        List<WasteTypeReportDto> data = reportService.getForReportByClassDanger(classDanger);
        byte[] pdfBytes = pdfGenerator.generateReport(data);

        String filename = "report_waste_type_class_" + classDanger + "_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}


