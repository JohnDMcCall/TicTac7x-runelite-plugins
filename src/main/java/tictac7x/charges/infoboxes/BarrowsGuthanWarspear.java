package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class BarrowsGuthanWarspear extends ChargedItemInfoBox {
    public BarrowsGuthanWarspear(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.GUTHANS_WARSPEAR, client, client_thread, configs, items, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.GUTHANS_WARSPEAR, 100),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_100, 100),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_75, 75),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_50, 50),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_25, 25),
            new TriggerItem(ItemID.GUTHANS_WARSPEAR_0, 0)
        };
    }
}