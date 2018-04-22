package uk.co.carelesslabs.map;

import uk.co.carelesslabs.Enums.TileType;
import uk.co.carelesslabs.Entity;
import com.badlogic.gdx.graphics.Texture;

public class Tile extends Entity {
    public int size;
    public int row;
    public int col;
    public String code;
    public Texture secondary_texture;
    public Texture texture;
    public TileType type;
    
    public Tile(float x, float y, int size, TileType type, Texture texture){
        super();
        pos.x = x*size;
        pos.y = y*size;
        this.size = size;
        this.texture = texture;
        this.col = (int) x;
        this.row = (int) y;
        this.type = type;
        this.code = "";
    }

    public String details(){
        return "x: " + pos.x + " y: " + pos.y + " row: " + row
                + " col: " + col + " code: " + code + " type: " + type.toString();
    }

    public boolean is_grass() {
        return type == TileType.GRASS;
    }
    
    public boolean is_water() {
        return type == TileType.WATER;
    }
    
    public boolean is_cliff() {
        return type == TileType.CLIFF;
    }
    
    public boolean is_passable() {
        return !is_water() && !is_cliff();
    }
    
    public boolean is_not_passable() {
        return !is_passable();
    }
}
