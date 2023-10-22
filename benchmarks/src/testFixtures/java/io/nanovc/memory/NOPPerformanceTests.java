package io.nanovc.memory;

import org.junit.jupiter.api.TestInfo;

public class NOPPerformanceTests extends PerformanceTests
{
    /**
     * Creates the system that is being tested.
     * Subclasses will be created that represent the specific implementation that we are testing.
     *
     * @return The system that is being tested.
     */
    @Override
    public SystemUnderTest createSystemUnderTest(TestInfo testInfo)
    {
        return new NOPSystemUnderTest();
    }
}
