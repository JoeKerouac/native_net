package com.github.JoeKerouac.nativenet.command;

import com.joe.command.CommandEngine;
import com.joe.command.CommandEngineFactory;
import com.joe.utils.telnet.TelnetServer;

/**
 * @author JoeKerouac
 * @version 2020年03月24日 21:18
 */
public class CommandServer {

    private static volatile TelnetServer SERVER;

    /**
     * 启动命令服务器，端口
     */
    public static void start() {
        CommandEngine engine = CommandEngineFactory.build();
        engine.registerCommand(new FilterCommand());
        SERVER = new TelnetServer(engine::exec);
        SERVER.start();
    }


}
