package io.nanovc.memory;

import io.git.nanovc.NanoCommands;
import io.git.nanovc.NanoVersionControl;
import io.git.nanovc.Repo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Tests the Git Inspiration decomposition.
 */
public class GitInspirationOperationalDecompositionTests extends OperationalDecompositionTests
{

    /**
     * The handler to interface with NanoVC.
     */
    protected NanoCommands nano;

    /**
     * The repo being tested.
     * This is initialised before each test using {@link #createRepo()}.
     */
    protected Repo repo;

    /**
     * The last commit that was created.
     */
    protected MemoryCommit lastCommit;

    /**
     * This holds the last content area that was checked out.
     */
    protected StringHashMapArea lastCheckout;

    /**
     * The name of the last branch that was created.
     */
    protected String lastBranchName;

    /**
     * The name of branch 1.
     */
    protected String branch1Name;

    /**
     * The name of branch 2.
     */
    protected String branch2Name;

    /**
     * Commit labelled 1.
     */
    protected MemoryCommit commit1;

    /**
     * Commit labelled 2.
     */
    protected MemoryCommit commit2;

    /**
     * Commit labelled 3.
     */
    protected MemoryCommit commit3;

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    @Override
    protected void createRepo()
    {
        // Create a new repo and only use our nano interface for simplicity:
        this.nano = NanoVersionControl.newHandler().asNanoCommands();

        this.repo = nano.init();
    }

    /**
     * New (N): New content is created in the content-area.
     */
    @Override
    protected void newContent()
    {
        // Create some content:
        nano.putWorkingAreaContent("path","Hello World".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    protected void modifyContent()
    {
        nano.putWorkingAreaContent("path","Hello Again World".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    protected void modify1()
    {
        // Get the content from commit 1:
        repo.checkoutIntoArea(this.commit1, this.content);

        // Modify the content:
        content.putString("path","Hello Again World and Again");
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    protected void modify1B()
    {
        // Get the content from commit 1:
        repo.checkoutIntoArea(this.commit1, this.content);

        // Modify the content:
        content.putString("path","Hello Again World and Again and Again");
    }

    /**
     * Delete (D): Content is deleted in the content-area.
     */
    @Override
    protected void deleteContent()
    {
        content.removeContent("path");
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    protected void commit()
    {
        // Check whether we have a branch name already:
        if (this.lastBranchName == null)
        {
            // We don't have a branch name.
            // Save the commit:
            this.lastCommit = repo.commit(content, "Commit", CommitTags.none());
        }
        else
        {
            // We have a branch name already.
            // Save the commit:
            this.lastCommit = repo.commitToBranch(content, this.lastBranchName, "Commit", CommitTags.none());
        }
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    protected void commit1()
    {
        // Perform a commit:
        commit();

        // Save the reference to this commit:
        this.commit1 = this.lastCommit;
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    protected void commit2()
    {
        // Perform a commit:
        commit();

        // Save the reference to this commit:
        this.commit2 = this.lastCommit;
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    protected void commit3()
    {
        // Perform a commit:
        commit();

        // Save the reference to this commit:
        this.commit3 = this.lastCommit;
    }

    /**
     * Checkout (ON): A previously committed snapshot is checked-out from the repo.
     * If the checkout is followed by a number N, then it relates to the commit CN with the corresponding number.
     */
    @Override
    protected void checkout()
    {
        this.lastCheckout = repo.checkout(this.lastCommit);
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    protected void branch()
    {
        this.lastBranchName = "Branch";
        repo.createBranchAtCommit(this.lastCommit, this.lastBranchName);
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    protected void branch1()
    {
        this.branch1Name = "Branch1";
        this.lastBranchName = this.branch1Name;
        repo.createBranchAtCommit(this.lastCommit, this.branch1Name);
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    protected void branch2()
    {
        this.branch2Name = "Branch2";
        this.lastBranchName = this.branch1Name;
        repo.createBranchAtCommit(this.lastCommit, this.branch2Name);
    }

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    @Override
    protected void merge1_2__1()
    {
        this.lastCommit = repo.mergeIntoBranchFromCommit(this.branch1Name, this.commit1, "Merge Commit 1 + Commit 2 into Branch 1", CommitTags.none());
    }

    /**
     * Merge (GX|Y>Z): Two or more commits are merged into one branch.
     * If the merge is followed by symbols, then the values separated by pipes represent the corresponding commits.
     * The destination branch is preceded by an angle bracket >.
     * Therefore GX|Y>Z means that commit CX and CY was merged into branch BZ.
     */
    @Override
    protected void merge3_2__1()
    {
        this.lastCommit = repo.mergeCommits(this.commit3, this.commit2, "Merge Commit 3 + Commit 2", CommitTags.none());
        this.lastCommit = repo.mergeIntoBranchFromCommit(this.branch1Name, this.lastCommit, "Merge Commit 3 + Commit 2 into Branch 1", CommitTags.none());
    }
}
