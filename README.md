# brain-iot-sensiNact-Door

sensiNact IoT gateway's southbound bridge allowing to connect to Robotnik Door Http API 

### door-api

In this module, the extended `BraintIoTEvents` allowing to use to interact with the robotnik's door HTTP based endpoint.

### door

In this module, the mechanism allowing to translate `BraintIoTEvents` into sensiNact's `AccessMethod` requests and next into HTTP calls, and sensiNact's `AccessMethod` responses into `BraintIoTEvents` is implemented

### door-app

This module provides a standalone executable jar archive allowing to launch a sensiNact IoT gateway instance integrated to a Brain-IoT `EventBus` through brain-iot-sensiNact-smartbehaviour, brain-iot-sensiNact-Door api and implementation for a local execution.
