package sircow.preservedblizzard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import sircow.preservedblizzard.effect.ModEffects;
import sircow.preservedblizzard.platform.Services;
import sircow.preservedblizzard.trigger.ModTriggers;

import java.util.*;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("pblizzard")) {
            Constants.LOG.info("Initialising Preserved: Blizzard");
            ModEffects.registerModEffects();
            ModTriggers.registerTriggers();
            // other
            suppressSpecificLogLines();
        }
    }

    public static void suppressSpecificLogLines() {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        final List<String> suppressedMessages = Arrays.asList(
                "Couldn't parse data file 'minecraft:shield'"
        );

        rootLogger.addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                String msg = event.getMessage().getFormattedMessage();
                if (msg != null) {
                    for (String suppressedMsgPart : suppressedMessages) {
                        if (msg.contains(suppressedMsgPart)) {
                            return Result.DENY;
                        }
                    }
                }
                return Filter.Result.NEUTRAL;
            }
        });
    }
}
