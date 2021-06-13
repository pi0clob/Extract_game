package com.extractss.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.extractss.game.SimpleClasses.Building;
import com.extractss.game.ClassesForLists.BuildingsInInventory;
import com.extractss.game.ExtractSolarSys;
import com.extractss.game.SimpleClasses.MyButtons;
import com.extractss.game.SimpleClasses.User;
import com.extractss.game.utils.Constants;

import java.util.ArrayList;

import static com.extractss.game.ExtractSolarSys.backgroundsOther;
import static com.extractss.game.ExtractSolarSys.bitmapFont;
import static com.extractss.game.ExtractSolarSys.buttonDownSound;
import static com.extractss.game.ExtractSolarSys.buttonUpSound;
import static com.extractss.game.ExtractSolarSys.downNinePatch;
import static com.extractss.game.ExtractSolarSys.energyTexture;
import static com.extractss.game.ExtractSolarSys.inventTexture;
import static com.extractss.game.ExtractSolarSys.metalTexture;
import static com.extractss.game.ExtractSolarSys.moneyTexture;
import static com.extractss.game.ExtractSolarSys.screenManager;
import static com.extractss.game.ExtractSolarSys.upNinePatch;
import static com.extractss.game.utils.Constants.APP_HEIGHT;
import static com.extractss.game.utils.Constants.APP_WIDTH;
import static com.extractss.game.utils.Constants.BUTTON_HEIGHT;
import static com.extractss.game.utils.Constants.SCALEXY_NEW;
import static com.extractss.game.utils.Operations.isInPlace;
import static com.extractss.game.utils.Operations.parseAndSavePrefsBuildings;

public class MiniWindowInventory implements MyScreen {
    ExtractSolarSys sys;
    User user;
    Building building;

    private float buildingY;
    private BuildingsInInventory item;

    private Batch batch;

    private ArrayList<MyButtons> myButtons;
    private MyButtons myButton;

    private Texture texture;

    private TextureRegion picTextureRegion;

    private long lastAnimationTime;
    private int curScreenAnimation = 0;

    private float touchedX;
    private float touchedY;
    private long lastTouchTime = 0;

    private static float titleX;
    private static float titleY;

    private static float frameX;
    private static float frameWidth;
    private static float frameY;
    private static float frameHigh;

    private static float firstResourceYMoney;
    private static float firstResourceYMetal;
    private static float firstResourceYEnergy;
    private static float firstResourcePicYMoney;
    private static float firstResourcePicYMetal;
    private static float firstResourcePicYEnergy;

    private static float inventLvlBuildingY;
    private static float inventLvlBuildingPicY;

    private static float secondResourceYMoney;
    private static float secondResourceYMetal;
    private static float secondResourceYEnergy;
    private static float secondResourcePicYMoney;
    private static float secondResourcePicYMetal;
    private static float secondResourcePicYEnergy;

    private static float resourceHighWidth;
    private static float resourceX;
    private static float resourcePicX;

    private static float costY;

    private static float productivityY;

    private static float picX;
    private static float picY;
    private static float picWidth;
    private static float picHigh;

    private static float cancelX;
    private static float cancelY;

    private static float useX;
    private static float useY;

    public MiniWindowInventory(ExtractSolarSys sys, User user, BuildingsInInventory item) {
        this.sys = sys;
        this.user = user;
        building = item.getBuilding();
        buildingY = item.getY();
        this.item = item;

        batch = new SpriteBatch();

        myButtons = new ArrayList<>();
        myButtons.add(new MyButtons(0, APP_WIDTH / 2, APP_HEIGHT / 40, BUTTON_HEIGHT));
        myButtons.add(new MyButtons(APP_WIDTH / 2, APP_WIDTH / 2, APP_HEIGHT / 40,
                BUTTON_HEIGHT));

        myButton = myButtons.get(0);

        texture = item.getBuilding().getPicture();

        picTextureRegion = new TextureRegion(texture, texture.getWidth(), texture.getHeight());


        titleX = APP_WIDTH / 2 - building.getName().length() * 11 * Constants.SCALEXY_NEW;
        titleY = APP_HEIGHT - bitmapFont.getCapHeight() * 1.5f;

        cancelX = APP_WIDTH / 2 - APP_WIDTH / 4 - "cancel".length() * 11 * Constants.SCALEXY_NEW;
        cancelY = BUTTON_HEIGHT / 2 + APP_HEIGHT / 40 + bitmapFont.getCapHeight() / 2;
        useX = APP_WIDTH / 2 + APP_WIDTH / 4 - "buy".length() * 11 * Constants.SCALEXY_NEW;
        useY = cancelY;

        frameX = APP_WIDTH / 20;
        frameWidth = 9 * APP_WIDTH / 10;
        frameY = APP_HEIGHT / 40 + myButtons.get(0).getHeight() + myButtons.get(0).getY1();
        frameHigh = APP_HEIGHT - frameY - APP_HEIGHT / 40;

        picWidth = frameHigh / 5;
        picHigh = frameHigh / 5;
        picX = APP_WIDTH / 2 - picWidth / 2;
        picY = APP_HEIGHT - bitmapFont.getCapHeight() * 3 - frameHigh / 5;

        costY = picY - bitmapFont.getCapHeight() * 0.5f;

        resourceX = APP_WIDTH / 10 + bitmapFont.getCapHeight();
        resourcePicX = APP_WIDTH / 10;

        firstResourcePicYMoney = costY - bitmapFont.getCapHeight() * 2.5f;
        firstResourcePicYMetal = costY - bitmapFont.getCapHeight() * 3.6f;
        firstResourcePicYEnergy = costY - bitmapFont.getCapHeight() * 4.7f;
        inventLvlBuildingPicY = costY - bitmapFont.getCapHeight() * 5.8f;
        firstResourceYMoney = firstResourcePicYMoney + bitmapFont.getCapHeight();
        firstResourceYMetal = firstResourcePicYMetal + bitmapFont.getCapHeight();
        firstResourceYEnergy = firstResourcePicYEnergy + bitmapFont.getCapHeight();
        inventLvlBuildingY = inventLvlBuildingPicY + bitmapFont.getCapHeight();

        productivityY = inventLvlBuildingY - bitmapFont.getCapHeight() * 1.5f;

        secondResourcePicYMoney = productivityY - bitmapFont.getCapHeight() * 2.5f;
        secondResourcePicYMetal = productivityY - bitmapFont.getCapHeight() * 3.6f;
        secondResourcePicYEnergy = productivityY - bitmapFont.getCapHeight() * 4.7f;
        secondResourceYMoney = secondResourcePicYMoney + bitmapFont.getCapHeight();
        secondResourceYMetal = secondResourcePicYMetal + bitmapFont.getCapHeight();
        secondResourceYEnergy = secondResourcePicYEnergy + bitmapFont.getCapHeight();

        resourceHighWidth = bitmapFont.getCapHeight();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*
        Производим анимацию фона.
         */
        if (System.currentTimeMillis() - lastAnimationTime >= 550) {
            switch (curScreenAnimation) {
                case 0:
                    curScreenAnimation = 1;
                    break;
                case 1:
                    curScreenAnimation = 0;
                    break;
            }
            lastAnimationTime = System.currentTimeMillis();
        }

        batch.begin();

        batch.draw(backgroundsOther.get(curScreenAnimation), 0, 0, APP_WIDTH, APP_HEIGHT);

        upNinePatch.draw(batch, frameX, frameY, frameWidth, frameHigh);

        /*
        Проверяем кнопки на нажатие.
         */
        for (int i = 0; i < myButtons.size(); i++) {
            myButton = myButtons.get(i);
            if (Gdx.input.isTouched()) {
                lastTouchTime = System.currentTimeMillis();
                touchedX = Gdx.input.getX();
                touchedY = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (isInPlace(touchedX, touchedY, myButton)) {
                    downNinePatch.draw(batch, myButton.getX1(), myButton.getY1(), myButton.getWidth(),
                            myButton.getHeight());
                    if (!myButton.isPressedToSound()) {
                        buttonDownSound.play(user.getSoundsVolume());
                        myButton.setPressedToSound(true);
                    }
                } else {
                    upNinePatch.draw(batch, myButton.getX1(), myButton.getY1(), myButton.getWidth(),
                            myButton.getHeight());
                    if (myButton.isPressedToSound()) {
                        buttonUpSound.play(user.getSoundsVolume());
                        myButton.setPressedToSound(false);
                    }
                }
            } else {
                if (isInPlace(touchedX, touchedY, myButton) && lastTouchTime != 0) {
                    if (!myButton.isPressedToSound() || i != 1) {
                        buttonUpSound.play(user.getSoundsVolume());
                    }
                    myButton.setPressedToSound(false);
                    downNinePatch.draw(batch, myButton.getX1(), myButton.getY1(), myButton.getWidth(),
                            myButton.getHeight());
                    this.buttonActivated(i);
                    touchedX = touchedY = -1;
                } else {
                    upNinePatch.draw(batch, myButton.getX1(), myButton.getY1(), myButton.getWidth(),
                            myButton.getHeight());
                }
            }
        }

        bitmapFont.draw(batch, building.getName(), titleX, titleY);

        upNinePatch.draw(batch, picX - 1, picY - 1, picWidth + 2, picHigh + 2);
        batch.draw(picTextureRegion, picX, picY, picWidth, picHigh);

        bitmapFont.draw(batch, "cost:", resourcePicX, costY);
        if (building.isProductiveType())
            bitmapFont.draw(batch, "productivity:", resourcePicX, productivityY);
        else bitmapFont.draw(batch, "storage:", resourcePicX, productivityY);

        bitmapFont.draw(batch, String.valueOf(building.getCostMoney()), resourceX, firstResourceYMoney);
        bitmapFont.draw(batch, String.valueOf(building.getCostMetal()), resourceX, firstResourceYMetal);
        bitmapFont.draw(batch, String.valueOf(building.getCostEnergy()), resourceX, firstResourceYEnergy);
        bitmapFont.draw(batch, ">=" + building.getInventLvl(), resourceX, inventLvlBuildingY);

        batch.draw(moneyTexture, resourcePicX, firstResourcePicYMoney, resourceHighWidth, resourceHighWidth);
        batch.draw(metalTexture, resourcePicX, firstResourcePicYMetal, resourceHighWidth, resourceHighWidth);
        batch.draw(energyTexture, resourcePicX, firstResourcePicYEnergy, resourceHighWidth, resourceHighWidth);
        batch.draw(inventTexture, resourcePicX, inventLvlBuildingPicY, resourceHighWidth, resourceHighWidth);

        if (building.isProductiveType()) {
            bitmapFont.draw(batch, building.getUsefulMoney() + "/min", resourceX, secondResourceYMoney);
            bitmapFont.draw(batch, building.getUsefulMetal() + "/min", resourceX, secondResourceYMetal);
            bitmapFont.draw(batch, building.getUsefulEnergy() + "/min", resourceX, secondResourceYEnergy);
        } else {
            bitmapFont.draw(batch, "+" + building.getUsefulMoney(), resourceX, secondResourceYMoney);
            bitmapFont.draw(batch, "+" + building.getUsefulMetal(), resourceX, secondResourceYMetal);
            bitmapFont.draw(batch, "+" + building.getUsefulEnergy(), resourceX, secondResourceYEnergy);
        }

        batch.draw(moneyTexture, resourcePicX, secondResourcePicYMoney, resourceHighWidth, resourceHighWidth);
        batch.draw(metalTexture, resourcePicX, secondResourcePicYMetal, resourceHighWidth, resourceHighWidth);
        batch.draw(energyTexture, resourcePicX, secondResourcePicYEnergy, resourceHighWidth, resourceHighWidth);

        bitmapFont.draw(batch, "cancel", cancelX, cancelY);
        bitmapFont.draw(batch, "use", useX, useY);

        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        parseAndSavePrefsBuildings(user);
    }

    @Override
    public void dispose() {
        parseAndSavePrefsBuildings(user);
        batch.dispose();
    }

    @Override
    public void buttonActivated(int i) {
        switch (i) {
            case 0:
                sys.setScreen(screenManager.getInventoryScreen());
                break;
            case 1:
                screenManager.setPlanetScreen(new Planet(sys, user, item));
                sys.setScreen(screenManager.getPlanetScreen());
                break;
        }
    }
}
