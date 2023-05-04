package ActionSubTasks;

public record KeySubTask(KeySubTaskType t, int k) implements ActionSubTask {
    @Override
    public boolean isKeyboardTask() {
        return true;
    }
}
