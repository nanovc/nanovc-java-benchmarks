package io.nanovc.memory;

import org.junit.jupiter.api.*;

/**
 * The use-case scenarios are made up of combinations of the following operations:
 *
 * <ul>
 *     <li>New (N):</li>
 *     <ul>
 *         New content is created in the content-area.
 *     </ul>
 * </ul>
 *
 * <ul>
 *     <li>Modify (MN):</li>
 *     <ul>
 *         Content is modified in the content-area. If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
 *     </ul>
 * </ul>
 * <ul>
 *     <li>Delete (D):</li>
 *     <ul>
 *         Content is deleted in the content-area.
 *     </ul>
 * </ul>
 * <ul>
 *     <li>Commit (CN):</li>
 *     <ul>
 *          The content-area is committed to the repo to create a snapshot. If the commit is followed by a number N, then the number is used to label the specific commit.
 *     </ul>
 * </ul>
 * <ul>
 *     <li>Checkout (ON):</li>
 *     <ul>
 *          A previously committed snapshot is checked-out from the repo. If the checkout is followed by a number N, then it relates to the commit CN with the corresponding number.
 *     </ul>
 * </ul>
 * <ul>
 *     <li>Branch (BN):</li>
 *     <ul>
 *          A branch is created in the repo. If the branch is followed by a number N, then the number is used to label the specific branch.
 *     </ul>
 * </ul>
 * <ul>
 *     <li>Merge (GX|Y>Z):</li>
 *     <ul>
 *          Two or more commits are merged into one branch. If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits. The destination branch is preceded by an angle bracket >. Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
 *     </ul>
 * </ul>
 * The scenarios for the experimental setup represent common real-world use-cases that are made up of the operations mentioned above.
 *
 * These tests and all subclasses, are meant to be run at full speed so that we can measure the performance of each implementation.
 */
public abstract class PerformanceTests extends ScenarioTests
{
    // Define the number of method calls before JAVA decides to JIT Compile the implementation:
    public final int JIT_THRESHOLD = 10_000;

    // Define the number of extra calls we want as a margin:
    public final int ITERATION_BUFFER = 1_000;

    /**
     * The number of times to perform each scenario for the performance test.
     */
    public final int PERFORMANCE_TEST_ITERATIONS = 1_000_000;

    /**
     * Gets the number of times the performance test should run to warm up.
     * Override this in subclasses if we need a different number of iterations.
     * @return The number of times the performance test should run for the warm up.
     */
    public int getPerformanceWarmUpIterations()
    {
        // Define the total number of iterations:
        return JIT_THRESHOLD + ITERATION_BUFFER;
    }

    /**
     * This is used to warm up the implementations.
     */
    @Test
    @Order(0)
    public void warmUp()
    {
        // Get the number of warm up iterations we need to perform:
        final int MAX = getPerformanceWarmUpIterations();

        System.out.printf("Starting to warm up for %,d iterations...%n", MAX);
        for (int i = 0; i < MAX; i++)
        {
            // Perform all the operations:
            this.systemUnderTest.allOperations();

            if ((i % 1_000) == 0) System.out.println("Iteration: " + i);
        }
        System.out.printf("Done warming up%n");
    }

    /**
     * Gets the number of times the performance test should run the scenario.
     * Override this in subclasses if we need a different number of iterations.
     * @return The number of times the performance test should run the scenario.
     */
    public int getPerformanceTestIterations()
    {
        return PERFORMANCE_TEST_ITERATIONS;
    }

    /**
     * The timestamp when we started running the test.
     */
    private long startNanos;

    /**
     * The timestamp when we stopped running the test.
     */
    private long endNanos;

    @BeforeEach
    public void startTiming(TestInfo testInfo)
    {
        // Start timing:
        this.startNanos = System.nanoTime();
    }

    @AfterEach
    public void endTiming(TestInfo testInfo)
    {
        // Stop timing:
        this.endNanos = System.nanoTime();

        // Work out the duration:
        long deltaNanos = endNanos - startNanos;

        // Get the total number of iterations that were performed:
        int performanceTestIterations = getPerformanceTestIterations();

        // Print out the timing:
        System.out.printf(
            "System: %s | Test: %-50s | Duration (ns): %,12d ns | Duration (ms): %,6d ms | Iterations: %,10d | Rate: %,15.2f /s |%n",
            this.systemUnderTest.getSystemName(),
            testInfo.getDisplayName(),
            deltaNanos,
            deltaNanos / 1_000_000,
            performanceTestIterations,
            ((double)performanceTestIterations) * 1_000_000_000.0 / ((double) deltaNanos)
        );
    }

    /**
     * Creates the repo before running the scenario.
     */
    protected void createRepo()
    {
        this.systemUnderTest.createRepo();
    }

    /**
     * Frees the repo after running the scenario.
     */
    protected void freeRepo()
    {
        this.systemUnderTest.freeRepo();
    }

    //#region Scenario Performance Tests

    /**
     * (NC): New + Commit:
     * In this scenario, we create new content in the content area and then commit it to the repo.
     */
    @Test
    public void NC()
    {
        for (int i = 0; i < getPerformanceTestIterations(); i++)
        {
            createRepo();
            scenario_NC();
            freeRepo();
        }
    }


    /**
     * (NCMCDC): New + Commit + Modify + Commit + Delete + Commit:
     * In this scenario, we create new content, commit it then modify all of it, commit again, then delete all of it and commit a final time.
     */
    @Test
    public void NCMCDC()
    {
        for (int i = 0; i < getPerformanceTestIterations(); i++)
        {
            createRepo();
            scenario_NCMCDC();
            freeRepo();
        }
    }

    /**
     * (NCO): New + Commit + Checkout:
     * In this scenario, we create new content, commit it and then check it out.
     */
    @Test
    public void NCO()
    {
        for (int i = 0; i < getPerformanceTestIterations(); i++)
        {
            createRepo();
            scenario_NCO();
            freeRepo();
        }
    }

    /**
     * (NCB): New + Commit + Branch:
     * In this scenario, we create new content, commit it and then create a branch.
     */
    @Test
    public void NCB()
    {
        for (int i = 0; i < getPerformanceTestIterations(); i++)
        {
            createRepo();
            scenario_NCB();
            freeRepo();
        }
    }
    /**
     * (NCBMC): New + Commit + Branch + Modify + Commit:
     * In this scenario, we create new content, commit it, create a branch, modify that content and then commit it again to that branch.
     */
    @Test
    public void NCBMC()
    {
        for (int i = 0; i < getPerformanceTestIterations(); i++)
        {
            createRepo();
            scenario_NCBMC();
            freeRepo();
        }
    }
    /**
     * (NC1B1MC2G1|2>1): New + Commit1 + Branch1 + Modify + Commit2 + Merge1|2>1:
     * In this scenario, we create new content, commit it, modify that content, commit that and then merge both branches into the first branch.
     * This scenario allows for a fast-forward operation which can be cheap.
     */
    @Test
    public void NC1B1MC2G1_2__1()
    {
        for (int i = 0; i < getPerformanceTestIterations(); i++)
        {
            createRepo();
            scenario_NC1B1MC2G1_2__1();
            freeRepo();
        }
    }
    /**
     * (NC1B1M1C2B2M1C3G2|3>1): New + Commit1 + Branch1 + Modify1 + Commit2 + Branch2 + Modify1 + Commit3 Merge2|3>1:
     * In this scenario, which is the most complex of the scenarios, we create new content, commit it to branch 1.
     * We then modify that content in branch 1 and commit again.
     * We then create another branch from the first commit and then make a change to that content and commit that to branch 2.
     * We then merge both branches back into the first branch.
     * This scenario cannot allow for a fast-forward operation because a merge is required since changes were made in both branches.
     */
    @Test
    public void NC1B1M1C2B2M1C3G2_3__1()
    {
        for (int i = 0; i < getPerformanceTestIterations(); i++)
        {
            createRepo();
            scenario_NC1B1M1C2B2M1C3G2_3__1();
            freeRepo();
        }
    }

    //#endregion Scenarios
}
