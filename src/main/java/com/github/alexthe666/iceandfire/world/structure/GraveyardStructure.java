package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class GraveyardStructure extends StructureFeature<NoneFeatureConfiguration> {

    public GraveyardStructure(Codec<NoneFeatureConfiguration> p_i51440_1_) {
        super(p_i51440_1_);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public String getFeatureName() {
        return IceAndFire.MODID + ":graveyard";
    }

    @Override
    public StructureStartFactory getStartFactory() {
        return GraveyardStructure.Start::new;
    }

   /* 
    public int getSize() {
        return 3;
    }

    protected int getSeedModifier() {
        return 123456789;
    }

    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return 8;// Math.max(IafConfig.spawnGorgonsChance, 2);
    }

    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return 4; //Math.max(IafConfig.spawnGorgonsChance / 2, 1);
    }*/

    public static class Start extends StructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> structure, ChunkPos chunkPos, int refCount, long seed) {
            super(structure, chunkPos, refCount, seed);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistries, ChunkGenerator chunkGenerator, StructureManager templateManager, ChunkPos pos, Biome biome, NoneFeatureConfiguration config, LevelHeightAccessor height) {
            Rotation rotation = Rotation.getRandom(this.random);
            int xOffset = 5;
            int yOffset = 5;
            if (rotation == Rotation.CLOCKWISE_90) {
                xOffset = -5;
            } else if (rotation == Rotation.CLOCKWISE_180) {
                xOffset = -5;
                yOffset = -5;
            } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                yOffset = -5;
            }

            int x = pos.getMiddleBlockX();
            int z = pos.getMiddleBlockZ();
            int y1 = chunkGenerator.getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, height);
            int y2 = chunkGenerator.getFirstOccupiedHeight(x, z + yOffset, Heightmap.Types.WORLD_SURFACE_WG, height);
            int y3 = chunkGenerator.getFirstOccupiedHeight(x + xOffset, z, Heightmap.Types.WORLD_SURFACE_WG, height);
            int y4 = chunkGenerator.getFirstOccupiedHeight(x + xOffset, z + yOffset, Heightmap.Types.WORLD_SURFACE_WG, height);
            int yMin = Math.min(Math.min(y1, y2), Math.min(y3, y4));
            BlockPos blockpos = pos.getMiddleBlockPosition(yMin + 1);
            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            // No manual pieces class needed.
            JigsawPlacement.addPieces(
                dynamicRegistries,
                new JigsawConfiguration(() -> dynamicRegistries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                    .get(new ResourceLocation(IceAndFire.MODID, "graveyard/top_pool")),
                    2),
                PoolElementStructurePiece::new,
                chunkGenerator,
                templateManager,
                blockpos,
                this,
                this.random,
                false,
                false,
                height);
            this.createBoundingBox();
        }
    }
}
