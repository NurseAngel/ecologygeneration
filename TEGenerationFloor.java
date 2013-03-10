package nurseangel.EcologyGeneration;

import ic2.api.Direction;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;

public class TEGenerationFloor extends TileEntity implements IEnergySource{


	/**
	 * 隣にEUを送信できるか
	 */
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return true;
	}

	/**
	 * エネルギーネットワークに接続できるか?
	 */
	@Override
	public boolean isAddedToEnergyNet() {
		return true;
	}

	@Override
	public int getMaxEnergyOutput() {
		return Integer.MAX_VALUE;
	}

}
/*
	※現在不使用。
	内部EUストレージを使うときに必要になるようだがよくわからん

	ソース参考

	チャージングベンチ
	http://forum.industrial-craft.net/index.php?page=Thread&threadID=7900

*/