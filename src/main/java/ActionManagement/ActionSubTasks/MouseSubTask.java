package ActionManagement.ActionSubTasks;

public record MouseSubTask(MouseSubTaskType t, int button, int x, int y) implements ActionSubTask{

    @Override
    public boolean isKeyboardTask() {
        return false;
    }
}
