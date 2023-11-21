package com.kjms.service.utils;

import java.util.*;
import java.util.function.Function;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;

import static com.kjms.config.Constants.EXCEL_FILE_EXTENSIONS;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Util interface for Files
 */
@Service
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static String convertBytesToReadableSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            double kb = (double) bytes / 1024;
            return String.format("%.2f KB", kb);
        } else {
            double mb = (double) bytes / (1024 * 1024);
            return String.format("%.2f MB", mb);
        }
    }

    public static List<Map<String, Object>> readExcel(MultipartFile multipartFile) {

        if (multipartFile == null) {
            return new ArrayList<>();
        }

        String fileExtension = FileNameUtils.getExtension(multipartFile.getOriginalFilename());

        if (fileExtension.equals("xlsx")) {
            return readXslsExcel(multipartFile);
        } else if (fileExtension.equals("csv")) {
            return readCsvExcel(multipartFile);
        } else if (fileExtension.equals("tsv")) {
            return readTsvExcel(multipartFile);
        }

        return new ArrayList<>();
    }

    private static List<Map<String, Object>> readCsvExcel(MultipartFile multipartFile) {

        List<Map<String, Object>> result = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(multipartFile.getInputStream()))) {
            List<String[]> excelData = reader.readAll();

            String[] headers = excelData.get(0);

            for (int i = 1; i < excelData.size(); i++) {
                String[] row = excelData.get(i);

                Map<String, Object> rowData = new HashMap<>();

                for (int j = 0; j < headers.length; j++) {
                    String header = headers[j];
                    String value = (j < row.length) ? row[j] : null;
                    rowData.put(header, value);
                }

                result.add(rowData);
            }

        } catch (IOException | CsvException e) {

            log.error("CSV Excel Cannot be read: {}", multipartFile.getOriginalFilename());

            return new ArrayList<>();
        }

        return result;
    }

    private static List<Map<String, Object>> readTsvExcel(MultipartFile multipartFile) {
        List<Map<String, Object>> result = new ArrayList<>();

        try (CSVParser parser = CSVParser.parse(new InputStreamReader(multipartFile.getInputStream()), CSVFormat.TDF)) {
            List<String> headers = parser.getHeaderNames();

            for (CSVRecord record : parser) {
                Map<String, Object> rowData = new HashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    String header = headers.get(i);
                    String value = record.get(i);
                    rowData.put(header, value);
                }

                result.add(rowData);
            }
        } catch (IOException e) {
            log.error("Tsv Excel Cannot be read: {}", multipartFile.getOriginalFilename());

            return new ArrayList<>();
        }

        return result;
    }

    private static List<Map<String, Object>> readXslsExcel(MultipartFile multipartFile) {
        try (Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream())) {

            List<Map<String, Object>> result = new ArrayList<>();

            // Helper lambda expression to get a string value from a cell, handling different cell types
            Function<Cell, String> getStringValueFromCell = cell -> {
                if (cell == null) {
                    return "";
                }

                switch (cell.getCellType()) {
                    case STRING:
                        return cell.getStringCellValue();
                    case NUMERIC:
                        return String.valueOf(cell.getNumericCellValue());
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    default:
                        return "";
                }

            };

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                // Iterate through rows
                for (Row row : sheet) {
                    Map<String, Object> rowData = new HashMap<>();

                    // Iterate through cells in the row
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);

                        String columnName = sheet.getRow(0).getCell(j).getStringCellValue();

                        String cellValue = getStringValueFromCell.apply(cell);

                        rowData.put(columnName, cellValue);
                    }

                    result.add(rowData);
                }
            }

            if (result.size() > 1) {
                result.remove(0);
            }

            return result;

        } catch (IOException | EncryptedDocumentException e) {
            log.error("Excel Cannot be read: {}", multipartFile.getOriginalFilename());
            return new ArrayList<>();
        }
    }

    /**
     * Validate the multipart file is valid Excel or not.
     *
     * @param multipartFile the file to validate.
     * @return return true if the file is valid, Excel else return false.
     */
    public static boolean isValidExcel(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return false;
        }

        String fileExtension = FileNameUtils.getExtension(multipartFile.getOriginalFilename());

        return EXCEL_FILE_EXTENSIONS.stream().anyMatch(extension -> extension.equals(fileExtension));
    }

}
