create stream requested_journey_waiting as select
    user,
    round(distance) as distance,
    weather_description,
    round(TAXI_WAIT(weather_description, distance)) as waiting_time_in_minn
from requested_journey;