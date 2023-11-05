package com.sunyi.algo.modules;

import com.sunyi.algo.modules.behavioral_decision.Controller;
import com.sunyi.algo.modules.cost.Calculator;
import com.sunyi.algo.modules.global_path_planning.PathPlanner;
import com.sunyi.algo.modules.motion_planning.MotionPlanner;
import com.sunyi.algo.modules.preprocessing.Processor;
import com.sunyi.algo.modules.traffic_signal_detection.Detector;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * 初始化所有模块
 */
@Getter
@Component
public class FirstAlgoRunner {

    private Processor preprocessor;

    private Detector detector;

    private Calculator calculator;

    private PathPlanner pathPlanner;

    private MotionPlanner motionPlanner;

    private Controller controller;

    public FirstAlgoRunner() {

        //start();
    }

    public void start() {
        preprocessor = new Processor();
        detector = new Detector();
        //calculator = new Calculator();
        pathPlanner = new PathPlanner();
        //motionPlanner = new MotionPlanner();
        controller = new Controller();
    }

    public void stop() {
        preprocessor.close();
        detector.close();
        calculator.close();
        pathPlanner.close();
        motionPlanner.close();
        controller.close();
    }

    public void restart() {
        stop();
        start();
    }


}
