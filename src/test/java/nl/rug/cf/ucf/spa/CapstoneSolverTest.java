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
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CapstoneSolverTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    @DisplayName("Should solve simple (i.e., obvious) allocation and get expected answers.")
    void simpleAllocation() throws IOException, SpreadsheetFormatException, SolverException {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        CapstoneSolver solver = new CapstoneSolver(ss);

        HashMap<String,String> allocations = solver.getAssignments(true);
        System.out.println(allocations);
        for (String student : allocations.keySet()){
            switch (student){
                case "John Doe": assertEquals("Project A", allocations.get(student)); break;
                case "Martha Glenn": assertEquals("Project D", allocations.get(student)); break;
                case "Carol Smith": assertEquals("Project B", allocations.get(student)); break;
            }
        }
    }

    @Test
    @DisplayName("Should solve allocation with supervisor restriction caveat and get expected answers.")
    void supervisorRestrictionOnAllocation() throws IOException, SpreadsheetFormatException, SolverException {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone-restriction.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        CapstoneSolver solver = new CapstoneSolver(ss);

        HashMap<String,String> allocations = solver.getAssignments(true);
        for (String student : allocations.keySet()){
            switch (student){
                case "John Doe": assertEquals("Project A", allocations.get(student)); break;
                case "Martha Glenn": assertEquals("Project E", allocations.get(student)); break;
                case "Carol Smith": assertEquals("Project B", allocations.get(student)); break;
            }
        }
    }

    @Test
    @DisplayName("Should solve allocation with supervisor restriction caveat off and get expected answers.")
    void supervisorRestrictionOffAllocation() throws IOException, SpreadsheetFormatException, SolverException {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone-restriction.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        CapstoneSolver solver = new CapstoneSolver(ss);

        HashMap<String,String> allocations = solver.getAssignments(false);
        for (String student : allocations.keySet()){
            switch (student){
                case "John Doe": assertEquals("Project A", allocations.get(student)); break;
                case "Martha Glenn": assertEquals("Project C", allocations.get(student)); break;
                case "Carol Smith": assertEquals("Project B", allocations.get(student)); break;
            }
        }
    }

    @Test
    @DisplayName("Should solve allocation with non-conflicting fixed allocation and get expected answers.")
    void nonConflictingFixedAllocation() throws IOException, SpreadsheetFormatException, SolverException, DataNotFoundException {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        CapstoneSolver solver = new CapstoneSolver(ss);
        solver.fixAssignment("John Doe", "Project C");

        HashMap<String,String> allocations = solver.getAssignments(true);
        for (String student : allocations.keySet()){
            switch (student){
                case "John Doe": assertEquals("Project C", allocations.get(student)); break;
                case "Martha Glenn": assertEquals("Project D", allocations.get(student)); break;
                case "Carol Smith": assertEquals("Project B", allocations.get(student)); break;
            }
        }
    }

    @Test
    @DisplayName("Should solve allocation with conflicting fixed allocation and get expected answers.")
    void conflictingFixedAllocation() throws IOException, SpreadsheetFormatException, SolverException, DataNotFoundException {
        String filepath = classLoader.getResource("sampleSpreadsheet-capstone-clash.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        CapstoneSolver solver = new CapstoneSolver(ss);
        solver.fixAssignment("John Doe", "Project A");

        HashMap<String,String> allocations = solver.getAssignments(true);
        for (String student : allocations.keySet()){
            switch (student){
                case "John Doe": assertEquals("Project A", allocations.get(student)); break;
                case "Martha Glenn": assertEquals("Project E", allocations.get(student)); break;
                case "Carol Smith": assertEquals("Project C", allocations.get(student)); break;
            }
        }
    }
}