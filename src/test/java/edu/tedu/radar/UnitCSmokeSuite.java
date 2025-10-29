package edu.tedu.radar;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        Target_Test.class,
        Detection_Test.class
})
public class UnitCSmokeSuite {
}