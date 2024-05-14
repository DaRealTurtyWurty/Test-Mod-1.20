package dev.turtywurty.testmod.cauldron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CauldronItemEnterRegistry {
    private static final Map<CauldronType, List<ItemEnterCallback>> ITEM_ENTER_CALLBACKS = new HashMap<>();

    public static void registerCallback(CauldronType type, ItemEnterCallback callback) {
        ITEM_ENTER_CALLBACKS.computeIfAbsent(type, t -> new ArrayList<>()).add(callback);
    }

    public static List<ItemEnterCallback> getCallbacks(CauldronType type) {
        return ITEM_ENTER_CALLBACKS.computeIfAbsent(type, t -> new ArrayList<>());
    }
}
