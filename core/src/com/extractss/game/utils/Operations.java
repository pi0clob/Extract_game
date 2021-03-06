package com.extractss.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.extractss.game.ClassesForLists.BuildingsOnField;
import com.extractss.game.ClassesForLists.ItemResearch;
import com.extractss.game.ClassesForLists.ItemSelectingPlanet;
import com.extractss.game.ClassesForLists.ItemShop;
import com.extractss.game.ExtractSolarSys;
import com.extractss.game.SimpleClasses.Building;
import com.extractss.game.SimpleClasses.MyButtons;
import com.extractss.game.SimpleClasses.User;
import com.extractss.game.screens.NoConnectionToIncrementResources;
import com.extractss.game.screens.SelectingPlanetScreen;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

import static com.extractss.game.ExtractSolarSys.backFieldAtlas;
import static com.extractss.game.ExtractSolarSys.backgroundMusic;
import static com.extractss.game.ExtractSolarSys.backgroundsMain;
import static com.extractss.game.ExtractSolarSys.backgroundsOther;
import static com.extractss.game.ExtractSolarSys.bitmapFont;
import static com.extractss.game.ExtractSolarSys.bitmapFontSmall;
import static com.extractss.game.ExtractSolarSys.buildingsOnFields;
import static com.extractss.game.ExtractSolarSys.buttonDownSound;
import static com.extractss.game.ExtractSolarSys.buttonUpSound;
import static com.extractss.game.ExtractSolarSys.currentPlanet;
import static com.extractss.game.ExtractSolarSys.earthTexture;
import static com.extractss.game.ExtractSolarSys.energyTexture;
import static com.extractss.game.ExtractSolarSys.gameLogo;
import static com.extractss.game.ExtractSolarSys.getIncrementMechanicUpgradeCost;
import static com.extractss.game.ExtractSolarSys.helpButtonSign;
import static com.extractss.game.ExtractSolarSys.incrementEnergy;
import static com.extractss.game.ExtractSolarSys.incrementMechanicMaxValue;
import static com.extractss.game.ExtractSolarSys.incrementMetal;
import static com.extractss.game.ExtractSolarSys.incrementMoney;
import static com.extractss.game.ExtractSolarSys.inventTexture;
import static com.extractss.game.ExtractSolarSys.inventoryBuildings;
import static com.extractss.game.ExtractSolarSys.isTrainingComplete;
import static com.extractss.game.ExtractSolarSys.jupiterTexture;
import static com.extractss.game.ExtractSolarSys.lastAdNonRewardedShown;
import static com.extractss.game.ExtractSolarSys.lastIncrementGatherTime;
import static com.extractss.game.ExtractSolarSys.lastMeteorFellTime;
import static com.extractss.game.ExtractSolarSys.marsTexture;
import static com.extractss.game.ExtractSolarSys.maxEnergy;
import static com.extractss.game.ExtractSolarSys.maxMetal;
import static com.extractss.game.ExtractSolarSys.maxMoney;
import static com.extractss.game.ExtractSolarSys.mercuryTexture;
import static com.extractss.game.ExtractSolarSys.metalTexture;
import static com.extractss.game.ExtractSolarSys.moneyTexture;
import static com.extractss.game.ExtractSolarSys.musicTexture;
import static com.extractss.game.ExtractSolarSys.neptuneTexture;
import static com.extractss.game.ExtractSolarSys.planetFieldsBackground;
import static com.extractss.game.ExtractSolarSys.saturnTexture;
import static com.extractss.game.ExtractSolarSys.screenManager;
import static com.extractss.game.ExtractSolarSys.selectingPlanetArrayList;
import static com.extractss.game.ExtractSolarSys.settingsButtonSign;
import static com.extractss.game.ExtractSolarSys.soundTexture;
import static com.extractss.game.ExtractSolarSys.successSound;
import static com.extractss.game.ExtractSolarSys.uranusTexture;
import static com.extractss.game.ExtractSolarSys.venusTexture;
import static com.extractss.game.utils.Constants.AVERAGE_VALUE_TO_BUY_RES;
import static com.extractss.game.utils.Constants.BUTTON_HEIGHT;
import static com.extractss.game.utils.Constants.LIST_ELEMENT_HEIGHT;

public class Operations {
    public static boolean isEnableToBuy(User user, Building listItem) {
        /*
        ???????????????????? ??????????: ?????????? ???? ?????????? ???????????? ????????????, ???????? ?????????????? ????????????????, ?????????????? ?? ???????? ????????.
         */
        return user.getMoney() >= listItem.getCostMoney() &&
                user.getEnergy() >= listItem.getCostEnergy() &&
                user.getMetal() >= listItem.getCostMetal() &&
                user.getInvents() >= listItem.getInventLvl();
    }

    public static boolean isEnableToBuy(User user, ItemResearch listItem) {
        /*
        ???????????????????? ??????????: ?????????? ???? ?????????? ???????????? ????????????, ???????? ?????????????? ????????????????, ?????????????? ?? ???????? ????????.
        (???????????? ?????? ???????????? ????????????????????????)
         */
        return user.getMoney() >= listItem.getCostMoney() &&
                user.getEnergy() >= listItem.getCostEnergy() &&
                user.getMetal() >= listItem.getCostMetal() &&
                user.getInvents() == listItem.getInventLvl() - 1;
    }

    public static boolean isEnableToBuy(User user, ItemShop listItem) {
        /*
        ???????????????????? ??????????: ?????????? ???? ?????????? ???????????? ????????????, ???????? ?????????????? ????????????????, ?????????????? ?? ???????? ????????.
        (???????????? ?????? ???????????? ????????????????)
         */
        return user.getMoney() >= listItem.getCostMoney() &&
                user.getEnergy() >= listItem.getCostEnergy() &&
                user.getMetal() >= listItem.getCostMetal();
    }

    public static boolean isEnableToBuy(User user, ItemSelectingPlanet listItem) {
        /*
        ???????????????????? ??????????: ?????????? ???? ?????????? ???????????? ??????????????, ???????? ?????????????? ????????????????, ?????????????? ?? ???????? ????????.
        (???????????? ?????? ???????????? ?????????? ????????????)
         */
        return user.getMoney() >= listItem.getCostMoney() &&
                user.getEnergy() >= listItem.getCostEnergy() &&
                user.getMetal() >= listItem.getCostMetal() &&
                user.getInvents() >= listItem.getInventLvl() &&
                currentPlanet != selectingPlanetArrayList.indexOf(listItem);
    }

    public static void parseAndSavePrefsBuildings(User user) {
        /*
        ?????????????????????? ?????????? ???????????????????? ????????, ?????????????????? ?????????????? ??????????????????: ???????????????????? ????????????????,
         ???????????? ?? ??????????????????, ???? ???????? ?? ??.??.
         */
        Preferences prefs = Gdx.app.getPreferences("com.extractss.GameProgress");

        prefs.clear();
        prefs.flush();

        prefs.putInteger("maxMoney", maxMoney);
        prefs.putInteger("maxMetal", maxMetal);
        prefs.putInteger("maxEnergy", maxEnergy);

        prefs.putInteger("money", user.getMoney());
        prefs.putInteger("metal", user.getMetal());
        prefs.putInteger("energy", user.getEnergy());
        prefs.putInteger("invents", user.getInvents());
        prefs.putLong("lastIncrementGatherTime", lastIncrementGatherTime);
        prefs.putInteger("incrementMoney", incrementMoney);
        prefs.putInteger("incrementMetal", incrementMetal);
        prefs.putInteger("incrementEnergy", incrementEnergy);
        prefs.putInteger("AVERAGE_VALUE_TO_BUY_RES", AVERAGE_VALUE_TO_BUY_RES);

        if (inventoryBuildings.size() > 0) {
            for (int i = 0; i < inventoryBuildings.size(); i++) {
                prefs.putString("i" + i + "name",
                        inventoryBuildings.get(i).getBuilding().getName());
                prefs.putBoolean("i" + i + "productiveType",
                        inventoryBuildings.get(i).getBuilding().isProductiveType());
                prefs.putInteger("i" + i + "costMoney",
                        inventoryBuildings.get(i).getBuilding().getCostMoney());
                prefs.putInteger("i" + i + "costMetal",
                        inventoryBuildings.get(i).getBuilding().getCostMetal());
                prefs.putInteger("i" + i + "costEnergy",
                        inventoryBuildings.get(i).getBuilding().getCostEnergy());
                prefs.putInteger("i" + i + "usefulMoney",
                        inventoryBuildings.get(i).getBuilding().getUsefulMoney());
                prefs.putInteger("i" + i + "usefulMetal",
                        inventoryBuildings.get(i).getBuilding().getUsefulMetal());
                prefs.putInteger("i" + i + "usefulEnergy",
                        inventoryBuildings.get(i).getBuilding().getUsefulEnergy());
                prefs.putInteger("i" + i + "inventLvl",
                        inventoryBuildings.get(i).getBuilding().getInventLvl());
                prefs.putFloat("i" + i + "y", inventoryBuildings.get(i).getY());
            }
        }

        prefs.putInteger("currentPlanet", currentPlanet);

        for (int j = 0; j < buildingsOnFields.size(); j++) {
            if (buildingsOnFields.get(j).size() > 0) {
                for (int i = 0; i < buildingsOnFields.get(j).size(); i++) {
                    prefs.putString(j + "f" + i + "name",
                            buildingsOnFields.get(j).get(i).getBuilding().getName());
                    prefs.putBoolean(j + "f" + i + "productiveType",
                            buildingsOnFields.get(j).get(i).getBuilding().isProductiveType());
                    prefs.putInteger(j + "f" + i + "costMoney",
                            buildingsOnFields.get(j).get(i).getBuilding().getCostMoney());
                    prefs.putInteger(j + "f" + i + "costMetal",
                            buildingsOnFields.get(j).get(i).getBuilding().getCostMetal());
                    prefs.putInteger(j + "f" + i + "costEnergy",
                            buildingsOnFields.get(j).get(i).getBuilding().getCostEnergy());
                    prefs.putInteger(j + "f" + i + "usefulMoney",
                            buildingsOnFields.get(j).get(i).getBuilding().getUsefulMoney());
                    prefs.putInteger(j + "f" + i + "usefulMetal",
                            buildingsOnFields.get(j).get(i).getBuilding().getUsefulMetal());
                    prefs.putInteger(j + "f" + i + "usefulEnergy",
                            buildingsOnFields.get(j).get(i).getBuilding().getUsefulEnergy());
                    prefs.putInteger(j + "f" + i + "inventLvl",
                            buildingsOnFields.get(j).get(i).getBuilding().getInventLvl());
                    prefs.putInteger(j + "f" + i + "i", buildingsOnFields.get(j).get(i).getI());
                    prefs.putInteger(j + "f" + i + "j", buildingsOnFields.get(j).get(i).getJ());
                    prefs.putInteger(j + "f" + i + "buildingLvl",
                            buildingsOnFields.get(j).get(i).getBuilding().getBuildingLvl());
                }
            }
        }

        for (int i = 0; i < selectingPlanetArrayList.size(); i++) {
            prefs.putString(i + "selectingPlanetArrayList" + "name", selectingPlanetArrayList.get(i).getName());
            prefs.putInteger(i + "selectingPlanetArrayList" + "money", selectingPlanetArrayList.get(i).getCostMoney());
            prefs.putInteger(i + "selectingPlanetArrayList" + "metal", selectingPlanetArrayList.get(i).getCostMetal());
            prefs.putInteger(i + "selectingPlanetArrayList" + "energy", selectingPlanetArrayList.get(i).getCostEnergy());
            prefs.putInteger(i + "selectingPlanetArrayList" + "invent", selectingPlanetArrayList.get(i).getInventLvl());
            prefs.putFloat(i + "selectingPlanetArrayList" + "y", selectingPlanetArrayList.get(i).y);
        }

        prefs.putBoolean("soundsActive", user.isSoundsActive());
        prefs.putBoolean("musicActive", user.isMusicActive());

        prefs.putFloat("soundsVolume", user.getSoundsVolume());
        prefs.putFloat("musicVolume", user.getMusicVolume());

        prefs.putBoolean("isTrainingComplete", isTrainingComplete);

        prefs.putInteger("incrementMechanicMaxValue", incrementMechanicMaxValue);
        prefs.putInteger("getIncrementMechanicUpgradeCost", getIncrementMechanicUpgradeCost);

        prefs.putLong("lastMeteorFellTime", lastMeteorFellTime);

        prefs.putLong("lastAdNonRewardedShown", lastAdNonRewardedShown);

        prefs.flush();
    }

    public static boolean isInPlace(float curX, float curY, MyButtons item) {
        return isInPlaceMain(curX, curY, item.getX1(), item.getY1(), item.getWidth(),
                item.getHeight());
    }

    public static boolean isInPlaceMain(float curX, float curY, float x, float y, float width,
                                        float height) {
        /*
        ??????????????????, ???????????? ???? ?????????????? ?? ??????????-???? ???????????? ???? ???????????? ?????? ??????.
         */
        if (curX >= x && curX <= x + width) {
            return curY >= y && curY <= y + height;
        }
        return false;
    }

    public static void disposeAllResources() {
        /*
        ?????????????? ???????????? ???? ???????? (Disposable) ???????????????? ????????.
         */
        backgroundMusic.dispose();
        buttonDownSound.dispose();
        buttonUpSound.dispose();
        successSound.dispose();
        soundTexture.dispose();
        musicTexture.dispose();
        for (Texture t : backgroundsMain
        ) {
            t.dispose();
        }
        for (Texture t : backgroundsOther
        ) {
            t.dispose();
        }
        moneyTexture.dispose();
        metalTexture.dispose();
        energyTexture.dispose();
        inventTexture.dispose();
        bitmapFont.dispose();
        bitmapFontSmall.dispose();
        gameLogo.dispose();
        helpButtonSign.dispose();
        settingsButtonSign.dispose();
        backFieldAtlas.dispose();
    }

    public static long getServerTime() throws Exception {
        /*
        ???????????????? ???????????????? ?????????????? ?????????? ??????????????.
         */
        System.setProperty("http.agent", "Chrome");
        String url = "https://time.is/Unix_time_now";
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        String[] tags = new String[]{
                "div[id=time_section]",
                "div[id=clock0_bg]"
        };
        Elements elements = doc.select(tags[0]);
        for (String tag : tags) {
            elements = elements.select(tag);
        }
        return Long.parseLong(elements.text()) * 1000;
    }

    public static boolean isBuildingUnderDefense(BuildingsOnField buildingOnField) {
        /*
        ????????????????: ?????????? ???? ?????????? ???? ?????????????? ????????????????, ?????????? ???????????? ???? ??????????????????.
        ???????????????? ?????????????? ???????????? ?????????????????? ???? 5 ????????????, ?????????????? ???????? (??????????????).
        -   +   -
        +   +   +
        -   +   -
        ???????????????? ?????????????? ???????????? ?????????????????? ???? 9 ????????????, ?????????????? ???????? (??????????????????).
        +   +   +
        +   +   +
        +   +   +
         */
        BuildingsOnField buildingForCycle;
        if (buildingOnField.getBuilding().getName().equals("defender")) return true;

        for (int k = 0; k < buildingsOnFields.get(currentPlanet).size(); k++) {
            buildingForCycle = buildingsOnFields.get(currentPlanet).get(k);

            if (buildingForCycle.getBuilding().getName().equals("defender")) {

                if (buildingForCycle.getI() >= buildingOnField.getI() - 1 &&
                        buildingForCycle.getI() <= buildingOnField.getI() + 1) {

                    if (buildingForCycle.getJ() >= buildingOnField.getJ() - 1 &&
                            buildingForCycle.getJ() <= buildingOnField.getJ() + 1) {

                        if (buildingForCycle.getBuilding().getBuildingLvl() == 1 ||
                                buildingForCycle.getI() == buildingOnField.getI() ||
                                buildingForCycle.getJ() == buildingOnField.getJ()) return true;

                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<ItemSelectingPlanet> initializeSelectingPlanetArrayList (ArrayList<ItemSelectingPlanet> listItems){
        listItems.add(new ItemSelectingPlanet("earth", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\earth.png"))),
                0, 0, 0, 0,
                BUTTON_HEIGHT));
        listItems.add(new ItemSelectingPlanet("mars", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\mars.png"))),
                167, 348, 243, 1,
                listItems.get(listItems.size()-1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new ItemSelectingPlanet("venus", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\venus.png"))),
                1243, 843, 2134, 1,
                listItems.get(listItems.size()-1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new ItemSelectingPlanet("mercury", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\mercury.png"))),
                7567, 3125, 4657, 2,
                listItems.get(listItems.size()-1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new ItemSelectingPlanet("jupiter", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\jupiter.png"))),
                10743, 15342, 13467, 3,
                listItems.get(listItems.size()-1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new ItemSelectingPlanet("saturn", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\saturn.png"))),
                30278, 37152, 42057, 3,
                listItems.get(listItems.size()-1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new ItemSelectingPlanet("uranus", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\uranus.png"))),
                58345, 64375, 49835, 4,
                listItems.get(listItems.size()-1).y + LIST_ELEMENT_HEIGHT));
        listItems.add(new ItemSelectingPlanet("neptune", new TextureRegion(new Texture(Gdx.files.internal("pngFiles\\planets\\neptune.png"))),
                112345, 75682, 87351, 5,
                listItems.get(listItems.size()-1).y + LIST_ELEMENT_HEIGHT));
        return listItems;
    }
}
