package io.nanovc.memory;

import io.nanovc.junit.TestDirectory;
import io.nanovc.junit.TestDirectoryExtension;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Path;

/**
 * Tests the JGit operational decomposition.
 */
@ExtendWith(TestDirectoryExtension.class)
public class JGitOperationTests extends OperationalDecompositionTests
{
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
