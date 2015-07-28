/*
* Copyright (c) 2015 Renaud Humbert-Labeaumaz
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*
*/

package com.equinox.jmeter;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
import java.io.IOException;

public class JMeterRunner {

    private File jmeterHome;
    private File jmxFile;
    private File logFile;

    public static JMeterRunner aJMeterRunner() {
        return new JMeterRunner();
    }

    public JMeterRunner home(File jmeterHome) {
        this.jmeterHome = jmeterHome;
        return this;
    }

    public JMeterRunner from(File jmxFile) {
        this.jmxFile = jmxFile;
        return this;
    }

    public JMeterRunner logTo(File logFile) {
        this.logFile = logFile;
        return this;
    }

    public void doExecute() throws IOException {

        // JMeter Engine
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // Initialize Properties, logging, locale, etc.
        JMeterUtils.loadJMeterProperties(jmeterHome.getAbsolutePath() + "/bin/jmeter.properties");
        JMeterUtils.setJMeterHome(jmeterHome.getAbsolutePath());
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();

        // Initialize JMeter SaveService
        SaveService.loadProperties();

        // Load existing .jmx Test Plan
        HashTree testPlanTree = SaveService.loadTree(jmxFile);

        // Configure log file
        if (hasToLogResult()) {
            ResultCollector logger = configureLogger();
            testPlanTree.add(testPlanTree.getArray()[0], logger);
        }

        // Run JMeter Test
        jmeter.configure(testPlanTree);
        jmeter.run();
    }

    private boolean hasToLogResult() {
        return logFile != null;
    }

    private ResultCollector configureLogger() {
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(logFile.getPath());
        return logger;
    }
}
