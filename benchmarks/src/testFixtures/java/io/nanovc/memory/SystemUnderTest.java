package io.nanovc.memory;

/**
 * This defines a base class for a system that we are testing.
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
public abstract class SystemUnderTest
{
    /**
     * Gets the name of the system being tested.
     * @return The name of the system being tested.
     */
    public abstract String getSystemName();

    /**
     * This contains all the operations that exist.
     * This can be used for warming up the instance.
     */
    public void allOperations()
    {
        createRepo();
        newContent();
        modifyContent();
        deleteContent();
        commit();
        branch();
        checkout();
        commit1();
        commit2();
        commit3();
        branch1();
        branch2();
        modify1();
        modify1B();
        merge1_2__1();
        merge3_2__1();
        freeRepo();
    }

    //#region Operations

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    public abstract void createRepo();

    /**
     * New (N): New content is created in the content-area.
     */
    public abstract void newContent();

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    public abstract void modifyContent();

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    public abstract void modify1();

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    public abstract void modify1B();

    /**
     * Delete (D): Content is deleted in the content-area.
     */
    public abstract void deleteContent();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    public abstract void commit();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    public abstract void commit1();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    public abstract void commit2();

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    public abstract void commit3();

    /**
     * Checkout (ON): A previously committed snapshot is checked-out from the repo.
     * If the checkout is followed by a number N, then it relates to the commit CN with the corresponding number.
     */
    public abstract void checkout();

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    public abstract void branch();

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    public abstract void branch1();

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    public abstract void branch2();

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    public abstract void merge1_2__1();

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    public abstract void merge3_2__1();

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    public abstract void freeRepo();

    //#endregion Operations
}
