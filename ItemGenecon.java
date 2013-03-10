package nurseangel.EcologyGeneration;

import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ItemGenecon extends Item implements IElectricItem {

	/**
	 * コンストラクタ
	 *
	 * @param par1
	 *            アイテムID
	 */
	protected ItemGenecon(int par1) {
		super(par1);

		setMaxStackSize(1);
		setMaxDamage(0);

		setIconIndex(0);
		setTextureFile(Reference.TEXTURE_FILE);
		setCreativeTab(CreativeTabs.tabTools);
	}

	/**
	 * 右クリックした
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer par3EntityPlayer) {
		try {
			ElectricItem.charge(itemStack, 1, 1, false, false);
			FMLClientHandler.instance().getClient().thePlayer.swingItem(); //腕を振る
		} catch (Exception e) {
		}
		super.onItemRightClick(itemStack, world, par3EntityPlayer);

		return itemStack;
	}


	/**
	 * 電気を供給可能か
	 */
	@Override
	public boolean canProvideEnergy() {
		return true;
	}

	/**
	 * 満チャージしたアイテムID 特に変えたいときに別の値を返す?
	 */
	@Override
	public int getChargedItemId() {
		return this.itemID;
	}

	/**
	 * 空のときのアイテムID
	 */
	@Override
	public int getEmptyItemId() {
		return this.itemID;
	}

	/**
	 * 最大充電容量
	 */
	@Override
	public int getMaxCharge() {
		return 10000;
	}

	/**
	 * アイテムランク バッテリーが1、エナジークリスタルが2、ラポトロンが3
	 */
	@Override
	public int getTier() {
		return 1;
	}

	/**
	 * 最大電圧
	 */
	@Override
	public int getTransferLimit() {
		return 32;
	}

	/**
	 * 画面にメッセージを表示
	 *
	 * @param str
	 */
	protected void message(String str) {
		if (EcologyGeneration.isTest) {
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				FMLClientHandler.instance().getClient().thePlayer.addChatMessage(str);
			}
		}
	}
}
