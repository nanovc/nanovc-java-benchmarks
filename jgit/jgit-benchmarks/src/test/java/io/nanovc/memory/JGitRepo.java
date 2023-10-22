package io.nanovc.memory;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests the jgit decomposition.
 */
public class JGitRepo extends SystemUnderTest
{

    /**
     * Gets the name of the system being tested.
     *
     * @return The name of the system being tested.
     */
    @Override
    public String getSystemName()
    {
        return "JGit Repo";
    }

    /**
     * The path where we can work for the unit test that is running.
     */
    public Path testPath;

    /**
     * The repo path where files can be written.
     * This is valid after {@link #createRepo()} is called.
     */
    public Path repoPath;

    /**
     * The last commit that was created.
     */
    public RevCommit lastCommit;

    /**
     * This holds the reference to the last check out.
     */
    public Ref lastCheckout;

    /**
     * The name of the last branch that was created.
     */
    public String lastBranchName;

    /**
     * The name of branch 1.
     */
    public String branch1Name;

    /**
     * The name of branch 2.
     */
    public String branch2Name;

    /**
     * Commit labelled 1.
     */
    public RevCommit commit1;

    /**
     * Commit labelled 2.
     */
    public RevCommit commit2;

    /**
     * Commit labelled 3.
     */
    public RevCommit commit3;

    /**
     * The git repo that we are creating.
     */
    public Git git;

    /**
     * This is the next instance counter for creating the repo.
     */
    protected AtomicInteger nextRepoInstance = new AtomicInteger(0);

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    @Override
    public void createRepo()
    {
        // Get the next instance path for the repo:
        int nextInstance = this.nextRepoInstance.incrementAndGet();

        // Get the path for the next instance:
        this.repoPath = this.testPath.resolve(Integer.toString(nextInstance));

        try
        {
            git = Git.init().setInitialBranch("main").setDirectory(this.repoPath.toFile()).call();
        }
        catch (GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void closeGit()
    {
        if (git != null) git.close();
    }

    /**
     * New (N): New content is created in the content-area.
     */
    @Override
    public void newContent()
    {
        try
        {
            Files.writeString(repoPath.resolve("path.txt"), "Hello World", StandardOpenOption.CREATE_NEW);
            DirCache index1 = git.add().addFilepattern("path.txt").call();
        }
        catch (IOException | GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    public void modifyContent()
    {
        try
        {
            Files.writeString(repoPath.resolve("path.txt"), "Hello Again World", StandardOpenOption.WRITE);
            DirCache index1 = git.add().addFilepattern("path.txt").call();
        }
        catch (IOException | GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    public void modify1()
    {
        try
        {
            // Checkout the content for commit 1:
            git.checkout().setName(this.lastBranchName).setStartPoint(this.commit1).call();
            Files.writeString(repoPath.resolve("path.txt"), "Hello Again World and Again", StandardOpenOption.WRITE);
            DirCache index1 = git.add().addFilepattern("path.txt").call();
        }
        catch (IOException | GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    public void modify1B()
    {
        try
        {
            // Checkout the content for commit 1:
            git.checkout().setName(this.lastBranchName).setStartPoint(this.commit1).call();
            Files.writeString(repoPath.resolve("path.txt"), "Hello Again World and Again and Again", StandardOpenOption.WRITE);
            DirCache index1 = git.add().addFilepattern("path.txt").call();
        }
        catch (IOException | GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete (D): Content is deleted in the content-area.
     */
    @Override
    public void deleteContent()
    {
        try
        {
            Files.deleteIfExists(this.repoPath.resolve("path.txt"));
            DirCache index1 = git.add().addFilepattern("path.txt").call();
        }
        catch (IOException | GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    public void commit()
    {
        try
        {
            // Check whether we have a branch name already:
            if (this.lastBranchName != null)
            {
                // We have a branch name already.

                // Get the current branch name that is checked out:
                String currentBranchName = git.getRepository().getBranch();

                // Check which branch we are on:
                if (!this.lastBranchName.equals(currentBranchName))
                {
                    // We are not on the branch that we need to be on.

                    // Stash the content:
                    git.stashCreate()
                        .setIncludeUntracked(true)
                            .call();

                    // Switch to the branch:
                    git.checkout()
                        .setName(this.lastBranchName)
                            .call();

                    // Un-stash the content:
                    git.stashApply()
                        .call();
                }
            }

            // Save the commit:
            this.lastCommit = git.commit().setAll(true).setMessage("Commit").call();
        }
        catch (IOException | GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    public void commit1()
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
    public void commit2()
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
    public void commit3()
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
    public void checkout()
    {
        try
        {
            // Create the checkout command:
            CheckoutCommand checkoutCommand = git.checkout();

            // Make sure we have a branch name:
            if (this.lastBranchName == null) this.lastBranchName = "main";

            // Set the name of the branch we want to check out:
            checkoutCommand.setName(this.lastBranchName);

            // Set the last commit as the one we want to checkout:
            checkoutCommand.setStartPoint(this.lastCommit);

            // Perform the checkout:
            this.lastCheckout = checkoutCommand.call();
        }
        catch (GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    public void branch()
    {
        this.lastBranchName = "Branch";
        try
        {
            git.branchCreate().setName(this.lastBranchName).call();
        }
        catch (GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    public void branch1()
    {
        this.branch1Name = "Branch1";
        this.lastBranchName = this.branch1Name;
        try
        {
            git.branchCreate().setName(this.lastBranchName).setStartPoint(this.commit1).call();
        }
        catch (GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    public void branch2()
    {
        this.branch2Name = "Branch2";
        this.lastBranchName = this.branch2Name;
        try
        {
            git.branchCreate().setName(this.lastBranchName).setStartPoint(this.commit2).call();
        }
        catch (GitAPIException e)
        {
            throw new RuntimeException(e);
        }
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
        try
        {
            // Make sure we are on the branch we want to be in:
            if (!this.lastBranchName.equals("Branch1"))
            {
                // We are not on the branch that we want to be in.

                // Checkout the destination branch:
                git.checkout().setName("Branch1").call();
            }
            // Now we are on the branch that we want to be in.

            // Merge the changes into this branch for commit 1:
            MergeResult mergeResult1 = this.git.merge().include(this.commit1).setMessage("Merge Commit 1 into Branch 1").call();

            // Merge the changes into this branch for commit 2:
            MergeResult mergeResult2 = this.git.merge().include(this.commit2).setMessage("Merge Commit 2 into Branch 1").call();
        }
        catch (GitAPIException e)
        {
            throw new RuntimeException(e);
        }
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
        try
        {
            // Make sure we are on the branch we want to be in:
            if (!this.lastBranchName.equals("Branch1"))
            {
                // We are not on the branch that we want to be in.

                // Checkout the destination branch:
                git.checkout().setName("Branch1").call();
            }
            // Now we are on the branch that we want to be in.

            // Merge the changes into this branch for commit 3:
            MergeResult mergeResult3 = this.git.merge().include(this.commit3).setMessage("Merge Commit 3 into Branch 1").call();

            // Merge the changes into this branch for commit 2:
            MergeResult mergeResult2 = this.git.merge().include(this.commit2).setMessage("Merge Commit 2 into Branch 1").call();
        }
        catch (GitAPIException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    @Override
    public void freeRepo()
    {
        if (git != null) git.close();

        this.git = null;
        this.commit1 = null;
        this.commit2 = null;
        this.commit3 = null;
        this.branch1Name = null;
        this.branch2Name = null;
        this.lastCheckout = null;
        this.lastCommit = null;
        this.lastBranchName = null;
    }
}
