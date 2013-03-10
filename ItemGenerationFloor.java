package nurseangel.EcologyGeneration;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemGenerationFloor extends ItemBlock{
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
	 * 内部的ブロックの名前を返す<br />
	 * メタデータごとに名前を設定する場合必須<br />
	 */
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		return (new StringBuilder()).append(getItemName()).append(i).toString();
	}

	@Override
	public String getLocalItemName(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		return (new StringBuilder()).append(getItemName()).append(i).toString();
	}

}
