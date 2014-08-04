package minechem.tileentity.blueprintprojector;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import minechem.ModMinechem;
import minechem.block.BlockSimpleContainer;
import minechem.item.blueprint.ItemBlueprint;
import minechem.item.blueprint.MinechemBlueprint;
import minechem.network.server.CommonProxy;
import minechem.utils.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlueprintProjectorBlock extends BlockSimpleContainer
{

    public BlueprintProjectorBlock(int id)
    {
        super(Material.iron);
        setBlockName("minechem.blockBlueprintProjector");
        setCreativeTab(ModMinechem.CREATIVE_TAB);
        setLightLevel(0.7F);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase el, ItemStack is)
    {
        super.onBlockPlacedBy(world, x, y, z, el, is);
        int facing = MathHelper.floor_double(el.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, facing, 2);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float par7, float par8, float par9)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof BlueprintProjectorTileEntity)
        {
            entityPlayer.openGui(ModMinechem.INSTANCE, 0, world, x, y, z);
            return true;
        }
        return false;
    }

    private ItemStack takeBlueprintFromProjector(BlueprintProjectorTileEntity projector)
    {
        MinechemBlueprint blueprint = projector.takeBlueprint();
        ItemStack blueprintItem = ItemBlueprint.createItemStackFromBlueprint(blueprint);
        return blueprintItem;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new BlueprintProjectorTileEntity();
    }

    @Override
    public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList<ItemStack> itemStacks)
    {
        if (tileEntity instanceof BlueprintProjectorTileEntity)
        {
            BlueprintProjectorTileEntity projector = (BlueprintProjectorTileEntity) tileEntity;
            if (projector.hasBlueprint())
                itemStacks.add(takeBlueprintFromProjector(projector));
        }
        return;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        blockIcon = ir.registerIcon(Reference.BLUEPRINTPROJECTOR_TEX);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return CommonProxy.RENDER_ID;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

}
