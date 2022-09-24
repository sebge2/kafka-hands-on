package be.sgerard.kafka;

import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;

@UdfDescription(
        name = "taxi_wait",
        description = "Return expected wait time in minutes",
        version = "0.1.0",
        author = "Simon Aubury"
)
public class TaxiWait {

    public static final int DEFAULT_FACTOR = 5;

    @Udf(description = "Given weather and distance return expected wait time in minutes")
    public double taxi_wait(final String weatherDescription, final double dist) {
        final double factor = computeFactor(weatherDescription);

        return dist * factor / 50.0;
    }

    private static double computeFactor(String weatherDescription) {
        if (weatherDescription == null) {
            return DEFAULT_FACTOR;
        }

        switch (weatherDescription.toLowerCase()) {
            case "light rain":
                return 2;
            case "heavy rain":
                return 4;
            case "fog":
                return 6;
            case "haze":
                return 1.5;
            case "sunny":
                return 1;
            default:
                return DEFAULT_FACTOR;
        }
    }
}





