package edu.tedu.radar;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

// 🧮 This suite groups main logic and functional-level tests
@Suite
@SelectClasses({
        Radar_Test.class
})
public class FunctionalCCoreLogicSuite {
    // Also just a container for grouping tests
}
