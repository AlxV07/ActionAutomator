package ActionManagement.ActionSubTasks;

public record WaitSubTask(long waitTime) implements ActionSubTask {

    @Override
    public int getTypeOfTask() {
        return 2;
    }
}
