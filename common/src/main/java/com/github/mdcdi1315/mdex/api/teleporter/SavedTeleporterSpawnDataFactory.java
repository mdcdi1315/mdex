package com.github.mdcdi1315.mdex.api.teleporter;

import net.minecraft.nbt.CompoundTag;

import java.util.function.Function;
import java.util.function.Supplier;

public record SavedTeleporterSpawnDataFactory(Supplier<TeleporterSpawnData> createInstance , Function<CompoundTag , TeleporterSpawnData> loadfunction)
{ }
