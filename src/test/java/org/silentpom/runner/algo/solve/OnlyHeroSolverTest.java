package org.silentpom.runner.algo.solve;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 18.09.2018.
 */
public class OnlyHeroSolverTest {
    @Test
    public void testFindBestCommand1() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("mapDig1.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);
            //estimation=new DoubleMask(estimation.getRows(), estimation.getColumns());

            for (int i = 3; i < 8; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }

    @Test
    public void testFindBestCommand2() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("mapDig2.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);
            //estimation=new DoubleMask(estimation.getRows(), estimation.getColumns());

            for (int i = 3; i < 8; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }

    @Test
    public void testFindBestCommandDig() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("mapDig2.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(5);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }

    @Test
    public void testFindBestCommand3() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("mapDig3.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            for (int i = 3; i < 8; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }

    @Test
    public void testEnemy1() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("enemy1.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            for (int i = 4; i < 5; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }

    @Test
    public void testEnemy2() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("enemy2.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            for (int i = 4; i < 5; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }


    @Test
    public void testEnemy3() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("enemy3.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            for (int i = 4; i < 5; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }

    @Test
    public void testEnemy4() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("enemy4.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            for (int i = 4; i < 5; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }


    @Test
    public void testStop1() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("stop1.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            estimator.forceOneMode();
            Estimator.Result estimation = estimator.estimate(info);
            //estimation = new DoubleMask(simpleMap.rows(), simpleMap.columns());

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            for (int i = 5; i < 6; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }






    @DataProvider(name = "files")
    public static Object[][] files() {
        return new Object[][]{{"mapHunt1.txt"}, {"mapHunt2.txt"}, {"mapHunt3.txt"}, {"mapHunt4.txt"}};
    }

    @Test(dataProvider = "files")
    public void testFindBestCommandFile(String file) throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream(file);
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            Estimator.Result estimation = estimator.estimate(info);

            //System.out.println(estimation.getStringView());
            //Thread.sleep(5000);

            for (int i = 3; i < 8; ++i) {
                OnlyHeroSolver heroSolver = new OnlyHeroSolver(i);
                long time = System.currentTimeMillis();
                GameCommand bestCommand = heroSolver.findBestCommand(estimation, info);
                time = System.currentTimeMillis() - time;
                System.out.printf("Command: %s, value: %f, calls: %d, time: %d ms %n",
                        bestCommand.getCode(),
                        heroSolver.getValue(),
                        heroSolver.getCalls(),
                        time
                );
            }
        }
    }
}

