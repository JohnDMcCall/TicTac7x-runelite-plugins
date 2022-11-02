package tictac7x.charges;

import javax.annotation.Nullable;
import javax.inject.Inject;

import net.runelite.api.*;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provides;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
@PluginDescriptor(
	name = "Item Charges Improved",
	description = "Show charges of various items",
	tags = { "charges" }
)
public class ChargesPlugin extends Plugin {
	public static final int SLOTS_INVENTORY = 28;
	public static final int SLOTS_EQUIPMENT = 14;

	@Inject
	private Client client;

	@Inject
	private ClientThread client_thread;

	@Inject
	private ItemManager items;

	@Inject
	private ConfigManager configs;

	@Inject
	private InfoBoxManager infoboxes;

	@Inject
	private ChargesConfig config;

	@Provides
	ChargesConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChargesConfig.class);
	}

	private ChargesConfigManager charges_config_manager;
	private ChargesChatManager charges_chat_manager;
	private ChargesAnimationManager charges_animation_manager;
	private ChargesWidgetManager charges_widget_manager;
	private ChargesItems charges_items;
	private ChargesInfoBox[] infoboxes_inventory, infoboxes_equipment;

	@Nullable
	public static ChargesInfoBox INFOBOX_TRIDENT_OF_THE_SEAS;

	@Override
	protected void startUp() {
		// Prepare infoboxes arrays.
		infoboxes_inventory = new ChargesInfoBox[SLOTS_INVENTORY];
		infoboxes_equipment = new ChargesInfoBox[SLOTS_EQUIPMENT];

		// Equipment infoboxes.
		infoboxes_equipment[ 0] = new ChargesInfoBox("equipment_head",   this);
		infoboxes_equipment[ 1] = new ChargesInfoBox("equipment_cape",   this);
		infoboxes_equipment[ 2] = new ChargesInfoBox("equipment_neck",   this);
		infoboxes_equipment[ 3] = new ChargesInfoBox("equipment_weapon", this);
		infoboxes_equipment[ 4] = new ChargesInfoBox("equipment_body",   this);
		infoboxes_equipment[ 5] = new ChargesInfoBox("equipment_shield", this);
		infoboxes_equipment[ 6] = new ChargesInfoBox("empty_1",          this);
		infoboxes_equipment[ 7] = new ChargesInfoBox("equipment_legs",   this);
		infoboxes_equipment[ 8] = new ChargesInfoBox("empty_2",          this);
		infoboxes_equipment[ 9] = new ChargesInfoBox("equipment_gloves", this);
		infoboxes_equipment[10] = new ChargesInfoBox("equipment_boots",  this);
		infoboxes_equipment[11] = new ChargesInfoBox("empty_3",          this);
		infoboxes_equipment[12] = new ChargesInfoBox("equipment_ring",   this);
		infoboxes_equipment[13] = new ChargesInfoBox("equipment_cape",   this);

		// Inventory infoboxes.
		for (int i = 0; i < SLOTS_INVENTORY; i++) {
			infoboxes_inventory[i] = new ChargesInfoBox("inventory_" + (i + 1), this);
		}

		// Add all infoboxes to overlay.
		for (final InfoBox infobox : infoboxes_inventory) infoboxes.addInfoBox(infobox);
		for (final InfoBox infobox : infoboxes_equipment) infoboxes.addInfoBox(infobox);

		// Create charges items manager after infoboxes have been created.
		charges_items = new ChargesItems(client, configs, items, infoboxes, config, infoboxes_inventory, infoboxes_equipment);
		charges_config_manager = new ChargesConfigManager();
		charges_chat_manager = new ChargesChatManager(configs);
		charges_animation_manager = new ChargesAnimationManager(client, configs, config);
		charges_widget_manager = new ChargesWidgetManager(client, configs);
	}

	@Override
	protected void shutDown() {
		// Remove all infoboxes from overlay.
		for (final InfoBox infobox : infoboxes_inventory) infoboxes.removeInfoBox(infobox);
		for (final InfoBox infobox : infoboxes_equipment) infoboxes.removeInfoBox(infobox);
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		if (
			event.getContainerId() == InventoryID.INVENTORY.getId() ||
			event.getContainerId() == InventoryID.EQUIPMENT.getId()
		) {
			final ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
			final ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);

			if (inventory != null && equipment != null) {
				charges_items.updateInfoboxes(inventory, equipment);
			}
		}
	}

	@Subscribe
	public void onChatMessage(final ChatMessage event) {
		charges_chat_manager.onChatMessage(event);
	}

	@Subscribe
	public void onAnimationChanged(final AnimationChanged event) {
		charges_animation_manager.onAnimationChanged(event);
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		charges_config_manager.onConfigChanged(event);
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		client_thread.invokeLater(() -> {
			charges_widget_manager.onWidgetLoaded(event);
		});
	}
}

