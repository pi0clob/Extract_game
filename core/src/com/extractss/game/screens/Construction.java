 package com.extractss.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.extractss.game.SimpleClasses.Building;
import com.extractss.game.ExtractSolarSys;
import com.extractss.game.SimpleClasses.MyButtons;
import com.extractss.game.SimpleClasses.User;
import com.extractss.game.utils.IncrementResourcesTimeCheck;

import java.util.ArrayList;

import static com.extractss.game.ExtractSolarSys.backgroundsOther;
import static com.extractss.game.ExtractSolarSys.bitmapFont;
import static com.extractss.game.ExtractSolarSys.bitmapFontSmall;
import static com.extractss.game.ExtractSolarSys.buttonDownSound;
import static com.extractss.game.ExtractSolarSys.buttonUpSound;
import static com.extractss.game.ExtractSolarSys.downNinePatch;
import static com.extractss.game.ExtractSolarSys.energyTexture;
import static com.extractss.game.ExtractSolarSys.incrementingThreadTime;
import static com.extractss.game.ExtractSolarSys.listButtonDown;
import static com.extractss.game.ExtractSolarSys.listButtonUp;
import static com.extractss.game.ExtractSolarSys.metalTexture;
import static com.extractss.game.ExtractSolarSys.moneyTexture;
import static com.extractss.game.ExtractSolarSys.progressBarKnobNinePatch;
import static com.extractss.game.ExtractSolarSys.progressBarBackNinePatch;
import static com.extractss.game.ExtractSolarSys.screenManager;
import static com.extractss.game.ExtractSolarSys.unknownNinePatch;
import static com.extractss.game.ExtractSolarSys.upNinePatch;
import static com.extractss.game.utils.Constants.APP_HEIGHT;
import static com.extractss.game.utils.Constants.APP_WIDTH;
import static com.extractss.game.utils.Constants.BOTTOM_BUTTONS_TEXT_Y;
import static com.extractss.game.utils.Constants.BUTTON_HEIGHT;
import static com.extractss.game.utils.Constants.HEIGHT_FOR_RESOURCES;
import static com.extractss.game.utils.Constants.HEIGHT_RESOURCES_TABLE;
import static com.extractss.game.utils.Constants.KNOB_WIDTH;
import static com.extractss.game.utils.Constants.KNOB_X;
import static com.extractss.game.utils.Constants.LEFT_INDENT;
import static com.extractss.game.utils.Constants.LIST_ELEMENT_HEIGHT;
import static com.extractss.game.utils.Constants.LIST_ELEMENT_PIC_SIZE;
import static com.extractss.game.utils.Constants.LIST_ELEMENT_TITLE_X_CENTER;
import static com.extractss.game.utils.Constants.LIST_HEIGHT;
import static com.extractss.game.utils.Constants.LIST_ELEMENT_PIC_X;
import static com.extractss.game.utils.Constants.LIST_WIDTH;
import static com.extractss.game.utils.Constants.SCALEXY_NEW;
import static com.extractss.game.utils.Constants.SMALLER_SCALE;
import static com.extractss.game.utils.Constants.TOP_BUTTONS_TEXT_Y;
import static com.extractss.game.utils.Constants.Y_RESOURCES_TABLE;
import static com.extractss.game.utils.Operations.isEnableToBuy;
import static com.extractss.game.utils.Operations.isInPlace;
import static com.extractss.game.utils.Operations.isInPlaceMain;
import static com.extractss.game.utils.Operations.parseAndSavePrefsBuildings;

public class Construction implements MyScreen {

    private ExtractSolarSys sys;
    User user;

    private Batch batch;

    private ArrayList<MyButtons> myButtons;
    private MyButtons myButton;

    private ArrayList<Building> listItems;
    private Building listElementForCycle;

    private float touchedX;
    private float touchedY;
    private float touchedListY;
    private long lastTouchTime = 0;
    private long lastListTouchTime = 1000;
    static long lastPanelTouchTime = 0;
    private long lastAnimationTime;
    private int curScreenAnimation = 0;
    private float knobHeight;
    private static float yForIcons;
    private static float heightForIcons;
    private static float widthForIcons;
    private static float yForResourcesText;
    private static float xForPriceListElements;
    private static float xForIconsListElements;
    private static float appWidthToTwentyFour = APP_WIDTH / 24;
    private static float firstElementY;
    private static float lastElementY;
    private static float boarderUp;
    private static boolean listTouchMode;
    private float resCoord = 0;
    private static float moneyTextureX;
    private static float metalTextureX;
    private static float energyTextureX;
    private static float moneyValueX;
    private static float metalValueX;
    private static float energyValueX;
    private static float deltaFirstElementY;
    private static float menuX;
    private static float inventoryX;
    private static float shopX;

    IncrementResourcesTimeCheck incrementResourcesTimeCheck;

    public Construction(ExtractSolarSys sys, User user) {
        this.sys = sys;
        this.user = user;

        batch = new SpriteBatch();

        listTouchMode = false;

        myButtons = new ArrayList<>();
        myButtons.add(new MyButtons(0, APP_WIDTH / 2f, 0, BUTTON_HEIGHT));
        myButtons.add(new MyButtons(APP_WIDTH / 2f, APP_WIDTH / 2f, 0, BUTTON_HEIGHT));
        myButtons.add(new MyButtons(0, APP_WIDTH, APP_HEIGHT - BUTTON_HEIGHT, BUTTON_HEIGHT));
        myButton = myButtons.get(0);

        /*
        ?????????????? ?????????????????????? ???????????? ???????????? ????????.
         */
        listItems = new ArrayList<>();
        listItems.add(new Building("bank lvl 1", true,
                10, 7, 13, 2, 0, 0,
                0, BUTTON_HEIGHT));
        listItems.add(new Building("tpp coal", true,
                14, 11, 9, 0, 0, 1,
                0, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("placer", true,
                16, 10, 14, 0, 3, 0,
                0, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("tpp oil", true,
                54, 43, 35, 0, 0, 2,
                1, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("garage", false,
                67, 38, 59, 350, 350, 350,
                1, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("bank lvl 2", true,
                54, 38, 70, 5, 0, 0,
                2, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("tpp gas", true,
                208, 166, 135, 0, 0, 4,
                2, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("hangar", false,
                184, 105, 162, 3150, 3150, 3150,
                3, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("gpp", true,
                800, 638, 519, 0, 0, 7,
                3, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("pit", true,
                463, 290, 405, 0, 13, 0,
                3, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("defender", true,
                8241, 6512, 5623, 0, 0, 0,
                3, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("bank lvl 3", true,
                1116, 786, 1447, 18, 0, 0,
                4, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("warehouse", false,
                990, 565, 872, 25200, 25200, 25200,
                5, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("windmill", true,
                4303, 3432, 2792, 0, 0, 15,
                4, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("solar panel", true,
                23143, 18459, 15017, 0, 0, 31,
                5, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("mega storage", false,
                10436, 5956, 9193, 151200, 151200, 151200,
                6, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("bank lvl 4", true,
                32281, 22736, 41856, 73, 0, 0,
                6, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("mine", true,
                26250, 16442, 22962, 0, 70, 0,
                6, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("nuc plant", true,
                174257, 138988, 113072, 0, 0, 72,
                7, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new Building("rocket", false,
                0, 0, 0, 0, 0, 0,
                8, listItems.get(listItems.size() - 1).y + LIST_ELEMENT_HEIGHT));

        knobHeight = LIST_HEIGHT * LIST_HEIGHT / (listItems.size() * LIST_ELEMENT_HEIGHT);

        yForIcons = HEIGHT_FOR_RESOURCES - 7 * bitmapFontSmall.getCapHeight() / 4;
        moneyTextureX = 2 * appWidthToTwentyFour / 3;
        metalTextureX = moneyTextureX + 8 * appWidthToTwentyFour;
        energyTextureX = moneyTextureX + 16 * appWidthToTwentyFour;
        moneyValueX = moneyTextureX + 2 * appWidthToTwentyFour;
        metalValueX = metalTextureX + 2 * appWidthToTwentyFour;
        energyValueX = energyTextureX + 2 * appWidthToTwentyFour;
        heightForIcons = 3 * bitmapFontSmall.getCapHeight() / 2;
        widthForIcons = 3 * bitmapFontSmall.getCapHeight() / 2;
        yForResourcesText = HEIGHT_FOR_RESOURCES - bitmapFontSmall.getCapHeight() / 2;
        xForPriceListElements = LEFT_INDENT + LIST_ELEMENT_HEIGHT + 3 * bitmapFontSmall.getCapHeight() / 2;
        xForIconsListElements = LEFT_INDENT + LIST_ELEMENT_HEIGHT + bitmapFontSmall.getCapHeight() / 2;

        boarderUp = APP_HEIGHT - BUTTON_HEIGHT - LIST_ELEMENT_HEIGHT - 2 * bitmapFontSmall.getCapHeight();

        menuX = APP_WIDTH / 4f - "menu".length() * 11 * SCALEXY_NEW;
        inventoryX = 3 * APP_WIDTH / 4f - "inventory".length() * 11 * SCALEXY_NEW;
        shopX = APP_WIDTH / 2f - "shop".length() * 11 * SCALEXY_NEW;

        incrementResourcesTimeCheck = new IncrementResourcesTimeCheck(sys, user);

        lastAnimationTime = System.currentTimeMillis();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*
        ??????????????????, ???????????? ???? ????????????, ?????????? ?????????????????? ???????????????? ?????????????????????????? ????????????????.
         */
        if (System.currentTimeMillis() - incrementingThreadTime > 60000) {
            incrementResourcesTimeCheck.test();
            incrementingThreadTime = System.currentTimeMillis();
        }

        /*
        ???????????????????? ???????????????? ????????.
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

        /*
        ???????????????????????? ???????????? ????????????, ???????? ?????????? ???????????????? ???? ????????????.
         */
        firstElementY = listItems.get(0).y;
        lastElementY = listItems.get(listItems.size() - 1).y;
        if (!listTouchMode && Gdx.input.isTouched() && isInPlaceMain(Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY(), LEFT_INDENT, BUTTON_HEIGHT,
                LIST_WIDTH, LIST_HEIGHT)) {
            if (System.currentTimeMillis() - lastListTouchTime < 50) {
                resCoord = (-touchedListY + Gdx.graphics.getHeight() - Gdx.input.getY()) * 2;
                if (firstElementY + resCoord > BUTTON_HEIGHT) {
                    resCoord -= firstElementY + resCoord - BUTTON_HEIGHT;
                } else if (lastElementY + resCoord < boarderUp) {
                    resCoord += boarderUp - lastElementY - resCoord;
                }
                for (int i = 0; i < listItems.size(); i++) {
                    listItems.get(i).y += resCoord;
                }
                lastListTouchTime = System.currentTimeMillis() - lastListTouchTime;
            } else {
                lastListTouchTime = System.currentTimeMillis();
            }
            touchedListY = Gdx.graphics.getHeight() - Gdx.input.getY();
        } else {
            if (lastElementY < boarderUp) {
                for (int i = 0; i < listItems.size(); i++) {
                    listItems.get(i).y += boarderUp - lastElementY + 1;
                }
            } else if (firstElementY > BUTTON_HEIGHT) {
                for (int i = 0; i < listItems.size(); i++) {
                    listItems.get(i).y += BUTTON_HEIGHT - firstElementY;
                }
            }
        }


        for (int i = 0; i < listItems.size(); i++) {
            listElementForCycle = listItems.get(i);
            /*
            ???????? ?? ???????????? ?????????????? ?????????????????? ???????????????? ????????????, ?????????????????? ?????????? ?? ?????????????????????? ?? ????????????,
            ?????? ?????? ?????????? ???????????? (?????? ?????????????? ?????????????????????????? ???????????????? ?? ??????????????????).
             */
            if (listTouchMode
                    && Gdx.input.isTouched()
                    && isInPlaceMain(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(),
                    LEFT_INDENT, listElementForCycle.y, LIST_WIDTH, LIST_ELEMENT_HEIGHT)
                    && Gdx.input.getY() < (LIST_HEIGHT + BUTTON_HEIGHT)
                    && Gdx.input.getY() > BUTTON_HEIGHT
                    && user.getInvents() >= listElementForCycle.getInventLvl()) {
                lastTouchTime = System.currentTimeMillis();
                touchedX = Gdx.input.getX();
                touchedY = Gdx.graphics.getHeight() - Gdx.input.getY();
                miniWindowActivated(listItems.get(i));
            }
            /*
            ???????????????????????? ???????????? ?????????????? ????????????, ?????????????? ???????????????????? ???? ??????????.
             */
            if ((listElementForCycle.y < APP_HEIGHT + BUTTON_HEIGHT && listElementForCycle.y > -LIST_ELEMENT_HEIGHT)) {
                if (isEnableToBuy(user, listElementForCycle)) {
                    upNinePatch.draw(batch, LEFT_INDENT, listElementForCycle.y, LIST_WIDTH, LIST_ELEMENT_HEIGHT);
                } else {
                    downNinePatch.draw(batch, LEFT_INDENT, listElementForCycle.y, LIST_WIDTH, LIST_ELEMENT_HEIGHT);
                }
                if (user.getInvents() >= listElementForCycle.getInventLvl()) {
                    upNinePatch.draw(batch, LIST_ELEMENT_PIC_X - 1, listElementForCycle.y + LIST_ELEMENT_HEIGHT / 10f - 1,
                            LIST_ELEMENT_PIC_SIZE + 2, LIST_ELEMENT_PIC_SIZE + 2);
                    batch.draw(listElementForCycle.getPicture(), LIST_ELEMENT_PIC_X,
                            listElementForCycle.y + LIST_ELEMENT_HEIGHT / 10f, LIST_ELEMENT_PIC_SIZE, LIST_ELEMENT_PIC_SIZE);
                    bitmapFontSmall.draw(batch, listElementForCycle.getName(),
                            LIST_ELEMENT_TITLE_X_CENTER - listElementForCycle.getName().length() * 11 * SMALLER_SCALE,
                            listElementForCycle.y - bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                    bitmapFontSmall.draw(batch, String.valueOf(listElementForCycle.getCostMoney()),
                            xForPriceListElements, listElementForCycle.y - 3 * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                    bitmapFontSmall.draw(batch, String.valueOf(listElementForCycle.getCostMetal()),
                            xForPriceListElements, listElementForCycle.y - 4.5f * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                    bitmapFontSmall.draw(batch, String.valueOf(listElementForCycle.getCostEnergy()),
                            xForPriceListElements, listElementForCycle.y - 6 * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                } else {
                    unknownNinePatch.draw(batch, LIST_ELEMENT_PIC_X - 1,
                            listElementForCycle.y + LIST_ELEMENT_HEIGHT / 10f - 1, LIST_ELEMENT_PIC_SIZE + 2, LIST_ELEMENT_PIC_SIZE + 2);
                    bitmapFontSmall.draw(batch, "?????", LIST_ELEMENT_TITLE_X_CENTER - "?????".length() * 11 * SMALLER_SCALE,
                            listElementForCycle.y - bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                    bitmapFontSmall.draw(batch, "???",
                            xForPriceListElements, listElementForCycle.y - 3 * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                    bitmapFontSmall.draw(batch, "???",
                            xForPriceListElements, listElementForCycle.y - 4.5f * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                    bitmapFontSmall.draw(batch, "???",
                            xForPriceListElements, listElementForCycle.y - 6 * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT);
                }
                batch.draw(moneyTexture, xForIconsListElements, listElementForCycle.y -
                        4 * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT, bitmapFontSmall.getCapHeight(), bitmapFontSmall.getCapHeight());
                batch.draw(metalTexture, xForIconsListElements, listElementForCycle.y -
                        5.5f * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT, bitmapFontSmall.getCapHeight(), bitmapFontSmall.getCapHeight());
                batch.draw(energyTexture, xForIconsListElements, listElementForCycle.y -
                        7 * bitmapFontSmall.getCapHeight() + LIST_ELEMENT_HEIGHT, bitmapFontSmall.getCapHeight(), bitmapFontSmall.getCapHeight());
            }
        }

        /*
        ???????????????????????? ???????????? ???????????? ???? ????????????.
         */
        batch.draw(new TextureRegion(backgroundsOther.get(curScreenAnimation), 0,
                        19 * backgroundsOther.get(curScreenAnimation).getHeight() / 9,
                        backgroundsOther.get(curScreenAnimation).getWidth(),
                        backgroundsOther.get(curScreenAnimation).getHeight() / 9), 0,
                APP_HEIGHT - BUTTON_HEIGHT - 2 * bitmapFontSmall.getCapHeight(), APP_WIDTH,
                BUTTON_HEIGHT + 2 * bitmapFont.getCapHeight());
        batch.draw(new TextureRegion(backgroundsOther.get(curScreenAnimation), 0, 0,
                        backgroundsOther.get(curScreenAnimation).getWidth(),
                        backgroundsOther.get(curScreenAnimation).getHeight() / 9), 0, 0,
                APP_WIDTH, BUTTON_HEIGHT);

        upNinePatch.draw(batch, 0, Y_RESOURCES_TABLE, APP_WIDTH, HEIGHT_RESOURCES_TABLE);
        batch.draw(moneyTexture, moneyTextureX, yForIcons, widthForIcons, heightForIcons);
        batch.draw(metalTexture, metalTextureX, yForIcons, widthForIcons, heightForIcons);
        batch.draw(energyTexture, energyTextureX, yForIcons, widthForIcons, heightForIcons);
        bitmapFontSmall.draw(batch, String.valueOf(user.getMoney()),
                moneyValueX, yForResourcesText);
        bitmapFontSmall.draw(batch, String.valueOf(user.getMetal()),
                metalValueX, yForResourcesText);
        bitmapFontSmall.draw(batch, String.valueOf(user.getEnergy()),
                energyValueX, yForResourcesText);

        /*
        ?????????????????? ???????????? ???? ??????????????.
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
                    if (myButton.isPressedToSound()) {
                        buttonUpSound.play(user.getSoundsVolume());
                        myButton.setPressedToSound(false);
                    }
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

        /*
        ???????????????????????? ????????????????, ???????????????????????? ?????????? ?? ????????????, ?? ?????????????? ???? ??????????????????.
         */
        progressBarBackNinePatch.draw(batch, KNOB_X, BUTTON_HEIGHT, KNOB_WIDTH, LIST_HEIGHT);
        progressBarKnobNinePatch.draw(batch, KNOB_X, BUTTON_HEIGHT -
                knobHeight * (listItems.get(0).y - BUTTON_HEIGHT) / LIST_HEIGHT, KNOB_WIDTH, knobHeight);

        /*
        ???????? ???????????? ???? ????????????????, ???? ???????????????????????? ?? ???? ?????????? ????????????, ?? ?????????? ?????????? ???????????????? ???? ????????????.
         */
        if (Gdx.input.isTouched() && isInPlaceMain(touchedX, touchedY, KNOB_X, BUTTON_HEIGHT, KNOB_WIDTH, LIST_HEIGHT)) {
            if (touchedY + knobHeight / 2 > LIST_HEIGHT + BUTTON_HEIGHT) {
                touchedY = LIST_HEIGHT + BUTTON_HEIGHT - knobHeight / 2;
            } else if (touchedY - knobHeight / 2 < BUTTON_HEIGHT) {
                touchedY = BUTTON_HEIGHT + knobHeight / 2;
            }
            deltaFirstElementY = -listItems.get(0).y + (LIST_HEIGHT *
                    (BUTTON_HEIGHT - touchedY + knobHeight / 2) / knobHeight + BUTTON_HEIGHT);
            for (int i = 0; i < listItems.size(); i++) {
                listItems.get(i).y += deltaFirstElementY;
            }
        }

        /*
        ???????? ???????????? ???????????? ?????????? ???? ????????????, ???? ?????????????????????????? ??????????:
        ?????????? ?????????????????????????? - ?????????? ?????????????? ???? ??????????????.
         */
        if (Gdx.input.isTouched() && isInPlaceMain(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(),
                0, BUTTON_HEIGHT, LEFT_INDENT, LIST_HEIGHT) && System.currentTimeMillis() - lastPanelTouchTime > 300) {
            if (!listTouchMode) buttonDownSound.play(user.getSoundsVolume());
            else buttonUpSound.play(user.getSoundsVolume());
            listTouchMode = !listTouchMode;
            lastPanelTouchTime = System.currentTimeMillis();
        }

        /*
        ???????????????????????? ???????????? ?????????????? ?????????? ???? ????????????.
         */
        if (listTouchMode) {
            listButtonDown.draw(batch, 0, BUTTON_HEIGHT, LEFT_INDENT, LIST_HEIGHT);
        } else {
            listButtonUp.draw(batch, 0, BUTTON_HEIGHT, LEFT_INDENT, LIST_HEIGHT);
        }

        bitmapFont.draw(batch, "menu", menuX, BOTTOM_BUTTONS_TEXT_Y);
        bitmapFont.draw(batch, "inventory", inventoryX, BOTTOM_BUTTONS_TEXT_Y);
        bitmapFont.draw(batch, "shop", shopX, TOP_BUTTONS_TEXT_Y);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
                sys.setScreen(screenManager.getMainScreen());
                break;
            case 1:
                sys.setScreen(screenManager.getInventoryScreen());
                break;
            case 2:
                screenManager.setShopScreen(new Shop(sys, user));
                sys.setScreen(screenManager.getShopScreen());
                break;
        }
    }

    private void miniWindowActivated(Building item) {
        if (item.getName() == "rocket") sys.setScreen(screenManager.getGameOverScreen());
        else {
            screenManager.setMiniWindowBuyingScreen(new MiniWindowBuying(sys, user, new Building(item)));
            sys.setScreen(screenManager.getMiniWindowBuyingScreen());
        }
    }
}
