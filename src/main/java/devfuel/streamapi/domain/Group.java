package devfuel.streamapi.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@ToString(of = {"id", "child"})
public class Group {
    private final int id;
    private final int parent;
    private final String name;

    private Set<Group> child = new HashSet<>();
}
