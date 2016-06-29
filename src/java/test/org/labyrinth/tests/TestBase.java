package org.labyrinth.tests;

import org.labyrinth.helper.JSONHelper;
import org.testng.annotations.BeforeClass;

/**
 * 
 * @author Revati Krishnan Iyer
 *
 */
public class TestBase {

    JSONHelper jsonhelper;
    
    @BeforeClass
    public void setUp(){
        jsonhelper=new JSONHelper();
    }
}
