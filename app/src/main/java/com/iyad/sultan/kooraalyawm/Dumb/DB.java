package com.iyad.sultan.kooraalyawm.Dumb;

import com.iyad.sultan.kooraalyawm.Model.Group3;

import java.util.List;

public class DB {


    private List<Group3> getUserGroup(){
        //connect to google Firebase
        //Dumb Data
        DumbGen gen = new DumbGen();
        return gen.createGroups50();
    }
}
