package io.nanovc.memory;

import io.git.nanovc.*;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Tests the Git Inspiration decomposition.
 */
public class GitInspirationOperationalDecompositionTests extends OperationalDecompositionTests
{

    /**
     * The handler to use for the nano repo.
     */
    protected RepoHandler repoHandler;

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
    protected Commit lastCommit;

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
    protected Commit commit1;

    /**
     * Commit labelled 2.
     */
    protected Commit commit2;

    /**
     * Commit labelled 3.
     */
    protected Commit commit3;


    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    @Override
    protected void createRepo()
    {
        // Create a new repo and only use our nano interface for simplicity:
        this.repoHandler = NanoVersionControl.newHandler();
        this.nano = repoHandler.asNanoCommands();
        this.repo = nano.init();
        repoHandler.setAuthorAndCommitter("Luke Machowski");
        //repoHandler.setNowOverride(ZonedDateTime.of(2023, 6, 19, 12, 34, 56, 789, ZoneId.of("Z")));
    }

    /**
     * New (N): New content is created in the content-area.
     */
    @Override
    protected void newContent()
    {
        // Create some content:
        nano.putWorkingAreaContent("/path","Hello World".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    protected void modifyContent()
    {
        nano.putWorkingAreaContent("/path","Hello Again World".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    protected void modify1()
    {
        // Get the content from commit 1:
        nano.checkout(this.commit1.hash.value);

        // Modify the content:
        nano.putWorkingAreaContent("/path","Hello Again World and Again".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Modify (MN): Content is modified in the content-area.
     * If the modification is followed by a number N, then it represents a modification to the content-area that was committed in commit CN (see below).
     */
    @Override
    protected void modify1B()
    {
        // Get the content from commit 1:
        nano.checkout(this.commit1.hash.value);

        // Modify the content:
        nano.putWorkingAreaContent("/path","Hello Again World and Again and Again".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Delete (D): Content is deleted in the content-area.
     */
    @Override
    protected void deleteContent()
    {
        repo.workingArea.removeContent("/path");
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot.
     * If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    protected void commit()
    {
        // Check whether we have a branch name already:
        if (this.lastBranchName != null)
        {
            // We have a branch name already.

            // Check which branch we are on:
            if (!this.lastBranchName.equals(nano.getCurrentBranchName()))
            {
                // We are not on the branch that we need to be on.

                // Save the current working area:
                ContentMap<MutableContent> contentMapSnapshot = repo.workingArea.getContentMapSnapshot();

                // Switch to the branch:
                repoHandler.checkout(this.lastBranchName);

                // Clear the working area:
                repo.workingArea.clear();

                // Apply the content again:
                for (Map.Entry<String, MutableContent> entry : contentMapSnapshot.entrySet())
                {
                    nano.putWorkingAreaContent(entry.getKey(), entry.getValue().content);
                }
            }
        }

        // Stage all the content:
        repo.stagingArea.clear();
        nano.addAll(true);

        // Save the commit:
        this.lastCommit = nano.commitAll("Commit", true);
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
        nano.checkout(this.lastCommit.hash.value);
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    protected void branch()
    {
        this.lastBranchName = "Branch";
        nano.branch(this.lastBranchName);
        nano.checkout(this.lastBranchName);
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
        nano.branch(this.lastBranchName);
        nano.checkout(this.lastBranchName);
    }

    /**
     * Branch (BN): A branch is created in the repo.
     * If the branch is followed by a number N, then the number is used to label the specific branch.
     */
    @Override
    protected void branch2()
    {
        this.branch2Name = "Branch2";
        this.lastBranchName = this.branch2Name;
        nano.branch(this.lastBranchName);
        nano.checkout(this.lastBranchName);
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
        this.lastCommit = mergeCommitsIntoBranch(this.commit1, this.commit2, this.branch1Name, "Merge Commit 1 + Commit 2 into Branch 1");
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
        this.lastCommit = mergeCommitsIntoBranch(this.commit3, this.commit2, this.branch1Name, "Merge Commit 3 + Commit 2 into Branch 1");
    }

    /**
     * Merges the two given commits into the given branch.
     * @param commit1 The first commit to merge.
     * @param commit2 The second commit to merge.
     * @param branchName The name of the branch to merge into.
     * @param message    The commit message to use.
     * @return The commit that was created for the merge.
     */
    protected Commit mergeCommitsIntoBranch(Commit commit1, Commit commit2, String branchName, String message)
    {
        // Get the common ancestor for commits 1 and 2:
        Commit commonAncestor = findCommonAncestor(commit1, commit2);

        // Get the content area for the common ancestor:
        nano.checkout(commonAncestor.hash.value);

        // Create a snapshot of the content area:
        ContentMap<MutableContent> commonContentArea = repo.workingArea.getContentMapSnapshot();

        // Get the content for the first commit:
        nano.checkout(commit1.hash.value);

        // Create a snapshot of the content area:
        ContentMap<MutableContent> commit1ContentArea = repo.workingArea.getContentMapSnapshot();

        // Get the content for the second commit:
        nano.checkout(commit2.hash.value);

        // Create a snapshot of the content area:
        ContentMap<MutableContent> commit2ContentArea = repo.workingArea.getContentMapSnapshot();

        // Work out the diffs between the common ancestor and commit 1:
        Map<String, Diff> commit1Diffs = computeDiffs(commonContentArea, commit1ContentArea);
        Map<String, Diff> commit2Diffs = computeDiffs(commonContentArea, commit2ContentArea);

        // Checkout the destination branch:
        nano.checkout(branchName);

        // Apply the diffs from commit 1:
        applyDiffs(commit1ContentArea, commit1Diffs, repo.workingArea);

        // Apply the diffs from commit 2:
        applyDiffs(commit2ContentArea, commit2Diffs, repo.workingArea);

        // Stage the changes:
        nano.addAll(true);

        // Commit the changes:
        return nano.commitAll(message, true);
    }

    /**
     * Applies the  diffs to the content area.
     * @param content The content to apply.
     * @param diffs The diffs to apply.
     * @param destinationContentArea The destination content area to apply the diffs to.
     */
    protected void applyDiffs(ContentMap<MutableContent> content, Map<String, Diff> diffs, MutableContentArea destinationContentArea)
    {
        // Apply the diffs:
        for (Map.Entry<String, Diff> diffEntry : diffs.entrySet())
        {
            // Get the path:
            String path = diffEntry.getKey();

            // Apply the change:
            switch (diffEntry.getValue())
            {
                case Added:
                case Changed:
                    destinationContentArea.putContent(path, content.getContent(path).content);
                    break;
                case Deleted:
                    destinationContentArea.removeContent(path);
                    break;
            }
        }
    }

    /**
     * Captures the type of difference between content areas.
     */
    protected enum Diff
    {
        Added,
        Changed,
        Deleted,
    }

    /**
     * Computes the difference between the two given content areas.
     * @param from The content area that we are computing from.
     * @param to The content area that we are computing to.
     * @return The differences between the two content areas. The key is the path, the value is the type of difference.
     */
    protected Map<String, Diff> computeDiffs(ContentMap<MutableContent> from, ContentMap<MutableContent> to)
    {
        // Create the result:
        LinkedHashMap<String, Diff> differences = new LinkedHashMap<>();

        // Go through the content area we are starting from:
        for (Map.Entry<String, MutableContent> fromEntry : from.entrySet())
        {
            // Get the path:
            String path = fromEntry.getKey();

            // Check whether the destination content area has this path:
            if (!to.containsKey(path))
            {
                // The destination content area doesn't have this path.
                // Flag it as being deleted:
                differences.put(path, Diff.Deleted);
            }
            else
            {
                // The destination has this path.

                // Get the content for both paths:
                MutableContent fromContent = fromEntry.getValue();
                MutableContent toContent = to.getContent(path);

                // Check whether they are the same:
                if (!Arrays.equals(fromContent.content, toContent.content))
                {
                    // The content is different.

                    // Flag that the content has changed:
                    differences.put(path, Diff.Changed);
                }
            }
        }

        // Go through the content area we are ending with:
        for (Map.Entry<String, MutableContent> toEntry : to.entrySet())
        {
            // Get the path:
            String path = toEntry.getKey();

            // Check whether the source content area has this path:
            if (!from.containsKey(path))
            {
                // The destination content area has more content than the source.
                // Flag it as being added:
                differences.put(path, Diff.Added);
            }
        }

        return differences;
    }


    /**
     * Finds the common ancestor for both commits.
     * @param commit1 The first commit to search through.
     * @param commit2 The second commit to search through.
     * @return The common ancestor commit for both commits or null if there is none.
     */
    protected Commit findCommonAncestor(Commit commit1, Commit commit2)
    {
        // Get the ancestors for each of the commits:
        Set<String> allAncestorHashesForCommit1 = getAllAncestorHashes(commit1);
        Set<String> allAncestorHashesForCommit2 = getAllAncestorHashes(commit2);

        // Walk the first set and find the first common ancestor:
        for (String hash : allAncestorHashesForCommit1)
        {
            // Check if the other commit has that ancestor:
            if (allAncestorHashesForCommit2.contains(hash))
            {
                // We have a common hash.

                // Get the commit:
                return repoHandler.resolveCommit(hash);
            }
        }

        // If we get here then there is no common ancestor:
        return null;
    }

    /**
     * Gets all the ancestors of the given commit by walking the references.
     * @param commit The commit to get all the ancestors of.
     * @return The set of ancestors for the given commit.
     */
    protected Set<String> getAllAncestorHashes(Commit commit)
    {
        // Create the set for the result:
        Set<String> result = new LinkedHashSet<>();

        // Walk the commits recursively:
        getAllAncestorHashesRecursively(commit, result);

        return result;
    }

    /**
     * Gets all the ancestor hashes by walking the commits recursively.
     * @param commit The current commit to walk.
     * @param setToAddTo The set of commit hashes to add to as we walk.
     */
    private void getAllAncestorHashesRecursively(Commit commit, Set<String> setToAddTo)
    {
        // Make sure that we have a commit to walk:
        if (commit == null) return;

        // Add this commit to our set:
        setToAddTo.add(commit.hash.value);

        // Go through each parent hash for the current commit:
        if (commit.parentCommitHashValues != null)
        {
            for (String parentCommitHashValue : commit.parentCommitHashValues)
            {
                // Check whether we have already walked this commit:
                if (!setToAddTo.contains(parentCommitHashValue))
                {
                    // We have not walked this commit yet.

                    // Get the parent commit:
                    Commit parentCommit = repoHandler.resolveCommit(parentCommitHashValue);

                    // Walk the commit recursively:
                    getAllAncestorHashesRecursively(parentCommit, setToAddTo);
                }
            }
        }


    }

}
