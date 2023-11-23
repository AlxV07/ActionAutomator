package com.actionautomator.ActionManagement;

import com.actionautomator.ActionManagement.SubActions.*;

import java.util.List;

public record Action(List<SubAction> subActions) {
}
