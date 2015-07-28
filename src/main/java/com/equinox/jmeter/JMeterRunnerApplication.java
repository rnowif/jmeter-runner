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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

import java.io.File;
import java.io.IOException;

public class JMeterRunnerApplication {

    @Option(name = "-h", required = true, usage = "JMeter home directory")
    private File jmeterPath;

    @Option(name = "-t", required = true, usage = "JMX file to execute")
    private File jmxFile;

    @Option(name = "-l", required = false, usage = "JTL file to log results")
    private File logFile;

    private void doMain(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);

            System.out.println("JMeter home : " + jmeterPath.getAbsolutePath());
            System.out.println("File to execute : " + jmxFile.getAbsolutePath());
            System.out.println("Log file : " + logFile.getAbsolutePath());

            JMeterRunner.aJMeterRunner()
                    .home(jmeterPath)
                    .from(jmxFile)
                    .logTo(logFile)
                    .doExecute();

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("Usage : ");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("Exemple : " + parser.printExample(OptionHandlerFilter.ALL));
        }

    }

    public static void main(String[] args) throws IOException {
        new JMeterRunnerApplication().doMain(args);
    }
}
