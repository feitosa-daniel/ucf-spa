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

import java.util.HashMap;

public abstract class AssignmentSolver {
    
    protected Spreadsheet data;
    protected HashMap<Integer,Integer> fixedAassignments = new HashMap<>();
    
    /** Index of the first student column in the spreadsheet **/
    protected final int fsi;

    public AssignmentSolver(Spreadsheet data) throws SpreadsheetFormatException {
        this.data = data;
        this.fsi = getSpreadsheetIndexOfFirstStudent();
        validateData();
    }
    
    abstract protected int getSpreadsheetIndexOfFirstStudent();
    
    abstract protected void validateData() throws SpreadsheetFormatException;
    
    abstract public void setSolverConfig(String property, Object value) throws IllegalArgumentException;

    public String[] getProjects() {
        String[] projects = new String[data.getNumRows()-1];
        for (int p = 1; p < data.getNumRows(); p++) {
            projects[p-1] = data.getCell(p,0);
        }
        return projects;
    }

    public String[] getStudents() {
        String[] students = new String[data.getNumColumns()-fsi];
        for (int s = fsi; s < data.getNumColumns(); s++) {
            students[s-fsi] = data.getCell(0,s);
        }
        return students;
    }
    
    abstract public String getProjectInfoHTML(String project) throws DataNotFoundException;
    
    abstract public String getStudentInfoHTML(String student) throws DataNotFoundException;
        
    public void fixAssignment(String student, String project) throws DataNotFoundException {
        int pIdx = getProjectIndex(project);
        int sIdx = getStudentIndex(student);
        unfixAssignment(student);
        fixedAassignments.put(sIdx, pIdx);
    }

    public void unfixAssignment(String student) throws DataNotFoundException {
        int idx = getStudentIndex(student);
        if (fixedAassignments.containsKey(idx)){
            fixedAassignments.remove(idx);
        }
    }

    protected int getProjectIndex(String project) throws DataNotFoundException {
        for (int p = 1; p < data.getNumRows(); p++) {
            if (data.getCell(p,0).equals(project)) {
                return p;
            }
        }
        throw new DataNotFoundException(String.format("Project '%s' not found in the spreadsheet.", project));
    }

    protected int getStudentIndex(String student) throws DataNotFoundException {
        for (int s = fsi; s < data.getNumColumns(); s++) {
            if (data.getCell(0,s).equals(student)) {
                return s;
            }
        }
        throw new DataNotFoundException(String.format("Student '%s' not found in the spreadsheet.", student));
    }
    
    public int getMaxStudentRank() {
        int max = 0;
        for (int s = fsi; s < data.getNumColumns(); s++) {
            for (int p = 1; p < data.getNumRows(); p++) {
                if (!data.getCell(p,s).isEmpty()) {
                    max = Math.max(max, (int) Double.parseDouble(data.getCell(p,s)));
                }
            }
        }
        return max;
    }
    
    abstract public HashMap<String,String> getAssignments() throws SolverException;
}
