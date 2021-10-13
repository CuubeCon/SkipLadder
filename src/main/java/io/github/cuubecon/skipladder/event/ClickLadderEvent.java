package io.github.cuubecon.skipladder.event;

import io.github.cuubecon.skipladder.SkipLadder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TransportationHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ICollisionReader;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.block.HorizontalBlock.FACING;

@SuppressWarnings("DuplicatedCode")
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ClickLadderEvent
{
    private final static int[][] offsetArray = {{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}};

    @SubscribeEvent
    public static void onBlocksRegistry(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if(!world.isClientSide)
       {
           BlockPos pos = event.getPos();
           PlayerEntity player = event.getPlayer();
           int foodLevel = player.getFoodData().getFoodLevel();
           Iterable<ItemStack> stack = player.getHandSlots();

           for (ItemStack itemstack : stack) {
               if(!itemstack.isEmpty())
                   return;
           }

           if(foodLevel < 5 && !player.isCreative())
           {
               player.displayClientMessage(new TranslationTextComponent("skipladder.message.morefood"), true);
               return;
           }

           if(world.getBlockState(pos).getBlock().equals(Blocks.LADDER))
           {
               double lY;
               BlockPos posL;
               double lookY_UP = pos.getY()+1;
               double lookY_DOWN = pos.getY()-1;
               boolean up = false;

               BlockPos posUP = new BlockPos(pos.getX(),lookY_UP,pos.getZ());
               BlockPos posDOWN = new BlockPos(pos.getX(),lookY_DOWN,pos.getZ());

               if(world.getBlockState(posUP).getBlock().is(Blocks.LADDER) & world.getBlockState(posDOWN).getBlock().is(Blocks.LADDER))
                   return;

               if(world.getBlockState(posUP).getBlock().is(Blocks.LADDER))
               {
                   up = true;
                   posL = posUP;
               }
               else
                   posL = posDOWN;
               int counter = 0;

               while(world.getBlockState(posL).getBlock().is(Blocks.LADDER))
               {
                   counter++;

                   if(up)
                       lY = posL.getY()+1;
                   else
                       lY = posL.getY()-1;
                   posL = new BlockPos(posL.getX(),lY,posL.getZ());
               }

               teleportPlayer(world, player, foodLevel, posL, counter);
           }
           else if(world.getBlockState(pos).getBlock().is(BlockTags.STAIRS))
           {

               BlockState state = world.getBlockState(pos);
               BlockPos posUP = null;
               BlockPos posDOWN = null;
               boolean south, north, east, west;
               south = north = east = west = false;
                if(state.getValue(FACING).getName().equals("south"))
                {
                    south = true;
                    posUP = new BlockPos(pos.getX(),pos.getY()+1,pos.getZ()+1);
                    posDOWN = new BlockPos(pos.getX(),pos.getY()-1,pos.getZ()-1);
                }else if(state.getValue(FACING).getName().equals("north"))
                {
                    north = true;
                    posUP = new BlockPos(pos.getX(),pos.getY()+1,pos.getZ()-1);
                    posDOWN = new BlockPos(pos.getX(),pos.getY()-1,pos.getZ()+1);
                }else if(state.getValue(FACING).getName().equals("east"))
                {
                    east = true;
                    posUP = new BlockPos(pos.getX()+1,pos.getY()+1,pos.getZ());
                    posDOWN = new BlockPos(pos.getX()-1,pos.getY()-1,pos.getZ());
                }else if(state.getValue(FACING).getName().equals("west"))
                {
                    west = true;
                    posUP = new BlockPos(pos.getX()-1,pos.getY()+1,pos.getZ());
                    posDOWN = new BlockPos(pos.getX()+1,pos.getY()-1,pos.getZ());
                }

               BlockPos posL;
               boolean up = false;



               if(world.getBlockState(posUP).getBlock().is(BlockTags.STAIRS) & world.getBlockState(posDOWN).getBlock().is(BlockTags.STAIRS))
                   return;
              // StairsBlock
               if(world.getBlockState(posUP).getBlock().is(BlockTags.STAIRS))
               {
                   up = true;
                   posL = posUP;
               }
               else
                   posL = posDOWN;

               double lY, lX = posL.getX(), lZ = posL.getZ();
               int counter = 0;
               while(world.getBlockState(posL).getBlock().is(BlockTags.STAIRS))
               {
                   counter++;
                   if(up)
                   {
                       lY = posL.getY()+1;
                       if(south)
                           lZ = posL.getZ()+1;
                       else if(north)
                           lZ = posL.getZ()-1;
                       else if(west)
                           lX = posL.getX()-1;
                       else if(east)
                           lZ = posL.getX()+1;
                   }
                   else
                   {
                       lY = posL.getY()-1;
                       if(south)
                           lZ = posL.getZ()-1;
                       else if(north)
                           lZ = posL.getZ()+1;
                       else if(west)
                           lX = posL.getX()+1;
                       else if(east)
                           lZ = posL.getX()-1;
                   }



                   posL = new BlockPos(lX,lY,lZ);
               }

               teleportPlayer(world, player, foodLevel, posL, counter);

           }
       }


    }

    private static void teleportPlayer(World world, PlayerEntity player, int foodLevel, BlockPos posL, int counter) {
        if(counter > 150)
            counter = 150;
        int fooddecrease = (counter /10);
        if(fooddecrease >= foodLevel)
            fooddecrease = foodLevel -2;
        if(fooddecrease <=0)
            fooddecrease = 1;
        System.out.println(fooddecrease + " " + counter);
        Vector3d targetblock = findSafeTeleportLocation(world, posL);
        if(targetblock != null)
        {
            System.out.println(targetblock.toString());
            player.teleportTo(targetblock.x, targetblock.y, targetblock.z);
        }
        else
        {
            player.teleportTo(posL.getX()+0.5, posL.getY()+1.0, posL.getZ()+0.5);
        }

        if(!player.isCreative())
            player.getFoodData().setFoodLevel(foodLevel - fooddecrease);
        world.playSound(null, posL, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 10, 1);
    }

    private static Vector3d findSafeTeleportLocation(ICollisionReader world, BlockPos pos)
    {

        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        for (int[] offset: offsetArray)
        {
            blockpos$mutable.set(pos.getX() + offset[0],pos.getY(), pos.getZ() + offset[1]);
            Vector3d vector3d = TransportationHelper.findSafeDismountLocation(EntityType.PLAYER, world, blockpos$mutable, false);
            if(vector3d != null)
                return vector3d;
        }
        return null;
    }
}
