# build.gradle
* Added gson and commons

# gameclass
* renamed to GameClass
* added SaveGame
* use objectmanager for island
* only sort island entities if not saving or loading

# Media
* add save icon

# Bird
* new constructor from json, box2dworld & objectmanager
* add getTile method to help json constructor
* create setup method to finish up bird creation for both constructors
* change moveToDestination

# Hero
* call super in constructor
* add constructor from json and box2dworld

# Tree
* add constructor from json and box2dworld
* create setup method to call from both constructors

# SaveGame - NEW class
* save - turn objectmanager into json then output to dir/entities.json
* load -  read save file into json, get all chunks then entities and turn them into objects, add those objects to objectmanager

# Zipper - NEW class
* compressString - encodes string to make it smaller
* uncompressSting - decode sting encoded by compressString method

# ObjectManager - NEW class
* variables to hold entities and chunks of gameclass
  * Now use multiple chunks
* clearAll - clear method for entities and chunks

# Chunk
* new constructor from json
* change row and col to be attribute of class, use float in some math

# Island
* Add variables for chunk size
* Use objectmanager instead of chunk and entities
* Move lots of map generation to MapGenerator class
  * updateImage method
* In setupTiles method, embed tile loops in chunk loops
  * Instead of a single chunk of 32x32 tiles, the world is 3x3 chunk of 32x32 tiles. So the world is 96x96
  * Chunk 4 is the middle chunk of the 3x3, so only that one gets land
  * All chunks other than 4 (the middle) get just water
* codeTiles also adds for loop chunks
* add accesser to chunk at Vector2 pos

# MapGenerator
* Move methods from Island class to this class
  * setTileSecondaryTile
  * randomGrass
  * randomWater

# Tile
* added chunk as attribute
* add constructor from json, chunk, and box2dworld
* add draw method here to take care of secondary tile

# SquareMenu
* Add save button and saving when that button is clicked
* Add load when settings button is clicked
