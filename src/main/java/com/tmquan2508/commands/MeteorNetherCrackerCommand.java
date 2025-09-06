package com.tmquan2508.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class MeteorNetherCrackerCommand extends Command {

    private static final int SEARCH_RADIUS = 128;

    public MeteorNetherCrackerCommand() {
        super("nethercracker", "Finds bedrock at Y=4 and Y=123 in the Nether within a specified radius.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            if (mc.player == null || mc.world == null) {
                error("Player or world not available.");
                return SINGLE_SUCCESS;
            }
            if (!mc.world.getRegistryKey().equals(World.NETHER)) {
                error("You must be in the Nether to use this command.");
                return SINGLE_SUCCESS;
            }

            BlockPos playerPos = mc.player.getBlockPos();
            ChunkPos centerChunkPos = mc.world.getChunk(playerPos).getPos();

            List<BlockPos> bedrockCandidates = new ArrayList<>();

            int chunkRadius = (SEARCH_RADIUS >> 4) + 1;

            info("Scanning chunks in a %d block radius (%d chunk radius)...", SEARCH_RADIUS, chunkRadius);

            for (int r = 0; r < chunkRadius; r++) {
                for (int chunkX = centerChunkPos.x - r; chunkX <= centerChunkPos.x + r; chunkX++) {
                    for (int chunkZ = centerChunkPos.z - r; chunkZ <= centerChunkPos.z + r; chunkZ++) {
                        if (r > 0 && (chunkX > centerChunkPos.x - r && chunkX < centerChunkPos.x + r) && (chunkZ > centerChunkPos.z - r && chunkZ < centerChunkPos.z + r)) {
                            continue;
                        }

                        Chunk chunk = mc.world.getChunk(chunkX, chunkZ);

                        if (chunk instanceof WorldChunk) {
                            addBedrockBlocks((WorldChunk) chunk, bedrockCandidates);
                        }
                    }
                }
            }

            info(String.format("Found %d bedrock blocks at y=4 and y=123.", bedrockCandidates.size()));

            if (!bedrockCandidates.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (BlockPos pos : bedrockCandidates) {
                    sb.append(String.format("%d %d %d\n", pos.getX(), pos.getY(), pos.getZ()));
                }
                String coords = sb.toString().trim();

                 Text copyText = Texts.bracketed(
                     Text.literal("Copy Coords")
                         .fillStyle(Style.EMPTY
                             .withColor(Formatting.GREEN)
                             .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, coords))
                             .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to copy all coordinates")))
                             .withInsertion(coords)
                         )
                 );

                info(copyText);
            }

            return SINGLE_SUCCESS;
        });
    }

    private static void addBedrockBlocks(WorldChunk chunk, List<BlockPos> blockCandidates) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        ChunkPos chunkPos = chunk.getPos();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getStartX() + x;
                int worldZ = chunkPos.getStartZ() + z;

                mutablePos.set(worldX, 4, worldZ);
                if (chunk.getBlockState(mutablePos).isOf(Blocks.BEDROCK)) {
                    blockCandidates.add(mutablePos.toImmutable());
                    continue;
                }

                mutablePos.set(worldX, 123, worldZ);
                if (chunk.getBlockState(mutablePos).isOf(Blocks.BEDROCK)) {
                    blockCandidates.add(mutablePos.toImmutable());
                }
            }
        }
    }
}