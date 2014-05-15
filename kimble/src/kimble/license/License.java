/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.license;

import java.util.Scanner;

/**
 *
 * @author Christoffer
 */
public enum License {

    LWJGL_LICENSE("lwjgl_license.txt"),
    TWL_LICENSE("twl_license.txt"),
    XPP3_LICENSE("xpp3_license.txt"),
    GSON_LICENSE("gson_license.txt");

    // Enum instance code below. To add a new license just extend the list above this comment.
    // Save the license file in the res/licenses/ directory
    //
    // Example:
    // LICENSE_1("filename_1"), LICENSE_2("filename_2"); etc
    //
    // You access the license text with "License.LICENSE_1.getText();".
    //
    public static final String LICENSE_DIR = "/res/licenses/";
    private final String filename;

    License(String filename) {
        this.filename = filename;
    }

    public StringBuilder getText() {
        StringBuilder s = new StringBuilder();
        Scanner scanner = new Scanner(License.class.getResourceAsStream(LICENSE_DIR + filename));
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            s.append(line).append("\n");
        }
        scanner.close();
        return s;
    }
}
