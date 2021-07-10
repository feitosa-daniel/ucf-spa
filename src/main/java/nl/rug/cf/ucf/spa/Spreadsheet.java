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

import java.util.LinkedList;

/**
 * Class representing a simple spreadsheet that supports {@link java.lang.String} cells only.
 */
public class Spreadsheet {

    private final LinkedList<LinkedList<String>> data = new LinkedList<>();

    public Spreadsheet() { }

    public int getNumRows() {
        return data.size();
    }

    public int getNumColumns() {
        return data.getFirst().size();
    }
    
    public void addRow() {
        data.addLast(new LinkedList<>());
        for (int x = 0; x < getNumColumns(); x++) {
            data.getLast().add("");
        }
    }

    public void removeRow(int index) {
        data.remove(index);
    }

    public void addColumn() {
        for (LinkedList<String> l : data) {
            l.addLast("");
        }
    }

    public void removeColumn(int index) {
        for (LinkedList<String> l : data) {
            l.remove(index);
        }
    }

    public String getCell(int row, int column) {
        return data.get(row).get(column);
    }

    public void setCell(String value, int row, int column) {
        data.get(row).set(column, value.trim());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (LinkedList<String> row : data) {
            for (String cellData : row) {
                if (cellData.equals(""))
                    cellData = "-empty-";
                str.append(cellData).append("  ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}