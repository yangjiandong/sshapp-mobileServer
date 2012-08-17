package org.ssh.tool.script;

import java.io.FileReader;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JsHelloworld {
    public static void main(String[] args)throws Exception{
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName ("JavaScript");
        //engine.eval("print('Hello World - 中文')");

        //engine.eval(new Reader("HelloWorld.js"));

        //while (true) {
            engine.eval(new InputStreamReader(JsHelloworld.class.getResourceAsStream("/js/HelloWorld.js")));
            Thread.sleep(1000);
        //}
    }
}
