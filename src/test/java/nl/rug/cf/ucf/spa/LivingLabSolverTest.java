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

class LivingLabSolverTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    @DisplayName("Should solve living lab allocation and get expected answers.")
    void simpleAllocation() throws IOException, SpreadsheetFormatException, SolverException {
        String filepath = classLoader.getResource("sampleSpreadsheet-livinglab.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        LivingLabSolver solver = new LivingLabSolver(ss);

        HashMap<String,String> allocations = solver.getAssignments();
        System.out.println(allocations);
        for (String student : allocations.keySet()){
            switch (student){
                case "John Doe": assertEquals("Project A", allocations.get(student)); break;
                case "Martha Glenn": assertEquals("Project A", allocations.get(student)); break;
                case "Carol Smith": assertEquals("Project B", allocations.get(student)); break;
                case "Agatha Ash": assertEquals("Project A", allocations.get(student)); break;
                case "Marc Groen": assertEquals("Project D", allocations.get(student)); break;
                case "Debora Visser": assertEquals("Project A", allocations.get(student)); break;
                case "Gregory Adam": assertEquals("Project C", allocations.get(student)); break;
            }
        }
    }

    @Test
    @DisplayName("Should solve living lab allocation  with pre-fixed allocation and get expected answers.")
    void supervisorRestrictionOnAllocation() throws IOException, SpreadsheetFormatException, SolverException, DataNotFoundException {
        String filepath = classLoader.getResource("sampleSpreadsheet-livinglab.csv").getFile();
        Spreadsheet ss = FileLoader.importSpreadsheet(new File(filepath));
        LivingLabSolver solver = new LivingLabSolver(ss);
        solver.fixAssignment("Carol Smith", "Project A");
        
        HashMap<String,String> allocations = solver.getAssignments();
        for (String student : allocations.keySet()){
            switch (student){
                case "John Doe": assertEquals("Project A", allocations.get(student)); break;
                case "Martha Glenn": assertEquals("Project A", allocations.get(student)); break;
                case "Carol Smith": assertEquals("Project A", allocations.get(student)); break;
                case "Agatha Ash": assertEquals("Project A", allocations.get(student)); break;
                case "Marc Groen": assertEquals("Project C", allocations.get(student)); break;
                case "Debora Visser": assertEquals("Project D", allocations.get(student)); break;
                case "Gregory Adam": assertEquals("Project B", allocations.get(student)); break;
            }
        }
    }
}