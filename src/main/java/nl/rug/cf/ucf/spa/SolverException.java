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

/**
 * Class to represent exceptions while solving an assignment optimization (see {@link nl.rug.cf.ucf.spa.AssignmentSolver:getAssignments()}).
 */
public class SolverException extends Exception {
    public SolverException(String errorMessage) {
        super(errorMessage);
    }
}
