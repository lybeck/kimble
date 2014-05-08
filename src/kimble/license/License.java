/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christoffer
 */
public enum License {

    LWJGL_LICENSE("lwjgl_license.txt"),
    TWL_LICENSE("twl_license.txt");

    // Enum instance code below. To add a new license just extend the list above this comment.
    // Save the license file in the res/licenses/ directory
    //
    // Example:
    // LICENSE_1("filename_1"), LICENSE_2("filename_2"); etc
    //
    // You access the license text with "License.LICENSE_1.getText();".
    //
    public static final String LICENSE_DIR = "res/licenses/";
    private final String filename;

    License(String filename) {
        this.filename = filename;
    }

    public StringBuilder getText() {
        BufferedReader reader;
        StringBuilder s = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(new File(LICENSE_DIR + filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                s.append(line).append("\n");
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
}
