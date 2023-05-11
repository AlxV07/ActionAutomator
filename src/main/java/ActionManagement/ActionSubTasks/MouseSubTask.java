package ActionManagement.ActionSubTasks;

public record MouseSubTask(MouseSubTaskType specType, int button, int x, int y) implements ActionSubTask{
    @Override
    public int getTypeOfTask() {
        return 1;
    }
}
