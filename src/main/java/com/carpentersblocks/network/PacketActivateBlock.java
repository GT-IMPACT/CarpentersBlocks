package com.carpentersblocks.network;

import com.carpentersblocks.block.BlockCoverable;
import com.carpentersblocks.util.BlockProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketActivateBlock extends TilePacket
{

	private int side;

	public PacketActivateBlock()
	{
	}

	public PacketActivateBlock(int x, int y, int z, int side)
	{
		super(x, y, z);
		this.side = side;
	}

	@Override
	public void processData(EntityPlayer player, ByteBufInputStream bbis) throws IOException
	{
		super.processData(player, bbis);

		this.side = bbis.readInt();
		World world = player.worldObj;
		int x = this.x;
		int y = this.y;
		int z = this.z;
		ItemStack heldItem = player.getHeldItem();

		if (heldItem != null && heldItem.getItem() instanceof ItemBlock && !BlockProperties.isOverlay(heldItem))
			return;
		if (player.getDistanceSq(x, y, z) > 64)
			return;
		if (!world.blockExists(x, y, z))
			return;

		Block block = world.getBlock(x, y, z);

		if (!(block instanceof BlockCoverable))
			return;

		block.onBlockActivated(world, x, y, z, player, this.side, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void appendData(ByteBuf buffer) throws IOException
	{
		super.appendData(buffer);
		buffer.writeInt(this.side);
	}

}
