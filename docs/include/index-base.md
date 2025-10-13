# Package com.ekezet.hurok

A multiplatform UDF-based application framework

```mermaid
flowchart LR
    A[First State] -->|Input| B{Action} -->|Mutate| C(Model) --> D[Renderer] -->|Derive| E[Next State] -.->|Input| B
    B -->|Trigger| F([Effect]) --> B
```

## Parts

| Name        | Description                                        |
|-------------|----------------------------------------------------|
| State       | Loop state derived from the `Model`                |
| Model       | Holds data for business logic                      |
| Args        | A way to pass inputs to an existing `Loop`         |
| ArgsApplyer | Applies arguments to the `Model`                   |
| Renderer    | Uses the `Model` to create new `State`             |
| Action      | Mutates the `Model` and can trigger (any) `Effect` |
| Effect      | Does background work and triggers (any) `Action`   |
| Loop        | Glue that holds the parts together                 |

# Package com.ekezet.hurok.test

# Utilities for library testing

Contains a general [Loop](com.ekezet.hurok.Loop) implementation (the [TestLoop]) and other tools for testing.
