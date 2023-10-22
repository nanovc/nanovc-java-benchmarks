package io.nanovc.memory;

import org.junit.jupiter.api.TestInfo;

/**
 * Tests the Nano VC decomposition.
 */
public class NanoVCOperationTests extends OperationalDecompositionTests
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
        return new NanoVCRepo();
    }
}
