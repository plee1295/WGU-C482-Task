/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgu_c482;

import wgu_c482.Part;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author parkerlee
 */
public class Outsourced extends Part {

    private final StringProperty companyName;

    public Outsourced() {
        super();
        companyName = new SimpleStringProperty();
    }

    public void setPartCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getPartCompanyName() {
        return this.companyName.get();
    }
}
