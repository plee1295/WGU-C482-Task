/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgu_c482;

import wgu_c482.Part;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author parkerlee
 */
public class InHouse extends Part {

    private final IntegerProperty machineID;

    public InHouse() {
        super();
        machineID = new SimpleIntegerProperty();
    }

    public void setPartMachineID(int machineID) {
        this.machineID.set(machineID);
    }

    public int getPartMachineID() {
        return this.machineID.get();
    }
}
