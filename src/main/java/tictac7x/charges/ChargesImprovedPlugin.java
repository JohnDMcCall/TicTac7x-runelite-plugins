package tictac7x.charges;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ActorSpotAnim;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemInfobox;
import tictac7x.charges.item.ChargedItemOverlay;
import tictac7x.charges.items.A_CrystalBody;
import tictac7x.charges.items.A_CrystalHelm;
import tictac7x.charges.items.A_CrystalLegs;
import tictac7x.charges.items.B_FremennikSeaBoots;
import tictac7x.charges.items.C_ArdougneCloak;
import tictac7x.charges.items.C_Coffin;
import tictac7x.charges.items.C_ForestryKit;
import tictac7x.charges.items.C_MagicCape;
import tictac7x.charges.items.H_CircletOfWater;
import tictac7x.charges.items.H_KandarinHeadgear;
import tictac7x.charges.items.J_BraceletOfClay;
import tictac7x.charges.items.J_BraceletOfExpeditious;
import tictac7x.charges.items.J_BraceletOfFlamtaer;
import tictac7x.charges.items.J_BraceletOfSlaughter;
import tictac7x.charges.items.J_Camulet;
import tictac7x.charges.items.J_DesertAmulet;
import tictac7x.charges.items.J_EscapeCrystal;
import tictac7x.charges.items.J_NecklaceOfDodgy;
import tictac7x.charges.items.J_NecklaceOfPassage;
import tictac7x.charges.items.J_NecklaceOfPhoenix;
import tictac7x.charges.items.J_RingOfCelestial;
import tictac7x.charges.items.J_RingOfRecoil;
import tictac7x.charges.items.J_RingOfShadows;
import tictac7x.charges.items.J_RingOfSlayer;
import tictac7x.charges.items.J_RingOfSuffering;
import tictac7x.charges.items.J_XericsTalisman;
import tictac7x.charges.items.S_Chronicle;
import tictac7x.charges.items.S_CrystalShield;
import tictac7x.charges.items.S_DragonfireShield;
import tictac7x.charges.items.S_FaladorShield;
import tictac7x.charges.items.S_KharedstMemoirs;
import tictac7x.charges.items.S_TomeOfFire;
import tictac7x.charges.items.U_AshSanctifier;
import tictac7x.charges.items.U_BoneCrusher;
import tictac7x.charges.items.U_BottomlessCompostBucket;
import tictac7x.charges.items.U_CoalBag;
import tictac7x.charges.items.U_FishBarrel;
import tictac7x.charges.items.U_FungicideSpray;
import tictac7x.charges.items.U_GemBag;
import tictac7x.charges.items.U_GricollersCan;
import tictac7x.charges.items.U_HerbSack;
import tictac7x.charges.items.U_JarGenerator;
import tictac7x.charges.items.U_LogBasket;
import tictac7x.charges.items.U_OgreBellows;
import tictac7x.charges.items.U_SeedBox;
import tictac7x.charges.items.U_SoulBearer;
import tictac7x.charges.items.U_StrangeOldLockpick;
import tictac7x.charges.items.U_TackleBox;
import tictac7x.charges.items.U_TeleportCrystal;
import tictac7x.charges.items.U_Waterskin;
import tictac7x.charges.items.W_Arclight;
import tictac7x.charges.items.W_BryophytasStaff;
import tictac7x.charges.items.W_CrystalBow;
import tictac7x.charges.items.W_CrystalHalberd;
import tictac7x.charges.items.W_IbansStaff;
import tictac7x.charges.items.W_PharaohsSceptre;
import tictac7x.charges.items.W_SanguinestiStaff;
import tictac7x.charges.items.W_SkullSceptre;
import tictac7x.charges.items.W_TridentOfTheSeas;
import tictac7x.charges.items.barrows.AhrimsHood;
import tictac7x.charges.items.barrows.AhrimsRobeskirt;
import tictac7x.charges.items.barrows.AhrimsRobetop;
import tictac7x.charges.items.barrows.AhrimsStaff;
import tictac7x.charges.items.barrows.DharoksGreataxe;
import tictac7x.charges.items.barrows.DharoksHelm;
import tictac7x.charges.items.barrows.DharoksPlatebody;
import tictac7x.charges.items.barrows.DharoksPlatelegs;
import tictac7x.charges.items.barrows.GuthansChainskirt;
import tictac7x.charges.items.barrows.GuthansHelm;
import tictac7x.charges.items.barrows.GuthansPlatebody;
import tictac7x.charges.items.barrows.GuthansWarspear;
import tictac7x.charges.items.barrows.KarilsCoif;
import tictac7x.charges.items.barrows.KarilsCrossbow;
import tictac7x.charges.items.barrows.KarilsLeatherskirt;
import tictac7x.charges.items.barrows.KarilsLeathertop;
import tictac7x.charges.items.barrows.ToragsHammers;
import tictac7x.charges.items.barrows.ToragsHelm;
import tictac7x.charges.items.barrows.ToragsPlatebody;
import tictac7x.charges.items.barrows.ToragsPlatelegs;
import tictac7x.charges.items.barrows.VeracsBrassard;
import tictac7x.charges.items.barrows.VeracsFlail;
import tictac7x.charges.items.barrows.VeracsHelm;
import tictac7x.charges.items.barrows.VeracsPlateskirt;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PluginDescriptor(
	name = "Item Charges Improved",
	description = "Show charges of various items",
	tags = {
		"charges",
		"barrows",
		"crystal",
		"ardougne",
		"coffing",
		"magic",
		"cape",
		"circlet",
		"bracelet",
		"clay",
		"expeditious",
		"flamtaer",
		"slaughter",
		"camulet",
		"celestial",
		"ring",
		"escape",
		"recoil",
		"shadow",
		"suffering",
		"slayer",
		"xeric",
		"talisman",
		"chronicle",
		"dragonfire",
		"falador",
		"kharedst",
		"memoirs",
		"ash",
		"sanctifier",
		"bone",
		"crusher",
		"bottomless",
		"compost",
		"bucket",
		"coal",
		"bag",
		"fish",
		"barrel",
		"fungicide",
		"spray",
		"gem",
		"gricoller",
		"can",
		"herb",
		"sack",
		"log",
		"basket",
		"ogre",
		"bellows",
		"seed",
		"box",
		"soul",
		"bearer",
		"teleport",
		"waterskin",
		"arclight",
		"bryophyta",
		"staff",
		"bow",
		"halberd",
		"iban",
		"pharaoh",
		"sceptre",
		"sanguinesti",
		"skull",
		"trident",
		"sea",
		"toxic",
		"jar",
	}
)
@Slf4j
public class ChargesImprovedPlugin extends Plugin {
	private final String plugin_version = "v0.4";
	private final String plugin_message = "" +
		"<colHIGHLIGHT>Item Charges Improved " + plugin_version + ":<br>" +
		"<colHIGHLIGHT>* Fish barrel max charges fixed.<br>" +
		"<colHIGHLIGHT>* Coal bag added.<br>" +
		"<colHIGHLIGHT>* Herb sack added.<br>" +
		"<colHIGHLIGHT>* Strange old lockpick added.<br>" +
		"<colHIGHLIGHT>* Phoenix necklace added.<br>" +
		"<colHIGHLIGHT>* Dodgy necklace added.<br>" +
		"<colHIGHLIGHT>* Tome of fire added.<br>" +
		"<colHIGHLIGHT>* Able to hide item charges in bank.<br>" +
		"<colHIGHLIGHT>* Activateable items have configurable positive/negative colors."
	;

	private final int VARBIT_MINUTES = 8354;

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
	private OverlayManager overlays;

	@Inject
	private ChargesImprovedConfig config;

	@Inject
	private ChatMessageManager chat_messages;

	@Inject
	private TooltipManager tooltipManager;

	@Inject
	private Notifier notifier;

	@Provides
	ChargesImprovedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChargesImprovedConfig.class);
	}

	private Store store;

	private ChargedItemOverlay overlay_charged_items;

	private ChargedItem[] chargedItems;
	private List<InfoBox> chargedItemsInfoboxes = new ArrayList<>();

	private final ZoneId timezone = ZoneId.of("Europe/London");

	@Override
	protected void startUp() {
		store = new Store(client, items, configs);

		chargedItems = new ChargedItem[]{
			// Weapons
			new W_Arclight(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_TridentOfTheSeas(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_SkullSceptre(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_IbansStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_PharaohsSceptre(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_BryophytasStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_SanguinestiStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_CrystalBow(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new W_CrystalHalberd(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Shields
			new S_KharedstMemoirs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_Chronicle(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_CrystalShield(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_FaladorShield(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_DragonfireShield(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new S_TomeOfFire(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Boots
			new B_FremennikSeaBoots(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Jewellery
			new J_BraceletOfClay(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_BraceletOfExpeditious(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_BraceletOfFlamtaer(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_BraceletOfSlaughter(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_Camulet(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfCelestial(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_DesertAmulet(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_EscapeCrystal(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_NecklaceOfPassage(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_NecklaceOfPhoenix(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_NecklaceOfDodgy(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfRecoil(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfShadows(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfSuffering(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_RingOfSlayer(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new J_XericsTalisman(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Helms
			new H_CircletOfWater(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new H_KandarinHeadgear(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Capes
			new C_ArdougneCloak(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new C_Coffin(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new C_ForestryKit(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new C_MagicCape(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Utilities
			new U_AshSanctifier(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_BoneCrusher(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_BottomlessCompostBucket(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_CoalBag(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_FishBarrel(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_GemBag(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_HerbSack(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_JarGenerator(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_FungicideSpray(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_GricollersCan(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_LogBasket(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_OgreBellows(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_SeedBox(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_SoulBearer(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_StrangeOldLockpick(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_TackleBox(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_TeleportCrystal(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new U_Waterskin(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Crystal armor set
			new A_CrystalBody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new A_CrystalHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new A_CrystalLegs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			// Barrows armor sets
			new AhrimsHood(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new AhrimsRobetop(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new AhrimsRobeskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new AhrimsStaff(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new DharoksHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new DharoksPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new DharoksPlatelegs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new DharoksGreataxe(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new GuthansHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new GuthansPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new GuthansChainskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new GuthansWarspear(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new KarilsCoif(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new KarilsLeathertop(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new KarilsLeatherskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new KarilsCrossbow(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new ToragsHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new ToragsPlatebody(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new ToragsPlatelegs(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new ToragsHammers(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),

			new VeracsHelm(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new VeracsBrassard(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new VeracsPlateskirt(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
			new VeracsFlail(client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, this),
		};

		// Items overlays.
		overlay_charged_items = new ChargedItemOverlay(client, tooltipManager, items, config, chargedItems);
		overlays.add(overlay_charged_items);

		// Items infoboxes.
		chargedItemsInfoboxes.clear();
		Arrays.stream(chargedItems).forEach(chargedItem -> chargedItemsInfoboxes.add(new ChargedItemInfobox(chargedItem, items, infoboxes, client_thread, config, this)));
		chargedItemsInfoboxes.forEach(chargedItemInfobox -> infoboxes.addInfoBox(chargedItemInfobox));
	}

	@Override
	protected void shutDown() {
		overlays.remove(overlay_charged_items);
		chargedItemsInfoboxes.forEach(chargedItemInfobox -> infoboxes.removeInfoBox(chargedItemInfobox));
	}

	@Subscribe
	public void onItemContainerChanged(final ItemContainerChanged event) {
		store.onItemContainerChanged(event);

		for (final ChargedItem infobox : chargedItems) {
			infobox.onItemContainerChanged(event);
		}

//		String itemContainer = "ITEM CONTAINER | " + event.getContainerId();
//		for (final Item item : event.getItemContainer().getItems()) {
//			itemContainer += "\r\n" +
//				item.getId() + ": " + items.getItemComposition(item.getId()).getName() +
//				", quantity: " + item.getQuantity();
//		}
//		log.debug(itemContainer);
	}

	@Subscribe
	public void onChatMessage(final ChatMessage event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onChatMessage(event));

//		log.debug("MESSAGE | " +
//			"type: " + event.getType().name() +
//			", message: " + event.getMessage().replaceAll("</?col.*?>", "").replaceAll("<br>", " ") +
//			", sender: " + event.getSender()
//		);
	}

	@Subscribe
	public void onGraphicChanged(final GraphicChanged event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onGraphicChanged(event));

//		if (event.getActor() == client.getLocalPlayer()) {
//			log.debug("GRAPHIC | " +
//				"id: " + event.getActor().getGraphic()
//			);
//		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onConfigChanged(event));

//		if (event.getGroup().equals(ChargesImprovedConfig.group)) {
//			log.debug("CONFIG | " +
//				"key: " + event.getKey() +
//				", old value: " + event.getOldValue() +
//				", new value: " + event.getNewValue()
//			);
//		}
	}

	@Subscribe
	public void onHitsplatApplied(final HitsplatApplied event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onHitsplatApplied(event));

//		log.debug("HITSPLAT | " +
//			"actor: " + (event.getActor() == client.getLocalPlayer() ? "self" : "enemy") +
//			", type: " + event.getHitsplat().getHitsplatType() +
//			", amount:" + event.getHitsplat().getAmount() +
//			", others = " + event.getHitsplat().isOthers() +
//			", mine = " + event.getHitsplat().isMine()
//		);
	}

	@Subscribe
	public void onWidgetLoaded(final WidgetLoaded event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onWidgetLoaded(event));

//		log.debug("WIDGET | " +
//			"group: " + event.getGroupId()
//		);
	}

	@Subscribe
	public void onMenuOptionClicked(final MenuOptionClicked event) {
		store.onMenuOptionClicked(event);
		int impostorId = -1;
		try {
			impostorId = client.getObjectDefinition(event.getMenuEntry().getIdentifier()).getImpostor().getId();
		} catch (final Exception ignored) {}

		log.debug("MENU OPTION | " +
			"option: " + event.getMenuOption() +
			", target: " + event.getMenuTarget() +
			", action name: " + event.getMenuAction().name() +
			", action id: " + event.getMenuAction().getId() +
			", item id: " + event.getItemId() +
			", impostor id " + impostorId
		);
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGING_IN) {
			checkForChargesReset();
		}

		if (event.getGameState() != GameState.LOGGED_IN) return;

		// Send message about plugin updates for once.
		if (!config.getVersion().equals(plugin_version)) {
			configs.setConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.version, plugin_version);
			chat_messages.queue(QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(plugin_message)
				.build()
			);
		}
	}

	@Subscribe
	public void onStatChanged(final StatChanged event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onStatChanged(event));
		store.onStatChanged(event);

//		log.debug("STAT CHANGED | " +
//			event.getSkill().getName() +
//			", level: " + event.getLevel() +
//			", xp: " + event.getXp()
//		);
	}

	@Subscribe
	public void onItemDespawned(final ItemDespawned event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onItemDespawned(event));

//		log.debug("ITEM DESPAWNED | " +
//			event.getItem().getId() +
//			", quantity: " + event.getItem().getQuantity()
//		);
	}

	@Subscribe
	public void onVarbitChanged(final VarbitChanged event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onVarbitChanged(event));

		// If server minutes are 0, it's a new day!
		if (event.getVarbitId() == VARBIT_MINUTES && client.getGameState() == GameState.LOGGED_IN && event.getValue() == 0) {
			checkForChargesReset();
		}

//		log.debug("VARBIT CHANGED | " +
//			"id: " + event.getVarbitId() +
//			", value: " + event.getValue()
//		);
	}

	@Subscribe
	public void onMenuEntryAdded(final MenuEntryAdded event) {
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onMenuEntryAdded(event));

//		if (event.getMenuEntry().getItemId() != -1) {
//			log.debug("MENU ENTRY ADDED | " +
//				"item id: " + event.getMenuEntry().getItemId() +
//				", option: " + event.getOption() +
//				", target: " + event.getTarget()
//			);
//		}
	}

	@Subscribe
	public void onGameTick(final GameTick gametick) {
		store.onGameTick(gametick);
	}

	private void checkForChargesReset() {
		final String date = LocalDateTime.now(timezone).format(DateTimeFormatter.ISO_LOCAL_DATE);
		if (date.equals(config.getResetDate())) return;

		configs.setConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.date, date);
		Arrays.stream(chargedItems).forEach(infobox -> infobox.onResetDaily());

		chat_messages.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage("Daily item charges have been reset.")
			.build()
		);
	}

	public static String getChargesMinified(final int charges) {
		if (charges == Charges.UNLIMITED) return "∞";
		if (charges == Charges.UNKNOWN) return "?";
		if (charges < 1000) return String.valueOf(charges);
		if (charges >= 1000000) return charges / 1000000 + "M";

		final int thousands = charges / 1000;
		final int hundreds = Math.min((charges % 1000 + 50) / 100, 9);

		return thousands + (thousands < 10 && hundreds > 0 ? "." + hundreds : "") + "K";
	}
}

