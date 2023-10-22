package io.nanovc.memory;

import org.junit.jupiter.api.*;

import java.lang.reflect.Method;

/**
 * This defines a base class of operations that we can define scenarios with.
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
 */
@DisplayNameGeneration(OperationTests.NameGenerator.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class OperationTests
{
    /**
     * Creates the system that is being tested.
     * Subclasses will be created that represent the specific implementation that we are testing.
     * @return The system that is being tested.
     */
    public abstract SystemUnderTest createSystemUnderTest(TestInfo testInfo);

    /**
     * This is the system being tested.
     */
    protected SystemUnderTest systemUnderTest;

    @BeforeEach
    public void initSystemBeingTested(TestInfo testInfo)
    {
        this.systemUnderTest = createSystemUnderTest(testInfo);
    }

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
