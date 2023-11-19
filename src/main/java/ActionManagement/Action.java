package ActionManagement;

import ActionManagement.SubActions.*;

import java.util.List;

public record Action(List<SubAction> subActions) {
}
