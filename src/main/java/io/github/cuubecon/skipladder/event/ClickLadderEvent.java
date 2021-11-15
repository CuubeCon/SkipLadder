package io.github.cuubecon.skipladder.event;

import io.github.cuubecon.skipladder.config.SkipLadderConfig;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.FACING;

public class ClickLadderEvent implements UseBlockCallback {

    private final static int[][] offsetArray = {{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}};

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

        if(!world.isClient)
        {
            BlockPos pos = hitResult.getBlockPos();
            int foodLevel = player.getHungerManager().getFoodLevel();
            Iterable<ItemStack> stack = player.getItemsHand();

            for (ItemStack itemstack : stack)
            {
                if(isInvalidItem(itemstack.getItem()))
                    return ActionResult.PASS;
            }

            if(foodLevel < 5 && !player.isCreative())
            {
                player.sendMessage(new TranslatableText("skipladder.message.morefood"), true);
                return ActionResult.PASS;
            }

            if(world.getBlockState(pos).getBlock().equals(Blocks.LADDER))
            {
                double lY;
                BlockPos posL;
                double lookY_UP = pos.getY()+1;
                double lookY_DOWN = pos.getY()-1;
                double lookY_UP2 = pos.getY()+2;
                double lookY_DOWN2 = pos.getY()-2;
                boolean up = false;

                BlockPos posUP = new BlockPos(pos.getX(),lookY_UP,pos.getZ());
                BlockPos posDOWN = new BlockPos(pos.getX(),lookY_DOWN,pos.getZ());

                BlockPos posUP2 = new BlockPos(pos.getX(),lookY_UP2,pos.getZ());
                BlockPos posDOWN2 = new BlockPos(pos.getX(),lookY_DOWN2,pos.getZ());

                if((world.getBlockState(posUP).getBlock().equals(Blocks.LADDER) && world.getBlockState(posUP2).getBlock().equals(Blocks.LADDER))
                        & (world.getBlockState(posDOWN).getBlock().equals(Blocks.LADDER) && world.getBlockState(posDOWN2).getBlock().equals(Blocks.LADDER)))
                    return ActionResult.PASS;

                if(world.getBlockState(posUP).getBlock().equals(Blocks.LADDER) & world.getBlockState(posUP2).getBlock().equals(Blocks.LADDER))
                {
                    up = true;
                    posL = posUP;
                }
                else
                    posL = posDOWN;

                int counter = 0;

                while(world.getBlockState(posL).getBlock().equals(Blocks.LADDER))
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
            else if(world.getBlockState(pos).getBlock().equals(BlockTags.STAIRS))
            {

                BlockState state = world.getBlockState(pos);
                BlockPos posUP = null;
                BlockPos posDOWN = null;
                BlockPos posUP2 = null;
                BlockPos posDOWN2 = null;
                boolean south, north, east, west;
                south = north = east = west = false;
                if(state.get(FACING).getName().equals("south"))
                {
                    south = true;
                    posUP = new BlockPos(pos.getX(),pos.getY()+1,pos.getZ()+1);
                    posDOWN = new BlockPos(pos.getX(),pos.getY()-1,pos.getZ()-1);

                    posUP2 = new BlockPos(pos.getX(),pos.getY()+2,pos.getZ()+2);
                    posDOWN2 = new BlockPos(pos.getX(),pos.getY()-2,pos.getZ()-2);
                }
                else if(state.get(FACING).getName().equals("north"))
                {
                    north = true;
                    posUP = new BlockPos(pos.getX(),pos.getY()+1,pos.getZ()-1);
                    posDOWN = new BlockPos(pos.getX(),pos.getY()-1,pos.getZ()+1);

                    posUP2 = new BlockPos(pos.getX(),pos.getY()+2,pos.getZ()-2);
                    posDOWN2 = new BlockPos(pos.getX(),pos.getY()-2,pos.getZ()+2);
                }
                else if(state.get(FACING).getName().equals("east"))
                {
                    east = true;
                    posUP = new BlockPos(pos.getX()+1,pos.getY()+1,pos.getZ());
                    posDOWN = new BlockPos(pos.getX()-1,pos.getY()-1,pos.getZ());

                    posUP2 = new BlockPos(pos.getX()+2,pos.getY()+2,pos.getZ());
                    posDOWN2 = new BlockPos(pos.getX()-2,pos.getY()-2,pos.getZ());
                }
                else if(state.get(FACING).getName().equals("west"))
                {
                    west = true;
                    posUP = new BlockPos(pos.getX()-1,pos.getY()+1,pos.getZ());
                    posDOWN = new BlockPos(pos.getX()+1,pos.getY()-1,pos.getZ());

                    posUP2 = new BlockPos(pos.getX()-2,pos.getY()+2,pos.getZ());
                    posDOWN2 = new BlockPos(pos.getX()+2,pos.getY()-2,pos.getZ());
                }

                BlockPos posL;
                boolean up = false;



                if((world.getBlockState(posUP).getBlock().equals(BlockTags.STAIRS) && world.getBlockState(posUP2).getBlock().equals(BlockTags.STAIRS))
                        & (world.getBlockState(posDOWN).getBlock().equals(BlockTags.STAIRS) && world.getBlockState(posDOWN2).getBlock().equals(BlockTags.STAIRS)))
                    return ActionResult.PASS;

                if(world.getBlockState(posUP).getBlock().equals(BlockTags.STAIRS) & world.getBlockState(posUP2).getBlock().equals(BlockTags.STAIRS))
                {
                    up = true;
                    posL = posUP;
                }
                else
                    posL = posDOWN;

                double lY, lX = posL.getX(), lZ = posL.getZ();
                int counter = 0;
                while(world.getBlockState(posL).getBlock().equals(BlockTags.STAIRS))
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
                            lX = posL.getX()+1;
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
                            lX = posL.getX()-1;
                    }



                    posL = new BlockPos(lX,lY,lZ);
                }

                teleportPlayer(world, player, foodLevel, posL, counter);
            }
        }

        return ActionResult.SUCCESS;
    }

    private static void teleportPlayer(World world, PlayerEntity player, int foodLevel, BlockPos posL, int counter)
    {

        if(counter > SkipLadderConfig.MAX_AMOUNT)
            counter = SkipLadderConfig.MAX_AMOUNT;

        int fooddecrease = (counter / SkipLadderConfig.HUNGER_AMOUNT);

        if(fooddecrease >= foodLevel)
            fooddecrease = foodLevel - 2;

        if(fooddecrease <= 0)
            fooddecrease = 1;

        Vec3d targetblock = findSafeTeleportLocation(world, posL);
        if(targetblock != null)
            player.teleport(targetblock.x, targetblock.y, targetblock.z);
        else
            player.teleport(posL.getX()+0.5, posL.getY()+1.0, posL.getZ()+0.5);


        if(!player.isCreative() && SkipLadderConfig.REMOVE_HUNGER)
            player.getHungerManager().setFoodLevel(foodLevel - fooddecrease);

        if(SkipLadderConfig.PLAY_TELEPORTSOUND)
            world.playSound(null, posL, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 10, 1);
    }
    @Nullable
    private static Vec3d findSafeTeleportLocation(World world, BlockPos pos)
    {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        for (int[] offset: offsetArray)
        {
            blockpos$mutable.set(pos.getX() + offset[0],pos.getY(), pos.getZ() + offset[1]);
            Vec3d vector3d = Dismounting.findRespawnPos(EntityType.PLAYER, world, blockpos$mutable, false);
            if(vector3d != null)
                return vector3d;
        }
        return null;
    }

    private static boolean isInvalidItem(Item item)
    {
        boolean is_invalid = false;

        if(item instanceof BlockItem)
            is_invalid = true;
        else if(item instanceof PotionItem)
            is_invalid = true;
        else if(item instanceof EnderPearlItem)
            is_invalid = true;
        else if(item instanceof SnowballItem)
            is_invalid = true;

        return is_invalid;
    }
}
