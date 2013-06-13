package mods.nurseangel.ecologygeneration;

import ic2.api.item.Items;

import java.util.logging.Level;

import mods.nurseangel.ecologygeneration.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class EcologyGeneration {

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static boolean isTest = false;

	// コンストラクタ的なもの
	@Mod.PreInit
	public void myPreInitMethod(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		int blockIdStart = 1270;
		int itemIdStart = 5060;

		try {
			cfg.load();
			geneconItemId = cfg.getItem("Genecon ID", itemIdStart++).getInt();
			generationFloorBlockID = cfg.getBlock("Generation floor ID", blockIdStart++).getInt();

			isTest = cfg.get(Configuration.CATEGORY_GENERAL, "isTest", false, "Always false").getBoolean(false);

		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, Reference.MOD_NAME + " configuration loadding failed");
		} finally {
			cfg.save();
		}

	}

	// load()なもの
	@Mod.Init
	public void myInitMethod(FMLInitializationEvent event) {
		try {
			// 発電床
			if (generationFloorBlockID > 1) {
				this.addGerationFloorBlockID();
			}

			// 手回し発電機
			if (geneconItemId > 1) {
				this.addItemGenecon();
			}

			if (isTest) {
				this.addTestRecipes();
			}
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, Reference.MOD_NAME + " initialization failed");
		}
	}

	public static int geneconItemId, generationFloorBlockID;
	public static ItemGenecon itemGenecon;
	public static BlockGenerationFloor blockGenerationFloor;

	private void addGerationFloorBlockID() {
		String name = "Generation floor";

		blockGenerationFloor = new BlockGenerationFloor(generationFloorBlockID, 0, EnumMobType.players, Material.wood);
		blockGenerationFloor.setBlockName(name);
		LanguageRegistry.addName(blockGenerationFloor, name);
		LanguageRegistry.instance().addNameForObject(blockGenerationFloor, "ja_JP", "発電床");

		GameRegistry.registerBlock(blockGenerationFloor, name);
		GameRegistry.addRecipe(new ItemStack(blockGenerationFloor, 1),
				new Object[] { "ACB", 'A', Items.getItem("insulatedCopperCableItem"), 'B', Items.getItem("generator"), 'C',
						new ItemStack(Block.stoneSingleSlab, 1, 0) });
	}

	private void addItemGenecon() {
		String name = "Hand-cranked generator";

		itemGenecon = new ItemGenecon(geneconItemId);
		itemGenecon.setItemName(name);

		LanguageRegistry.addName(itemGenecon, name);
		LanguageRegistry.instance().addNameForObject(itemGenecon, "ja_JP", "手回し発電機");
		GameRegistry.addRecipe(new ItemStack(itemGenecon, 1),
				new Object[] { "ACB", 'A', Items.getItem("insulatedCopperCableItem"), 'B', Items.getItem("generator"), 'C', Items.getItem("rubber") });

	}

	private void addTestRecipes() {
		GameRegistry.addRecipe(new ItemStack(itemGenecon, 64, 0), new Object[] { "DD", 'D', Block.dirt });
		GameRegistry.addRecipe(new ItemStack(blockGenerationFloor, 64, 0), new Object[] { "D", 'D', Block.dirt });
		GameRegistry.addRecipe(new ItemStack(Block.slowSand, 64), new Object[] { "DD", "DD", "DD", 'D', Block.dirt });
		GameRegistry.addRecipe(new ItemStack(Block.pistonStickyBase, 64), new Object[] { "DDD", "DDD", 'D', Block.dirt });
		GameRegistry.addRecipe(new ItemStack(Block.lever, 64), new Object[] { "D D", "DDD", 'D', Block.dirt });
		GameRegistry.addRecipe(new ItemStack(Block.redstoneWire, 64), new Object[] { "DDD", "D  ", 'D', Block.dirt });
		GameRegistry.addRecipe(new ItemStack(Block.dirt, 64), new Object[] { "DD", "DD", 'D', Block.dirt });
	}

}