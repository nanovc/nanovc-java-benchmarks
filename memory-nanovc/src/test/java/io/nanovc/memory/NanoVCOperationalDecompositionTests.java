package io.nanovc.memory;

import io.nanovc.CommitTags;
import io.nanovc.areas.StringHashMapArea;
import io.nanovc.memory.strings.StringNanoRepo;

import java.util.HashMap;

/**
 * Tests the Nano VC decomposition.
 */
public  class NanoVCOperationalDecompositionTests extends OperationalDecompositionTests
{
    /**
     * The repo being tested.
     * This is initialised before each test using {@link #createRepo()}.
     */
    protected StringNanoRepo repo;

    /**
     * The content that is being operated on.
     */
    protected StringHashMapArea content;

    /**
     * The last commit that was created.
     */
    protected MemoryCommit lastCommit;

    /**
     * This creates the repo for the test.
     * You should store the repo in an instance field.
     * This is called once before each test starts.
     */
    @Override
    protected void createRepo()
    {
        repo = new StringNanoRepo();

        new HashMap<String, String>();
    }

    /**
     * New (N): New content is created in the content-area.
     */
    @Override
    protected void newContent()
    {
        // Create some content:
        content = new StringHashMapArea();

        content.putString("path","Hello World");
    }

    /**
     * Commit (CN): The content-area is committed to the repo to create a snapshot. If the commit is followed by a number N, then the number is used to label the specific commit.
     */
    @Override
    protected void commit()
    {
        // Save the commit:
        this.lastCommit = repo.commit(content, "Commit", CommitTags.none());;
    }
}
