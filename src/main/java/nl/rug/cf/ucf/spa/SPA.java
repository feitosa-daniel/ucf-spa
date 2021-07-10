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

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JOptionPane;
import nl.rug.cf.ucf.spa.gui.MainWindow;

public class SPA {
    public static void main(String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(() -> {
            MainWindow main = new MainWindow();
            main.setVisible(true);

            if (!System.getProperty("sun.arch.data.model").equals("64")) {
                JOptionPane.showMessageDialog(main, "You do not have Java 64-bit version installed.\nYou can find it at https://java.com/download/manual.jsp", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}
