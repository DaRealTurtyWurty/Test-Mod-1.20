package dev.turtywurty.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ExampleBedBlock extends BedBlock {
    private static final VoxelShape HEAD_SHAPE = makeHeadShape();
    private static final VoxelShape FOOT_SHAPE = makeFootShape();

    private static final Map<Direction, VoxelShape> HEAD_SHAPES = runCalculation(new HashMap<>(), HEAD_SHAPE);
    private static final Map<Direction, VoxelShape> FOOT_SHAPES = runCalculation(new HashMap<>(), FOOT_SHAPE);

    public ExampleBedBlock(Properties properties) {
        super(DyeColor.RED, properties);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, @Nullable Entity player) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        Direction connectedDirection = getConnectedDirection(state);
        Direction headDirection = connectedDirection.getOpposite();
        BedPart part = state.getValue(PART);

        return part == BedPart.HEAD ? HEAD_SHAPES.get(headDirection) : FOOT_SHAPES.get(connectedDirection);
    }

    private static Map<Direction, VoxelShape> runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (final Direction direction : Direction.values()) {
            shapes.put(direction, calculateShapes(direction, shape));
        }

        return shapes;
    }

    private static VoxelShape calculateShapes(Direction to, VoxelShape shape) {
        final VoxelShape[] buffer = { shape, Shapes.empty() };

        final int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY,
                                   maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    private static VoxelShape makeHeadShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 0.25, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 0.25, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0, 1, 0.625, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.625, 0, 0.875, 0.8125, 0.4375), BooleanOp.OR);

        return shape;
    }

    private static VoxelShape makeFootShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.125, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.875, 1, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0, 1, 0.625, 1), BooleanOp.OR);

        return shape;
    }
}