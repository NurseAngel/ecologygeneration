package mods.nurseangel.ecologygeneration;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemGenerationFloor extends ItemBlock {
	/**
	 * コンストラクタ
	 *
	 * @param アイテムID
	 */
	public ItemGenerationFloor(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
		setNoRepair();
	}

	/**
	 * メタデータ(=subType)を返す <br />
	 * ブロックの設置時に使用
	 */
	@Override
	public int getMetadata(int i) {
		return i;
	}

	/**
	 * 内部的ブロックの名前を返す
	 *
	 * @param ItenStack
	 * @return String
	 */
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		return (new StringBuilder()).append(getUnlocalizedName()).append(i).toString();
	}

}
