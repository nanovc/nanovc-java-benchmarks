package io.nanovc.memory;

import org.junit.jupiter.api.TestInfo;

public class GitInspiredPerformanceTests extends PerformanceTests
{
    /**
     * Creates the system that is being tested.
     * Subclasses will be created that represent the specific implementation that we are testing.
     *
     * @param testInfo
     * @return The system that is being tested.
     */
    @Override
    public SystemUnderTest createSystemUnderTest(TestInfo testInfo)
    {
        return new GitInspiredRepo();
    }
}
