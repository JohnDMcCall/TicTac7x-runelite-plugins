package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesItem;
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerWidget;

public class J_Camulet extends ChargedItemInfoBox {
    public J_Camulet(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.CAMULET, ItemID.CAMULET, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, plugin);
        this.config_key = ChargesImprovedConfig.camulet;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.CAMULET),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your Camulet has (?<charges>.+) charges? left."),
            new TriggerChatMessage("You recharge the Camulet using camel dung. Yuck!").fixedCharges(4),
            new TriggerChatMessage("The Camulet is already fully charged.").fixedCharges(4),
            new TriggerChatMessage("The Camulet has unlimited charges.").fixedCharges(-2),
        };
    }
}
