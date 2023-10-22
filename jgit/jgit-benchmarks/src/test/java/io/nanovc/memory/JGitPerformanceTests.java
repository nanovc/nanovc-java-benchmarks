package io.nanovc.memory;

import io.nanovc.junit.TestDirectory;
import io.nanovc.junit.TestDirectoryExtension;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Path;

/**
 * Tests the JGit performance.
 */
@ExtendWith(TestDirectoryExtension.class)
public class JGitPerformanceTests extends PerformanceTests
{
    /**
     * Gets the number of times the performance test should run to warm up.
     * Override this in subclasses if we need a different number of iterations.
     *
     * @return The number of times the performance test should run for the warm up.
     */
    @Override
    public int getPerformanceWarmUpIterations()
    {
        return 0;
    }

    /**
     * Gets the number of times the performance test should run the scenario.
     * Override this in subclasses if we need a different number of iterations.
     *
     * @return The number of times the performance test should run the scenario.
     */
    @Override
    public int getPerformanceTestIterations()
    {
        return 1000;
    }

    /**
     * The path where we can work for the unit test that is running.
     */
    @TestDirectory(useTestName = true)
    public Path testPath;

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
        JGitRepo jGitRepo = new JGitRepo();
        jGitRepo.testPath = testPath;
        return jGitRepo;
    }
}
