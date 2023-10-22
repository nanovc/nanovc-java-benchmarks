package io.nanovc.memory;

public class NOPSystemUnderTest extends SystemUnderTest
{
    /**
     * Gets the name of the system being tested.
     *
     * @return The name of the system being tested.
     */
    @Override
    public String getSystemName()
    {
        return "No Operation";
    }

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    @Override
    public void createRepo()
    {

    }

    /**
     * New (N): New content is created in the content-area.
     */
    @Override
    public void newContent()
    {

    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    public void modifyContent()
    {

    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    public void modify1()
    {

    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    public void modify1B()
    {

    }

    /**
     * Delete (D): Content is deleted in the content-area.
     */
    @Override
    public void deleteContent()
    {

    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    public void commit()
    {

    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    public void commit1()
    {

    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    public void commit2()
    {

    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    public void commit3()
    {

    }

    /**
     * Checkout (ON): A previously committed snapshot is checked-out from the repo.
     * If the checkout is followed by a number N, then it relates to the commit CN with the corresponding number.
     */
    @Override
    public void checkout()
    {

    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    public void branch()
    {

    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    public void branch1()
    {

    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    public void branch2()
    {

    }

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    @Override
    public void merge1_2__1()
    {

    }

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    @Override
    public void merge3_2__1()
    {

    }

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    @Override
    public void freeRepo()
    {

    }
}
