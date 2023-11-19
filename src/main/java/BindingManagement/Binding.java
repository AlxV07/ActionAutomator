package BindingManagement;

import java.util.Arrays;

public class Binding {
    private String name;
    private String code;
    private final int[] keySequence;
    private int nofKeys;

    public Binding(String name) {
        this.name = name;
        this.keySequence = new int[] {-1, -1, -1, -1};
        this.nofKeys = 0;
    }

    /**
     * @return the name of the binding
     */
    public String getName() {
        return this.name;
    }

    /**
     * Rename the binding
     * @param name A new name for the binding
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The action code of the binding
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the action code of the binding
     * @param code New action code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return The key sequence of the binding
     */
    public int[] getKeySequence() {
        return this.keySequence;
    }

    public int getNofKeys() {
        return this.nofKeys;
    }

    /**
     *
     * @param tarKey The key to check for (NativeKeyEvent VC)
     * @return If {@code tarKey} is contained in the key sequence of the binding
     */
    private boolean containsKey(int tarKey) {
        for (int canKey : keySequence) {
            if (canKey == tarKey) {
                return true;
            }
            if (canKey == -1) {
                break;
            }
        }
        return false;
    }

    /**
     * Set a key in the key sequence of the binding, if the key is not already contained in the key sequence
     * @param idx The index to set {@code key} in the key sequence of the binding
     * @param key The key to set in the key sequence of the binding (NativeKeyEvent VC)
     */
    public void setKey(int idx, int key) {
        if (!containsKey(key)) {
            keySequence[idx] = key;
            if (idx == nofKeys) {
                nofKeys += 1;
            }
        }
    }

    /**
     * Add a new key to the end of the key sequence of the binding
     * @param key The new key to be added (NativeKeyEvent VC)
     */
    public void addKey(int key) {
        this.setKey(nofKeys, key);
    }

    /**
     * Removes the given key from the key sequence and shifts all following keys to fill
     * @param key The key to remove from the key sequence (NativeKeyEvent VC)
     */
    public void removeKey(int key) {
        if (key == -1) {  // Should only be called on initial BindingPanel constructor bind
            return;
        }
        for (int i = 0; i < keySequence.length; i++) {
            if (keySequence[i] == key) {
                keySequence[i] = -1;
                for (int j = i; j < keySequence.length - 1; j++) {
                    keySequence[j] = keySequence[j + 1];
                }
                keySequence[keySequence.length - 1] = -1;
                nofKeys -= 1;
                return;
            }
        }
    }

    /**
     * Check if the key sequences of two bindings are equal
     * @param tarBinding The binding to compare key sequences with
     * @return If the key sequences of the binding and {@code tarBinding} are equal
     */
    public boolean keysEqual(Binding tarBinding) {
        return Arrays.equals(keySequence, tarBinding.keySequence);
    }

    @Override
    public String toString() {
        return "(" + this.name + " " + Arrays.toString(keySequence) + ")";
    }
}
