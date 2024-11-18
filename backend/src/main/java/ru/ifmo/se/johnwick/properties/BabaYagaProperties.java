package ru.ifmo.se.johnwick.properties;

import io.smallrye.config.ConfigMapping;

import java.time.Duration;

@ConfigMapping(prefix = "baba-yaga")
public interface BabaYagaProperties {

    HeadHuntOrderProperties headHuntOrder();

    RegularOrderProperties regularOrder();

    interface HeadHuntOrderProperties {
        Duration priceIncreaseInterval();

        double priceIncreaseFactor();
    }

    interface RegularOrderProperties {
        Duration cancellationInterval();

        Duration maxAgeForCancellation();
    }
}
