# Data Models & Persistence Specification

## Domain Models

### `Car`
- `id`: String
- `name`: String
- `subtitle`: String
- `baseSpeed`: Int (e.g. 210 km/h)
- `baseDriftControl`: Int (1..100)
- `baseNitroCapacity`: Int (1..100)
- `colorHex`: Long
- `price`: Int
- `engineUpgradeLevel`: Int (0..5)
- `tiresUpgradeLevel`: Int (0..5)
- `nitroUpgradeLevel`: Int (0..5)

### `GirlCompanion`
- `id`: String
- `name`: String
- `title`: String
- `bio`: String
- `affection`: Int (0..100)
- `bonusType`: CompanionBonusType
- `baseBonusValue`: Float
- `dialogueLines`: List<String>
- `accentColorHex`: Long
- `favoriteGift`: String

### `GameSaveData`
- `cash`: Int (default: 2,500)
- `gems`: Int (default: 50)
- `selectedCarId`: String
- `selectedCompanionId`: String
- `ownedCarIds`: Set<String>
- `unlockedTrackIds`: Set<String>
- `companionAffinities`: Map<String, Int>
- `completedTutorial`: Boolean

## Save Payload Format (`save_payload`)
Delimited string format stored in `SharedPreferences` / `NSUserDefaults`:

`cash||gems||selectedCarId||selectedCompanionId||ownedCarIds||unlockedTrackIds||companionAffinities||completedTutorial||carCustoms`

- `ownedCarIds`: comma-separated (`trueno_ae86,rx7_spirit`)
- `unlockedTrackIds`: comma-separated (`track_tutorial,track_neon_city`)
- `companionAffinities`: semicolon-separated pairs (`aoi:25;sora:10`)
- `carCustoms`: semicolon-separated tuples (`carId:engineLvl,tiresLvl,nitroLvl,colorHex`)
