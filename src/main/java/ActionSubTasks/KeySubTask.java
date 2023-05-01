package ActionSubTasks;

public record KeySubTask(KeySubTaskType t, char c) implements ActionSubTask {
    @Override
    public boolean isKeyboardTask() {
        return true;
    }
}
