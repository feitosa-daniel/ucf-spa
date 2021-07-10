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

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author danielfeitosa
 */
public class LivingLabSolver extends AssignmentSolver {

    public LivingLabSolver(Spreadsheet data) throws SpreadsheetFormatException {
        super(data);
    }

    @Override
    protected int getSpreadsheetIndexOfFirstStudent() {
        return 4;
    }
    
    @Override
    protected void validateData() throws SpreadsheetFormatException {
        for (int p = 1; p < data.getNumRows(); p++) {
            if (data.getCell(p,0).isEmpty()) {
                throw new SpreadsheetFormatException(String.format("No project in row %d. Last identified row is %d", p+1, data.getNumRows()));
            }
            if (data.getCell(p,2).isEmpty() || data.getCell(p,3).isEmpty()) {
                throw new SpreadsheetFormatException("Columns 3 and 4 cannot be empty. They must contain the min. and max. number of students allowed in the project.");
            }
            try{
                Double.parseDouble(data.getCell(p,2));
                Double.parseDouble(data.getCell(p,3));
            } catch (NumberFormatException e) {
                throw new SpreadsheetFormatException("Columns 3 and 4 must contain the min. and max. number of students allowed in the project.");
            }
        }
        for (int s = fsi; s < data.getNumColumns(); s++) {
            if (data.getCell(0,s).isEmpty()) {
                throw new SpreadsheetFormatException(String.format("No student in column %d. Last identified column is %d", s+1, data.getNumColumns()));
            }
            for (int p = 1; p < data.getNumRows(); p++) {
                if (!data.getCell(p,s).isEmpty()){
                    try{
                        Double.parseDouble(data.getCell(p,s));
                    } catch (NumberFormatException e) {
                        throw new SpreadsheetFormatException(String.format("Student ranking in row %d, column %d ('%s') is not a number.", p+1, s+1, data.getCell(p,s)));
                    }
                }
            }
        }
    }
    
    @Override
    public void setSolverConfig(String property, Object value) throws IllegalArgumentException { }
    
    @Override
    public String getProjectInfoHTML(String project) throws DataNotFoundException {
        int pIdx = getProjectIndex(project);
        String host = data.getCell(pIdx, 1);
        String minS = data.getCell(pIdx, 2);
        String maxS = data.getCell(pIdx, 3);
        
        return String.format("<b>%s</b><br>&nbsp;&nbsp;Host: %s<br>&nbsp;&nbsp;From %s to %s students", project, host, minS, maxS);
    }

    @Override
    public String getStudentInfoHTML(String student) throws DataNotFoundException {
        int sIdx = getStudentIndex(student);
        String text = String.format("<b>%s</b><ol>", student);
        HashMap<Integer,String> projects = new HashMap<>();
        
        for (int p=1; p<data.getNumRows(); p++) {
            if (!data.getCell(p, sIdx).isEmpty()) {
                projects.put((int) Double.parseDouble(data.getCell(p, sIdx)), data.getCell(p, 0));
            }
        }
        
        for (Integer s : new TreeSet<>(projects.keySet())) {
            text += String.format("<li>%s</li>", projects.get(s));
        }
        
        return String.format("%s</ol>", text);
    }

    @Override
    public HashMap<String, String> getAssignments() throws SolverException {
        // Define sizes of the cost matrix
        int numWorkers = data.getNumColumns() - fsi;
        int numTasks = data.getNumRows() - 1;

        // Create double matrix
        double[][] costs = new double[numWorkers][numTasks];
        double maxCost = 100;
        double maxRank = getMaxStudentRank();
        for (int i = 0; i < costs.length; i++) {
            int s = i + fsi;
            int sp = fixedAassignments.getOrDefault(s,-1);
            for (int j = 0; j < costs[0].length; j++) {
                int p = j + 1;
                if (p == sp){
                    costs[i][j] = 0;
                } else {
                    costs[i][j] = maxCost;
                    if (!data.getCell(p, s).isEmpty()) {
                        costs[i][j] -= (maxRank - (int) Double.parseDouble(data.getCell(p, s)) + 1);
                    }
                }
            }
        }

        // Create solver
        Loader.loadNativeLibraries();
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            throw new SolverException("Internal error: Could not create solver.");
        }

        // Create variables
        // x[i][j] is an array of 0-1 variables, which will be 1
        // if worker i is assigned to task j.
        MPVariable[][] x = new MPVariable[numWorkers][numTasks];
        for (int i = 0; i < numWorkers; ++i) {
            for (int j = 0; j < numTasks; ++j) {
                x[i][j] = solver.makeIntVar(0, 1, "");
            }
        }

        // Constraints
        // Each worker is assigned to exactly one task.
        for (int i = 0; i < numWorkers; ++i) {
            MPConstraint constraint = solver.makeConstraint(1, 1, "");
            for (int j = 0; j < numTasks; ++j) {
                constraint.setCoefficient(x[i][j], 1);
            }
        }
        // Min. and max. number of workers in each task very.
        for (int j = 0; j < numTasks; ++j) {
            int min = (int) Double.parseDouble(data.getCell(j+1, 2));
            int max = (int) Double.parseDouble(data.getCell(j+1, 3));
            MPConstraint constraint = solver.makeConstraint(min, max, "");
            for (int i = 0; i < numWorkers; ++i) {
                constraint.setCoefficient(x[i][j], 1);
            }
        }

        // Set objective
        MPObjective objective = solver.objective();
        for (int i = 0; i < numWorkers; ++i) {
            for (int j = 0; j < numTasks; ++j) {
                objective.setCoefficient(x[i][j], costs[i][j]);
            }
        }
        objective.setMinimization();

        // Solve
        MPSolver.ResultStatus resultStatus = solver.solve();

        // Get student-project assignments
        HashMap<String,String> assignments = new HashMap<>();
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL  || resultStatus == MPSolver.ResultStatus.FEASIBLE) {
            for (int i = 0; i < numWorkers; ++i) {
                for (int j = 0; j < numTasks; ++j) {
                    if (x[i][j].solutionValue() > 0.5) {
                        String student = data.getCell(0,i+fsi);
                        String project = data.getCell(j+1,0);
                        assignments.put(student,project);
                    }
                }
            }
        } else {
            throw new SolverException("An assignment could not be determined."+
                    " To deal with this problem, you can try: (1) removing the supervisor restriction,"+
                    " or (2) manually assigning some students. Then, run the assignment again.");
        }

        return assignments;
    }
}
