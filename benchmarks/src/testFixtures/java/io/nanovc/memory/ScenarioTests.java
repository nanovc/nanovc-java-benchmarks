package io.nanovc.memory;

/**
 * The scenarios for the experimental setup represent common real-world use-cases that are made up of the operations mentioned above.
 */
public abstract class ScenarioTests extends OperationTests
{
    //#region Scenarios

    /**
     * (NC): New + Commit:
     * In this scenario, we create new content in the content area and then commit it to the repo.
     */
    public void scenario_NC()
    {
        this.systemUnderTest.newContent();
        this.systemUnderTest.commit();
    }


    /**
     * (NCMCDC): New + Commit + Modify + Commit + Delete + Commit:
     * In this scenario, we create new content, commit it then modify all of it, commit again, then delete all of it and commit a final time.
     */
    public void scenario_NCMCDC()
    {
        this.systemUnderTest.newContent();
        this.systemUnderTest.commit();
        this.systemUnderTest.modifyContent();
        this.systemUnderTest.commit();
        this.systemUnderTest.deleteContent();
        this.systemUnderTest.commit();
    }

    /**
     * (NCO): New + Commit + Checkout:
     * In this scenario, we create new content, commit it and then check it out.
     */
    public void scenario_NCO()
    {
        this.systemUnderTest.newContent();
        this.systemUnderTest.commit();
        this.systemUnderTest.checkout();
    }

    /**
     * (NCB): New + Commit + Branch:
     * In this scenario, we create new content, commit it and then create a branch.
     */
    public void scenario_NCB()
    {
        this.systemUnderTest.newContent();
        this.systemUnderTest.commit();
        this.systemUnderTest.branch();
    }
    /**
     * (NCBMC): New + Commit + Branch + Modify + Commit:
     * In this scenario, we create new content, commit it, create a branch, modify that content and then commit it again to that branch.
     */
    public void scenario_NCBMC()
    {
        this.systemUnderTest.newContent();
        this.systemUnderTest.commit();
        this.systemUnderTest.branch();
        this.systemUnderTest.modifyContent();
        this.systemUnderTest.commit();
    }
    /**
     * (NC1B1MC2G1|2>1): New + Commit1 + Branch1 + Modify + Commit2 + Merge1|2>1:
     * In this scenario, we create new content, commit it, modify that content, commit that and then merge both branches into the first branch.
     * This scenario allows for a fast-forward operation which can be cheap.
     */
    public void scenario_NC1B1MC2G1_2__1()
    {
        this.systemUnderTest.newContent();
        this.systemUnderTest.commit1();
        this.systemUnderTest.branch1();
        this.systemUnderTest.modifyContent();
        this.systemUnderTest.commit2();
        this.systemUnderTest.merge1_2__1();
    }
    /**
     * (NC1B1M1C2B2M1C3G2|3>1): New + Commit1 + Branch1 + Modify1 + Commit2 + Branch2 + Modify1 + Commit3 Merge2|3>1:
     * In this scenario, which is the most complex of the scenarios, we create new content, commit it to branch 1.
     * We then modify that content in branch 1 and commit again.
     * We then create another branch from the first commit and then make a change to that content and commit that to branch 2.
     * We then merge both branches back into the first branch.
     * This scenario cannot allow for a fast-forward operation because a merge is required since changes were made in both branches.
     */
    public void scenario_NC1B1M1C2B2M1C3G2_3__1()
    {
        this.systemUnderTest.newContent();
        this.systemUnderTest.commit1();
        this.systemUnderTest.branch1();
        this.systemUnderTest.modify1();
        this.systemUnderTest.commit2();
        this.systemUnderTest.branch2();
        this.systemUnderTest.modify1B();
        this.systemUnderTest.commit3();
        this.systemUnderTest.merge3_2__1();
    }

    //#endregion Scenarios
}
