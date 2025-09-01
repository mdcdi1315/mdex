package com.github.mdcdi1315.mdex.codecs;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Provides Codec manipulation &amp; easy creation methods.
 */
public final class CodecUtils
{
    private CodecUtils() {}

    /**
     * Gets a singleton of the 'probability' codec, that is a codec that can only take a floating range of values from 0 to 1, all inclusive.
     */
    public static Codec<Float> FLOAT_PROBABILITY = Codec.floatRange(0f , 1f);

    /**
     * Gets a singleton of the 'probability' codec, that is a codec that can only take a double-precision floating range of values from 0 to 1, all inclusive.
     */
    public static Codec<Double> DOUBLE_PROBABILITY = Codec.doubleRange(0d , 1d);

    public static <TCODEC , C1T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            Function<C1T, TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            BiFunction<C1T, C2T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            Function3<C1T, C2T , C3T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            Function4<C1T, C2T , C3T , C4T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            Function5<C1T , C2T , C3T , C4T , C5T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            Function6<C1T , C2T , C3T , C4T , C5T , C6T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            Function7<C1T , C2T , C3T , C4T , C5T , C6T , C7T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            Function8<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            Function9<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            Function10<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            Function11<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C12T> codecfield12,
            Function12<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11,
                        codecfield12
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C12T> codecfield12,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C13T> codecfield13,
            Function13<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11,
                        codecfield12,
                        codecfield13
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T , C14T> Codec<TCODEC> CreateCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C12T> codecfield12,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C13T> codecfield13,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C14T> codecfield14,
            Function14<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T , C14T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.create(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11,
                        codecfield12,
                        codecfield13,
                        codecfield14
                ).apply(instance , instancecreatefunction)
        ).stable();
    }

    public static <TCODEC , C1T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            Function<C1T, TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            BiFunction<C1T, C2T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            Function3<C1T, C2T , C3T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3
                ).apply(instance , instancecreatefunction)
        );
    }


    public static <TCODEC , C1T , C2T , C3T , C4T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            Function4<C1T, C2T , C3T , C4T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            Function5<C1T , C2T , C3T , C4T , C5T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            Function6<C1T , C2T , C3T , C4T , C5T , C6T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6
                ).apply(instance , instancecreatefunction)
        );
    }


    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            Function7<C1T , C2T , C3T , C4T , C5T , C6T , C7T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            Function8<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            Function9<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            Function10<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            Function11<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C12T> codecfield12,
            Function12<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11,
                        codecfield12
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C12T> codecfield12,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C13T> codecfield13,
            Function13<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11,
                        codecfield12,
                        codecfield13
                ).apply(instance , instancecreatefunction)
        );
    }

    public static <TCODEC , C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T , C14T> MapCodec<TCODEC> CreateMapCodecDirect(
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C1T> codecfield1,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C2T> codecfield2,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C3T> codecfield3,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C4T> codecfield4,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C5T> codecfield5,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C6T> codecfield6,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C7T> codecfield7,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C8T> codecfield8,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C9T> codecfield9,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C10T> codecfield10,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C11T> codecfield11,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C12T> codecfield12,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C13T> codecfield13,
            com.mojang.datafixers.kinds.App<RecordCodecBuilder.Mu<TCODEC> , C14T> codecfield14,
            Function14<C1T , C2T , C3T , C4T , C5T , C6T , C7T , C8T , C9T , C10T , C11T , C12T , C13T , C14T , TCODEC> instancecreatefunction
    )
    {
        return RecordCodecBuilder.mapCodec(
                (RecordCodecBuilder.Instance<TCODEC> instance) -> instance.group(
                        codecfield1,
                        codecfield2,
                        codecfield3,
                        codecfield4,
                        codecfield5,
                        codecfield6,
                        codecfield7,
                        codecfield8,
                        codecfield9,
                        codecfield10,
                        codecfield11,
                        codecfield12,
                        codecfield13,
                        codecfield14
                ).apply(instance , instancecreatefunction)
        );
    }

}