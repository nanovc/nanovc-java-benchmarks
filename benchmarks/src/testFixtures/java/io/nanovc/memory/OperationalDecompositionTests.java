package io.nanovc.memory;

import org.junit.jupiter.api.*;

import java.lang.reflect.Method;

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
 * These tests and all subclasses, are meant to be run with the code coverage analyser so that we can see what other method calls are made to implement the scenarios.
 */
@DisplayNameGeneration(OperationalDecompositionTests.NameGenerator.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class OperationalDecompositionTests
{

    /**
     * This is used so that we can hook up the profiler in JVisualVM.
     * @throws InterruptedException
     */
    @Test
    @Order(0)
    public void waitToConnectJVisualVM() throws InterruptedException
    {
        Thread.sleep(20_000);
    }

    //#region Scenarios

    /**
     * (NC): New + Commit:
     * In this scenario, we create new content in the content area and then commit it to the repo.
     */
    @Test
    public void NC()
    {
        newContent();
        commit();
    }


    /**
     * (NCMCDC): New + Commit + Modify + Commit + Delete + Commit:
     * In this scenario, we create new content, commit it then modify all of it, commit again, then delete all of it and commit a final time.
     */
    @Test
    public void NCMCDC()
    {
        newContent();
        commit();
        modifyContent();
        commit();
        deleteContent();
        commit();
    }

    /**
     * (NCO): New + Commit + Checkout:
     * In this scenario, we create new content, commit it and then check it out.
     */
    @Test
    public void NCO()
    {
        newContent();
        commit();
        checkout();
    }

    /**
     * (NCB): New + Commit + Branch:
     * In this scenario, we create new content, commit it and then create a branch.
     */
    @Test
    public void NCB()
    {
        newContent();
        commit();
        branch();
    }
    /**
     * (NCBMC): New + Commit + Branch + Modify + Commit:
     * In this scenario, we create new content, commit it, create a branch, modify that content and then commit it again to that branch.
     */
    @Test
    public void NCBMC()
    {
        newContent();
        commit();
        branch();
        modifyContent();
        commit();
    }
    /**
     * (NC1B1MC2G1|2>1): New + Commit1 + Branch1 + Modify + Commit2 + Merge1|2>1:
     * In this scenario, we create new content, commit it, modify that content, commit that and then merge both branches into the first branch.
     * This scenario allows for a fast-forward operation which can be cheap.
     */
    @Test
    public void NC1B1MC2G1_2__1()
    {
        newContent();
        commit1();
        branch1();
        modifyContent();
        commit2();
        merge1_2__1();
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
        newContent();
        commit1();
        branch1();
        modify1();
        commit2();
        branch2();
        modify1B();
        commit3();
        merge3_2__1();
    }

    //#endregion Scenarios

    //#region Operations

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    protected abstract void createRepo();

    @BeforeEach
    public void beforeEachTest()
    {
        // Create the repo for the test:
        createRepo();
    }


    /**
     * New (N): New content is created in the content-area.
     */
    protected abstract void newContent();

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    protected abstract void modifyContent();

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    protected abstract void modify1();

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    protected abstract void modify1B();

    /**
     * Delete (D): Content is deleted in the content-area.
     */
    protected abstract void deleteContent();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    protected abstract void commit();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    protected abstract void commit1();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    protected abstract void commit2();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    protected abstract void commit3();

    /**
     * Checkout (ON): A previously committed snapshot is checked-out from the repo.
     * If the checkout is followed by a number N, then it relates to the commit CN with the corresponding number.
     */
    protected abstract void checkout();

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    protected abstract void branch();

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    protected abstract void branch1();

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    protected abstract void branch2();

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    protected abstract void merge1_2__1();

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    protected abstract void merge3_2__1();

    //#endregion Operations

    //#region Helper Methods

    public static class NameGenerator implements DisplayNameGenerator
    {

        /**
         * Generate a display name for the given top-level or {@code static} nested test class.
         *
         * @param testClass the class to generate a name for; never {@code null}
         * @return the display name for the class; never {@code null} or blank
         */
        @Override
        public String generateDisplayNameForClass(Class<?> testClass)
        {
            return testClass.getSimpleName();
        }

        /**
         * Generate a display name for the given Nested inner test class.
         *
         * @param nestedClass the class to generate a name for; never {@code null}
         * @return the display name for the nested class; never {@code null} or blank
         */
        @Override
        public String generateDisplayNameForNestedClass(Class<?> nestedClass)
        {
            return nestedClass.getSimpleName();
        }

        /**
         * Generate a display name for the given method.
         *
         * @param testClass  the class the test method is invoked on; never {@code null}
         * @param testMethod method to generate a display name for; never {@code null}
         * @return the display name for the test; never {@code null} or blank
         * @implNote The class instance supplied as {@code testClass} may differ from
         * the class returned by {@code testMethod.getDeclaringClass()} &mdash; for
         * example, when a test method is inherited from a superclass.
         */
        @Override
        public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod)
        {
            String className = testClass.getSimpleName();
            className = className.replace("Base", "");
            className = className.replace("Tests", "");
            className = className.replace("Test", "");
            return className + ": " + testMethod.getName();
        }

    }
    //#endregion Helper Methods
}
