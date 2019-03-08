- [ ] Add dependency injection
- [X] Test using in-memory state
- [X] Check task dependence
- [X] Persist state between execution to file
- [ ] Control nodes
- [ ] Composite nodes
Approach 1: Create instance of nestedWorkflow at every invocation and waitUntilCondition
and then let the nestedWorkflowInstances handle execution independently
    Pros: Handles delegation to multiple users doing the same sequence of tasks independently
    Cons: Keep track of all instances and proceed when all are done.
Approach 2: One instance of nestedWorkflow and delegate execution to current task
of nestedWorkflow instance.
    Pros: Clean delegation, no keeping track of multiple instances and when to proceed.
- [ ] Wait and notify 
- [ ] Task execution output to next task in addition to state updates
- [ ] Automated task execution, based on task type and if input is required.

