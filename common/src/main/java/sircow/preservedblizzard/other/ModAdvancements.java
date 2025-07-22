package sircow.preservedblizzard.other;

import net.minecraft.resources.ResourceLocation;
import sircow.preservedblizzard.Constants;

import java.util.List;

public class ModAdvancements {
    public static final List<ResourceLocation> EXCLUDED_ADVANCEMENTS = List.of(
            Constants.id("mastery/root"),
            Constants.id("mastery/adequate"),
            Constants.id("mastery/advanced"),
            Constants.id("mastery/beginner"),
            Constants.id("mastery/champion"),
            Constants.id("mastery/disciple"),
            Constants.id("mastery/infernal"),
            Constants.id("mastery/master"),
            Constants.id("mastery/novice"),
            Constants.id("mastery/starter"),
            ResourceLocation.withDefaultNamespace("end/root"),
            ResourceLocation.withDefaultNamespace("story/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "agriculture/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "combat/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "exploration/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "fishing/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "nether/root")
    );

    private ModAdvancements() {}
}
