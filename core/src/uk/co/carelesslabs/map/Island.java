package uk.co.carelesslabs.map;

import java.util.ArrayList;
import java.util.Iterator;
import uk.co.carelesslabs.Enums.TileType;
import uk.co.carelesslabs.Media;
import uk.co.carelesslabs.box2d.Box2DHelper;
import uk.co.carelesslabs.box2d.Box2DWorld;
import uk.co.carelesslabs.entity.Entity;
import uk.co.carelesslabs.entity.Tree;
import uk.co.carelesslabs.managers.ObjectManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Island {  
    public Tile centreTile;
    Tile clickedTile;
    public int chunkSize = 3;
    int chunkWidth = 32;
    int chunkTileSize = 8;
    int chunkTileWidth = chunkWidth * chunkTileSize;
    
    // Stores Entities and Chunk data
    // Will allow all game objects to be converted to JSON in one conversion
    public ObjectManager objectManager;
    
    // TRACK CLICK
    int currentTileNo;
    int currentCol;
    int currentRow;
    
    public Island(Box2DWorld box2D){
        objectManager = new ObjectManager();
    }
    
    public void reset(Box2DWorld box2D) {
        System.out.println("Reset");
        objectManager.clearAll(box2D);
        box2D.clearAllBodies();
        setupTiles();
        codeTiles();
        generateHitboxes(box2D);
        addEntities(box2D);
    }
     
    private void generateHitboxes(Box2DWorld box2D) {
        // Loop all of the rows of chunks
        for (Integer key : objectManager.chunks.descendingKeySet()) {
            Chunk chunk = objectManager.chunks.get(key);
            
            for(ArrayList<Tile> row : chunk.tiles){
                for(Tile tile : row){ 
                    if(tile.isNotPassable() && tile.notIsAllWater()){
                        Box2DHelper.createBody(box2D.world, chunk.tileSize, chunk.tileSize, 0, 0, tile.pos, BodyType.StaticBody);
                    }
                }
            }     
        }
    }
  
    private void setupTiles(){
        // 3 x 3 chunks
        int currentRow = 0;
        Chunk chunk = new Chunk(chunkWidth, chunkWidth, chunkTileSize);
        ArrayList<Tile> tileArray;
        
        // ISLAND VARS
        int rngW = MathUtils.random(5,8);
        int rngH = MathUtils.random(5,8);
        
        int centreTileRow = chunk.numberRows / 2;
        int centreTileCol = chunk.numberCols /2;
        int firstTileRow = centreTileRow - (rngH);
        
        int maxRow = centreTileRow + rngH;
        int minRow = centreTileRow - rngH;
        int maxCol = centreTileCol + rngW;
        int minCol = centreTileCol - rngW;
        
        // CHUNK ROWS AND COLUMNS
        for(int chunkRow = 0; chunkRow < chunkSize; chunkRow ++){
            for(int chunkCol = 0; chunkCol < chunkSize; chunkCol ++){
                chunk = new Chunk(chunkWidth, chunkWidth, chunkTileSize);
                tileArray = new ArrayList<Tile>();
                chunk.chunkNumber =  (chunkRow * chunkSize) + chunkCol;
                chunk.col = chunkCol;
                chunk.row = chunkRow;
                
                // TILES
                for(int row = 0; row < chunk.numberRows; row ++){
                    for(int col = 0; col < chunk.numberCols; col ++){
                        Tile tile = new Tile(col, row, chunk.tileSize, TileType.WATER, MapGenerator.randomWater(), chunk);
                        
                        // Middle chunk becomes an island
                        if(chunk.chunkNumber == 4){
                            // Make a small island
                            if(row > minRow && row < maxRow && col > minCol && col < maxCol){
                                tile.texture = MapGenerator.randomGrass();
                                tile.tileType = TileType.GRASS;
                                
                                if(row == firstTileRow + 1){
                                    tile.texture = Media.cliff;
                                    tile.tileType = TileType.CLIFF;
                                } else {
                                    // Chance to add trees etc
                                }
                            } 
                        }
                        
                        // ADD TILE TO CHUNK
                        if(currentRow == row){
                            // Add tile to current row
                            tileArray.add(tile);
                            
                            // Last row and column?
                            if (row == chunk.numberRows - 1 && col == chunk.numberCols - 1){
                                chunk.tiles.add(tileArray);
                            }
                        } else { 
                            // New row
                            currentRow = row;
                            
                            // Add row to chunk
                            chunk.tiles.add(tileArray);
                            
                            // Clear chunk row
                            tileArray = new ArrayList<Tile>();
                            
                            // Add first tile to the new row
                            tileArray.add(tile);
                        }
                    }
                }

                objectManager.chunks.put(chunk.chunkNumber, chunk);
                currentRow = 0;
            }
        }
        
        // Set centre tile for camera positioning
        centreTile = objectManager.chunks.get(4).tiles.get(centreTileRow).get(centreTileCol);
        
        // Set the current Chunk
        objectManager.currentChunk = objectManager.chunks.get(4);
       
    }
    
    private void updateImage(Tile tile) {
       MapGenerator.setTileSecondaryTexture(tile);        
    }
      
    private void codeTiles() {
        // Loop all tiles and set the initial code
        // Loop all of the rows of chunks
        for (Integer key : objectManager.chunks.descendingKeySet()) {
            // One row of chunks
            Chunk chunk = objectManager.chunks.get(key);
            
            for(ArrayList<Tile> row : chunk.tiles){
                for(Tile tile : row){ 
                    // Check all surrounding tiles and set 1 for pass 0 for non pass
                    // 0 0 0
                    // 0 X 0
                    // 0 0 0
                    
                    int[] rows = {1,0,-1};
                    int[] cols = {-1,0,1};
                    
                    for(int r: rows){
                        for(int c: cols){
                            tile.code += chunk.getTileCode(tile.row + r, tile.col + c);
                            updateImage(tile);
                        }
                    }    
                }
            }      
        }
    }
       
    private void addEntities(Box2DWorld box2D) {
        // Loop all of the rows of chunks
        for (Integer key : objectManager.chunks.descendingKeySet()) {
            // One chunk
            Chunk chunk = objectManager.chunks.get(key);

            for(ArrayList<Tile> row : chunk.tiles){
                // Loop all tiles and add random trees
                for(Tile tile : row){ 
                    if (tile.isGrass()){
                        if(MathUtils.random(100) > 90){
                            objectManager.entities.add(new Tree(tile.pos, box2D));
                        }    
                    }   
                }
            }
        }
    }
    
    public Vector3 getCentrePosition(){
        return centreTile.pos;
    }
 
    public void dispose() {
        
    }
    
    public void clearRemovedEntities(Box2DWorld box2D) {
        Iterator<Entity> it = objectManager.entities.iterator();
        while(it.hasNext()) {
            Entity e = it.next();
            if(e.remove){
                e.removeBodies(box2D);
                box2D.removeEntityToMap(e);
       	
                it.remove();
            }
        }
    }

    public boolean hasEntities() {
        return  objectManager.entities != null && objectManager.entities.size() > 0;
    }

    public Chunk chunkAt(Vector2 pos) {
        // Width of one chunk
        int row = (int) pos.y / chunkTileWidth;
        int col = (int) pos.x / chunkTileWidth;
        int chunkNumber =  (row * chunkSize) + (int) col;

        return objectManager.chunks.get(chunkNumber);    
    }

}
