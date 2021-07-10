/*
 *  This file is part of ucf-spa.
 *
 *  ucf-spa is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ucf-spa is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */
package nl.rug.cf.ucf.spa;

import java.io.*;
import java.nio.file.Files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;

/**
 * Class to handle the importing of spreadsheet data from XLSX, XLS, and CSV files.
 */
public class FileLoader {

    public static Spreadsheet importSpreadsheet(File file) throws IOException {
        if (!file.isFile()){
            throw new IOException(String.format("'%s' is not a file.", file.getName()));
        }

        String fileFormat = FilenameUtils.getExtension(file.getName());
        switch(fileFormat){
            case "csv":  return importFromCSV(file);
            case "xlsx":
            case "xls":  return importFromExcel(file);
            default:     throw new IOException(String.format("'%s' format is not supported.", fileFormat));
        }
    }

    public static Spreadsheet importFromCSV(File file) throws IOException {
        Spreadsheet ss = new Spreadsheet();

        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                ss.addRow();
                while (ss.getNumColumns() < record.size()){
                    ss.addColumn();
                }
                for (int col = 0; col < record.size() ; col++){
                    ss.setCell(record.get(col), ss.getNumRows()-1,col);
                }
            }
        } catch (IOException ex) {
            throw new IOException(String.format("Cannot read from CSV file '%s'", file.getName()));
        }

        return ss;
    }


    public static Spreadsheet importFromExcel(File file) throws IOException {
        Spreadsheet ss = new Spreadsheet();
        System.out.println(file.getAbsolutePath());
        Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
        Sheet content = workbook.getSheetAt(0);
        int nRows = content.getLastRowNum() + 1;
        int nCols = content.getRow(0).getLastCellNum() + 1;

        // Initialize spreadsheet
        for (int i = 0; i < nRows; i++) {
            ss.addRow();
        }
        for (int j = 0; j < nCols; j++) {
            ss.addColumn();
        }

        // Add data from Excel file
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Cell cell = content.getRow(i).getCell(j, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
                ss.setCell(getCellDataAsString(cell), i, j);
            }
        }

        // Delete extra columns and rows
        while (ss.getCell(0, ss.getNumColumns() - 1).equals("")) {
            ss.removeColumn(ss.getNumColumns()-1);
        }
        while (ss.getCell(ss.getNumRows() - 1, 0).equals("")) {
            ss.removeRow(ss.getNumRows()-1);
        }

        return ss;
    }

    private static String getCellDataAsString(Cell cell){
        if (cell != null) {
            switch(cell.getCellType()){
                case STRING: return cell.getStringCellValue();
                case NUMERIC: return String.valueOf((int)cell.getNumericCellValue());
                default: return "";
            }
        } else {
            return "";
        }
    }
}
