package edu.tedu.radar;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

// 🧪 This suite groups basic unit tests (constructors, getters)
@Suite
@SelectClasses({
        Target_Test.class,
        Detection_Test.class
})
public class UnitCSmokeSuite {
    // No code needed inside — this class serves as a suite container
}
