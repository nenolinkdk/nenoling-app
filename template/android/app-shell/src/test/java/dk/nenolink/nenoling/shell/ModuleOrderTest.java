package dk.nenolink.nenoling.shell;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dk.nenolink.nenoling.content.ContentModels.Module;
import dk.nenolink.nenoling.content.ContentModels.TextPair;

public class ModuleOrderTest {
    @Test
    public void grammarIsLastAndLevelsKeepNumericOrder() {
        Module grammar = module("g", "grammar", null);
        Module level3 = module("l3", "level", 3);
        Module children = module("c", "children", null);
        Module level1 = module("l1", "level", 1);
        List<Module> result = ModuleOrder.ordered(Arrays.asList(grammar, level3, children, level1));
        assertEquals("l1", result.get(0).id);
        assertEquals("l3", result.get(1).id);
        assertEquals("c", result.get(2).id);
        assertEquals("g", result.get(3).id);
    }

    private Module module(String id, String type, Integer level) {
        return new Module(id, type, level, "all", new TextPair(id, id),
                Collections.emptyList(), Collections.emptyList());
    }
}
