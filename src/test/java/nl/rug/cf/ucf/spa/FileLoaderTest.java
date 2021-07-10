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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileLoaderTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    @DisplayName("Should fail to find file.")
    void importFileError() {
        Exception exception = assertThrows(IOException.class, () -> {
            FileLoader.importSpreadsheet(new File("non-existing-file.csv"));
        });
        String expectedMessage = "'non-existing-file.csv' is not a file.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should fail to recognize extension.")
    void importExtensionError() {
        Exception exception = assertThrows(IOException.class, () -> {
            String filepath = classLoader.getResource("unknownExtension.ext").getFile();
            FileLoader.importSpreadsheet(new File(filepath));
        });
        String expectedMessage = "'ext' format is not supported.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should import CSV successfully and find correct data in spreadsheet.")
    void importFromCSV() throws Exception {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        assertAll(() -> assertCapstoneSpreadsheetContent(ss));
    }

    @Test
    @DisplayName("Should import XLSX successfully and find correct data in spreadsheet.")
    void importFromXLSX() throws Exception {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone.xlsx").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        assertAll(() -> assertCapstoneSpreadsheetContent(ss));
    }

    @Test
    @DisplayName("Should import XLS successfully and find correct data in spreadsheet.")
    void importFromXLS() throws Exception {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone.xls").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        assertAll(() -> assertCapstoneSpreadsheetContent(ss));
    }

    void assertCapstoneSpreadsheetContent(Spreadsheet ss) {
        assertEquals(7, ss.getNumRows());
        assertEquals(6, ss.getNumColumns());
        assertEquals("John Doe", ss.getCell(0,3));
        assertEquals("", ss.getCell(5,5));
        assertEquals("y", ss.getCell(6,2));
    }
}