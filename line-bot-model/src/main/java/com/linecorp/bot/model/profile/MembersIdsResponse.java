package com.linecorp.bot.model.profile;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * @see <a href="https://devdocs.line.me?java#get-group-room-member-ids">//devdocs.line.me#get-group-room-member-ids</a>
 */
@Value
public class MembersIdsResponse {
    /**
     * List of user IDs of the members in the group or room.
     *
     * <p>Max: 100 user IDs
     */
    List<String> memberIds;

    String next;

    @JsonCreator
    public MembersIdsResponse(
            @JsonProperty("memberIds") final List<String> memberIds,
            @JsonProperty("next") final String next) {
        this.memberIds = requireNonNull(memberIds, "memberIds is null");
        this.next = next;
    }

    /**
     * Parameter to get continue member ids in next API call. a.k.a continuationToken
     *
     * <p>Returned if and only if there are more user IDs remaining.
     */
    public Optional<String> getNext() {
        return Optional.ofNullable(next);
    }
}
