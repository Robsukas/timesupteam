package com.timesupteam.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.timesupteam.TimesUpTeamGame;

public class HUD implements Disposable {

    // Scene2D Stage and its Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    // Displayed variables
    private Integer worldTimer;
    private float timeCount;
    private Integer keyCount;
    private boolean timeUp;

    // Scene2D widgets
    Label countdownLabel;
    Label keyCountLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label characterLabel;

    public HUD(SpriteBatch sb) {
        worldTimer = 60;
        timeCount = 0;
        keyCount = 0;

        viewport = new FitViewport(TimesUpTeamGame.V_WIDTH, TimesUpTeamGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // Create our desired font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/NEON.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        BitmapFont font12 = generator.generateFont(parameter);
        generator.dispose();

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%02d", worldTimer), new Label.LabelStyle(font12, Color.WHITE));
        keyCountLabel =new Label(String.format("%01d", keyCount), new Label.LabelStyle(font12, Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(font12, Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(font12, Color.WHITE));
        worldLabel = new Label("LEVEL", new Label.LabelStyle(font12, Color.WHITE));
        characterLabel = new Label("KEY COUNT", new Label.LabelStyle(font12, Color.WHITE));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(characterLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(keyCountLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        //add our table to the stage
        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%02d", worldTimer));
            timeCount = 0;
        }
    }

    public void addKeyCount(int value){
        keyCount += value;
        keyCountLabel.setText(String.format("%06d", keyCount));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }
}
