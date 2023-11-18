package BindingManagement;

import java.util.Arrays;

public class Binding {
    private final int[] binding;
    private int length;

    public Binding() {
        this.binding = new int[] {-1, -1, -1, -1};
    }

    public int[] getBinding() {
        return this.binding;
    }

    public void addKey(int key) {
        if (length < binding.length) {
            if (!containsKey(key)) {
                binding[length] = key;
                length += 1;
            }
        }
    }

    public boolean containsKey(int key) {
        for (int i : binding) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }

    public void setKey(int idx, int key) {
        if (idx < binding.length) {
            binding[idx] = key;
            if (idx == length) {
                length += 1;
            }
        }
    }

    public void removeKey(int key) {
        for (int i = 0; i < binding.length; i++) {
            if (binding[i] == key) {
                binding[i] = -1;
                for (int j = i; j < binding.length - 1; j++) {
                    binding[j] = binding[j + 1];
                }
                binding[binding.length - 1] = -1;
                length -= 1;
                return;
            }
        }
    }

    public boolean equals(Binding b) {
        for (int i = 0; i < binding.length; i++) {
            if (b.getBinding()[i] != this.getBinding()[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(binding);
    }
}
