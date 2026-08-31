package dk.nenolink.nenoling.shell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dk.nenolink.nenoling.content.ContentModels.Module;

/** Standard Nenoling home ordering while still accepting custom module types. */
public final class ModuleOrder {
    private ModuleOrder() {}

    public static List<Module> ordered(List<Module> modules) {
        List<Module> result = new ArrayList<>(modules);
        result.sort(Comparator.comparingInt(ModuleOrder::homeOrder));
        return result;
    }

    static int homeOrder(Module module) {
        if ("level".equals(module.type)) return module.level == null ? 50 : module.level;
        if ("children".equals(module.type)) return 80;
        if ("grammar".equals(module.type)) return 100;
        return 90;
    }
}
