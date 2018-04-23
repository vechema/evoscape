package uk.co.carelesslabs;

import java.util.ArrayList;
import uk.co.carelesslabs.map.Tile;
import uk.co.carelesslabs.map.Island;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class gameclass extends ApplicationAdapter {
    OrthographicCamera camera;
    Control control;
    SpriteBatch batch;

    // Display Size
    private int displayW;
    private int displayH;

    // Hero
    Hero hero;
    
    // Island
    Island island;

    @Override
    public void create() {
        Media.load_assets();
        batch = new SpriteBatch();
        
        // CAMERA
        displayW = Gdx.graphics.getWidth();
        displayH = Gdx.graphics.getHeight();
        
        // For 800x600 we will get 266*200
        int h = (int) (displayH/Math.floor(displayH/160));
        int w = (int) (displayW/(displayH/ (displayH/Math.floor(displayH/160))));
        
        camera = new OrthographicCamera(w,h);
        camera.zoom = .4f;
        
        // Used to capture Keyboard Input
        control = new Control(displayW, displayH, camera);
        Gdx.input.setInputProcessor(control);
        
        island = new Island();
        
        // Hero
        hero = new Hero(island.centreTile.pos);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // GAME LOGIC
        hero.update(control);

        Vector3 cameraPos = new Vector3();
        cameraPos.x = hero.pos.x + hero.width/2;
        cameraPos.y = hero.pos.y + hero.height/2;
        camera.position.lerp(cameraPos, .1f);
        camera.update();
        
        // GAME DRAW
        batch.setProjectionMatrix(camera.combined);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        batch.begin();
        // Draw all tiles in the chunk / chunk rows
        for(ArrayList<Tile> row : island.chunk.tiles){
            for(Tile tile : row){
                batch.draw(tile.texture, tile.pos.x, tile.pos.y, tile.size, tile.size);
                if (tile.secondaryTexture != null) batch.draw(tile.secondaryTexture, tile.pos.x, tile.pos.y, tile.size, tile.size);
            }
        }
        hero.draw(batch);
        batch.end();
    }
	
    @Override
    public void dispose () {
        batch.dispose();
    }
}
