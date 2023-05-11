package ActionManagement.ActionSubTasks;

public record KeySubTask(KeySubTaskType specType, int k) implements ActionSubTask {
    @Override
    public int getTypeOfTask() {
        return 0;
    }
}
