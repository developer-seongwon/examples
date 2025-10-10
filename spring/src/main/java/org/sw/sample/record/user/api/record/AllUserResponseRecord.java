package org.sw.sample.record.user.api.record;

import org.sw.sample.node.UserNode;

import java.util.List;

public record AllUserResponseRecord(
        List<UserNode> users
) {
}
