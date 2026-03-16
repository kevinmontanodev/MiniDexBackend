# Minidex API

MiniDex API is a backend service designed for a **digital Pokémon collecting and battling game**.
It allows players (trainers) to collect Pokémon, build teams, battle against an AI opponent, and manage their Pokédex.

The API is built with **Spring Boot** and uses **MongoDB (cloud-hosted)** as its primary database.

The system focuses on performance, scalability, and a clean architecture that separates **domain logic, application services, and infrastructure concerns.**

------------------------------------------------------------------------

## Overview
MiniDex combines elements of **collection, progression, and strategic battles**.

Players can:

- Register and authenticate as trainers
- Open booster packs to obtain Pokémon
- Build a team of 6 Pokémon
- Battle against an AI opponent
- Earn coins and experience
- Level up and evolve their Pokémon
- Purchase packs or special Pokémon from the shop

The game loop encourages players to **collect, train, and improve their teams** over time.

------------------------------------------------------------------------

## Core Features 

### Trainer Management
The API allows trainers to manage their profile and progress within the game.

Trainer information includes:

- Name
- Username
- Level
- Coins
- Pokédex collection
- Daily pack availability
- Shop state

Authentication is handled using **JWT (JSON Web Tokens)** to secure protected endpoints.

------------------------------------------------------------------------

## Pokémon Collection System

Pokémon are obtained through **booster packs**.

Each trainer receives:

- **3 free packs per day**
- Each pack contains **3 Pokémon**

Additionally, the shop offers:

- Purchasable booster packs
- A **daily special Pokémon** available for purchase

Coins used in the shop can be earned by:

- Transferring Pokémon
- Winning battles

------------------------------------------------------------------------

## Pokémon Generation and Caching

MiniDex integrates with the public **PokéAPI** to retrieve Pokémon data.

However, to improve performance and reduce API calls, the system uses a **two-stage approach**:

**Pack Pokémon (Lightweight Objects)**

When generating booster packs, the API uses cached **Pack Pokémon** objects.

These objects contain only minimal information:

- Pokémon name
- Image sprite
- Shiny status
- Rarity

This allows packs to be generated **very quickly without repeated external API calls.**

### Full Pokémon Objects

Once a trainer opens a pack, the system generates the **full Pokémon objects in the background** and stores them in the trainer's Pokédex.

These objects include:

- Stats
- Types
- Moves
- Evolution data
- And more usefully data

This approach significantly improves responsiveness when opening packs.

------------------------------------------------------------------------

## Battle System

MiniDex includes a **turn-based battle system** where trainers fight against an AI-controlled opponent.

Each trainer can create a **team of up to 6 Pokémon** and battle against enemy teams generated dynamically.

Winning battles rewards players with:

- Coins
- Experience points
- Pokémon level progression

When a Pokémon reaches **Level 5**, it becomes eligible for evolution.

Evolution costs **100 coins** and upgrades the Pokémon to its next stage.

------------------------------------------------------------------------

## Event-Based Battle Engine

The battle engine uses an **event-driven architecture** to process combat interactions.

Instead of returning only a final result, the battle system generates a sequence of battle events that represent everything that happened during the turn.

Examples of events include:

- `ATTACK`
- `FAINT`
- `SWITCH`
- `STRATEGIC_SWITCH`
- `FINISH_BATTLE`

This approach provides several advantages:

- Efficient server-side battle processing
- Clear battle state tracking
- Smooth animation handling in the frontend

The frontend consumes these events sequentially to animate the battle in real time.

------------------------------------------------------------------------

## Architecture
The project follows a **layered architecture inspired by Domain-Driven Design principles.**

The main layers include:

### Domain Layer

Contains the **core business logic of the game**, including:

- Battle engine
- Damage calculations
- Pokémon models
- Evolution logic
- AI decision systems

This layer is completely independent of frameworks.

### 📂 Arquitectura del Proyecto

``` text
src/
├── main/
│   ├── java/org/kmontano/minidex/
│   │   ├── application
│   │   │   └── BattleFacade.java
│   │   ├── service
│   │   │   ├── AuthService.java
│   │   │   ├── BattleSessionService.java
│   │   │   ├── DailyPackService.java
│   │   │   ├── EvolutionService.java
│   │   │   ├── PokedexService.java
│   │   │   ├── PokemonStoreService.java
│   │   │   ├── PokemonTypeCacheService.java
│   │   │   ├── RewardService.java
│   │   │   └── TrainerService.java
│   │   ├── serviceImpl
│   │   │   ├── AuthServiceImpl.java
│   │   │   ├── BattleSessionServiceImpl.java
│   │   │   ├── DailyPackServiceImpl.java
│   │   │   ├── EvolutionServiceImpl.java
│   │   │   ├── PokedexServiceImpl.java
│   │   │   ├── PokemonStoreServiceImpl.java
│   │   │   ├── PokemonTypeCacheServiceImpl.java
│   │   │   ├── RewardServiceImpl.java
│   │   │   └── TrainerServiceImpl.java
│   │   ├── auth/
│   │   │   ├── AuthUtils.java
│   │   │   ├── JwtFilter.java
│   │   │   └── JwtUtil.java
│   │   ├── config/
│   │   │   ├── AdminApiKeyFilter.java
│   │   │   ├── CorsConfig.java
│   │   │   ├── EncoderBean.java
│   │   │   ├── JacksonConfig.java
│   │   │   ├── RestTemplateConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   └── SwaggerConfig.java
│   │   ├── controllers/
│   │   │   ├── AuthController.java
│   │   │   ├── BattleController.java
│   │   │   ├── PokemonController.java
│   │   │   └── TrainerController.java
│   │   ├── domain/
│   │   │   ├── battle/
│   │   │   │   ├── action
│   │   │   │   │   ├── AttackAction.java
│   │   │   │   │   ├── BattleAction.java
│   │   │   │   │   └── SwitchAction.java
│   │   │   │   ├── calculator
│   │   │   │   │   └── DamageCalculator.java
│   │   │   │   ├── engine
│   │   │   │   │   ├── BattleEngine.java
│   │   │   │   │   ├── BattleEventCollector.java
│   │   │   │   │   ├── BattleFinisher.java
│   │   │   │   │   ├── BattleInitializer.java
│   │   │   │   │   ├── BattleStateEvaluator.java
│   │   │   │   │   ├── TimedBattle.java
│   │   │   │   │   └── TypeEffectivenessCalculator.java
│   │   │   │   ├── event
│   │   │   │   │   ├── AttackEventDTO.java
│   │   │   │   │   ├── BattleEventDTO.java
│   │   │   │   │   ├── FaintEventDTO.java
│   │   │   │   │   ├── FinishEventDTO.java
│   │   │   │   │   ├── StrategicSwitchEventDTO.java
│   │   │   │   │   └── SwitchEventDTO.java
│   │   │   │   ├── model
│   │   │   │   │   ├── AttackResult.java
│   │   │   │   │   ├── BattleContext.java
│   │   │   │   │   ├── BattleEventTypes.java
│   │   │   │   │   ├── BattleLogEntry.java
│   │   │   │   │   ├── BattleSide.java
│   │   │   │   │   ├── BattleStatus.java
│   │   │   │   │   ├── BattleTurn.java
│   │   │   │   │   └── HitResult.java
│   │   │   │   └── service
│   │   │   │       └── AttackResolutionService.java
│   │   │   ├── enemy/
│   │   │   │   ├── decision/
│   │   │   │   │   ├── AiDecision.java
│   │   │   │   │   ├── SwitchCandidateSelector.java
│   │   │   │   │   └── SwitchDecisionPolicy.java
│   │   │   │   ├── factory/
│   │   │   │   │   └── EnemyTeamFactory.java
│   │   │   │   ├── service/
│   │   │   │   │   └── EnemyAiDecisionService.java
│   │   │   │   ├── EnemyAiService.java
│   │   │   │   └── EnemyBattleState.java
│   │   │   ├── pokedex/
│   │   │   │   └── Pokedex.java
│   │   │   ├── pokemon/
│   │   │   │   ├── Move.java
│   │   │   │   ├── NextEvolution.java
│   │   │   │   ├── Pokemon.java
│   │   │   │   ├── PokemonType.java
│   │   │   │   ├── PokemonTypeCache.java
│   │   │   │   ├── PokemonTypeRef.java
│   │   │   │   ├── Rarity.java
│   │   │   │   ├── RarityMapper.java
│   │   │   │   ├── Sprites.java
│   │   │   │   └── Stats.java
│   │   │   ├── pokemonShop/
│   │   │   │   └── TrainerShopState.java
│   │   │   └── trainer/
│   │   │       ├── DailyPackStatus.java
│   │   │       └── Trainer.java
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── BattleTurnRequest.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── PlayerActionRequest.java
│   │   │   │   └── UpdateNameAndUsernameRequest.java
│   │   │   ├── response/ #most importants
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── BattleTurnResponse.java
│   │   │   │   ├── BoosterResponseDTO.java
│   │   │   │   ├── EvolutionPokemonResponse.java
│   │   │   │   ├── PokedexDTO.java
│   │   │   │   ├── PokemonDTO.java
│   │   │   │   ├── PokemonStoreDTO.java
│   │   │   │   ├── StartBattleResponse.java
│   │   │   │   └── TrainerDTO.java
│   │   │   └── shared/
│   │   │   │   ├── ActionType.java
│   │   │   │   ├── BattlePokemon.java
│   │   │   │   └── BattleReward.java
│   │   ├── exception/
│   │   │   ├── DomainConflictException.java
│   │   │   ├── DomainException.java
│   │   │   ├── DomainValidationException.java
│   │   │   ├── EnvelopeLimitReachedException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── factory/
│   │   │   ├── MoveFactory.java
│   │   │   ├── PackPokemonFactory.java
│   │   │   └── PokemonFactory.java
│   │   ├── infrastructure/
│   │   │   ├── api/
│   │   │   │   ├── dto/
│   │   │   │   │   ├── Generationlx.java
│   │   │   │   │   ├── ScarletViolet.java
│   │   │   │   │   ├── TypeApiResponse.java
│   │   │   │   │   └── TypeSprites.java
│   │   │   │   └── PokemonApiClient.java
│   │   │   ├── cache/
│   │   │   │   ├── CacheAdminController.java
│   │   │   │   ├── PackPokemonCache.java
│   │   │   │   ├── PackPokemonCacheRepository.java
│   │   │   │   └── PackPokemonCacheService.java
│   │   │   ├── mapper/ # most importants
│   │   │   │   ├── ChainLink.java
│   │   │   │   ├── EvolutionChain.java
│   │   │   │   ├── Home.java
│   │   │   │   ├── Species.java
│   │   │   │   └── Sprites.java
│   │   │   └── repository/
│   │   │       ├── PokedexRepository.java
│   │   │       ├── PokemonTypeCacheRepository.java
│   │   │       ├── TrainerRepository.java
│   │   │       └── TrainerShopStateRepository.java
│   │   └── MinidexApplication.java
│   └── resources/
│       ├── application.properties
```

------------------------------------------------------------------------

## Application Layer

Coordinates domain logic through **facades and services.**

Responsibilities include:

- Managing battle sessions
- Processing trainer actions
- Handling rewards and progression
- Coordinating evolution and pack opening

------------------------------------------------------------------------

## Infrastructure Layer

Handles integration with external systems and technical concerns such as:

- MongoDB repositories
- PokéAPI client
- caching systems
- external data mapping

------------------------------------------------------------------------

## Presentation Layer

The API exposes functionality through **REST controllers**, which serve as entry points for the frontend application.

Controllers handle:

- Request validation
- Authentication
- DTO transformations
- Error handling

------------------------------------------------------------------------

## Security

MiniDex uses **JWT-based authentication** to secure its endpoints.

The authentication system includes:

- Login and registration
- JWT token generation
- JWT validation filters
- Spring Security configuration

Protected endpoints require a valid JWT token in the request headers.

------------------------------------------------------------------------

## Error Handling

The API implements a **centralized exception handling system** using a global exception handler.

Custom exceptions include:

- ResourceNotFoundException
- DomainValidationException
- DomainConflictException
- EnvelopeLimitReachedException

This ensures that API responses remain **consistent and predictable.**

------------------------------------------------------------------------

## Data Model (MongoDB)

MiniDex uses **MongoDB** as its main persistence layer.

Core collections include:

# Trainers

Stores trainer profiles and progression data.

Example structure:

``` json
{
  "id": string,
  "name": string,
  "username": string,
  "coins": number,
  "level": number,
  "xp" : number,
  "wins": number,
  "loses": number,
  "dailyPackStatus": {
    "numEnvelopes": number,
    "lastResetDate": string
  }
}
```



Pokémon

Pokémon are stored as embedded objects within a trainer's Pokédex.

Each Pokémon contains:
```json
{
    "uuid": string,
    "numPokedex": number,
    "name": number,
    "rarity": string,
    "sprites": {
        "smallFront": string,
        "smallBack": string,
        "mainImage": string
    },
    "shiny": boolean,
    "level": number,
    "nextEvolution": string,
    "stats": {
        "hp": number,
        "attack": number,
        "defense": number,
        "speed": number
    },
  "types": [
    {
      "name": string,
      "iconUrl": string
    }
  ],
  "moves": [
    {
        "moveName": string,
        "type": string,
        "power": number,
        "accuracy": number
    }
  ],
  "canEvolve": boolean
}
```

- stats
- moves
- type references
- shiny status
- level
- evolution data

This structure allows trainers to manage their collections efficiently without complex relational joins.

------------------------------------------------------------------------

## Performance Considerations

Several strategies were implemented to improve performance:

- Pokémon type caching
- Pack Pokémon caching
- Background Pokémon generation
- Event-based battle responses

These optimizations reduce external API calls and improve response times during gameplay.

------------------------------------------------------------------------

## Battle Engine Architecture

The battle system in **MiniDex** is implemented using an **event-driven battle engine** designed to be deterministic, extensible, and optimized for frontend animation.

Instead of returning only the final battle state, the engine produces a sequence of battle events that represent everything that happened during a turn. These events allow the frontend to render animations such as attacks, damage, switches, and fainting in the correct order.

The battle engine is composed of several core components:

- `BattleInitializer`
- `BattleEngine`
- `AttackResolutionService`
- `BattleEventCollector`
- `BattleFinisher`

------------------------------------------------------------------------
## Battle Lifecycle

A battle follows the lifecycle below:

- Battle Initialization
- Turn Execution
- Event Generation
- Battle State Evaluation
- Battle Finish

------------------------------------------------------------------------
## Battle Initialization

The `BattleInitializer` is responsible for preparing the battle context.

It retrieves the trainer's team from the Pokédex and generates a random enemy team using the `EnemyTeamFactory`.

```java
BattleContext context = new BattleContext(
    playerTeam.get(0),
    enemyTeam.get(0),
    playerTeam,
    enemyTeam,
    trainerId
);
```

The initializer validates that the player has a **full team of 6 Pokémon** before allowing the battle to start.

The resulting `BattleContext` contains:

- active player Pokémon
- active enemy Pokémon
- both teams
- trainer identifier
- battle status

This context acts as the **single source of truth** for the battle state.

------------------------------------------------------------------------
## Battle Context

The `BattleContext` class represents the entire battle state.

It stores:

- active Pokémon for each side
- the full teams
- the trainer id
- battle status

It also provides utility methods to evaluate the state of the battle:
```java
public boolean isPlayerDefeated();
public boolean isEnemyDefeated();
```

These methods determine if **all Pokémon on a team have fainted**, which ends the battle.

The context also exposes switching operations:
```java
switchPlayer();
switchEnemy();
```
------------------------------------------------------------------------
## Turn Execution

Each turn is executed by the `BattleEngine`.

The engine receives:

- player action
- enemy action
- battle context
- event collector

```java
executeTurn(context, playerAction, enemyAction, collector);
```

The engine performs three main steps:

### 1. Action Ordering

Actions are sorted based on:

1. Action priority
2. Pokémon speed
3. Random tie-breaker

```java
.sorted((a,b) -> {
    int priorityCompare = Integer.compare(b.getPriority(), a.getPriority());
```

This replicates mechanics similar to the original Pokémon games where some actions (like switching) have higher priority.

------------------------------------------------------------------------
### 2. Validity Checks

Before executing an action, the engine verifies if the Pokémon is still able to act.

For example, if a Pokémon fainted earlier in the same turn, its action is skipped unless the action is a switch.

------------------------------------------------------------------------

3. Action Execution

Each action executes itself using the **Command pattern**.

```java
BattleEventDTO event = action.execute(context);
```

The resulting event is then stored in the event collector.

------------------------------------------------------------------------
## Event Driven System

Instead of mutating the UI directly, the battle engine produces **events** that describe what happened during the turn.

These events are stored in the `BattleEventCollector`.
```java
collector.add(event);
```

At the end of the turn, the events are returned to the frontend.

------------------------------------------------------------------------
## Battle Events

The system defines several event types:

| Event	|             Description              |
| :--- | :---: |
| `ATTACK` |    	A Pokémon performed an attack    |
| `FAINT`	|          A Pokémon fainted           |
| `SWITCH` |       	Player switched Pokémon       |
| `STRATEGIC_SWITCH` |       	Enemy switched Pokémon        |
| `FINISH_BATTLE` | 	Battle finished and rewards granted |

All events extend the base class:
```java
abstract class BattleEventDTO
```

Each event contains the information required by the frontend to animate the action.

------------------------------------------------------------------------

## Attack Event

Represents a successful or failed attack.

Information included:

- attacking side
- move name
- damage dealt
- HP before and after
- effectiveness multiplier
- hit result (normal or critical)

Example structure:
```json
{
    "type": "ATTACK",
    "side": "PLAYER",
    "moveName": "thunderbolt",
    "damage": 34,
    "effectiveness": 2.0
}
```

------------------------------------------------------------------------
## Faint Event

Triggered when a Pokémon reaches 0 HP.
```json
{
  "type": "FAINT",
  "side": "ENEMY",
  "pokemonId": "uuid"
}
```

------------------------------------------------------------------------
## Switch Event

Occurs when a Pokémon is replaced with another one from the team.

Two types of switch events exist in the system:

- `SWITCH` → Standard switch triggered after a Pokémon faints.
- `STRATEGIC_SWITCH` → Tactical switch performed intentionally by the enemy AI or player.

### Faint Switch (Standard Switch)

When a Pokémon faints, it must be replaced by another Pokémon from the team.  
This type of switch **does not consume the turn**.

The behavior differs depending on the side:

- **Player:** the system waits for the frontend to select the next Pokémon.
- **Enemy:** the replacement Pokémon is selected automatically by the AI.

This ensures that fainting does not unfairly cost a turn.

### Strategic Switch

A `STRATEGIC_SWITCH` is a tactical decision made by the enemy AI or player during battle.

Unlike faint switches, this action **does consume the turn**, since it is treated as a normal battle action and follows the turn ordering rules.

This allows the AI or player to switch Pokémon when it detects disadvantageous conditions, such as:

- low HP
- type disadvantage
- unfavorable battle state

------------------------------------------------------------------------
## Finish Battle Event

Generated when the battle ends.

This event includes:

- coins earned
- experience gained
- level up information
- victory or defeat

Example:
```json
{
  "type": "FINISH_BATTLE",
  "coins": 120,
  "xpEarned": 50,
  "levelUp": true,
  "playerWin": true
}
```
------------------------------------------------------------------------
Attack Resolution

Attack resolution is handled by the `AttackResolutionService`.

The attack process follows several steps:

1. Accuracy check
2. Type effectiveness calculation
3. STAB bonus
4. Base damage calculation
5. Critical hit calculation
6. Final damage output

------------------------------------------------------------------------
## Damage Formula

The damage formula is implemented in `DamageCalculator`.
```java
((2 * level / 5 + 2) * power * attack / defense) / 50 + 2
```

The result is then modified by:

- type effectiveness
- STAB multiplier
- critical hit
- internal balancing multiplier

This simplified formula allows battles to remain **fast while preserving core Pokémon mechanics**.

------------------------------------------------------------------------
## Type Effectiveness

Type interactions are handled by `TypeEffectivenessCalculator`.

The system stores effectiveness rules in a static lookup table:
```java
Map<PokemonType, Map<PokemonType, Double>>
```

Multipliers include:

| Multiplier |	Meaning |
| :--- | :---: |
| `2.0`	| Super effective |
| `0.5` |	Not very effective |
| `1.0` |	Neutral |

When a defender has **multiple types**, multipliers are combined.

------------------------------------------------------------------------
## Critical Hits

Critical hits are calculated with a probability system.

Default values:

- 6.25% chance for a critical hit
- 1.5x damage multiplier

Some critical hits may also trigger a **lucky bonus multiplier**.

------------------------------------------------------------------------
## Battle Expiration

Battles are wrapped inside a `TimedBattle`.
```java
public boolean isExpired()
```

A battle automatically expires after **30 minutes**.

This prevents abandoned battle sessions from persisting indefinitely.

------------------------------------------------------------------------

## Battle Rewards

When a battle ends, the `BattleFinisher` handles the final logic.

It performs several operations:

1. Calculates rewards
2. Applies coins and experience to the trainer
3. Updates the Pokédex team levels
4. Records wins or losses
5. Generates the `FINISH_BATTLE` event

Example reward structure:

- coins
- experience
- trainer level progression
- Pokémon level increases

------------------------------------------------------------------------
Why Event-Driven Battles?

This architecture was chosen for several reasons:

### Frontend animation support

The frontend receives a **timeline of events**, allowing it to animate:

- attacks
- damage
- fainting
- switches
- rewards

in the correct order.

------------------------------------------------------------------------
## Clear battle state transitions

Events act as **explicit state transitions**, making debugging easier.

------------------------------------------------------------------------
## Extensibility

New mechanics can be introduced easily, such as:

- status effects
- abilities
- weather
- new move types

without rewriting the battle engine.

------------------------------------------------------------------------


## ⚙️ API Endpoints
All endpoints are prefixed with:
```text
/api/v1
```

Unless otherwise specified, endpoints require JWT authentication via:
```text
Authorization: Bearer <token>
```

------------------------------------------------------------------------

## Authentication
### Register Trainer

Creates a new trainer account and returns an authentication token.

### POST
```text
/auth/register
```

### Request
```json
{
  "name": "Ash Ketchum",
  "username": "ash",
  "password": "pikachu123"
}
```

### Response `200`
```json
{
  "token": "jwt_token",
  "trainerDTO": {
    "name": "Ash Ketchum",
    "username": "ash",
    "level": 1,
    "xp": 0,
    "coins": 500,
    "wins": 0,
    "loses": 0,
    "dailyPackStatus": {
      "numEnvelopes": 3,
      "lastResetDate": "2026-02-02"
    }
  }
}
```
### Error `409`
```json
{
  "message": "Username already exists"
}
```

------------------------------------------------------------------------

## Login

Authenticates an existing trainer and returns a JWT token.

### POST
```text
/auth/login
```

### Request
```json
{
    "username": "ash",
    "password": "pikachu123"
}
```

### Response `200`
```json
{
    "token": "jwt_token",
    "trainerDTO": {
        "name": "Ash Ketchum",
        "username": "ash",
        "level": 1,
        "xp": 0,
        "coins": 500,
        "wins": 0,
        "loses": 0,
      "dailyPackStatus": {
        "numEnvelopes": 3,
        "lastResetDate": "2026-02-02"
      }
    }
}
```

### Error `409`
```json
{
  "message": "Invalid credentials"
}
```
------------------------------------------------------------------------
## Trainer Endpoints

Base path:
```text
/trainers/me
```

------------------------------------------------------------------------

## Get Trainer Profile

Returns the authenticated trainer information.

### GET
```text
/trainers/me
```
### Response
```json
{
  "name": "Ash Ketchum",
  "username": "ash",
  "level": 1,
  "xp": 0,
  "coins": 500,
  "wins": 0,
  "loses": 0,
  "caughtPokemons": 12
}
```

------------------------------------------------------------------------
## Update Trainer Profile

Updates trainer name or username.

### PUT
```text
/trainers/me
```

### Request
```json
{
    "name": "Pueblo Paleta",
    "username": "ash"
}
```

### Response

Returns a new JWT token.
```json
{
"token": "new_token",
    "trainerDTO": {
        "name": "Pueblo Paleta",
        "username": "ash",
        "level": 1,
        "xp": 0,
        "coins": 500,
        "wins": 0,
        "loses": 0,
        "dailyPackStatus": {
            "numEnvelopes": 3,
            "lastResetDate": "2026-02-02"
        }
    }
}
```
------------------------------------------------------------------------
## Pokédex
### Get Pokédex

Returns the trainer's Pokémon collection and current team.

### GET
```text
/trainers/me/pokedex
```

### Response Example
```json

{
  "pokemons": [
      {
        "uuid": "2b4c08fb-c5df-4b38-b81c-98eeb56886fe",
        "numPokedex": 135,
        "name": "jolteon",
        "rarity": "UNCOMMON",
        "sprites": {
          "smallFront": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/135.gif",
          "smallBack": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/back/135.gif",
          "mainImage": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/135.png"
        },
        "shiny": false,
        "level": 1,
        "nextEvolution": null,
        "stats": {
          "hp": 65,
          "attack": 65,
          "defense": 60,
          "speed": 130
        },
        "types": [
          {
            "name": "electric",
            "iconUrl": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/types/generation-ix/scarlet-violet/13.png"
          }
        ],
        "moves": [
          {
            "moveName": "tera-blast",
            "type": "NORMAL",
            "power": 80,
            "accuracy": 100
          },
          {
            "moveName": "growl",
            "type": "NORMAL",
            "power": null,
            "accuracy": 100
          },
          {
            "moveName": "skull-bash",
            "type": "NORMAL",
            "power": 130,
            "accuracy": 100
          },
          {
            "moveName": "confide",
            "type": "NORMAL",
            "power": null,
            "accuracy": null
          }
        ],
        "canEvolve": false
      }
    ],
  "page": 0,
  "totalPages": 1,
  "totalElements": 1
}
```

Full Pokémon objects include:

- stats
- types
- moves
- sprites
- evolution data

------------------------------------------------------------------------
## Daily Packs
### Check Daily Packs

Returns the trainer's available daily packs.

### GET
```text
/trainers/me/daily-packs
```

### Response
```json
{
  "numEnvelopes": 3,
  "lastResetDate": "2026-02-02"
}

```

------------------------------------------------------------------------
## Open Daily Pack

Opens a free daily booster pack.

### POST
```text
/trainers/me/daily-packs/open
```
### Response

Each pack contains **3 Pokémon**.
```json
{
  "pokemons": [
    {
      "name": "jolteon",
      "image": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/135.png",
      "shiny": false,
      "rarity": "UNCOMMON"
    },
    {
      "name": "starmie",
      "image": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/121.png",
      "shiny": false,
      "rarity": "UNCOMMON"
    },
    {
      "name": "sentret",
      "image": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/161.png",
      "shiny": false,
      "rarity": "COMMON"
    }
  ]
}
```
### Error `409`
```json
{
  "message": "Daily envelope limit already reached"
}
```

------------------------------------------------------------------------

## Team Management
### Get Team

Returns the trainer's active battle team.

### GET
```text
/trainers/me/team
```
### Response
```json
{
  "team": []
}
```

Maximum team size: **6 Pokémon**

------------------------------------------------------------------------
## Add Pokémon to Team

Adds a Pokémon from the Pokédex to the battle team.

### POST
```text
/trainers/me/team
```

### Request
```json
{
  "pokemonId": "64356388-f83e-4b85-91ab-643035d5c3a0"
}
```

### Response `200`

### Errors
```json
{
  "message": "Pokemon is not in the Pokedex"
}
```
```json
{
  "message": "Pokemon is already in the team"
}
```

------------------------------------------------------------------------
## Remove Pokémon from Team

### DELETE
```text
/trainers/me/team/{pokemonId}
```

### Response `204 No Content`

### Errors
```json
{
  "message": "Pokemon is not in the team"
}
```
------------------------------------------------------------------------
## Remove Pokémon from Pokedex

### DELETE
```text
/trainers/me/pokedex/{pokemonId}
```

It returns the coins and XP obtained from the transfer, as well as the trainer level.
### Response `200`
```json
{
  "level": 1,
  "xp": 34,
  "coins": 284
}
```

### Errors
```json
{
  "message": "Pokemon is not in pokedex"
} 
```
------------------------------------------------------------------------
## Pokémon Evolution

Allows a Pokémon to evolve if it meets the requirements.

### POST
```text
/trainers/me/pokemons/{pokemonId}/evolution
```
### Requirements

- Pokémon level ≥ 15
- Trainer must have 100 coins

### Response
```json
{
  "coins": 400,
  "xp": 100,
  "level": 1,
  "evolvedPokemon": {
    "uuid": "64356388-f83e-4b85-91ab-643035d5c3a0",
    "numPokedex": 162,
    "name": "furret",
    "rarity": "COMMON",
    "sprites": {
      "smallFront": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/162.gif",
      "smallBack": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/back/162.gif",
      "mainImage": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/162.png"
    },
    "shiny": false,
    "level": 15,
    "nextEvolution": null,
    "stats": {
      "hp": 85,
      "attack": 76,
      "defense": 64,
      "speed": 90
    },
    "types": [
      {
        "name": "normal",
        "iconUrl": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/types/generation-ix/scarlet-violet/1.png"
      }
    ],
    "moves": [
      {
        "moveName": "grass-knot",
        "type": "GRASS",
        "power": null,
        "accuracy": 100
      },
      {
        "moveName": "echoed-voice",
        "type": "NORMAL",
        "power": 40,
        "accuracy": 100
      },
      {
        "moveName": "me-first",
        "type": "NORMAL",
        "power": null,
        "accuracy": null
      },
      {
        "moveName": "whirlpool",
        "type": "WATER",
        "power": 35,
        "accuracy": 85
      }
    ],
    "canEvolve":false
  }
}
```

### Error `409`
```json
{
  "message": "Pokemon can't evolve"
}
```
------------------------------------------------------------------------
## Pokémon Shop
### Get Shop Information

Returns the daily shop configuration.

### GET
```text
/shop
```

### Response
```json
{
    "specialPokemon": {
        "name": "mr-mime",
        "image": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/122.png",
        "shiny": true,
        "rarity": "UNCOMMON"
    },
    "specialPokemonPrice": 200,
    "specialPokemonPurchased": false,
    "boosterPrice": 200,
    "boostersRemaining": 3
}
```

------------------------------------------------------------------------
## Buy Booster Pack

### POST
```text
/trainers/me/booster-packs
```

### Response
```json
{
  "pokemons": [
    {
      "name": "tentacruel",
      "image": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/73.png",
      "shiny": false,
      "rarity": "UNCOMMON"
    },
    {
      "name": "dragonair",
      "image": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/148.png",
      "shiny": false,
      "rarity": "UNCOMMON"
    },
    {
      "name": "hitmontop",
      "image": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/237.png",
      "shiny": false,
      "rarity": "UNCOMMON"
    }
  ]
}
```

### Error
```json
{
  "message": "Daily booster limit reached"
}
```

------------------------------------------------------------------------
## Buy Special Pokémon

### POST
```text
/trainers/me/pokemon
```

### Response
```json
{
    "coins": 200,
    "xp": 300,
    "level": 1
}
```

### Errors
```json
{
  "message": "Special pokemon already purchased today"
}
```
```json
{
  "message": "You don't have enough coins"
}
```

------------------------------------------------------------------------
## Battle System
### Start Battle

Creates a new battle session.

### POST
```text
/battle/start
```

### Response
```json
{
    "battleId": "2173c4c3-ec98-4456-bbed-418daf5563a6",
    "status": "IN_PROGRESS",
    "enemyName": "Team Rocket"
}
```

The response also includes:

- current player Pokémon
- enemy Pokémon
- player team

------------------------------------------------------------------------

## Battle Turn

Processes a battle action.

### POST
```text
/battle/turn
```

### Request
```json
{
    "battleId": "2173c4c3-ec98-4456-bbed-418daf5563a6",
    "pokemonUuid": "47fa0e94-cd22-4227-ba98-24b13d3c781f",
    "moveName": "acrobatics",
    "action": "ATTACK"
}
```

### Response

Returns the updated battle state and the events generated during the turn.
```json
{
  "battleId": "2173c4c3-ec98-4456-bbed-418daf5563a6",
  "status": "IN_PROGRESS",
  "player": {
    "pokemonId": "47fa0e94-cd22-4227-ba98-24b13d3c781f",
    "maxHp": 90,
    "currentHp": 90,
    "fainted": false
  },
  "enemy": {
    "pokemonId": "b143cd51-994e-4142-a0b1-1274c3ebe414",
    "maxHp": 60,
    "currentHp": 60,
    "fainted": false
  },
  "events": [
    {
      "type": "ATTACK",
      "hit": true,
      "side": "PLAYER",
      "hitResult": "NORMAL",
      "moveName": "acrobatics",
      "moveType": "FLYING",
      "damage": 100,
      "hpBefore": 90,
      "hpAfter": 0,
      "effectiveness": 2.0
    },
    {
      "type": "FAINT",
      "side": "ENEMY",
      "pokemonId": "33c9b79d-a026-404b-830f-3cc40cf2b59e"
    },
    {
      "type": "SWITCH",
      "side": "ENEMY",
      "newPokemon": {
        "pokemonId": "b143cd51-994e-4142-a0b1-1274c3ebe414",
        "name": "raichu",
        "maxHp": 60,
        "currentHp": 60,
        "sprites": {
          "smallFront": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/shiny/26.gif",
          "smallBack": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/back/shiny/26.gif",
          "mainImage": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/26.png"
        },
        "attack": 90,
        "defense": 55,
        "speed": 110,
        "level": 1,
        "moves": [
          {
            "moveName": "rest",
            "type": "PSYCHIC",
            "power": null,
            "accuracy": null
          },
          {
            "moveName": "mimic",
            "type": "NORMAL",
            "power": null,
            "accuracy": null
          },
          {
            "moveName": "calm-mind",
            "type": "PSYCHIC",
            "power": null,
            "accuracy": null
          },
          {
            "moveName": "thunder-punch",
            "type": "ELECTRIC",
            "power": 75,
            "accuracy": 100
          }
        ],
        "types": [
          "ELECTRIC"
        ],
        "fainted": false
      }
    }
  ]
}
```

Events may include:

- `ATTACK`
- `FAINT`
- `SWITCH`
- `STRATEGIC_SWITCH`
- `FINISH_BATTLE`

### Errors
```json
{
  "message": "Your Pokémon has fainted, you must switch"
}
```

------------------------------------------------------------------------

## Surrender Battle

Ends the current battle session.

### POST
```text
/battle/{battleId}/surrender
```

### Response `200 OK`
### Error
```json
{
  "message": "Battle already finished"
}
```

------------------------------------------------------------------------
## Error Format

All API errors follow a consistent format:
```json
{
  "message": "Error description"
}
```

Common HTTP codes:

| Code	| Meaning |
| :--- | :---: |
| 200	| Success |
| 204	| No content |
| 404	| Resource not found |
| 409	| Business rule conflict |
------------------------------------------------------------------------

## Configuration

The backend application is configured using a **Spring configuration file**.

In this repository the configuration is provided through:
```text
application.properties
```

For security reasons, the repository includes an example configuration file:
```text
application.properties.example
```

Developers should copy the example file and provide their own values.

Example configuration:
```text
spring.application.name=minidex

frontend.url=http://localhost:4321
api.version=v1

spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/minidex
spring.data.mongodb.database=minidex
spring.data.mongodb.auto-index-creation=true

admin.api.key=your_admin_key

jwt.secret=your_jwt_secret
jwt.expiration=86400000
```

------------------------------------------------------------------------

## Configuration Properties
### Application
| Property | Description |
| :------  | -------: |
| `spring.application.name` | Name of the application |
| `api.version` |	Version prefix used for API routes |

Example:
```text
/api/v1/...
```

------------------------------------------------------------------------

## Frontend Integration
| Property | Description |
| :------  | -------: |
| `frontend.url` | URL used for CORS configuration |

Example:
```text
http://localhost:4321
```

------------------------------------------------------------------------

## Database Configuration

The application uses:

- MongoDB

Required properties:

| Property | Description |
| :------  | -------: |
| `spring.data.mongodb.uri` | MongoDB connection string |
| `spring.data.mongodb.database` | Database name |
| `spring.data.mongodb.auto-index-creation` | Enables automatic index creation |

------------------------------------------------------------------------

## Authentication 

The API uses JWT authentication.

| Property | Description |
| :------  | -------: |
| `jwt.secret` | Secret used to sign tokens |
| `jwt.expiration` | Token expiration time in milliseconds |

------------------------------------------------------------------------

## Admin Security

Certain administrative endpoints require an Admin API Key.

| Property	      |                              Description |
|:---------------|-----------------------------------------:|
| `admin.api.key` |  Key used to access admin-only endpoints |

The key must be sent in the request header:
```text
X-ADMIN-KEY
```

------------------------------------------------------------------------

## Running the Application

The backend can be started using **two different methods**.

## Option 1 — Run with Docker (Recommended)

The project includes a **Dockerfile** that builds the application and runs it inside a container.

Technologies used:

- Docker
- Apache Maven
- Spring Boot

### Build the image
```bash
docker build -t minidex-api .
```
### Run the container
```bash
docker run -p 8080:8080 minidex-api
```

The API will be available at:
```text
http://localhost:8080
```

## Option 2 — Run with Maven

Developers can also run the application locally using Maven.

### Install dependencies
```bash
mvn clean install
```
### Run the application
```bash
mvn spring-boot:run
```

The application will start on:
```text
http://localhost:8080
```
------------------------------------------------------------------------

## Dockerfile Overview

The Dockerfile uses a **multi-stage** build to produce a lightweight runtime container.

### Build Stage

The first stage uses Maven to compile the project and generate the application JAR.
```dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS builder
```

Steps performed:

- download dependencies
- compile the project
- generate the final `.jar` file

### Runtime Stage

The second stage runs the application using a lightweight Java runtime.
```dockerfile
FROM eclipse-temurin:17-jdk
```

The compiled JAR is copied into the container and executed.
```dockerfile
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

The container exposes port:
```text
8080
```

------------------------------------------------------------------------

## Required Initialization

After starting the backend, the Pokémon cache must be preloaded before the application can generate packs.

This process fetches Pokémon data from:

- PokéAPI

and stores it in the internal cache collection.

To start the preload process:
```bash
curl -X POST http://localhost:8080/api/v1/admin/cache/preload \
-H "X-ADMIN-KEY: your_admin_key"
```

This will populate the Pokémon cache used by the pack generation system.

------------------------------------------------------------------------

## Related Repositories

Application logic and detailed documentation can be found in the following repositories:

Full Stack Aplication Repository (minidex-deploy)
https://github.com/kevinmontanodev/minidex-deploy

Frontend Repository
https://github.com/kevinmontanodev/MiniDexFrontEnd

Those repositories contain detailed documentation about:

- Full application stack ready to be used witch docker
- Frontend architecture
- Game mechanics
