package nurseangel.EcologyGeneration;

import ic2.api.ElectricItem;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BlockGenerationFloor extends BlockPressurePlate {

	private boolean initialized = false;

	/**
	 * コンストラクタ
	 *
	 * @param par1
	 *            BlockID
	 * @param par2
	 *            未使用
	 * @param par3EnumMobType
	 *            反応するモブ
	 * @param par4Material
	 *            素材
	 */
	protected BlockGenerationFloor(int par1, int par2, EnumMobType par3EnumMobType, Material par4Material) {
		super(par1, par2, par3EnumMobType, par4Material);
		setTextureFile(Reference.TEXTURE_FILE);
	}

	/**
	 * 使用するテクスチャ番号
	 */
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return 1;
	}

	@Override
	public boolean isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}

	@Override
	public boolean canProvidePower() {
		return false;
	}

	protected void message(String str) {
		if (EcologyGeneration.isTest) {
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				FMLClientHandler.instance().getClient().thePlayer.addChatMessage(str);
			}
		}
	}

	/*
	 * 以下BlockPressurePlateのコピペ。
	 * setStateIfMobInteractsWithPlateがprivateのせいで乗っ取り方がわからなかった
	 */

	/**
	 * World.scheduleBlockUpdateしたら呼ばれる
	 */
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		if (!par1World.isRemote) {
			if (par1World.getBlockMetadata(par2, par3, par4) != 0) {
				this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4);
			}
		}
	}

	/**
	 * エンティティが触れたら呼ばれる
	 */
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if (!par1World.isRemote) {
			if (par1World.getBlockMetadata(par2, par3, par4) != 1) {
				this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4);
			}
		}
	}

	/**
	 * onEntityCollidedWithBlock、scheduleBlockUpdateで呼ばれる
	 *
	 * @param par1World
	 * @param x
	 * @param y
	 * @param z
	 */
	private void setStateIfMobInteractsWithPlate(World par1World, int x, int y, int z) {
		boolean var5 = par1World.getBlockMetadata(x, y, z) == 1;
		boolean var6 = false;
		float var7 = 0.125F;
		List var8 = null;
		var8 = par1World.getEntitiesWithinAABB(
				EntityPlayer.class,
				AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double) ((float) x + var7), (double) y, (double) ((float) z + var7),
						(double) ((float) (x + 1) - var7), (double) y + 0.25D, (double) ((float) (z + 1) - var7)));

		if (!var8.isEmpty()) {
			Iterator var9 = var8.iterator();

			while (var9.hasNext()) {
				Entity var10 = (Entity) var9.next();

				if (!var10.doesEntityNotTriggerPressurePlate()) {
					var6 = true;
					break;
				}
			}
		}

		if (var6 && !var5) {
			// 踏んだ
			pushDown(par1World, x, y, z);
		} else if (!var6 && var5) {
			// 離した
			pushUp(par1World, x, y, z);
		}

		// 現状と違った状態になった場合は、次回呼び出しを予約する
		if (var6) {
			par1World.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
		}
	}

	/**
	 * 踏んだ状態から足を離した
	 *
	 * @param par1World
	 * @param x
	 * @param y
	 * @param z
	 */
	private void pushUp(World par1World, int x, int y, int z) {
		par1World.setBlockMetadataWithNotify(x, y, z, 0);
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		par1World.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		par1World.playSoundEffect((double) x + 0.5D, (double) y + 0.1D, (double) z + 0.5D, "random.click", 0.3F, 0.5F);

		sendEnergy(par1World, x, y, z, 1);
	}

	/**
	 * 通常状態から踏んだときに呼ばれた
	 *
	 * @param par1World
	 * @param x
	 * @param y
	 * @param z
	 */
	private void pushDown(World par1World, int x, int y, int z) {
		par1World.setBlockMetadataWithNotify(x, y, z, 1);
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		par1World.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		par1World.playSoundEffect((double) x + 0.5D, (double) y + 0.1D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
	}

	/**
	 * EUを発電
	 *
	 * @param par1World
	 * @param x
	 * @param y
	 * @param z
	 * @param energy
	 */
	private void sendEnergy(World par1World, int x, int y, int z, int energy) {
		/*
		 * TODO 本当はEnergyTileSourceEventで発電したかったがよくわからない
		 */

		try {

			// 手持ちアイテムを取得
			Minecraft minecraft = FMLClientHandler.instance().getClient();
			// クライアントのプレイヤー
			EntityClientPlayerMP playerMP = minecraft.thePlayer;
			// サーバのプレイヤー
			EntityPlayerMP entityPlayerMP = minecraft.getIntegratedServer().getConfigurationManager().getPlayerForUsername(playerMP.username);
			// 手持ちアイテム
			ItemStack itemStack = entityPlayerMP.inventory.getCurrentItem();
			if (itemStack == null) {
				return;
			}

			// アイテムを充電
			ElectricItem.charge(itemStack, 1, 1, false, false);
		} catch (Exception e) {

		}

	}

}
